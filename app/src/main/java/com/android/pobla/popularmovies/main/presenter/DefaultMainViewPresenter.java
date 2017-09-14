package com.android.pobla.popularmovies.main.presenter;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.android.pobla.popularmovies.data.MovieContract.MovieEntry;
import com.android.pobla.popularmovies.data.MovieDbHelper;
import com.android.pobla.popularmovies.data.sync.MovieSyncService;
import com.android.pobla.popularmovies.main.view.MainView;

import static com.android.pobla.popularmovies.data.MovieContract.MovieEntry.ALL_COLUMS;
import static com.android.pobla.popularmovies.data.MovieContract.MovieEntry.CONTENT_URI;
import static com.android.pobla.popularmovies.data.model.MovieDbUrlBuilder.buildMovieListUrl;

public class DefaultMainViewPresenter implements MainViewPresenter {

  private static final String FAV = "fav";
  private static final String METHOD = "method";

  private final MainView mainView;
  private final Context context;
  private final LoaderManager loaderManager;

  public DefaultMainViewPresenter(Context context, MainView mainView, LoaderManager loaderManager) {
    this.context = context;
    this.mainView = mainView;
    this.loaderManager = loaderManager;
  }


  @Override
  public void refreshMovies(MovieViewSelection method) {
    boolean isFav = MovieViewSelection.FAVOURITE == method;
    Bundle bundleArgs = createLoaderBundleArgs(method, isFav);
    syncIfNeeded(method, isFav);
    loaderManager.restartLoader(MainViewPresenter.MOVIE_LOADER_ID, bundleArgs, this);

  }

  @NonNull
  private Bundle createLoaderBundleArgs(MovieViewSelection method, boolean isFav) {
    Bundle args = new Bundle();
    args.putString(METHOD, method.getValue());
    args.putBoolean(FAV, isFav);
    return args;
  }

  private void syncIfNeeded(MovieViewSelection method, boolean isFav) {
    if (!isFav) {
      MovieSyncService.startImmediateSync(context, buildMovieListUrl(method.getValue()), method.getValue());
    }
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    if (MOVIE_LOADER_ID != id) {
      return null;
    }
    mainView.showLoadingDialog();
    return new CursorLoader(context, CONTENT_URI, ALL_COLUMS, getSelection(args), getSelectionArgs(args), null);
  }

  @NonNull
  private String[] getSelectionArgs(Bundle args) {
    String argument = args.getBoolean(FAV, true) ? MovieDbHelper.TRUE.toString() : args.getString(METHOD);
    return new String[]{argument};
  }

  @NonNull
  private String getSelection(Bundle args) {
    String column = args.getBoolean(FAV, true) ? MovieEntry.COLUMN_FAVOURITE : MovieEntry.COLUMN_TYPE;
    return column + MovieDbHelper.SQL_EQUALS_TO;
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    mainView.cancelLoadingDialog();
    mainView.showMovies(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    mainView.showMovies(null);
  }
}
