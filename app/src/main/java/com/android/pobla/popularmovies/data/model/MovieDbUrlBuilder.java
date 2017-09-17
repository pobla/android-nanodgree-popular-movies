package com.android.pobla.popularmovies.data.model;

import android.net.Uri;
import android.util.Log;

import com.android.pobla.popularmovies.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;

public class MovieDbUrlBuilder {

  public static final String UTF_8 = "UTF-8";

  private static final String VIDEOS = "videos";
  private static final String REVIEWS = "reviews";
  private static final String MOVIES_DB_BASE_URL = "https://api.themoviedb.org/3/movie";
  private static final String API_KEY = "api_key";

  private static final String YOUTUBE_URL = "http://www.youtube.com/watch?v=%s";

  private MovieDbUrlBuilder(){};

  public static URL buildMovieListUrl(String path) {
    try {
      return new URL(Uri.parse(MOVIES_DB_BASE_URL)
                       .buildUpon()
                       .appendPath(path)
                       .appendQueryParameter(API_KEY, BuildConfig.API_KEY)
                       .toString());
    } catch (MalformedURLException e) {
      Log.i(MovieDbUrlBuilder.class.toString(), "Error parsing url", e);
    }
    return null;
  }

  public static URL buildVideosUrlForMovie(int movieId) {
    try {
      return new URL(Uri.parse(MOVIES_DB_BASE_URL)
                       .buildUpon()
                       .appendPath(Integer.toString(movieId))
                       .appendPath(VIDEOS)
                       .appendQueryParameter(API_KEY, BuildConfig.API_KEY)
                       .toString());
    } catch (MalformedURLException e) {
      Log.i(MovieDbUrlBuilder.class.toString(), "Error parsing url", e);
    }
    return null;
  }

  public static URL buildReviewsUrlForMovie(int movieId) {
    try {
      return new URL(Uri.parse(MOVIES_DB_BASE_URL)
                       .buildUpon()
                       .appendPath(Integer.toString(movieId))
                       .appendPath(REVIEWS)
                       .appendQueryParameter(API_KEY, BuildConfig.API_KEY)
                       .toString());
    } catch (MalformedURLException e) {
      Log.i(MovieDbUrlBuilder.class.toString(), "Error parsing url", e);
    }
    return null;
  }

  public static Uri buildYoutubeUri(MovieVideos movieVideos) {
    return Uri.parse(String.format(YOUTUBE_URL, movieVideos.getKey()));
  }
}
