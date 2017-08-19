package com.slp.songwiki.data.artist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lakshmiprasad on 4/30/2017.
 */

public class FavouriteArtistDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "favouriteArtists.db";
    public static final int DATABASE_VERSION = 1;

    public FavouriteArtistDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVOURITE_ARTISTS_TABLE =
                "CREATE TABLE " + FavouriteArtistContract.ArtistEntry.TABLE_NAME + " (" +
                        FavouriteArtistContract.ArtistEntry.ARTIST_NAME + " TEXT PRIMARY KEY, " +
                        FavouriteArtistContract.ArtistEntry.IMAGE_LINK + " TEXT NOT NULL, " +
                        FavouriteArtistContract.ArtistEntry.LISTENERS + " INTEGER NULL, " +
                        FavouriteArtistContract.ArtistEntry.PUBLISHED_ON + " TEXT NULL, " +
                        FavouriteArtistContract.ArtistEntry.CONTENT + " TEXT NULL, " +
                        FavouriteArtistContract.ArtistEntry.SUMMARY + " TEXT NULL);";
        db.execSQL(SQL_CREATE_FAVOURITE_ARTISTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ FavouriteArtistContract.ArtistEntry.TABLE_NAME);
        onCreate(db);
    }
}
