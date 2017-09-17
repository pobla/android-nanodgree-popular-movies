package com.android.pobla.popularmovies.detail.view.presenter;


import android.os.AsyncTask;

import com.android.pobla.popularmovies.data.model.HasResults;
import com.android.pobla.popularmovies.detail.view.DetailView;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static com.android.pobla.popularmovies.data.model.MovieDbUrlBuilder.UTF_8;

abstract class RetrieveMoviewItemsAbstractAsyncTask<T extends HasResults> extends AsyncTask<URL, Void, T> {

  private final Gson gsonMapper = new Gson();
  private final DetailView detailView;

  private Class<T> type;

  public RetrieveMoviewItemsAbstractAsyncTask(DetailView detailView, Class<T> responseType) {
    this.detailView = detailView;
    this.type = responseType;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    detailView.showLoadingDialog();
  }

  @Override
  protected T doInBackground(URL... params) {
    URL url = params[0];
    try {
      URLConnection urlConnection = url.openConnection();
      InputStream content = (InputStream) urlConnection.getContent();
      return gsonMapper.fromJson(new InputStreamReader(content, UTF_8), type);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;

  }

  @Override
  protected void onPostExecute(T movieVideosResponse) {
    super.onPostExecute(movieVideosResponse);
    detailView.cancelLoadingDialog();
    if (movieVideosResponse == null) {
      detailView.showGenericError();
    } else if (movieVideosResponse.getResults() == null || movieVideosResponse.getResults().size() == 0) {
      detailView.showNoItemsDialog();
    } else {
      onFinish(movieVideosResponse);
    }
  }

  protected abstract void onFinish(T responseType);

}
