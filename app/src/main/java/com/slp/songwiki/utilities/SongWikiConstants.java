package com.slp.songwiki.utilities;

/**
 * Created by lshivaram on 4/30/2017.
 */

public interface SongWikiConstants {
    String LAST_FM_API_KEY = "";
    String ARTIST_INFO_END_POINT = "http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&format=json&api_key="+LAST_FM_API_KEY+"&artist=";
    String TOP_ARTISTS_END_POINT = "http://ws.audioscrobbler.com/2.0/?method=chart.gettopartists&limit=15&format=json&api_key="+LAST_FM_API_KEY;
    String SEARCH_ARTIST_END_POINT = "http://ws.audioscrobbler.com/2.0/?method=artist.search&api_key="+LAST_FM_API_KEY+"&limit=10&format=json&artist=";
    String ARTISTS = "artists";
    int IMAGE_SIZE = 3;
    String JPG_EXTENSION = ".jpg";
    String LOCAL_IMAGE_BASE_URL = "file:///storage/emulated/0/";
}
