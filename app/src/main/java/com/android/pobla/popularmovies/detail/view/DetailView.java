package com.android.pobla.popularmovies.detail.view;


import com.android.pobla.popularmovies.data.model.MovieVideos;

import java.util.List;

public interface DetailView {
  void showLoadingDialog();

  void cancelLoadingDialog();

  void showGenericError();

  void showTrailers(List<MovieVideos> results);
}
