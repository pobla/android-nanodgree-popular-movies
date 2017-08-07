package com.android.pobla.popularmovies.main.presenter;


import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.android.pobla.popularmovies.BuildConfig;
import com.android.pobla.popularmovies.main.view.MainView;
import com.android.pobla.popularmovies.model.MoviesResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DefaultMainViewPresenter implements MainViewPresenter {

  private static final String UTF_8 = "UTF-8";
  private final String MOVIES_DB_BASE_URL = "https://api.themoviedb.org/3/movie";
  private final String TOP_RATED = "top_rated";
  private final String POPULARITY = "popular";
  private final String API_KEY = "api_key";

  private final MainView mainView;

  private final Gson gsonMapper = new Gson();

  public DefaultMainViewPresenter(MainView mainView) {
    this.mainView = mainView;
  }

  @Override
  public void refreshMoviesByPopularity() {
    doRefresh(buildUrl(POPULARITY));
  }

  @Override
  public void refreshMoviesByRate() {
    doRefresh(buildUrl(TOP_RATED));
  }

  private void doRefresh(URL url) {
    new RetrieveMoviesAsynTasks().execute(url);
  }

  private URL buildUrl(String path) {
    try {
      return new URL(Uri.parse(MOVIES_DB_BASE_URL)
                       .buildUpon()
                       .appendPath(path)
                       .appendQueryParameter(API_KEY, BuildConfig.API_KEY)
                       .toString());
    } catch (MalformedURLException e) {
      Log.i(this.getClass().toString(), "Error parsing url", e);
    }
    return null;
  }

  private class RetrieveMoviesAsynTasks extends AsyncTask<URL, Void, MoviesResponse> {

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
      }else{
        mainView.showEmptyView();
      }

    }
  }
}
