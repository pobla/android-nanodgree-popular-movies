package com.android.pobla.popularmovies.data.sync;


import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.pobla.popularmovies.data.MovieContract.MovieEntry;
import com.android.pobla.popularmovies.data.MovieDbHelper;
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
  private static final String TYPE = "type";

  private final Gson gsonMapper = new Gson();

  public MovieSyncService() {
    super("MovieSyncService");
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent) {
    URL url = (java.net.URL) intent.getExtras().get(URL);
    String type = intent.getExtras().getString(TYPE);
    try {
      MoviesResponse moviesResponse = doRequest(url);
      ContentValues[] contentValues = moviesToContentsValues(moviesResponse, type);
      removeNotFavouritedMovies();
      storeMovies(contentValues);
    } catch (IOException e) {
      Log.e(this.getClass().toString(), "An error occurred requesting movies", e);
    }
  }


  private MoviesResponse doRequest(URL url) throws IOException {
    URLConnection urlConnection = url.openConnection();
    InputStream content = (InputStream) urlConnection.getContent();
    return gsonMapper.fromJson(new InputStreamReader(content, UTF_8), MoviesResponse.class);
  }

  private void removeNotFavouritedMovies() {
    getContentResolver().delete(MovieEntry.CONTENT_URI, MovieEntry.COLUMN_FAVOURITE + MovieDbHelper.SQL_EQUALS_TO, new String[]{MovieDbHelper.FALSE.toString()});
  }

  private void storeMovies(ContentValues[] contentValues) {
    getContentResolver().bulkInsert(MovieEntry.CONTENT_URI, contentValues);
  }

  private ContentValues[] moviesToContentsValues(MoviesResponse moviesResponse, String type) {
    List<Movie> results = moviesResponse.getResults();
    ContentValues[] contentValuesArray = new ContentValues[results.size()];
    for (int i = 0; i < results.size(); i++) {
      Movie movie = results.get(i);
      movie.setType(type);
      contentValuesArray[i] = MovieEntry.toContentValue(movie);

    }
    return contentValuesArray;
  }

  public static void startImmediateSync(@NonNull final Context context, URL url, String type) {
    Intent intentToSyncImmediately = new Intent(context, MovieSyncService.class);
    intentToSyncImmediately.putExtra(URL, url);
    intentToSyncImmediately.putExtra(TYPE, type);
    context.startService(intentToSyncImmediately);
  }
}
