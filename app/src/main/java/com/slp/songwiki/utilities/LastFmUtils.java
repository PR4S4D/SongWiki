package com.slp.songwiki.utilities;

import android.content.Context;
import android.net.Uri;

import com.slp.songwiki.R;
import com.slp.songwiki.model.Track;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Lakshmiprasad on 4/30/2017.
 */

public class LastFmUtils implements SongWikiConstants {

    public static URL getTopArtistUrl(Context context) throws MalformedURLException {
        String country = PreferenceUtils.getCountry(context);
        Uri.Builder builder;
        if (country.equals(context.getString(R.string.global))) {
            builder = Uri.parse(TOP_ARTISTS_GLOBAL_BASE_URL).buildUpon();
        } else {
            builder = Uri.parse(TOP_ARTISTS_BASE_URL).buildUpon()
                    .appendQueryParameter(COUNTRY, country);
        }
        builder.appendQueryParameter(LIMIT, PreferenceUtils.getTopArtistsLimit(context));
        appendAPIKey(builder);
        return new URL(builder.build().toString());
    }

    public static URL getTopArtistUrl() throws MalformedURLException {
        Uri.Builder builder = Uri.parse(TOP_ARTISTS_BASE_URL).buildUpon()
                .appendQueryParameter(LIMIT, String.valueOf(ARTIST_LIMIT));
        appendAPIKey(builder);
        return new URL(builder.build().toString());
    }

    @Deprecated
    public static URL getTopTracksUrl() throws MalformedURLException {
        Uri.Builder builder = Uri.parse(TOP_TRACKS_BASE_URL).buildUpon()
                .appendQueryParameter(LIMIT, String.valueOf(TRACK_LIMIT));
        appendAPIKey(builder);
        return new URL(builder.build().toString());
    }


    public static URL getTopTracksUrl(Context context) throws MalformedURLException {
        String country = PreferenceUtils.getCountry(context);
        Uri.Builder builder;
        if (context.getString(R.string.global).equals(country)) {
            builder = Uri.parse(TOP_TRACKS_GLOBAL_BASE_URL).buildUpon();
        } else {
            builder = Uri.parse(TOP_TRACKS_BASE_URL).buildUpon()
                    .appendQueryParameter(COUNTRY, country);
        }
        builder.appendQueryParameter(LIMIT, PreferenceUtils.getTopTracksLimit(context));
        appendAPIKey(builder);
        return new URL(builder.build().toString());
    }

    public static String getImage(JSONArray imageArray) throws JSONException {
        if (null != imageArray) {
            JSONObject imageObj = (JSONObject) imageArray.get(IMAGE_SIZE);
            if (null != imageObj) {
                return imageObj.getString(TEXT);
            }
        }
        return null;
    }

    public static URL getArtistInfoUrl(String artist, Context context) throws MalformedURLException, UnsupportedEncodingException {
        Uri.Builder builder = Uri.parse(ARTIST_INFO_BASE_URL).buildUpon().appendQueryParameter(ARTIST, artist);
        builder.appendQueryParameter(LANGUAGE, PreferenceUtils.getLanguage(context));
        appendAPIKey(builder);
        return new URL(builder.build().toString());
    }

    public static URL getTrackInfoUrl(String artist, String track) throws MalformedURLException {
        Uri.Builder builder = Uri.parse(TRACK_INFO_BASE__URL).buildUpon().appendQueryParameter(ARTIST, artist)
                .appendQueryParameter(TRACK, track);
        appendAPIKey(builder);
        return new URL(builder.build().toString());
    }

    private static void appendAPIKey(Uri.Builder builder) {
        builder.appendQueryParameter(API_KEY, LAST_FM_API_KEY);
    }

    public static URL getSearchArtistUrl(String artist) throws MalformedURLException {
        Uri.Builder builder = Uri.parse(SEARCH_ARTIST_BASE_URL).buildUpon().appendQueryParameter(ARTIST, artist);
        appendAPIKey(builder);
        return new URL(builder.build().toString());
    }

    public static URL getSearchTrackUrl(String track) throws MalformedURLException {
        Uri.Builder builder = Uri.parse(SEARCH_TRACK_BASE_URL).buildUpon().appendQueryParameter(TRACK, track);
        appendAPIKey(builder);
        return new URL(builder.build().toString());
    }

    public static URL getSimilarTracksUrl(Track track) throws MalformedURLException {
        Uri.Builder builder = Uri.parse(SIMILAR_TRACKS_BASE_URL).buildUpon().appendQueryParameter(TRACK, track.getTitle())
                .appendQueryParameter(ARTIST, track.getArtist());
        appendAPIKey(builder);
        return new URL(builder.build().toString());
    }

    public static URL getArtistTopTracksUrl(String artist) throws MalformedURLException {
        Uri.Builder builder = Uri.parse(ARTIST_TOP_TRACKS_BASE_URL).buildUpon().appendQueryParameter(ARTIST, artist);
        appendAPIKey(builder);
        return new URL(builder.build().toString());
    }
}
