package com.android.pobla.popularmovies.detail;

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

public class MovieDetailActivity extends AppCompatActivity {

  private static final String MOVIE = "MOVIE";

  private ImageView moviePoster;
  private TextView moviePlot;
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

    setUpTitle();
    setUpToolbar();

    moviePoster = (ImageView) findViewById(R.id.imageView_detail_moviePoster);
    moviePlot = (TextView) findViewById(R.id.textView_detail_moviePlot);

    setUpContent();

  }

  private void setUpContent() {
    Picasso.with(this)
      .load(movie.getImageUrlWithSize(MovieSizes.W500))
      .into(moviePoster);
    moviePlot.setText(movie.getTitle());

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
