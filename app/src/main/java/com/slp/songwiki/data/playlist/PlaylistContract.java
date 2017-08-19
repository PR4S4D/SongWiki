package com.slp.songwiki.data.playlist;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Lakshmiprasad on 19-08-2017.
 */

public class PlaylistContract {
    public static final String CONTENT_AUTHORITY = "com.slp.songwiki";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PLAYLIST = "playlist";

    public static final class PlaylistEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLAYLIST).build();
        public static final String TABLE_NAME = "playlist";
        public static final String TRACK = "track";
        public static final String ARTIST = "artist";
        public static final String IMAGE_LINK = "image_link";
        public static final String DATE = "date";
        public static final String VIDEO_ID = "videoId";
    }
}
