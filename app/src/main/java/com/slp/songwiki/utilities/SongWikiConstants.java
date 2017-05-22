package com.slp.songwiki.utilities;

import com.slp.songwiki.BuildConfig;

/**
 * Created by lshivaram on 4/30/2017.
 */

public interface SongWikiConstants {
    String LAST_FM_API_KEY = BuildConfig.MY_API_KEY;
    String ARTIST_INFO_END_POINT = "http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&format=json&api_key=" + LAST_FM_API_KEY + "&artist=";
    String ARTIST_INFO_BASE_URL = "http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&format=json";
    String TRACK_INFO_BASE__URL = "http://ws.audioscrobbler.com/2.0/?method=track.getInfo&format=json";
    String TOP_ARTISTS_BASE_URL = "http://ws.audioscrobbler.com/2.0/?method=geo.gettopartists&&format=json";
    String TOP_TRACKS_BASE_URL = "http://ws.audioscrobbler.com/2.0/?method=geo.gettoptracks&&format=json";
    String TOP_ARTISTS_GLOBAL_BASE_URL = "http://ws.audioscrobbler.com/2.0/?method=chart.gettopartists&format=json";
    String TOP_TRACKS_GLOBAL_BASE_URL = "http://ws.audioscrobbler.com/2.0/?method=chart.gettoptracks&format=json";
    String SEARCH_ARTIST_END_POINT = "http://ws.audioscrobbler.com/2.0/?method=artist.search&api_key=" + LAST_FM_API_KEY + "&limit=6&format=json&artist=";
    String SEARCH_ARTIST_BASE_URL = "http://ws.audioscrobbler.com/2.0/?method=artist.search&limit=12&format=json";
    String SEARCH_TRACK_BASE_URL = "http://ws.audioscrobbler.com/2.0/?method=track.search&limit=12&format=json";
    String SIMILAR_TRACKS_BASE_URL = "http://ws.audioscrobbler.com/2.0/?method=track.getsimilar&limit=7&format=json";
    String ARTIST_TOP_TRACKS_BASE_URL ="http://ws.audioscrobbler.com/2.0/?method=artist.gettoptracks&format=json&limit=10";
    String ARTISTS = "artists";
    int IMAGE_SIZE = 3;
    String JPG_EXTENSION = ".jpg";
    String LOCAL_IMAGE_BASE_URL = "file:///storage/emulated/0/";
    int TRACK_LIMIT = 30;
    int ARTIST_LIMIT = 20;
    String LIMIT = "limit";
    String API_KEY = "api_key";
    String ARTIST = "artist";
    String TRACK = "track";
    String TEXT = "#text";
    String TOP_ARTISTS = "topartists";
    String URL = "url";
    String IMAGE = "image";
    String NAME = "name";
    String UTF_8 = "UTF-8";
    String STATS = "stats";
    String LISTENERS = "listeners";
    String BIO = "bio";
    String SIMILAR = "similar";
    String TAGS = "tags";
    String TAG = "tag";
    String PUBLISHED = "published";
    String SUMMARY = "summary";
    String CONTENT = "content";
    String RESULTS = "results";
    String ARTIST_MATCHES = "artistmatches";
    String TRACKS = "tracks";
    String ALBUM = "album";
    String TITLE = "title";
    String WIKI = "wiki";
    String TOP_TAGS = "toptags";
    String TRACK_MATCHES = "trackmatches";
    String SIMILAR_TRACKS = "similartracks";
    String TOP_TRACKS = "toptracks";
    String COUNTRY = "country";
}
