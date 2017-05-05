package com.slp.songwiki.utilities;

import android.net.Uri;

import com.slp.songwiki.model.Track;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lshivaram on 4/30/2017.
 */

public class LastFmUtils implements SongWikiConstants {

    public static URL getTopArtistUrl() throws MalformedURLException {
        Uri.Builder builder = Uri.parse(TOP_ARTISTS_BASE_URL).buildUpon()
                .appendQueryParameter(LIMIT, String.valueOf(ARTIST_LIMIT));
        appendAPIKey(builder);
        return new URL(builder.build().toString());
    }

    public static URL getTopTracksUrl() throws MalformedURLException {
        Uri.Builder builder = Uri.parse(TOP_TRACKS_BASE_URL).buildUpon()
                .appendQueryParameter(LIMIT, String.valueOf(TRACK_LIMIT));
        appendAPIKey(builder);
        return new URL(builder.build().toString());

    }

    public static String getImage(JSONArray imageArray) throws JSONException {
        if (null != imageArray) {
            JSONObject imageObj = (JSONObject) imageArray.get(IMAGE_SIZE);
            if (null != imageObj) {
                return imageObj.getString("#text");
            }
        }
        return null;
    }

    public static URL getArtistInfoUrl(String artist) throws MalformedURLException, UnsupportedEncodingException {
        Uri.Builder builder = Uri.parse(ARTIST_INFO_BASE_URL).buildUpon().appendQueryParameter("artist", artist);
        appendAPIKey(builder);
        return new URL(builder.build().toString());
    }

    public static URL getTrackInfoUrl(String artist, String track) throws MalformedURLException {
        Uri.Builder builder = Uri.parse(TRACK_INFO_BASE__URL).buildUpon().appendQueryParameter("artist", artist)
                .appendQueryParameter("track", track);
        appendAPIKey(builder);
        return new URL(builder.build().toString());
    }

    private static void appendAPIKey(Uri.Builder builder) {
        builder.appendQueryParameter(API_KEY, LAST_FM_API_KEY);
    }

    public static URL getSearchArtistUrl(String artist) throws MalformedURLException {
        Uri.Builder builder = Uri.parse(SEARCH_ARTIST_BASE_URL).buildUpon().appendQueryParameter("artist", artist);
        appendAPIKey(builder);
        return new URL(builder.build().toString());
    }

    public static URL getSearchTrackUrl(String track) throws MalformedURLException {
        Uri.Builder builder = Uri.parse(SEARCH_TRACK_BASE_URL).buildUpon().appendQueryParameter("track", track);
        appendAPIKey(builder);
        return new URL(builder.build().toString());
    }

    public static URL getSimilarTracksUrl(Track track) throws MalformedURLException {
        Uri.Builder builder = Uri.parse(SIMILAR_TRACKS_BASE_URL).buildUpon().appendQueryParameter("track", track.getTitle())
                .appendQueryParameter("artist", track.getArtist());
        appendAPIKey(builder);
        return new URL(builder.build().toString());
    }
}
