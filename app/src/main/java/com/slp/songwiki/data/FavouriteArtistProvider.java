package com.slp.songwiki.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by lshivaram on 4/30/2017.
 */

public class FavouriteArtistProvider extends ContentProvider {
    public static final int CODE_ARTISTS = 100;
    public static final int CODE_ARTIST_WITH_ID = 101;

    FavouriteArtistDbHelper dbHelper;
    UriMatcher matcher = buildUrlMatcher();

    private UriMatcher buildUrlMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavouriteArtistContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, FavouriteArtistContract.PATH_FAVOURITE_ARTIST, CODE_ARTISTS);
        matcher.addURI(authority, FavouriteArtistContract.PATH_FAVOURITE_ARTIST + "/#", CODE_ARTIST_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new FavouriteArtistDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        switch (matcher.match(uri)) {
            case CODE_ARTISTS:
                cursor = dbHelper.getReadableDatabase().query(FavouriteArtistContract.ArtistEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
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
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri returnUri = null ;
        switch(matcher.match(uri)){
            case CODE_ARTISTS:
                long id = dbHelper.getWritableDatabase().insert(FavouriteArtistContract.ArtistEntry.TABLE_NAME,null,values);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(uri,id);
                }else{
                    throw new SQLException("DB insertion failed"+uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Invalid Uri:"+uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (matcher.match(uri)) {
            case CODE_ARTISTS:
                return dbHelper.getWritableDatabase().delete(FavouriteArtistContract.ArtistEntry.TABLE_NAME, selection, selectionArgs);

            default:
                throw new UnsupportedOperationException("Invalid Uri:" + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
