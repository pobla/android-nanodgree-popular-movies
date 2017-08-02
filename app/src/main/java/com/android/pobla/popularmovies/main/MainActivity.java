package com.android.pobla.popularmovies.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    presenter = new MainViewPresenterImpl(this);

    mainViewAdapter = new MainViewAdapter();
    movieGrid = (RecyclerView) findViewById(R.id.recycleView_main_movieGrid);
    movieGrid.setLayoutManager(new GridLayoutManager(this, 3));
    movieGrid.setAdapter(mainViewAdapter);
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (R.id.action_refresh == item.getItemId()) {
      presenter.refreshContent();
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void showMovies(List<Movie> movies) {
    Toast.makeText(this, String.format("Showing %d movies", movies.size()), Toast.LENGTH_SHORT).show();
    mainViewAdapter.setMovies(movies);

  }
}
