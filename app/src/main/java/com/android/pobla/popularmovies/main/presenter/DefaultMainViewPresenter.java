package com.android.pobla.popularmovies.main.presenter;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.android.pobla.popularmovies.data.sync.MovieSyncService;
import com.android.pobla.popularmovies.main.view.MainView;

import static com.android.pobla.popularmovies.data.MovieContract.MovieEntry.ALL_COLUMS;
import static com.android.pobla.popularmovies.data.MovieContract.MovieEntry.CONTENT_URI;
import static com.android.pobla.popularmovies.data.model.MovieDbUrlBuilder.buildMovieListUrl;

public class DefaultMainViewPresenter implements MainViewPresenter {

  private final MainView mainView;
  private final Context context;

  public DefaultMainViewPresenter(Context context, MainView mainView) {
    this.context = context;
    this.mainView = mainView;
  }

  @Override
  public void refreshMovies(MovieViewSelection method) {
    MovieSyncService.startImmediateSync(context, buildMovieListUrl(method.getValue()));
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    if (MOVIE_LOADER_ID != id) {
      return null;
    }
    mainView.showLoadingDialog();
    return new CursorLoader(context, CONTENT_URI, ALL_COLUMS, null, null, null);
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
