package com.android.pobla.popularmovies.data.model;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MoviesResponse implements HasResults<Movie> {

  @SerializedName("page")
  private int page;
  @SerializedName("total_results")
  private int totalResults;
  @SerializedName("total_pages")
  private int totalPages;
  @SerializedName("results")
  private List<Movie> results = new ArrayList<>();

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getTotalResults() {
    return totalResults;
  }

  public void setTotalResults(int totalResults) {
    this.totalResults = totalResults;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }

  public List<Movie> getResults() {
    return results;
  }

  public void setResults(List<Movie> results) {
    this.results = results;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MoviesResponse)) return false;

    MoviesResponse response = (MoviesResponse) o;

    if (page != response.page) return false;
    if (totalResults != response.totalResults) return false;
    if (totalPages != response.totalPages) return false;
    return results.equals(response.results);

  }

  @Override
  public int hashCode() {
    int result = page;
    result = 31 * result + totalResults;
    result = 31 * result + totalPages;
    result = 31 * result + results.hashCode();
    return result;
  }
}
