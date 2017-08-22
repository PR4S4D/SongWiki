package com.slp.songwiki.data.playlist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lakshmiprasad on 19-08-2017.
 */

public class PlaylistDbHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "playlist.db";
    public static final int DATABASE_VERSION = 1;

    public PlaylistDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_PLAYLIST_TABLE = "CREATE TABLE " + PlaylistContract.PlaylistEntry.TABLE_NAME + " ("+
                PlaylistContract.PlaylistEntry.TRACK + " TEXT NOT NULL, "+
                PlaylistContract.PlaylistEntry.ARTIST + " TEXT NOT NULL, "+
                PlaylistContract.PlaylistEntry.VIDEO_ID + " TEXT NOT NULL, "+
                PlaylistContract.PlaylistEntry.IMAGE_LINK + " TEXT NOT NULL, "+
                PlaylistContract.PlaylistEntry.DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, "+
                "PRIMARY KEY (" + PlaylistContract.PlaylistEntry.ARTIST+"," + PlaylistContract.PlaylistEntry.TRACK + "));";
        sqLiteDatabase.execSQL(SQL_CREATE_PLAYLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + PlaylistContract.PlaylistEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
