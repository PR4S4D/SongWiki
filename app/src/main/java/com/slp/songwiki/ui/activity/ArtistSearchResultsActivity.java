package com.slp.songwiki.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.slp.songwiki.R;
import com.slp.songwiki.adapter.ArtistAdapter;
import com.slp.songwiki.model.Artist;
import com.slp.songwiki.utilities.ArtistUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lshivaram on 4/30/2017.
 */

public class ArtistSearchResultsActivity  extends AppCompatActivity implements ArtistAdapter.ListItemClickListener {

    @Bind(R.id.rv_artists)
    RecyclerView rvArtists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_search_results);
        ButterKnife.bind(this);
        String searchQuery = getIntent().getStringExtra("artist");
        new SearchTask().execute(searchQuery);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Artist clickedArtist = ((ArtistAdapter) rvArtists.getAdapter()).getItem(clickedItemIndex);
        Intent intent = new Intent(this, ArtistActivity.class);
        intent.putExtra("artist", clickedArtist);
        startActivity(intent);

    }

    class SearchTask extends AsyncTask<String, Void, List<Artist>> {

        @Override
        protected List<Artist> doInBackground(String... params) {
            String artist = params[0];
            List<Artist> artistResult = null;
            try {
                artistResult = ArtistUtils.getArtistResult(artist);
                Log.i("doInBackground: ", String.valueOf(artistResult));

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return artistResult;
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            rvArtists.setAdapter(new ArtistAdapter(artists,ArtistSearchResultsActivity.this));
            int gridSize =2;
            rvArtists.setLayoutManager(new GridLayoutManager(ArtistSearchResultsActivity.this,gridSize));
            rvArtists.setHasFixedSize(true);
            super.onPostExecute(artists);
        }
    }


}
