package com.android.pobla.popularmovies.main.presenter;


public interface MainViewPresenter {

  String TOP_RATED = "top_rated";
  String POPULARITY = "popular";

  void refreshMovies(String method);
}
