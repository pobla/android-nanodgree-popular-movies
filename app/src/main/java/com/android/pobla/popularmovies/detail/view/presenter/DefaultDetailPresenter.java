package com.android.pobla.popularmovies.detail.view.presenter;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.android.pobla.popularmovies.data.MovieContract.MovieEntry;
import com.android.pobla.popularmovies.data.model.MovieReviewsResponse;
import com.android.pobla.popularmovies.detail.view.DetailView;
import com.android.pobla.popularmovies.data.model.Movie;
import com.android.pobla.popularmovies.data.model.MovieDbUrlBuilder;
import com.android.pobla.popularmovies.data.model.MovieVideosResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static com.android.pobla.popularmovies.data.MovieContract.MovieEntry.ALL_COLUMS;
import static com.android.pobla.popularmovies.data.model.MovieDbUrlBuilder.UTF_8;

public class DefaultDetailPresenter implements DetailPresenter{

  private final DetailView detailView;
  private final Context context;
  private final Uri uri;
  private Movie movie;
  private Gson gsonMapper = new Gson();

  public DefaultDetailPresenter(Context context, DetailView view, Uri uri) {
    this.context = context;
    this.detailView = view;
    this.uri = uri;
  }

  @Override
  public void getListOfTrailers() {
    URL url = MovieDbUrlBuilder.buildVideosUrlForMovie(movie.getId());
    new RetrieveTrailersAsyncTask().execute(url);
  }

  @Override
  public void getReviews() {
    URL url = MovieDbUrlBuilder.buildReviewsUrlForMovie(movie.getId());
    new RetrieveReviewsAsyncTask().execute(url);

  }

  @Override
  public void toggleFav() {
    movie.setFavourite(!movie.isFavourite());
    context.getContentResolver().update(uri, MovieEntry.toContentValue(movie), null, null);

  }


  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    if (MOVIE_LOADER_ID != id) {
      return null;
    }
    return new CursorLoader(context, uri, ALL_COLUMS, null, null, null);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    data.moveToFirst();
    Movie movie = MovieEntry.toMovie(data);
    this.movie = movie;
    detailView.bindMovie(movie);

  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
  }

  private class RetrieveTrailersAsyncTask extends AsyncTask<URL, Void, MovieVideosResponse> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      detailView.showLoadingDialog();
    }

    @Override
    protected MovieVideosResponse doInBackground(URL... params) {
      URL url = params[0];
      try {
        URLConnection urlConnection = url.openConnection();
        InputStream content = (InputStream) urlConnection.getContent();
        return gsonMapper.fromJson(new InputStreamReader(content, UTF_8), MovieVideosResponse.class);
      } catch (IOException e) {
        e.printStackTrace();
      }
      return null;

    }

    @Override
    protected void onPostExecute(MovieVideosResponse movieVideosResponse) {
      super.onPostExecute(movieVideosResponse);
      detailView.cancelLoadingDialog();
      if (movieVideosResponse == null || movieVideosResponse.getResults() == null) {
        detailView.showGenericError();
      } else {
        detailView.showTrailers(movieVideosResponse.getResults());
      }

    }
  }

  //TODO extract common functionality
  private class RetrieveReviewsAsyncTask extends AsyncTask<URL, Void, MovieReviewsResponse> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      detailView.showLoadingDialog();
    }

    @Override
    protected MovieReviewsResponse doInBackground(URL... params) {
      URL url = params[0];
      try {
        URLConnection urlConnection = url.openConnection();
        InputStream content = (InputStream) urlConnection.getContent();
        return gsonMapper.fromJson(new InputStreamReader(content, UTF_8), MovieReviewsResponse.class);
      } catch (IOException e) {
        e.printStackTrace();
      }
      return null;

    }

    @Override
    protected void onPostExecute(MovieReviewsResponse movieVideosResponse) {
      super.onPostExecute(movieVideosResponse);
      detailView.cancelLoadingDialog();
      if (movieVideosResponse == null || movieVideosResponse.getResults() == null) {
        detailView.showGenericError();
      } else {
        detailView.showReviews(movieVideosResponse.getResults());
      }

    }
  }

}
