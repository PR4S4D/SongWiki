package com.slp.songwiki.utilities;

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
            listeners = Long.valueOf(trackInfo.getString("listeners"));
            imageLink = LastFmUtils.getImage(trackInfo.getJSONArray("image"));
            tracks.add(new Track(trackTitle,artist,listeners,imageLink));
        }
        return tracks;
    }

    public static void addTrackInfo(Track track) throws IOException, JSONException {
        String response = NetworkUtils.getResponseFromHttpUrl(LastFmUtils.getTrackInfoUrl(track.getArtist(),track.getTitle()));
        JSONObject jsonObject = new JSONObject(response);
        JSONObject trackInfo = jsonObject.getJSONObject("track");
        if(trackInfo.has("album")){

            JSONObject albumJson = trackInfo.getJSONObject("album");
            String album = albumJson.getString("title");
            track.setAlbum(album);
        }

        if(trackInfo.has("wiki")){
            JSONObject trackWiki = trackInfo.getJSONObject("wiki");
            track.setSummary(trackWiki.getString("summary"));
        }


    }
}
