package com.android.pobla.popularmovies.model;


public enum MovieSizes {
  W185 ("/w185"),
  W500 ("/w500");

  private final String sizePath;

  MovieSizes(String sizePath) {
    this.sizePath = sizePath;
  }

  public String getSizePath() {
    return sizePath;
  }
}
