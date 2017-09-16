package com.slp.songwiki.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.slp.songwiki.R;
import com.slp.songwiki.adapter.ArtistAdapter;
import com.slp.songwiki.model.Artist;
import com.slp.songwiki.utilities.ArtistUtils;
import com.slp.songwiki.utilities.SongWikiConstants;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.GONE;

/**
 * Created by Lakshmiprasad on 4/30/2017.
 */

public class ArtistSearchResultsActivity extends AppCompatActivity implements ArtistAdapter.ListItemClickListener, SongWikiConstants {

    @Bind(R.id.rv_artists)
    RecyclerView rvArtists;
    @Bind(R.id.artist_search_loader)
    ProgressBar artistSearchLoader;
    @Bind(R.id.error)
    TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_search_results);
        ButterKnife.bind(this);
        String searchQuery = getIntent().getStringExtra(ARTIST);
        new SearchTask().execute(searchQuery);
        rvArtists.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onArtistItemClick(int clickedItemIndex) {
        Artist clickedArtist = ((ArtistAdapter) rvArtists.getAdapter()).getItem(clickedItemIndex);
        Pair[] pairs = new Pair[1];
        ArtistAdapter.ArtistViewHolder viewHolder = (ArtistAdapter.ArtistViewHolder) rvArtists.findViewHolderForAdapterPosition(clickedItemIndex);
        pairs[0] = new Pair<>(viewHolder.getArtistImage(), viewHolder.getArtistName().getText());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, pairs);
        Intent intent = new Intent(this, ArtistActivity.class);

        intent.putExtra(ARTIST, clickedArtist);
        startActivity(intent, options.toBundle());

    }

    class SearchTask extends AsyncTask<String, Void, List<Artist>> {

        @Override
        protected void onPreExecute() {
            artistSearchLoader.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected List<Artist> doInBackground(String... params) {
            String artist = params[0];
            List<Artist> artistResult = null;
            try {
                artistResult = ArtistUtils.getArtistResult(artist);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return artistResult;
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            if (null != artists && artists.size() > 0) {
                setTitle(getString(R.string.top_results));
                error.setVisibility(View.GONE);
                ArtistAdapter adapter = new ArtistAdapter(artists, ArtistSearchResultsActivity.this);
                rvArtists.setAdapter(adapter);
                int gridSize = getResources().getInteger(R.integer.artist_grid);
                rvArtists.setLayoutManager(new GridLayoutManager(ArtistSearchResultsActivity.this, gridSize));
                rvArtists.setHasFixedSize(true);
                artistSearchLoader.setVisibility(GONE);
                adapter.notifyDataSetChanged();
            } else {
                artistSearchLoader.setVisibility(GONE);
                error.setVisibility(View.VISIBLE);
            }
        }
    }


}
