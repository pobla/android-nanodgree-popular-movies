package com.android.pobla.popularmovies.detail.view.presenter;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.android.pobla.popularmovies.data.MovieContract.MovieEntry;
import com.android.pobla.popularmovies.data.model.Movie;
import com.android.pobla.popularmovies.data.model.MovieDbUrlBuilder;
import com.android.pobla.popularmovies.data.model.MovieReviewsResponse;
import com.android.pobla.popularmovies.data.model.MovieVideosResponse;
import com.android.pobla.popularmovies.detail.view.DetailView;

import java.net.URL;

import static com.android.pobla.popularmovies.data.MovieContract.MovieEntry.ALL_COLUMS;

public class DefaultDetailPresenter implements DetailPresenter {

  private final DetailView detailView;
  private final Context context;
  private final Uri uri;
  private Movie movie;

  public DefaultDetailPresenter(Context context, DetailView view, Uri uri) {
    this.context = context;
    this.detailView = view;
    this.uri = uri;
  }

  @Override
  public void getListOfTrailers() {
    URL url = MovieDbUrlBuilder.buildVideosUrlForMovie(movie.getId());
    new RetrieveTrailersAsyncTask(detailView, MovieVideosResponse.class).execute(url);
  }

  @Override
  public void getReviews() {
    URL url = MovieDbUrlBuilder.buildReviewsUrlForMovie(movie.getId());
    new RetrieveReviewsAsyncTask(detailView, MovieReviewsResponse.class).execute(url);

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

  private class RetrieveTrailersAsyncTask extends RetrieveMoviewItemsAbstractAsyncTask<MovieVideosResponse> {

    public RetrieveTrailersAsyncTask(DetailView detailView, Class<MovieVideosResponse> responseType) {
      super(detailView, responseType);
    }

    @Override
    protected void onFinish(MovieVideosResponse movieVideosResponse) {
      detailView.showTrailers(movieVideosResponse.getResults());
    }
  }

  private class RetrieveReviewsAsyncTask extends RetrieveMoviewItemsAbstractAsyncTask<MovieReviewsResponse> {

    public RetrieveReviewsAsyncTask(DetailView detailView, Class<MovieReviewsResponse> responseType) {
      super(detailView, responseType);
    }

    @Override
    protected void onFinish(MovieReviewsResponse movieVideosResponse) {
      detailView.showReviews(movieVideosResponse.getResults());
    }
  }

}
