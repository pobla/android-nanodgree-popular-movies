package com.android.pobla.popularmovies.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.pobla.popularmovies.R;
import com.android.pobla.popularmovies.main.presenter.MainViewPresenter;
import com.android.pobla.popularmovies.main.presenter.MainViewPresenterImpl;

public class MainActivity extends AppCompatActivity implements MainView {

  MainViewPresenter presenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    presenter = new MainViewPresenterImpl(this);
//    presenter.refreshContent();
    Button button = (Button) findViewById(R.id.button_main_remove);
    button.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        presenter.refreshContent();
      }
    });
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
}
