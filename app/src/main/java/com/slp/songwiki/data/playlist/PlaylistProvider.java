package com.slp.songwiki.data.playlist;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Lakshmiprasad on 19-08-2017.
 */

public class PlaylistProvider extends ContentProvider {
    public static final int CODE_PLAYLIST = 200;

    PlaylistDbHelper dbHelper;
    UriMatcher matcher = buildUrlMatcher();

    private UriMatcher buildUrlMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PlaylistContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, PlaylistContract.PATH_PLAYLIST, CODE_PLAYLIST);
        return matcher;

    }

    @Override
    public boolean onCreate() {
        dbHelper = new PlaylistDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        switch (matcher.match(uri)) {
            case CODE_PLAYLIST:
                cursor = dbHelper.getReadableDatabase().query(PlaylistContract.PlaylistEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Invalid Uri:" + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Uri returnUri = null;
        switch (matcher.match(uri)) {
            case CODE_PLAYLIST:
                long id = dbHelper.getWritableDatabase().insert(PlaylistContract.PlaylistEntry.TABLE_NAME, null, contentValues);
                if (0 < id) {
                    returnUri = ContentUris.withAppendedId(uri, id);
                }
                break;
            default:
                throw new UnsupportedOperationException("Invalid uri:" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (matcher.match(uri)) {
            case CODE_PLAYLIST:
                return dbHelper.getWritableDatabase().delete(PlaylistContract.PlaylistEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new UnsupportedOperationException("Invalid Uri:" + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
