package com.android.pobla.popularmovies.detail.view.presenter;


import android.os.AsyncTask;

import com.android.pobla.popularmovies.detail.view.DetailView;
import com.android.pobla.popularmovies.model.Movie;
import com.android.pobla.popularmovies.model.MovieDbUrlBuilder;
import com.android.pobla.popularmovies.model.MovieVideos;
import com.android.pobla.popularmovies.model.MovieVideosResponse;
import com.android.pobla.popularmovies.model.MoviesResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static com.android.pobla.popularmovies.model.MovieDbUrlBuilder.UTF_8;

public class DefaultDetailPresenter implements DetailPresenter {

  private final DetailView detailView;
  private final Gson gsonMapper = new Gson();
  private final Movie movie;

  public DefaultDetailPresenter(DetailView view, Movie movie) {
    this.detailView = view;
    this.movie = movie;
  }

  @Override
  public void getListOfTrailers() {
    URL url = MovieDbUrlBuilder.buildVideosUrlForMovie(movie.getId());
    new RetrieveTrailersAsyncTask().execute(url);

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

}
