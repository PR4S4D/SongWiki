package com.slp.songwiki.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by lshivaram on 4/30/2017.
 */

public class FavouriteArtistContract {
    public static final String CONTENT_AUTHORITY = "com.slp.songwiki";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_FAVOURITE_ARTIST = "favouriteArtist";

    public static final class ArtistEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITE_ARTIST).build();
        public static final String TABLE_NAME = "favourite_artists";
        public static final String ARTIST_NAME = "name";
        public static final String IMAGE_LINK = "image_link";
        public static final String LISTENERS = "listeners";
        public static final String PUBLISHED_ON = "published_on";
        public static final String CONTENT = "content";
        public static final String SUMMARY = "summary";
    }
}
