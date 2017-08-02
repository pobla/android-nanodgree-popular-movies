package com.android.pobla.popularmovies.main;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.android.pobla.popularmovies.R;
import com.android.pobla.popularmovies.main.presenter.MainViewPresenter;
import com.android.pobla.popularmovies.main.presenter.MainViewPresenterImpl;
import com.android.pobla.popularmovies.main.view.MainView;
import com.android.pobla.popularmovies.main.view.MainViewAdapter;
import com.android.pobla.popularmovies.model.Movie;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView {

  MainViewPresenter presenter;
  MainViewAdapter mainViewAdapter;

  RecyclerView movieGrid;
  private ProgressDialog progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    presenter = new MainViewPresenterImpl(this);

    mainViewAdapter = new MainViewAdapter();
    movieGrid = (RecyclerView) findViewById(R.id.recycleView_main_movieGrid);
    movieGrid.setLayoutManager(new GridLayoutManager(this, 3));
    movieGrid.setAdapter(mainViewAdapter);
    presenter.refreshMoviesByRate();
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
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

  @Override
  public void showMovies(List<Movie> movies) {
    mainViewAdapter.setMovies(movies);

  }

  @Override
  public void cancelLoadingDialog() {
    if (progressDialog != null){
      progressDialog.cancel();
    }
  }

  @Override
  public void showLoadingDialog() {
    progressDialog = new ProgressDialog(this);
    progressDialog.setTitle(getString(R.string.main_title_loading));
    progressDialog.setMessage(getString(R.string.main_loading_message));
    progressDialog.show();

  }
}
