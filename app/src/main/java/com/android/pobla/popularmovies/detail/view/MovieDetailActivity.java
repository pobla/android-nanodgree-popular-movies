package com.android.pobla.popularmovies.detail.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.pobla.popularmovies.R;
import com.android.pobla.popularmovies.model.Movie;
import com.android.pobla.popularmovies.model.MovieSizes;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

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

  private Movie movie;

  public static Intent buildIntent(Context context, Movie movie) {
    Intent intent = new Intent(context, MovieDetailActivity.class);
    intent.putExtra(MOVIE, movie);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movie_detail);
    ButterKnife.bind(this);

    setUpTitle();
    setUpToolbar();
    setUpContent();

  }

  private void setUpContent() {
    Picasso.with(this)
      .load(movie.getImageUrlWithSize(MovieSizes.W500))
      .into(moviePoster);
    moviePlot.setText(movie.getOverview());
    releaseDate.setText(movie.getReleaseDate());
    userRating.setText(movie.getVoteAverage() != null ? movie.getVoteAverage().toString() : " - ");

  }

  private void setUpToolbar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  private void setUpTitle() {
    Intent intentThatStartedThisActivity = getIntent();
    if (intentThatStartedThisActivity.hasExtra(MOVIE)) {
      movie = intentThatStartedThisActivity.getParcelableExtra(MOVIE);
      setTitle(movie.getTitle());
    }
  }

}
