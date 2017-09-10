package com.android.pobla.popularmovies.data.sync;


import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.pobla.popularmovies.data.MovieContract;
import com.android.pobla.popularmovies.data.MovieContract.MovieEntry;
import com.android.pobla.popularmovies.data.model.Movie;
import com.android.pobla.popularmovies.data.model.MoviesResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import static com.android.pobla.popularmovies.data.model.MovieDbUrlBuilder.UTF_8;

public class MovieSyncService extends IntentService {
  private static final String URL = "url";

  private final Gson gsonMapper = new Gson();

  public MovieSyncService() {
    super("MovieSyncService");
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent) {
    URL url = (java.net.URL) intent.getExtras().get(URL);
    try {
      URLConnection urlConnection = url.openConnection();
      InputStream content = (InputStream) urlConnection.getContent();
      MoviesResponse moviesResponse = gsonMapper.fromJson(new InputStreamReader(content, UTF_8), MoviesResponse.class);
      ContentValues[] contentValues = moviesToContentsValues(moviesResponse);
      ContentResolver contentResolver = getContentResolver();
      contentResolver.bulkInsert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
      return;
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  //TODO refactor this to another class
  private ContentValues[] moviesToContentsValues(MoviesResponse moviesResponse) {
    List<Movie> results = moviesResponse.getResults();
    ContentValues[] contentValuesArray = new ContentValues[results.size()];
    for (int i = 0; i < results.size(); i++) {
      Movie movie = results.get(i);
      ContentValues contentValues = new ContentValues();
      contentValues.put(MovieEntry._ID, movie.getId());
      contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());
      contentValues.put(MovieContract.MovieEntry.COLUMN_VIDEO, movie.getVideo());
      contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
      contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
      contentValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
      contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
      contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
      contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
      contentValues.put(MovieContract.MovieEntry.COLUMN_GENRE_IDS, movie.getGenreIdsAsString());
      contentValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
      contentValues.put(MovieContract.MovieEntry.COLUMN_ADULT, movie.getAdult());
      contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
      contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
      contentValuesArray[i] = contentValues;
    }
    return contentValuesArray;
  }

  public static void startImmediateSync(@NonNull final Context context, URL url) {
    Intent intentToSyncImmediately = new Intent(context, MovieSyncService.class);
    intentToSyncImmediately.putExtra(URL, url);
    context.startService(intentToSyncImmediately);
  }
}
