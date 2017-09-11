package com.android.pobla.popularmovies.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.pobla.popularmovies.R;
import com.android.pobla.popularmovies.data.MovieContract;
import com.android.pobla.popularmovies.data.model.Movie;
import com.android.pobla.popularmovies.detail.view.MovieDetailActivity;
import com.android.pobla.popularmovies.main.presenter.DefaultMainViewPresenter;
import com.android.pobla.popularmovies.main.presenter.MainViewPresenter;
import com.android.pobla.popularmovies.main.view.MainView;
import com.android.pobla.popularmovies.main.view.MainViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.pobla.popularmovies.main.presenter.MainViewPresenter.POPULARITY;
import static com.android.pobla.popularmovies.main.presenter.MainViewPresenter.TOP_RATED;

public class MainActivity extends AppCompatActivity implements MainView, MainViewAdapter.ItemClickListener {

  private static final String LAYOUT_MANAGER_STATE = "layoutManagerState";
  private static final int SCALING_FACTOR = 100;
  private static final String SORT_CRITERIA = "SORT_CRITERIA";

  MainViewPresenter presenter;
  MainViewAdapter mainViewAdapter;

  @BindView(R.id.recycleView_main_movieGrid)
  RecyclerView movieGrid;
  @BindView(R.id.textView_main_noMovies)
  TextView textViewNoMovies;

  private ProgressDialog progressDialog;
  private Parcelable movieGridState;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    presenter = new DefaultMainViewPresenter(this, this);

    mainViewAdapter = new MainViewAdapter(this);
    movieGrid.setLayoutManager(new GridLayoutManager(this, calculateNoOfColumns()));
    movieGrid.setAdapter(mainViewAdapter);

    getSupportLoaderManager().initLoader(MainViewPresenter.MOVIE_LOADER_ID, null, presenter);
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);
    int actionIdSelected = POPULARITY.equals(getCriteriaSelected()) ? R.id.action_popularity : R.id.action_rated;
    menu.findItem(actionIdSelected).setChecked(true);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_refresh:
        refreshSelected();
        return true;
      case R.id.action_popularity:
        refreshView(POPULARITY);
        item.setChecked(true);
        return true;
      case R.id.action_rated:
        refreshView(TOP_RATED);
        item.setChecked(true);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void refreshView(String sortCriteria) {
    presenter.refreshMovies(sortCriteria);
    SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
    Editor edit = sharedPref.edit();
    edit.putString(SORT_CRITERIA, sortCriteria);
    edit.apply();
  }


  private void refreshSelected() {
    presenter.refreshMovies(getCriteriaSelected());
  }

  @NonNull
  private String getCriteriaSelected() {
    SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
    return sharedPref.getString(SORT_CRITERIA, TOP_RATED);
  }

  @Override
  public void showMovies(Cursor movies) {
    movieGrid.setVisibility(View.VISIBLE);
    textViewNoMovies.setVisibility(View.GONE);
    mainViewAdapter.setCursor(movies);
    movieGrid.scrollToPosition(RecyclerView.NO_POSITION);

  }

  @Override
  public void cancelLoadingDialog() {
    if (progressDialog != null) {
      progressDialog.cancel();
    }
  }

  @Override
  public void showLoadingDialog() {
    progressDialog = new ProgressDialog(this);
    progressDialog.setTitle(getString(R.string.all_title_loading));
    progressDialog.setMessage(getString(R.string.all_loading_message));
    progressDialog.show();

  }

  @Override
  public void showEmptyView() {
    movieGrid.setVisibility(View.GONE);
    textViewNoMovies.setVisibility(View.VISIBLE);
  }

  @Override
  public void onItemClick(Movie movie) {
    Intent intent = new Intent(this, MovieDetailActivity.class);
    intent.setData(MovieContract.MovieEntry.buildUriWithMovieId(movie.getId()));
    startActivity(intent);
  }

  @Override
  protected void onResume() {
    super.onResume();
    movieGrid.getLayoutManager().onRestoreInstanceState(movieGridState);
    if (mainViewAdapter.getItemCount() == 0) {
      refreshSelected();
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    movieGridState = movieGrid.getLayoutManager().onSaveInstanceState();
    outState.putParcelable(LAYOUT_MANAGER_STATE, movieGridState);
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    if (savedInstanceState != null) {
      Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE);
      movieGrid.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
    }
  }

  private int calculateNoOfColumns() {
    DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
    float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
    return (int) (dpWidth / SCALING_FACTOR);
  }

}
