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
import com.android.pobla.popularmovies.detail.view.presenter.DefaultDetailPresenter;
import com.android.pobla.popularmovies.detail.view.presenter.DetailPresenter;
import com.android.pobla.popularmovies.model.Movie;
import com.android.pobla.popularmovies.model.MovieSizes;
import com.android.pobla.popularmovies.model.MovieVideos;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieDetailActivity extends AppCompatActivity implements DetailView {

  private static final String MOVIE = "MOVIE";
  private static final String YOUTUBE_URL = "http://www.youtube.com/watch?v=%s";

  private ImageView moviePoster;
  private TextView moviePlot;
  private TextView releaseDate;
  private TextView userRating;
  private Button showDetail;

  private Movie movie;
  private DetailPresenter presenter;
  private ProgressDialog progressDialog;

  public static Intent buildIntent(Context context, Movie movie) {
    Intent intent = new Intent(context, MovieDetailActivity.class);
    intent.putExtra(MOVIE, movie);
    return intent;
  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_detail);

    redMovieFromIntent();
    setUpToolbar();

    moviePoster = (ImageView) findViewById(R.id.imageView_detail_moviePoster);
    moviePlot = (TextView) findViewById(R.id.textView_detail_moviePlot);
    releaseDate = (TextView) findViewById(R.id.textView_detail_releaseDate);
    userRating = (TextView) findViewById(R.id.textView_detail_userRating);
    showDetail = (Button) findViewById(R.id.button_detail_showTrailer);

    setUpContent();

    presenter = new DefaultDetailPresenter(this, this.movie);

  }

  private void setUpContent() {
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

  }

  private void setUpToolbar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  private void redMovieFromIntent() {
    Intent intentThatStartedThisActivity = getIntent();
    if (intentThatStartedThisActivity.hasExtra(MOVIE)) {
      movie = intentThatStartedThisActivity.getParcelableExtra(MOVIE);
    }else{
      //TODO do something here?
    }
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
    return Uri.parse(String.format(YOUTUBE_URL,movieVideos.getKey()));
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
