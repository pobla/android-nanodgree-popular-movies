package com.android.pobla.popularmovies.data.model;


import java.util.List;

public interface HasResults<T> {
  List<T> getResults();
}
