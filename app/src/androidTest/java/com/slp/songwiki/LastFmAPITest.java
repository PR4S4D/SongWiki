package com.slp.songwiki;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.slp.songwiki.model.Artist;
import com.slp.songwiki.ui.activity.SongWikiActivity;
import com.slp.songwiki.utilities.ArtistUtils;

import org.json.JSONException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

/**
 * Created by lshivaram on 5/6/2017.
 */

@RunWith(AndroidJUnit4.class)
public class LastFmAPITest {
    @Rule
    public ActivityTestRule<SongWikiActivity> mActivityRule = new ActivityTestRule<>(
            SongWikiActivity.class);

    @Test
    public void apiTest() throws Throwable {
        mActivityRule.runOnUiThread(new Runnable() {
            @SuppressLint("Assert")
            @Override
            public void run() {


                new AsyncTask() {

                    @Override
                    protected Object doInBackground(Object[] params) {
                        List<Artist> artists = null;
                        try {
                            artists = ArtistUtils.getTopChartArtists();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                        assert null != artists;
                        return null;
                    }
                };

            }
        });
    }
}
