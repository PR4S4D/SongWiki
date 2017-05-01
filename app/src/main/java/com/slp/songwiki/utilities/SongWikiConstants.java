package com.slp.songwiki.utilities;

/**
 * Created by lshivaram on 4/30/2017.
 */

public interface SongWikiConstants {
    String LAST_FM_API_KEY = "";
    String ARTIST_INFO_END_POINT = "http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&format=json&api_key="+LAST_FM_API_KEY+"&artist=";
    String ARTIST_INFO_BASE_URL = "http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&format=json";
    String TRACK_INFO_BASE__URL = "http://ws.audioscrobbler.com/2.0/?method=track.getInfo&format=json";
    String TOP_ARTISTS_BASE_URL = "http://ws.audioscrobbler.com/2.0/?method=geo.gettopartists&country=canada&format=json";
    String TOP_TRACKS_BASE_URL = "http://ws.audioscrobbler.com/2.0/?method=geo.gettoptracks&country=canada&format=json";
    String TOP_ARTISTS_END_POINT = "http://ws.audioscrobbler.com/2.0/?method=chart.gettopartists&limit=15&format=json&api_key="+LAST_FM_API_KEY;
    String SEARCH_ARTIST_END_POINT = "http://ws.audioscrobbler.com/2.0/?method=artist.search&api_key="+LAST_FM_API_KEY+"&limit=10&format=json&artist=";
    String SEARCH_ARTIST_BASE_URL = "http://ws.audioscrobbler.com/2.0/?method=artist.search&limit=10&format=json";

    String SEARCH_TRACK_BASE_URL = "http://ws.audioscrobbler.com/2.0/?method=track.search&limit=10&format=json";
    String SIMILAR_TRACKS_BASE_URL = "http://ws.audioscrobbler.com/2.0/?method=track.getsimilar&limit=7&format=json";
    String ARTISTS = "artists";
    int IMAGE_SIZE = 3;
    String JPG_EXTENSION = ".jpg";
    String LOCAL_IMAGE_BASE_URL = "file:///storage/emulated/0/";
    int TRACK_LIMIT =30;
    int ARTIST_LIMIT = 10;
    String LIMIT = "limit";
    String API_KEY = "api_key";
}
