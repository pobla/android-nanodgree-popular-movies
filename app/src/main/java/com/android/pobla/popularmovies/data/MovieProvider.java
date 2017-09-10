package com.android.pobla.popularmovies.data;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.pobla.popularmovies.data.MovieContract.MovieEntry;

public class MovieProvider extends ContentProvider {

  public static final int CODE_MOVIES = 100;
  public static final int CODE_MOVIE_WITH_ID = 101;

  private static final UriMatcher sUriMatcher = buildUriMatcher();

  public static UriMatcher buildUriMatcher() {
    final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    final String authority = MovieContract.CONTENT_AUTHORITY;

    matcher.addURI(authority, MovieContract.PATH_MOVIES, CODE_MOVIES);
    matcher.addURI(authority, MovieContract.PATH_MOVIES + "/#", CODE_MOVIE_WITH_ID);

    return matcher;
  }

  private MovieDbHelper dbHelper;

  @Override
  public boolean onCreate() {
    dbHelper = new MovieDbHelper(getContext());
    return true;
  }

  @Nullable
  @Override
  public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
    Cursor cursor;
    //TODO review this code
    switch (sUriMatcher.match(uri)) {
      case CODE_MOVIE_WITH_ID: {
        String normalizedUtcDateString = uri.getLastPathSegment();
        String[] selectionArguments = new String[]{normalizedUtcDateString};

        cursor = dbHelper.getReadableDatabase().query(
          MovieContract.MovieEntry.TABLE_NAME,
          projection,
          MovieEntry._ID + " = ? ",
          selectionArguments,
          null,
          null,
          sortOrder);

        break;
      }
      case CODE_MOVIES: {
        cursor = dbHelper.getReadableDatabase().query(
          MovieContract.MovieEntry.TABLE_NAME,
          projection,
          selection,
          selectionArgs,
          null,
          null,
          sortOrder);
        break;
      }

      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }

    cursor.setNotificationUri(getContext().getContentResolver(), uri);
    return cursor;

  }

  @Nullable
  @Override
  public String getType(@NonNull Uri uri) {
    return null;
  }

  @Nullable
  @Override
  public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
    return null;
  }

  @Override
  public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
    final SQLiteDatabase db = dbHelper.getWritableDatabase();
//TODO review and this one
    switch (sUriMatcher.match(uri)) {

      case CODE_MOVIES:
        db.beginTransaction();
        int rowsInserted = 0;
        try {
          for (ContentValues value : values) {
            long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
            if (_id != -1) {
              rowsInserted++;
            }
          }
          db.setTransactionSuccessful();
        } finally {
          db.endTransaction();
        }

        if (rowsInserted > 0) {
          getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsInserted;

      default:
        return super.bulkInsert(uri, values);
    }

  }

  @Override
  public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
    return 0;
  }

  @Override
  public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
    return 0;
  }
}
