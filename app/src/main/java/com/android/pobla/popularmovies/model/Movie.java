package com.android.pobla.popularmovies.model;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

class Movie {

  @SerializedName("vote_count")
  private int voteCount;

  @SerializedName("id")
  private int id;

  @SerializedName("video")
  private Boolean video;

  @SerializedName("vote_average")
  private Double voteAverage;

  @SerializedName("title")
  private String title;

  @SerializedName("popularity")
  private Double popularity;

  @SerializedName("poster_path")
  private String posterPath;

  @SerializedName("original_language")
  private String originalLanguage;

  @SerializedName("original_title")
  private String originalTitle;

  @SerializedName("genre_ids")
  private List<Integer> genreIds = new ArrayList<>();

  @SerializedName("backdrop_path")
  private String backdropPath;

  @SerializedName("adult")
  private Boolean adult;

  @SerializedName("overview")
  private String overview;

  @SerializedName("release_date")
  private String releaseDate;

  public int getVoteCount() {
    return voteCount;
  }

  public void setVoteCount(int voteCount) {
    this.voteCount = voteCount;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Boolean getVideo() {
    return video;
  }

  public void setVideo(Boolean video) {
    this.video = video;
  }

  public Double getVoteAverage() {
    return voteAverage;
  }

  public void setVoteAverage(Double voteAverage) {
    this.voteAverage = voteAverage;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Double getPopularity() {
    return popularity;
  }

  public void setPopularity(Double popularity) {
    this.popularity = popularity;
  }

  public String getPosterPath() {
    return posterPath;
  }

  public void setPosterPath(String posterPath) {
    this.posterPath = posterPath;
  }

  public String getOriginalLanguage() {
    return originalLanguage;
  }

  public void setOriginalLanguage(String originalLanguage) {
    this.originalLanguage = originalLanguage;
  }

  public String getOriginalTitle() {
    return originalTitle;
  }

  public void setOriginalTitle(String originalTitle) {
    this.originalTitle = originalTitle;
  }

  public List<Integer> getGenreIds() {
    return genreIds;
  }

  public void setGenreIds(List<Integer> genreIds) {
    this.genreIds = genreIds;
  }

  public String getBackdropPath() {
    return backdropPath;
  }

  public void setBackdropPath(String backdropPath) {
    this.backdropPath = backdropPath;
  }

  public Boolean getAdult() {
    return adult;
  }

  public void setAdult(Boolean adult) {
    this.adult = adult;
  }

  public String getOverview() {
    return overview;
  }

  public void setOverview(String overview) {
    this.overview = overview;
  }

  public String getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(String releaseDate) {
    this.releaseDate = releaseDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Movie)) return false;

    Movie movie = (Movie) o;

    if (voteCount != movie.voteCount) return false;
    if (id != movie.id) return false;
    if (video != null ? !video.equals(movie.video) : movie.video != null) return false;
    if (voteAverage != null ? !voteAverage.equals(movie.voteAverage) : movie.voteAverage != null)
      return false;
    if (title != null ? !title.equals(movie.title) : movie.title != null) return false;
    if (popularity != null ? !popularity.equals(movie.popularity) : movie.popularity != null)
      return false;
    if (posterPath != null ? !posterPath.equals(movie.posterPath) : movie.posterPath != null)
      return false;
    if (originalLanguage != null ? !originalLanguage.equals(movie.originalLanguage) : movie.originalLanguage != null)
      return false;
    if (originalTitle != null ? !originalTitle.equals(movie.originalTitle) : movie.originalTitle != null)
      return false;
    if (genreIds != null ? !genreIds.equals(movie.genreIds) : movie.genreIds != null) return false;
    if (backdropPath != null ? !backdropPath.equals(movie.backdropPath) : movie.backdropPath != null)
      return false;
    if (adult != null ? !adult.equals(movie.adult) : movie.adult != null) return false;
    if (overview != null ? !overview.equals(movie.overview) : movie.overview != null) return false;
    return releaseDate != null ? releaseDate.equals(movie.releaseDate) : movie.releaseDate == null;

  }

  @Override
  public int hashCode() {
    int result = voteCount;
    result = 31 * result + id;
    result = 31 * result + (video != null ? video.hashCode() : 0);
    result = 31 * result + (voteAverage != null ? voteAverage.hashCode() : 0);
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (popularity != null ? popularity.hashCode() : 0);
    result = 31 * result + (posterPath != null ? posterPath.hashCode() : 0);
    result = 31 * result + (originalLanguage != null ? originalLanguage.hashCode() : 0);
    result = 31 * result + (originalTitle != null ? originalTitle.hashCode() : 0);
    result = 31 * result + (genreIds != null ? genreIds.hashCode() : 0);
    result = 31 * result + (backdropPath != null ? backdropPath.hashCode() : 0);
    result = 31 * result + (adult != null ? adult.hashCode() : 0);
    result = 31 * result + (overview != null ? overview.hashCode() : 0);
    result = 31 * result + (releaseDate != null ? releaseDate.hashCode() : 0);
    return result;
  }
}
