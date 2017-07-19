package com.android.pobla.popularmovies.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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



}
