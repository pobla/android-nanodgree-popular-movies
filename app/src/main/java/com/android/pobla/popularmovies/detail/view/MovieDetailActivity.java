package com.android.pobla.popularmovies.detail.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pobla.popularmovies.R;
import com.android.pobla.popularmovies.data.model.Movie;
import com.android.pobla.popularmovies.data.model.MovieSizes;
import com.android.pobla.popularmovies.data.model.MovieVideos;
import com.android.pobla.popularmovies.detail.view.presenter.DefaultDetailPresenter;
import com.android.pobla.popularmovies.detail.view.presenter.DetailPresenter;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity implements DetailView {

  private static final String MOVIE = "MOVIE";
  private static final String YOUTUBE_URL = "http://www.youtube.com/watch?v=%s";

  @BindView(R.id.imageView_detail_moviePoster)
  ImageView moviePoster;
  @BindView(R.id.textView_detail_moviePlot)
  TextView moviePlot;
  @BindView(R.id.textView_detail_releaseDate)
  TextView releaseDate;
  @BindView(R.id.textView_detail_userRating)
  TextView userRating;
  @BindView(R.id.button_detail_showTrailer)
  Button showDetail;
  @BindView(R.id.button_detail_addFav)
  Button addFav;

  private DetailPresenter presenter;
  private ProgressDialog progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_detail);
    ButterKnife.bind(this);

    setUpToolbar();

    presenter = new DefaultDetailPresenter(this, this, getIntent().getData());
    getSupportLoaderManager().initLoader(DetailPresenter.MOVIE_LOADER_ID, null, presenter);

  }

  @Override
  public void bindMovie(Movie movie) {
    Picasso.with(this)
      .load(movie.getImageUrlWithSize(MovieSizes.W500))
      .into(moviePoster);

    setTitle(movie.getTitle());
    moviePlot.setText(movie.getOverview());
    releaseDate.setText(movie.getReleaseDate());
    userRating.setText(movie.getVoteAverage() != null ? movie.getVoteAverage().toString() : " - ");

    showDetail.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        presenter.getListOfTrailers();
      }
    });
    addFav.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        presenter.addFavourite();
      }
    });

  }

  private void setUpToolbar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public void cancelLoadingDialog() {
    if (progressDialog != null) {
      progressDialog.cancel();
    }
  }

  @Override
  public void showLoadingDialog() {
    progressDialog = new ProgressDialog(this);
    progressDialog.setTitle(getString(R.string.all_title_loading));
    progressDialog.setMessage(getString(R.string.all_loading_message));
    progressDialog.show();

  }

  @Override
  public void showGenericError() {
    Toast.makeText(this, R.string.all_generic_error, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void showTrailers(final List<MovieVideos> results) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    String[] dialogRows = buildDialogRows(results);
    builder.setTitle("Select a clip to watch")
      .setItems(dialogRows, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
          launchVideoIntent(results.get(which));

        }
      });
    builder.create().show();
  }

  private void launchVideoIntent(MovieVideos movieVideos) {
    startActivity(new Intent(Intent.ACTION_VIEW, buildYoutubeUri(movieVideos)));
  }

  private Uri buildYoutubeUri(MovieVideos movieVideos) {
    return Uri.parse(String.format(YOUTUBE_URL, movieVideos.getKey()));
  }

  private String[] buildDialogRows(List<MovieVideos> results) {
    String[] rows = new String[results.size()];
    for (int i = 0; i < results.size(); i++) {
      MovieVideos item = results.get(i);
      rows[i] = getString(R.string.detail_dialog_row, item.getType(), item.getName(), item.getSize());
    }
    return rows;
  }
}
