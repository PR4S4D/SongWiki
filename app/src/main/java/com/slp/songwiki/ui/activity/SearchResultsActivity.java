package com.slp.songwiki.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.slp.songwiki.R;
import com.slp.songwiki.adapter.ArtistAdapter;
import com.slp.songwiki.adapter.TrackAdapter;
import com.slp.songwiki.model.Artist;
import com.slp.songwiki.model.Track;
import com.slp.songwiki.utilities.ArtistUtils;
import com.slp.songwiki.utilities.SongWikiConstants;
import com.slp.songwiki.utilities.TrackUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class SearchResultsActivity extends AppCompatActivity implements ArtistAdapter.ListItemClickListener, SongWikiConstants, TrackAdapter.TrackItemClickListener {

    @Bind(R.id.rv_artists_results)
    RecyclerView rvArtists;
    @Bind(R.id.rv_tracks_results)
    RecyclerView rvTracks;
    @Bind(R.id.search_loader)
    ProgressBar searchLoader;
    @Bind(R.id.error)
    TextView error;
    @Bind(R.id.tracks_label)
    TextView tracksLabel;
    @Bind(R.id.artists_label)
    TextView artistsLabel;
    private boolean noArtists = false;
    private boolean noTracks = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        ButterKnife.bind(this);

        String searchQuery = getIntent().getStringExtra(SEARCH_QUERY);
        new ArtistLoader().execute(searchQuery);

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

    class ArtistLoader extends AsyncTask<String, Void, List<Artist>> {

        @Override
        protected void onPreExecute() {
            searchLoader.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected List<Artist> doInBackground(String... params) {
            String searchQuery = params[0];
            List<Artist> artistResult = null;
            try {
                artistResult = ArtistUtils.getArtistResult(searchQuery);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return artistResult;
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            new TrackSearchTask().execute(getIntent().getStringExtra(SEARCH_QUERY));
            if (null != artists && artists.size() > 0) {
                setTitle(getString(R.string.top_results));
                artistsLabel.setVisibility(View.VISIBLE);
                error.setVisibility(View.GONE);
                ArtistAdapter adapter = new ArtistAdapter(artists, SearchResultsActivity.this);
                rvArtists.setAdapter(adapter);
                GridLayoutManager layout = new GridLayoutManager(SearchResultsActivity.this, 1);
                layout.setOrientation(LinearLayoutManager.HORIZONTAL);
                layout.setInitialPrefetchItemCount(2);
                rvArtists.setLayoutManager(layout);
                rvArtists.setHasFixedSize(true);
                rvArtists.setNestedScrollingEnabled(false);
                searchLoader.setVisibility(GONE);
                adapter.notifyDataSetChanged();
            } else {
                noArtists = true;

            }
        }
    }

    private class TrackSearchTask extends AsyncTask<String, Void, List<Track>> {
        @Override
        protected void onPreExecute() {
            searchLoader.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected List<Track> doInBackground(String... params) {
            String track = params[0];
            List<Track> trackResult = null;
            try {
                trackResult = TrackUtils.getTrackResult(track);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return trackResult;
        }

        @Override
        protected void onPostExecute(List<Track> tracks) {
            if (null != tracks && tracks.size() > 0) {
                tracksLabel.setVisibility(View.VISIBLE);
                rvTracks.setAdapter(new TrackAdapter(tracks, SearchResultsActivity.this));
                int gridSize = getResources().getInteger(R.integer.track_grid);

                rvTracks.setLayoutManager(new GridLayoutManager(SearchResultsActivity.this, gridSize));
                rvTracks.setHasFixedSize(true);
                rvTracks.setNestedScrollingEnabled(false);
            } else {
                noTracks = true;
                updateErrorView();
            }

            super.onPostExecute(tracks);
        }
    }

    private void updateErrorView() {
        if (noTracks && noArtists) {
            searchLoader.setVisibility(GONE);
            error.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onTrackItemClick(int position) {
        Track clickedTrack = ((TrackAdapter) rvTracks.getAdapter()).getItem(position);
        Pair[] pairs = new Pair[1];
        TrackAdapter.TrackViewHolder viewHolder = (TrackAdapter.TrackViewHolder) rvTracks.findViewHolderForAdapterPosition(position);
        pairs[0] = new Pair<>(viewHolder.getTrackImage(), viewHolder.getArtist().getText());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, pairs);
        Intent intent = new Intent(this, TrackActivity.class);

        intent.putExtra("track", clickedTrack);
        startActivity(intent, options.toBundle());
    }

}
