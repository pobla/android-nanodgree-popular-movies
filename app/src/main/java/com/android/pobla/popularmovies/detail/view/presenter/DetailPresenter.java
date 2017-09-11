package com.android.pobla.popularmovies.detail.view.presenter;


import android.database.Cursor;
import android.support.v4.app.LoaderManager.LoaderCallbacks;

public interface DetailPresenter extends LoaderCallbacks<Cursor> {

  int MOVIE_LOADER_ID = 231313;

  void getListOfTrailers();

  void addFavourite();
}
