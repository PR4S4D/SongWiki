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

/**
 * Created by lshivaram on 4/30/2017.
 */

public class ArtistUtils implements SongWikiConstants {


    public static List<Artist> getTopChartArtists() throws IOException, JSONException {

        String topArtistDetails = NetworkUtils.getResponseFromHttpUrl(LastFmUtils.getTopArtistUrl());
        JSONObject topArtistJsonObject = new JSONObject(topArtistDetails);
        JSONObject artists = topArtistJsonObject.getJSONObject(TOP_ARTISTS);
        JSONArray artistArray = (JSONArray) artists.get(ARTIST);
        return getArtists(artistArray);
    }

    @NonNull
    private static List<Artist> getArtists(JSONArray artistArray) throws JSONException {
        String name;
        //long listeners = null;
        String imageLink = "";
        String artistLink = "";
        List<Artist> artists = new ArrayList<>();
        for (int i = 0; i < artistArray.length(); i++) {
            JSONObject artist = (JSONObject) artistArray.get(i);
            if (null != artist) {
                name = (String) artist.get(NAME);
                artistLink = artist.getString(URL);
                imageLink = LastFmUtils.getImage((JSONArray) artist.get(IMAGE));
                artists.add(new Artist(name, imageLink, artistLink));

            }
        }
        return artists;
    }


    public static List<String> getTopChartArtistNames() throws IOException, JSONException {
        List<String> topChartArtist = new ArrayList<>();
        String topArtistDetails = NetworkUtils.getResponseFromHttpUrl(new URL(TOP_ARTISTS_END_POINT));
        JSONObject topArtistJsonObject = new JSONObject(topArtistDetails);
        JSONObject artists = topArtistJsonObject.getJSONObject(ARTISTS);
        JSONArray artistArray = (JSONArray) artists.get(ARTIST);
        for (int i = 0; i < artistArray.length(); i++) {
            JSONObject artist = (JSONObject) artistArray.get(i);
            if (null != artist) {
                topChartArtist.add(artist.getString(NAME));
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
        return URLEncoder.encode(string, UTF_8);
    }

    public static Artist getArtist(String artistDetails) throws JSONException {
        Artist artist = new Artist();
        if (null != artistDetails) {
            JSONObject jsonObject = new JSONObject(artistDetails);
            JSONObject artistObject = jsonObject.getJSONObject(ARTIST);
            artist.setName(artistObject.getString(NAME));
            artist.setImageLink(LastFmUtils.getImage(artistObject.getJSONArray(IMAGE)));

        }
        return artist;
    }

    public static boolean isArtistImageSet(Artist artist) {
        return null != artist.getImageLink();
    }

    public static void setArtistDetails(Artist artist) throws IOException, JSONException {
        if (null != artist) {

            //String name = getEncodedString(artist.getName());
            String artistDetails = NetworkUtils.getResponseFromHttpUrl(LastFmUtils.getArtistInfoUrl(artist.getName()));

            if (null != artistDetails) {
                JSONObject jsonObject = new JSONObject(artistDetails);
                JSONObject artistObject = jsonObject.getJSONObject(ARTIST);
                if (!isArtistImageSet(artist)) {
                    artist.setImageLink(LastFmUtils.getImage(artistObject.getJSONArray(IMAGE)));
                }
                artist.setListeners(Long.valueOf(artistObject.getJSONObject(STATS).getString(LISTENERS)));
                JSONObject artistBio = artistObject.getJSONObject(BIO);

                if (artistObject.has(SIMILAR)) {
                    List<Artist> similarArtists = getArtists(artistObject.getJSONObject(SIMILAR).getJSONArray(ARTIST));
                    artist.setSimilarArtists(similarArtists);
                }
                if (artistObject.has(TAGS)) {
                    List<String> tags = getTags(artistObject.getJSONObject(TAGS).getJSONArray(TAG));
                    artist.setTags(tags);
                }

                artist.setPublishedOn(artistBio.getString(PUBLISHED));
                artist.setSummary(artistBio.getString(SUMMARY));
                artist.setContent(artistBio.getString(CONTENT));
                if (null == artist.getArtistLink())
                    artist.setArtistLink(artistObject.getString(URL));
            }
        }
    }

    private static List<String> getTags(JSONArray tagArray) throws JSONException {
        List<String> tags = new ArrayList<>();
        for (int i = 0; i < tagArray.length(); i++) {
            tags.add(tagArray.getJSONObject(i).getString(NAME));
        }
        return tags;
    }


    public static List<Artist> getArtistResult(String artist) throws IOException, JSONException {
        String resultsJson = NetworkUtils.getResponseFromHttpUrl(LastFmUtils.getSearchArtistUrl(artist));
        JSONObject json = new JSONObject(resultsJson);
        JSONObject resultJson = json.getJSONObject(RESULTS);
        JSONArray artistArray = resultJson.getJSONObject(ARTIST_MATCHES).getJSONArray(ARTIST);
        return getArtists(artistArray);
    }

    public static ContentValues getArtistContent(Artist artist) {
        ContentValues cv = new ContentValues();
        cv.put(FavouriteArtistContract.ArtistEntry.ARTIST_NAME, artist.getName());
        cv.put(FavouriteArtistContract.ArtistEntry.IMAGE_LINK, LOCAL_IMAGE_BASE_URL + artist.getName() + JPG_EXTENSION);
        cv.put(FavouriteArtistContract.ArtistEntry.LISTENERS, artist.getListeners());
        cv.put(FavouriteArtistContract.ArtistEntry.PUBLISHED_ON, artist.getPublishedOn());
        cv.put(FavouriteArtistContract.ArtistEntry.CONTENT, artist.getContent());
        cv.put(FavouriteArtistContract.ArtistEntry.SUMMARY, artist.getSummary());

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
        int summary = cursor.getColumnIndex(FavouriteArtistContract.ArtistEntry.SUMMARY);
        if (cursor.moveToFirst()) {


            do {
                Artist artist = new Artist(cursor.getString(name), cursor.getLong(listeners), cursor.getString(imageLink), cursor.getString(publishedOn), cursor.getString(summary), cursor.getColumnName(content));
                Log.i("Artist", artist.toString());
                artists.add(artist);
            } while (cursor.moveToNext());

        }

        return artists;
    }


}
