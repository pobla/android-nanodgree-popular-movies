package com.android.pobla.popularmovies.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.pobla.popularmovies.data.MovieContract.MovieEntry;

public class MovieDbHelper extends SQLiteOpenHelper {


  public static final String SQL_EQUALS_TO = "= ?";
  public static final Integer TRUE =  1;
  public static final Integer FALSE = 0;

  private static final String DATABASE_NAME = "movies.db";
  private static final int DATABASE_VERSION = 6;

  public MovieDbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    final String SQL_CREATE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                                      MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                                      MovieEntry.COLUMN_VOTE_COUNT + " INTEGER NOT NULL, " +
                                      MovieEntry.COLUMN_VIDEO + " BOOLEAN NOT NULL, " +
                                      MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                                      MovieEntry.COLUMN_TITLE + " STRING NOT NULL, " +
                                      MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                                      MovieEntry.COLUMN_POSTER_PATH + " STRING NOT NULL, " +
                                      MovieEntry.COLUMN_ORIGINAL_LANGUAGE + " STRING NOT NULL, " +
                                      MovieEntry.COLUMN_ORIGINAL_TITLE + " STRING NOT NULL, " +
                                      MovieEntry.COLUMN_GENRE_IDS + " STRING NOT NULL, " + //Comma separated
                                      MovieEntry.COLUMN_BACKDROP_PATH + " STRING NOT NULL, " +
                                      MovieEntry.COLUMN_ADULT + " BOOLEAN NOT NULL, " +
                                      MovieEntry.COLUMN_OVERVIEW + " STRING NOT NULL, " +
                                      MovieEntry.COLUMN_RELEASE_DATE + " String NOT NULL, "+
                                      MovieEntry.COLUMN_FAVOURITE + " BOOLEAN NOT NULL, " +
                                      MovieEntry.COLUMN_TYPE + " STRING NOT NULL " +
                                ");";

    db.execSQL(SQL_CREATE_TABLE);

  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
    onCreate(db);
  }
}
