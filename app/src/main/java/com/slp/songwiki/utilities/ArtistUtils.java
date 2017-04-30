package com.slp.songwiki.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.slp.songwiki.data.FavouriteArtistContract;
import com.slp.songwiki.model.Artist;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.AEADBadTagException;

/**
 * Created by lshivaram on 4/30/2017.
 */

public class ArtistUtils implements SongWikiConstants {
    public static List<Artist> getTopChartArtists() throws IOException, JSONException {

        String topArtistDetails = NetworkUtils.getResponseFromHttpUrl(LastFmUtils.getTopArtistUrl());
        JSONObject topArtistJsonObject = new JSONObject(topArtistDetails);
        JSONObject artists = topArtistJsonObject.getJSONObject(ARTISTS);
        JSONArray artistArray = (JSONArray) artists.get("artist");
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
                imageLink = LastFmUtils.getImage((JSONArray) artist.get("image"));
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

    @Deprecated
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
            artist.setImageLink(LastFmUtils.getImage(artistObject.getJSONArray("image")));
        }
        return artist;
    }

    public static boolean isArtistInfoAvailable(Artist artist) {
        return  null != artist.getImageLink();
    }

    public static void setArtistDetails(Artist artist) throws IOException, JSONException {
        if (null != artist) {

            //String name = getEncodedString(artist.getName());
            String artistDetails = NetworkUtils.getResponseFromHttpUrl(LastFmUtils.getArtistInfoUrl(artist.getName()));

            if (null != artistDetails) {
                JSONObject jsonObject = new JSONObject(artistDetails);
                JSONObject artistObject = jsonObject.getJSONObject("artist");
                if(!isArtistInfoAvailable(artist)){
                    artist.setImageLink(LastFmUtils.getImage(artistObject.getJSONArray("image")));
                }
                JSONObject artistBio = artistObject.getJSONObject("bio");
                artist.setPublishedOn(artistBio.getString("published"));
                artist.setSummary(artistBio.getString("summary"));
            }
        }
    }


    public static List<Artist> getArtistResult(String artist) throws IOException, JSONException {
        //artist = getEncodedString(artist);
        URL url = new URL(SEARCH_ARTIST_END_POINT + artist);
        String resultsJson = NetworkUtils.getResponseFromHttpUrl(url);
        JSONObject json = new JSONObject(resultsJson);
        JSONObject resultJson = json.getJSONObject("results");
        JSONArray artistArray = resultJson.getJSONObject("artistmatches").getJSONArray("artist");
        return getArtists(artistArray);
    }

    public static ContentValues getArtistContent(Artist artist) {
        ContentValues cv = new ContentValues();
        cv.put(FavouriteArtistContract.ArtistEntry.ARTIST_NAME, artist.getName());
        cv.put(FavouriteArtistContract.ArtistEntry.IMAGE_LINK, LOCAL_IMAGE_BASE_URL + artist.getName() + JPG_EXTENSION);
        cv.put(FavouriteArtistContract.ArtistEntry.LISTENERS, artist.getListeners());
        cv.put(FavouriteArtistContract.ArtistEntry.PUBLISHED_ON, artist.getPublishedOn());
        cv.put(FavouriteArtistContract.ArtistEntry.CONTENT, artist.getContent());

        return cv;
    }

    public static boolean isFavourite(Context context, String artistName) {
        String[] selectionArgs = new String[]{String.valueOf(artistName)};
        Cursor cursor = context.getContentResolver().query(FavouriteArtistContract.ArtistEntry.CONTENT_URI, null, FavouriteArtistContract.ArtistEntry.ARTIST_NAME + "=?", selectionArgs, FavouriteArtistContract.ArtistEntry.ARTIST_NAME);
        Log.i("cursor count", String.valueOf(cursor.getCount()));
        if (cursor.getCount() >= 1)
            return true;
        return false;
    }

    public static void saveImage(final Artist artist, final Context context) {
        Picasso.with(context)
                .load(artist.getImageLink())
                .into(new Target() {
                          @Override
                          public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                              new Thread(new Runnable() {
                                  @Override
                                  public void run() {

                                      File file = new File(
                                              Environment.getExternalStorageDirectory().getPath() + "/"
                                                      + artist.getName() + JPG_EXTENSION);
                                      Log.i("run: ", file.toString());
                                      try {
                                          file.createNewFile();
                                          FileOutputStream ostream = new FileOutputStream(file);
                                          bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                                          ostream.close();
                                      } catch (Exception e) {
                                          e.printStackTrace();
                                      }
                                  }
                              }).start();
                          }

                          @Override
                          public void onBitmapFailed(Drawable errorDrawable) {
                          }

                          @Override
                          public void onPrepareLoad(Drawable placeHolderDrawable) {
                          }
                      }
                );
    }

    public static List<Artist> getFavouriteArtists(Context context) {
        List<Artist> artists = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(FavouriteArtistContract.ArtistEntry.CONTENT_URI, null, null, null,
                FavouriteArtistContract.ArtistEntry.ARTIST_NAME);
        int name = cursor.getColumnIndex(FavouriteArtistContract.ArtistEntry.ARTIST_NAME);
        int listeners = cursor.getColumnIndex(FavouriteArtistContract.ArtistEntry.LISTENERS);
        int imageLink = cursor.getColumnIndex(FavouriteArtistContract.ArtistEntry.IMAGE_LINK);
        int publishedOn = cursor.getColumnIndex(FavouriteArtistContract.ArtistEntry.PUBLISHED_ON);
        int content = cursor.getColumnIndex(FavouriteArtistContract.ArtistEntry.CONTENT);
        if (cursor.moveToFirst()) {


            do {
                Artist artist = new Artist(cursor.getString(name), cursor.getLong(listeners), cursor.getString(imageLink), cursor.getString(publishedOn), null, cursor.getColumnName(content));
                Log.i("Artist", artist.toString());
                artists.add(artist);
            } while (cursor.moveToNext());

        }

        return artists;
    }


}
