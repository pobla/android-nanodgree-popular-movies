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

import static com.android.pobla.popularmovies.data.MovieContract.MovieEntry.TABLE_NAME;

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
    switch (sUriMatcher.match(uri)) {
      case CODE_MOVIE_WITH_ID: {
        return queryById(uri, projection, sortOrder);
      }
      case CODE_MOVIES: {
        return queryAll(uri, projection, selection, selectionArgs, sortOrder);
      }
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
  }

  private Cursor queryAll(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
    Cursor cursor = dbHelper.getReadableDatabase().query(
      TABLE_NAME,
      projection,
      selection,
      selectionArgs,
      null,
      null,
      sortOrder);
    cursor.setNotificationUri(getContext().getContentResolver(), uri);
    return cursor;
  }

  private Cursor queryById(@NonNull Uri uri, @Nullable String[] projection, @Nullable String sortOrder) {
    String normalizedUtcDateString = uri.getLastPathSegment();
    String[] selectionArguments = new String[]{normalizedUtcDateString};
    return queryAll(uri, projection, MovieEntry._ID + " = ? ", selectionArguments, sortOrder);
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

    if (sUriMatcher.match(uri) == CODE_MOVIES) {
      db.beginTransaction();
      int rowsInserted = 0;
      try {
        for (ContentValues value : values) {
          long _id = db.insertOrThrow(TABLE_NAME, null, value);
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
    } else {
      return super.bulkInsert(uri, values);
    }

  }

  @Override
  public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
    if (sUriMatcher.match(uri) == CODE_MOVIES) {
      SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
      return writableDatabase.delete(TABLE_NAME, selection, selectionArgs);
    }
    return 0;
  }

  @Override
  public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
    if (sUriMatcher.match(uri) == CODE_MOVIE_WITH_ID) {
      String normalizedUtcDateString = uri.getLastPathSegment();
      String[] selectionArguments = new String[]{normalizedUtcDateString};
      SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
      int update = writableDatabase.update(TABLE_NAME, values, MovieEntry._ID + " = ? ", selectionArguments);
      if (update > 0) {
        getContext().getContentResolver().notifyChange(uri, null);
      }
      return update;
    }
    return 0;
  }
}
