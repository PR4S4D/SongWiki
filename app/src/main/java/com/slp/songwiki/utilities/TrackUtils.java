package com.slp.songwiki.utilities;

import android.util.Log;

import com.slp.songwiki.model.Track;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lshivaram on 4/30/2017.
 */

public class TrackUtils {

    public static List<Track> getTopChartTracks() throws IOException, JSONException {
        List<Track> topTracks = new ArrayList<>();
        String response = NetworkUtils.getResponseFromHttpUrl(LastFmUtils.getTopTracksUrl());
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray trackArray = jsonResponse.getJSONObject("tracks").getJSONArray("track");

        return getTracks(trackArray);
    }

    private static List<Track> getTracks(JSONArray jsonArray) throws JSONException {
        List<Track> tracks = new ArrayList<>();
        String trackTitle = null;
        String artist = null;
        long listeners = 0;
        String imageLink = null;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject trackInfo = (JSONObject) jsonArray.get(i);
            trackTitle = trackInfo.getString("name");
            artist = trackInfo.getJSONObject("artist").getString("name");
            if (trackInfo.has("listeners"))
                listeners = Long.valueOf(trackInfo.getString("listeners"));
            imageLink = LastFmUtils.getImage(trackInfo.getJSONArray("image"));
            tracks.add(new Track(trackTitle, artist, listeners, imageLink));
        }
        return tracks;
    }

    public static void addTrackInfo(Track track) throws IOException, JSONException {
        String response = NetworkUtils.getResponseFromHttpUrl(LastFmUtils.getTrackInfoUrl(track.getArtist(), track.getTitle()));
        JSONObject jsonObject = new JSONObject(response);
        JSONObject trackInfo = jsonObject.getJSONObject("track");
        if (trackInfo.has("album")) {

            JSONObject albumJson = trackInfo.getJSONObject("album");
            String album = albumJson.getString("title");
            track.setAlbum(album);
        }

        if (trackInfo.has("wiki")) {
            JSONObject trackWiki = trackInfo.getJSONObject("wiki");
            track.setSummary(trackWiki.getString("summary"));
        }

        if (trackInfo.has("toptags")) {
            track.setTags(getTopTags(trackInfo.getJSONObject("toptags").getJSONArray("tag")));
        }
    }

    private static List<String> getTopTags(JSONArray tagArray) throws JSONException {
        List<String> tags = new ArrayList<>();
        if (null != tags)
            for (int i = 0; i < tagArray.length(); i++) {
                tags.add(tagArray.getJSONObject(i).getString("name"));
            }
        return tags;
    }

    public static List<Track> getTrackResult(String track) throws IOException, JSONException {
        Log.i( "getTrackResult: ",LastFmUtils.getSearchTrackUrl(track).toString());
        String resultsJson = NetworkUtils.getResponseFromHttpUrl(LastFmUtils.getSearchTrackUrl(track));
        JSONObject json = new JSONObject(resultsJson);
        JSONObject resultJson = json.getJSONObject("results");
        JSONArray trackArray = resultJson.getJSONObject("trackmatches").getJSONArray("track");
        return getTracksFromMatches(trackArray);
    }

    private static List<Track> getTracksFromMatches(JSONArray trackArray) throws JSONException {
        List<Track> tracks = new ArrayList<>();
        String trackTitle = null;
        String artist = null;
        long listeners = 0;
        String imageLink = null;
        for (int i = 0; i < trackArray.length(); i++) {
            JSONObject trackInfo = (JSONObject) trackArray.get(i);
            trackTitle = trackInfo.getString("name");
            artist = trackInfo.getString("artist");
            listeners = Long.valueOf(trackInfo.getString("listeners"));
            imageLink = LastFmUtils.getImage(trackInfo.getJSONArray("image"));
            tracks.add(new Track(trackTitle, artist, listeners, imageLink));
        }
        return tracks;
    }

    public static List<Track> getSimilarTracks(Track track) throws IOException, JSONException {
        String response = NetworkUtils.getResponseFromHttpUrl(LastFmUtils.getSimilarTracksUrl(track));
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray similarTracks = jsonResponse.getJSONObject("similartracks").getJSONArray("track");
        return getTracks(similarTracks);
    }
}
