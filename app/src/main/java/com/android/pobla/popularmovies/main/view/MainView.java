package com.android.pobla.popularmovies.main.view;


import com.android.pobla.popularmovies.model.Movie;

import java.util.List;

public interface MainView {

  void showMovies(List<Movie> movies);

  void cancelLoadingDialog();

  void showLoadingDialog();

  void showEmptyView();

}
