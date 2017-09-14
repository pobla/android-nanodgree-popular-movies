package com.android.pobla.popularmovies.data;


import android.content.ContentValues;
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
    public static final String COLUMN_FAVOURITE = "favourite";
    public static final String COLUMN_TYPE = "type";

    protected static final int IDX_ID = 0;
    protected static final int IDX_VOTE_COUNT = 1;
    protected static final int IDX_VIDEO = 2;
    protected static final int IDX_VOTE_AVERAGE = 3;
    protected static final int IDX_TITLE = 4;
    protected static final int IDX_POPULARITY = 5;
    protected static final int IDX_POSTER_PATH = 6;
    protected static final int IDX_ORIGINAL_LANGUAGE = 7;
    protected static final int IDX_ORIGINAL_TITLE = 8;
    protected static final int IDX_GENRE_IDS = 9;
    protected static final int IDX_BACKDROP_PATH = 10;
    protected static final int IDX_ADULT = 11;
    protected static final int IDX_OVERVIEW = 12;
    protected static final int IDX_RELEASE_DATE = 13;
    protected static final int IDX_FAVOURITE = 14;
    protected static final int IDX_TYPE = 15;

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
      COLUMN_RELEASE_DATE,
      COLUMN_FAVOURITE,
      COLUMN_TYPE
    };

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                                            .appendPath(PATH_MOVIES)
                                            .build();

    public static Uri buildUriWithMovieId(int id) {
      return CONTENT_URI.buildUpon()
               .appendPath(Integer.toString(id))
               .build();
    }

    public static Movie toMovie(Cursor cursor) {
      Movie movie = new Movie();
      movie.setId(cursor.getInt(IDX_ID));
      movie.setVoteCount(cursor.getInt(IDX_VOTE_COUNT));
      movie.setVideo(cursor.getInt(IDX_VIDEO) == MovieDbHelper.TRUE);
      movie.setVoteAverage(cursor.getDouble(IDX_VOTE_AVERAGE));
      movie.setTitle(cursor.getString(IDX_TITLE));
      movie.setPopularity(cursor.getDouble(IDX_POPULARITY));
      movie.setPosterPath(cursor.getString(IDX_POSTER_PATH));
      movie.setOriginalLanguage(cursor.getString(IDX_ORIGINAL_LANGUAGE));
      movie.setOriginalTitle(cursor.getString(IDX_ORIGINAL_TITLE));
      movie.setGenreIds(cursor.getString(IDX_GENRE_IDS));
      movie.setBackdropPath(cursor.getString(IDX_BACKDROP_PATH));
      movie.setAdult(cursor.getInt(IDX_ADULT) == MovieDbHelper.TRUE);
      movie.setOverview(cursor.getString(IDX_OVERVIEW));
      movie.setReleaseDate(cursor.getString(IDX_RELEASE_DATE));
      movie.setFavourite(cursor.getInt(IDX_FAVOURITE) == MovieDbHelper.TRUE);
      movie.setType(cursor.getString(IDX_TYPE));
      return movie;
    }

    public static ContentValues toContentValue(Movie movie) {
      ContentValues contentValues = new ContentValues();
      contentValues.put(_ID, movie.getId());
      contentValues.put(COLUMN_VOTE_COUNT, movie.getVoteCount());
      contentValues.put(COLUMN_VIDEO, movie.getVideo());
      contentValues.put(COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
      contentValues.put(COLUMN_TITLE, movie.getTitle());
      contentValues.put(COLUMN_POPULARITY, movie.getPopularity());
      contentValues.put(COLUMN_POSTER_PATH, movie.getPosterPath());
      contentValues.put(COLUMN_ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
      contentValues.put(COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
      contentValues.put(COLUMN_GENRE_IDS, movie.getGenreIdsAsString());
      contentValues.put(COLUMN_BACKDROP_PATH, movie.getBackdropPath());
      contentValues.put(COLUMN_ADULT, movie.getAdult());
      contentValues.put(COLUMN_OVERVIEW, movie.getOverview());
      contentValues.put(COLUMN_RELEASE_DATE, movie.getReleaseDate());
      contentValues.put(COLUMN_FAVOURITE, movie.isFavourite());
      contentValues.put(COLUMN_TYPE, movie.getType());
      return contentValues;
    }
  }
}
