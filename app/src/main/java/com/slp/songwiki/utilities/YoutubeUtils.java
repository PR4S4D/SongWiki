package com.slp.songwiki.utilities;

import android.net.Uri;

import com.slp.songwiki.model.Track;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Lakshmiprasad on 6/24/2017.
 */

public class YoutubeUtils implements SongWikiConstants {


    public static String getVideoId(Track track) throws IOException, JSONException {
        String url = Uri.parse(YOUTUBE_SEARCH_BASE_URL).buildUpon().appendQueryParameter(YOUTUBE_SERACH, track.getArtist() + " " + track.getTitle())
                .appendQueryParameter(KEY, YOUTUBE_SEARCH_API_KEY)
                .appendQueryParameter(MAX_RESULTS, String.valueOf(NUMBER_OF_VIDEOS)).build().toString();
        JSONObject searchResults = new JSONObject(NetworkUtils.getResponseFromHttpUrl(new URL(url)));
        JSONObject video = (JSONObject) searchResults.getJSONArray("items").get(0);
        return video.getJSONObject("id").getString("videoId");
    }
}
