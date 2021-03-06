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
import com.android.pobla.popularmovies.main.presenter.MainViewPresenter.MovieViewSelection;
import com.android.pobla.popularmovies.main.view.MainView;
import com.android.pobla.popularmovies.main.view.MainViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


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
    presenter = new DefaultMainViewPresenter(this, this, getSupportLoaderManager());

    mainViewAdapter = new MainViewAdapter(this);
    movieGrid.setLayoutManager(new GridLayoutManager(this, calculateNoOfColumns()));
    movieGrid.setAdapter(mainViewAdapter);
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);
    int actionIdSelected = getCriteriaSelected().getActionId();
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
        refreshView(MovieViewSelection.POPULARITY);
        item.setChecked(true);
        return true;
      case R.id.action_rated:
        refreshView(MovieViewSelection.TOP_RATED);
        item.setChecked(true);
        return true;
      case R.id.action_favourite:
        refreshView(MovieViewSelection.FAVOURITE);
        item.setChecked(true);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void refreshView(MovieViewSelection sortCriteria) {
    presenter.refreshMovies(sortCriteria);
    SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
    Editor edit = sharedPref.edit();
    edit.putString(SORT_CRITERIA, sortCriteria.getValue());
    edit.apply();
  }


  private void refreshSelected() {
    presenter.refreshMovies(getCriteriaSelected());
  }

  @NonNull
  private MovieViewSelection getCriteriaSelected() {
    SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
    String string = sharedPref.getString(SORT_CRITERIA, MovieViewSelection.TOP_RATED.getValue());
    return MovieViewSelection.getFromValue(string);
  }

  @Override
  public void showMovies(Cursor movies) {
    movieGrid.setVisibility(View.VISIBLE);
    textViewNoMovies.setVisibility(View.GONE);
    mainViewAdapter.setCursor(movies);
    if (movieGridState != null) {
      movieGrid.getLayoutManager().onRestoreInstanceState(movieGridState);
      movieGridState = null;
    }
  }

  @Override
  public void cancelLoadingDialog() {
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.cancel();
    }
  }

  @Override
  public void showLoadingDialog() {
    cancelLoadingDialog();
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
      movieGridState = savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE);
      movieGrid.getLayoutManager().onRestoreInstanceState(movieGridState);
    }
  }

  private int calculateNoOfColumns() {
    DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
    float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
    return (int) (dpWidth / SCALING_FACTOR);
  }
}
