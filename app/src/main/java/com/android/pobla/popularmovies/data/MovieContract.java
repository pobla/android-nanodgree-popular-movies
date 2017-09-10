package com.android.pobla.popularmovies.data;


import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.android.pobla.popularmovies.data.model.Movie;

public class MovieContract {

  public static final String CONTENT_AUTHORITY = "com.android.pobla.popularmovies";
  public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

  public static final String PATH_MOVIES = "movies";

  public static final class MovieEntry implements BaseColumns {

    public static final String TABLE_NAME = "movies";

    public static final String COLUMN_VOTE_COUNT = "vote_count";
    public static final String COLUMN_VIDEO = "video";
    public static final String COLUMN_VOTE_AVERAGE = "vote_average";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_POPULARITY = "popularity";
    public static final String COLUMN_POSTER_PATH = "poster_path";
    public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";
    public static final String COLUMN_ORIGINAL_TITLE = "original_title";
    public static final String COLUMN_GENRE_IDS = "genre_ids";
    public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
    public static final String COLUMN_ADULT = "adult";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_RELEASE_DATE = "release_date";

    public static final String[] ALL_COLUMS = {
      _ID,
      COLUMN_VOTE_COUNT,
      COLUMN_VIDEO,
      COLUMN_VOTE_AVERAGE,
      COLUMN_TITLE,
      COLUMN_POPULARITY,
      COLUMN_POSTER_PATH,
      COLUMN_ORIGINAL_LANGUAGE,
      COLUMN_ORIGINAL_TITLE,
      COLUMN_GENRE_IDS,
      COLUMN_BACKDROP_PATH,
      COLUMN_ADULT,
      COLUMN_OVERVIEW,
      COLUMN_RELEASE_DATE
    };

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                                            .appendPath(PATH_MOVIES)
                                            .build();

    //TODO move this to another class
    public static Movie buildFromCursor(Cursor cursor) {
      Movie movie = new Movie();
      movie.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
      movie.setVoteCount(cursor.getInt(cursor.getColumnIndex(COLUMN_VOTE_COUNT)));
      movie.setVideo(cursor.getInt(cursor.getColumnIndex(COLUMN_VIDEO)) == 1);
      movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(COLUMN_VOTE_AVERAGE)));
      movie.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
      movie.setPopularity(cursor.getDouble(cursor.getColumnIndex(COLUMN_POPULARITY)));
      movie.setPosterPath(cursor.getString(cursor.getColumnIndex(COLUMN_POSTER_PATH)));
      movie.setOriginalLanguage(cursor.getString(cursor.getColumnIndex(COLUMN_ORIGINAL_LANGUAGE)));
      movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(COLUMN_ORIGINAL_TITLE)));
//      movie.setGenreIds(cursor.getString(cursor.getColumnIndex(COLUMN_GENRE_IDS)));
      movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(COLUMN_BACKDROP_PATH)));
      movie.setAdult(cursor.getInt(cursor.getColumnIndex(COLUMN_ADULT)) == 1);
      movie.setOverview(cursor.getString(cursor.getColumnIndex(COLUMN_OVERVIEW)));
      movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(COLUMN_RELEASE_DATE)));
      return movie;
    }
  }
}
