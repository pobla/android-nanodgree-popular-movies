package com.android.pobla.popularmovies.main.view;


import android.database.Cursor;

public interface MainView {

  void showMovies(Cursor movies);

  void cancelLoadingDialog();

  void showLoadingDialog();

  void showEmptyView();

}
