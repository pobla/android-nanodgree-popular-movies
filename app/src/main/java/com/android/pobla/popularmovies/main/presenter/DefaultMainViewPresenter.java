package com.android.pobla.popularmovies.main.presenter;


import android.os.AsyncTask;

import com.android.pobla.popularmovies.main.view.MainView;
import com.android.pobla.popularmovies.data.model.MoviesResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static com.android.pobla.popularmovies.data.model.MovieDbUrlBuilder.UTF_8;
import static com.android.pobla.popularmovies.data.model.MovieDbUrlBuilder.buildMovieListUrl;

public class DefaultMainViewPresenter implements MainViewPresenter {

  private final MainView mainView;

  private final Gson gsonMapper = new Gson();

  public DefaultMainViewPresenter(MainView mainView) {
    this.mainView = mainView;
  }

  @Override
  public void refreshMovies(String method) {
    new RetrieveMoviesAsyncTask().execute(buildMovieListUrl(method));
  }

  private class RetrieveMoviesAsyncTask extends AsyncTask<URL, Void, MoviesResponse> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      mainView.showLoadingDialog();
    }

    @Override
    protected MoviesResponse doInBackground(URL... params) {
      URL url = params[0];
      try {
        URLConnection urlConnection = url.openConnection();
        InputStream content = (InputStream) urlConnection.getContent();
        return gsonMapper.fromJson(new InputStreamReader(content, UTF_8), MoviesResponse.class);
      } catch (IOException e) {
        e.printStackTrace();
      }
      return null;

    }

    @Override
    protected void onPostExecute(MoviesResponse moviesResponse) {
      super.onPostExecute(moviesResponse);
      mainView.cancelLoadingDialog();
      if (moviesResponse != null && moviesResponse.getResults() != null) {
        mainView.showMovies(moviesResponse.getResults());
      } else {
        mainView.showEmptyView();
      }
    }
  }
}
