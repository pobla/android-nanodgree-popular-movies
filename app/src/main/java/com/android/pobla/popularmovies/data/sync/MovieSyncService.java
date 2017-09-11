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
      contentResolver.delete(MovieContract.MovieEntry.CONTENT_URI, null, null);
      contentResolver.bulkInsert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
      return;
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private ContentValues[] moviesToContentsValues(MoviesResponse moviesResponse) {
    List<Movie> results = moviesResponse.getResults();
    ContentValues[] contentValuesArray = new ContentValues[results.size()];
    for (int i = 0; i < results.size(); i++) {
      contentValuesArray[i] = MovieEntry.toContentValue(results.get(i));
    }
    return contentValuesArray;
  }

  public static void startImmediateSync(@NonNull final Context context, URL url) {
    Intent intentToSyncImmediately = new Intent(context, MovieSyncService.class);
    intentToSyncImmediately.putExtra(URL, url);
    context.startService(intentToSyncImmediately);
  }
}
