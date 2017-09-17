package com.android.pobla.popularmovies.detail.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pobla.popularmovies.R;
import com.android.pobla.popularmovies.data.model.Movie;
import com.android.pobla.popularmovies.data.model.MovieSizes;
import com.android.pobla.popularmovies.data.model.MovieVideos;
import com.android.pobla.popularmovies.data.model.Reviews;
import com.android.pobla.popularmovies.detail.view.presenter.DefaultDetailPresenter;
import com.android.pobla.popularmovies.detail.view.presenter.DetailPresenter;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.pobla.popularmovies.data.model.MovieDbUrlBuilder.buildYoutubeUri;

public class MovieDetailActivity extends AppCompatActivity implements DetailView {

  private static final String MOVIE = "MOVIE";

  @BindView(R.id.imageView_detail_moviePoster)
  ImageView moviePoster;
  @BindView(R.id.textView_detail_moviePlot)
  TextView moviePlot;
  @BindView(R.id.textView_detail_releaseDate)
  TextView releaseDate;
  @BindView(R.id.textView_detail_userRating)
  TextView userRating;
  @BindView(R.id.button_detail_showTrailer)
  Button showTrailer;
  @BindView(R.id.button_detail_showReviews)
  Button showReviews;
  @BindView(R.id.button_detail_addFav)
  CheckBox addFav;

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
    addFav.setChecked(movie.isFavourite());

    showTrailer.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        presenter.getListOfTrailers();
      }
    });
    showReviews.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        presenter.getReviews();
      }
    });
    addFav.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        presenter.toggleFav();
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


  private void showListAlertDialog(int title, String[] rows, DialogInterface.OnClickListener itemListener) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(title);
    builder.setItems(rows, itemListener);
    builder.create().show();
  }

  @Override
  public void showTrailers(final List<MovieVideos> results) {
    showListAlertDialog(R.string.detail_dialog_trailer_title, buildDialogRows(results), new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        launchViewIntent(buildYoutubeUri(results.get(which)));
      }
    });
  }

  @Override
  public void showReviews(final List<Reviews> results) {
    showListAlertDialog(R.string.detail_dialog_review_title, buildReviewDialogRows(results), new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        launchViewIntent(Uri.parse(results.get(which).getUrl()));
      }
    });
  }

  private void launchViewIntent(Uri uri) {
    startActivity(new Intent(Intent.ACTION_VIEW, uri));
  }

  private String[] buildDialogRows(List<MovieVideos> results) {
    String[] rows = new String[results.size()];
    for (int i = 0; i < results.size(); i++) {
      MovieVideos item = results.get(i);
      rows[i] = getString(R.string.detail_dialog_row, item.getType(), item.getName(), item.getSize());
    }
    return rows;
  }

  private String[] buildReviewDialogRows(List<Reviews> results) {
    int stringSize = calculateLengthOfReview();
    String[] rows = new String[results.size()];
    for (int i = 0; i < results.size(); i++) {
      Reviews item = results.get(i);
      int max = Math.min(stringSize, item.getContent().length());
      rows[i] = item.getContent().substring(0, max) + "...";
    }
    return rows;
  }

  private int calculateLengthOfReview() {
    DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
    float dpWidth = displayMetrics.widthPixels / displayMetrics.density / 10;
    return (int) (dpWidth);
  }
}
