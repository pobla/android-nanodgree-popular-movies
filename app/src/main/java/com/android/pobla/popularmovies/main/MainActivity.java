package com.android.pobla.popularmovies.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.pobla.popularmovies.R;
import com.android.pobla.popularmovies.detail.view.MovieDetailActivity;
import com.android.pobla.popularmovies.main.presenter.DefaultMainViewPresenter;
import com.android.pobla.popularmovies.main.presenter.MainViewPresenter;
import com.android.pobla.popularmovies.main.view.MainView;
import com.android.pobla.popularmovies.main.view.MainViewAdapter;
import com.android.pobla.popularmovies.model.Movie;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView, MainViewAdapter.ItemClickListener {

  MainViewPresenter presenter;
  MainViewAdapter mainViewAdapter;

  RecyclerView movieGrid;
  TextView textViewNoMovies;
  private ProgressDialog progressDialog;
  private Menu menu;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    presenter = new DefaultMainViewPresenter(this);

    mainViewAdapter = new MainViewAdapter(this);
    movieGrid = (RecyclerView) findViewById(R.id.recycleView_main_movieGrid);
    textViewNoMovies = (TextView) findViewById(R.id.textView_main_noMovies);
    movieGrid.setLayoutManager(new GridLayoutManager(this, 3));
    movieGrid.setAdapter(mainViewAdapter);
    presenter.refreshMoviesByRate();
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);
    this.menu = menu;
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_refresh:
        refreshSelected();
        return true;
      case R.id.action_popularity:
        presenter.refreshMoviesByPopularity();
        item.setChecked(true);
        return true;
      case R.id.action_rated:
        presenter.refreshMoviesByRate();
        item.setChecked(true);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void refreshSelected() {
    MenuItem item = menu.findItem(R.id.action_popularity);
    if (item != null && item.isChecked())
      presenter.refreshMoviesByPopularity();
    else
      presenter.refreshMoviesByRate();
  }

  @Override
  public void showMovies(List<Movie> movies) {
    movieGrid.setVisibility(View.VISIBLE);
    textViewNoMovies.setVisibility(View.GONE);
    mainViewAdapter.setMovies(movies);

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
  public void showEmptyView() {
    movieGrid.setVisibility(View.GONE);
    textViewNoMovies.setVisibility(View.VISIBLE);
  }

  @Override
  public void onItemClick(Movie movie) {
    Intent intent = MovieDetailActivity.buildIntent(this, movie);
    startActivity(intent);

  }
}
