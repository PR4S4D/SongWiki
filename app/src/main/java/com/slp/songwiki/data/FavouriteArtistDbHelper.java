package com.slp.songwiki.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.slp.songwiki.data.FavouriteArtistContract.ArtistEntry.ARTIST_NAME;
import static com.slp.songwiki.data.FavouriteArtistContract.ArtistEntry.CONTENT;
import static com.slp.songwiki.data.FavouriteArtistContract.ArtistEntry.IMAGE_LINK;
import static com.slp.songwiki.data.FavouriteArtistContract.ArtistEntry.LISTENERS;
import static com.slp.songwiki.data.FavouriteArtistContract.ArtistEntry.PUBLISHED_ON;
import static com.slp.songwiki.data.FavouriteArtistContract.ArtistEntry.SUMMARY;
import static com.slp.songwiki.data.FavouriteArtistContract.ArtistEntry.TABLE_NAME;

/**
 * Created by lshivaram on 4/30/2017.
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
                "CREATE TABLE " + TABLE_NAME + " (" +
                        ARTIST_NAME + " TEXT PRIMARY KEY, " +
                        IMAGE_LINK + " TEXT NOT NULL, " +
                        LISTENERS + " INTEGER NULL, " +
                        PUBLISHED_ON + " TEXT NULL, " +
                        CONTENT + " TEXT NULL, " +
                        SUMMARY + " TEXT NULL);";
        db.execSQL(SQL_CREATE_FAVOURITE_ARTISTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }
}
