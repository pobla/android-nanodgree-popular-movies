package com.android.pobla.popularmovies.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieVideosResponse {

  @SerializedName("id")
  private int id;
  @SerializedName("results")
  private List<MovieVideos> results;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public List<MovieVideos> getResults() {
    return results;
  }

  public void setResults(List<MovieVideos> results) {
    this.results = results;
  }


}
