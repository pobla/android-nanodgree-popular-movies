package com.android.pobla.popularmovies.data.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieReviewsResponse implements HasResults<Reviews > {

  @SerializedName("id")
  private int id;
  @SerializedName("results")
  private List<Reviews> results;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public List<Reviews> getResults() {
    return results;
  }

  public void setResults(List<Reviews> results) {
    this.results = results;
  }


}
