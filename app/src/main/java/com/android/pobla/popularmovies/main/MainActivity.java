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
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.pobla.popularmovies.R;
import com.android.pobla.popularmovies.data.model.Movie;
import com.android.pobla.popularmovies.data.sync.MovieSyncService;
import com.android.pobla.popularmovies.detail.view.MovieDetailActivity;
import com.android.pobla.popularmovies.main.presenter.DefaultMainViewPresenter;
import com.android.pobla.popularmovies.main.presenter.MainViewPresenter;
import com.android.pobla.popularmovies.main.view.MainView;
import com.android.pobla.popularmovies.main.view.MainViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.pobla.popularmovies.data.MovieContract.MovieEntry.ALL_COLUMS;
import static com.android.pobla.popularmovies.data.MovieContract.MovieEntry.CONTENT_URI;
import static com.android.pobla.popularmovies.data.model.MovieDbUrlBuilder.buildMovieListUrl;
import static com.android.pobla.popularmovies.main.presenter.MainViewPresenter.POPULARITY;
import static com.android.pobla.popularmovies.main.presenter.MainViewPresenter.TOP_RATED;

public class MainActivity extends AppCompatActivity implements MainView, MainViewAdapter.ItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

  private static final String LAYOUT_MANAGER_STATE = "layoutManagerState";
  private static final String MOVIES_STATE = "moviesState";
  private static final int SCALING_FACTOR = 100;
  private static final String SORT_CRITERIA = "SORT_CRITERIA";
  private static final int MOVIE_LOADER_ID = 231312;

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
    presenter = new DefaultMainViewPresenter(this);

    mainViewAdapter = new MainViewAdapter(this);
    movieGrid.setLayoutManager(new GridLayoutManager(this, calculateNoOfColumns()));
    movieGrid.setAdapter(mainViewAdapter);

    getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
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


  //TODO review this
  private void refreshSelected() {
    MovieSyncService.startImmediateSync(this, buildMovieListUrl(getCriteriaSelected()));
//    String refreshCriteria = getCriteriaSelected();
//    presenter.refreshMovies(refreshCriteria);
  }

  @NonNull
  private String getCriteriaSelected() {
    SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
    return sharedPref.getString(SORT_CRITERIA, TOP_RATED);
  }

  @Override
  public void showMovies(List<Movie> movies) {
    movieGrid.setVisibility(View.VISIBLE);
    textViewNoMovies.setVisibility(View.GONE);
    //TODO review this
//    mainViewAdapter.setMovies(movies);

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
    Intent intent = MovieDetailActivity.buildIntent(this, movie);
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
    //TODO review this to save the state
    outState.putParcelable(LAYOUT_MANAGER_STATE, movieGridState);
//    outState.putParcelableArrayList(MOVIES_STATE, new ArrayList<Parcelable>(mainViewAdapter.getMovies()));
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    if (savedInstanceState != null) {
      Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE);
      movieGrid.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
      ArrayList<Movie> parcelableArrayList = savedInstanceState.<Movie>getParcelableArrayList(MOVIES_STATE);
      if (parcelableArrayList == null || parcelableArrayList.size() == 0) {
        refreshSelected();
      }
      //TODO review this to save the state
//      mainViewAdapter.setMovies(parcelableArrayList);
    }
  }

  private int calculateNoOfColumns() {
    DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
    float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
    return (int) (dpWidth / SCALING_FACTOR);
  }

  //TODO Is the loader callback the main place?
  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    if (MOVIE_LOADER_ID != id) {
      return null;
    }

//        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
//        String selection = WeatherContract.WeatherEntry.getSqlSelectForTodayOnwards();

    return new CursorLoader(this, CONTENT_URI, ALL_COLUMS, null, null, null);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    mainViewAdapter.setCursor(data);
//    mForecastAdapter.swapCursor(data);
//    if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
//    mRecyclerView.smoothScrollToPosition(mPosition);
//    if (data.getCount() != 0) showWeatherDataView();

  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    mainViewAdapter.setCursor(null);
  }
}
