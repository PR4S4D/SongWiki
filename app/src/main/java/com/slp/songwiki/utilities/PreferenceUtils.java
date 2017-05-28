package com.slp.songwiki.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.slp.songwiki.R;

/**
 * Created by Lakshmiprasad on 5/14/2017.
 */

public class PreferenceUtils {
    public static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getTopTracksLimit(Context context){
        return getPreferences(context).getString("top_tracks_limit", context.getString(R.string.default_limit));
    }

    public static String getTopArtistsLimit(Context context){
        return getPreferences(context).getString("top_artists_limit", context.getString(R.string.default_limit));
    }

    public static String getCountry(Context context){
        return getPreferences(context).getString("country",context.getString(R.string.default_country));
    }
}
