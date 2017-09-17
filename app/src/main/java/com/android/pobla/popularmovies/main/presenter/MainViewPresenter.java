package com.android.pobla.popularmovies.main.presenter;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

import com.android.pobla.popularmovies.R;

public interface MainViewPresenter extends LoaderCallbacks<Cursor> {

  int MOVIE_LOADER_ID = 231312;

  enum MovieViewSelection {
    TOP_RATED("top_rated", R.id.action_rated), FAVOURITE("favourite", R.id.action_favourite), POPULARITY("popular", R.id.action_popularity);

    private final String value;
    private final int actionId;

    MovieViewSelection(String value, int actionId) {
      this.actionId = actionId;
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    public int getActionId() {
      return actionId;
    }

    public static MovieViewSelection getFromValue(String value) {
      for (int i = 0; i < MovieViewSelection.values().length; i++) {
        MovieViewSelection movieViewSelection = MovieViewSelection.values()[i];
        if (movieViewSelection.getValue().equals(value)) {
          return movieViewSelection;
        }
      }
      return FAVOURITE;
    }
  }

  void refreshMovies(MovieViewSelection method);

  @Override
  Loader<Cursor> onCreateLoader(int id, Bundle args);

  @Override
  void onLoadFinished(Loader<Cursor> loader, Cursor data);

  @Override
  void onLoaderReset(Loader<Cursor> loader);
}
