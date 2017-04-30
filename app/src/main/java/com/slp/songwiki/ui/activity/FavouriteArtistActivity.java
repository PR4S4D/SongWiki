package com.slp.songwiki.ui.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.slp.songwiki.R;
import com.slp.songwiki.model.Artist;
import com.slp.songwiki.utilities.ArtistUtils;

import java.util.List;

public class FavouriteArtistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_artist);
        new QueryArtistTask().execute();
    }

    class QueryArtistTask extends AsyncTask<String,Void,List<Artist>>{

        @Override
        protected List<Artist> doInBackground(String... params) {

            return ArtistUtils.getFavouriteArtists(getApplicationContext());
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {

            super.onPostExecute(artists);
        }
    }
}
