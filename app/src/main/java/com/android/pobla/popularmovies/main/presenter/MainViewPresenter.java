package com.android.pobla.popularmovies.main.presenter;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

public interface MainViewPresenter extends LoaderCallbacks<Cursor> {

  int MOVIE_LOADER_ID = 231312;

  String TOP_RATED = "top_rated";
  String POPULARITY = "popular";

  void refreshMovies(String method);

  @Override
  Loader<Cursor> onCreateLoader(int id, Bundle args);

  @Override
  void onLoadFinished(Loader<Cursor> loader, Cursor data);

  @Override
  void onLoaderReset(Loader<Cursor> loader);
}
