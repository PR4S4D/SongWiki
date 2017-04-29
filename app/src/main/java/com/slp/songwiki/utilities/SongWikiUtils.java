package com.slp.songwiki.utilities;

import android.support.annotation.NonNull;
import android.util.Log;

import com.slp.songwiki.model.Artist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lshivaram on 4/30/2017.
 */

public class SongWikiUtils implements SongWikiConstants {
    public static List<Artist> getTopChartArtists() throws IOException, JSONException {

        String topArtistDetails = NetworkUtils.getResponseFromHttpUrl(new URL(TOP_ARTISTS_END_POINT));
        JSONObject topArtistJsonObject = new JSONObject(topArtistDetails);
        JSONObject artists = topArtistJsonObject.getJSONObject(ARTISTS);
        JSONArray artistArray = (JSONArray) artists.get("artist");

        String name;
        long listeners;
        String imageLink;

        return getArtists(artistArray);
    }

    @NonNull
    private static List<Artist> getArtists(JSONArray artistArray) throws JSONException {
        String name;
        long listeners;
        String imageLink;
        List<Artist> artists = new ArrayList<>();
        for (int i = 0; i < artistArray.length(); i++) {
            JSONObject artist = (JSONObject) artistArray.get(i);
            if (null != artist) {
                name = (String) artist.get("name");
                listeners = Long.valueOf((String) artist.get("listeners"));
                imageLink = getArtistImage((JSONArray) artist.get("image"));
                artists.add(new Artist(name, listeners, imageLink));
            }
        }
        return artists;
    }

    public static List<String> getTopChartArtistNames() throws IOException, JSONException {
        List<String> topChartArtist = new ArrayList<>();
        String topArtistDetails = NetworkUtils.getResponseFromHttpUrl(new URL(TOP_ARTISTS_END_POINT));
        JSONObject topArtistJsonObject = new JSONObject(topArtistDetails);
        JSONObject artists = topArtistJsonObject.getJSONObject(ARTISTS);
        JSONArray artistArray = (JSONArray) artists.get("artist");
        for (int i = 0; i < artistArray.length(); i++) {
            JSONObject artist = (JSONObject) artistArray.get(i);
            if (null != artist) {
                topChartArtist.add(artist.getString("name"));
            }
        }
        return topChartArtist;
    }

    public static List<Artist> getTopChartArtist() throws IOException, JSONException {
        List<Artist> artists =
                new ArrayList<>();
        List<String> artistNames = getTopChartArtistNames();
        if (null != artistNames) {
            for (String name : artistNames) {
                name = getEncodedString(name);
                URL url = new URL(ARTIST_INFO_END_POINT + name);
                String artistDetails = NetworkUtils.getResponseFromHttpUrl(url);
                Log.i("getTopChartArtist: ", artistDetails);
                artists.add(getArtist(artistDetails));
            }
        }
        return artists;
    }

    private static String getEncodedString(String string) throws UnsupportedEncodingException {
        return URLEncoder.encode(string, "UTF-8");
    }

    public static Artist getArtist(String artistDetails) throws JSONException {
        Artist artist = new Artist();
        if (null != artistDetails) {
            JSONObject jsonObject = new JSONObject(artistDetails);
            JSONObject artistObject = jsonObject.getJSONObject("artist");
            artist.setName(artistObject.getString("name"));
            artist.setImageLink(getArtistImage(artistObject.getJSONArray("image")));
        }
        return artist;
    }

    public static void setArtistDetails(Artist artist) throws IOException, JSONException {
        if(null != artist){
            String name = getEncodedString(artist.getName());
            URL url = new URL(ARTIST_INFO_END_POINT + name);
            String artistDetails = NetworkUtils.getResponseFromHttpUrl(url);

            if (null != artistDetails) {
                JSONObject jsonObject = new JSONObject(artistDetails);
                JSONObject artistObject = jsonObject.getJSONObject("artist");

                JSONObject artistBio = artistObject.getJSONObject("bio");
                artist.setPublishedOn(artistBio.getString("published"));
                artist.setSummary(artistBio.getString("summary"));
            }
        }
    }

    private static String getArtistImage(JSONArray imageArray) throws JSONException {
        if (null != imageArray) {
            JSONObject imageObj = (JSONObject) imageArray.get(IMAGE_SIZE);
            if (null != imageObj) {
                return imageObj.getString("#text");
            }


        }
        return null;
    }

    public static List<Artist> getArtistResult(String artist) throws IOException, JSONException {
        artist = getEncodedString(artist);
        URL url = new URL(SEARCH_ARTIST_END_POINT+artist);
        String resultsJson = NetworkUtils.getResponseFromHttpUrl(url);
        JSONObject json = new JSONObject(resultsJson);
        JSONObject resultJson = json.getJSONObject("results");
        JSONArray artistArray = resultJson.getJSONObject("artistmatches").getJSONArray("artist");
        return getArtists(artistArray);



    }
}
