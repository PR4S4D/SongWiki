package com.slp.songwiki.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.slp.songwiki.R;
import com.slp.songwiki.adapter.TrackAdapter;
import com.slp.songwiki.model.Track;
import com.slp.songwiki.utilities.TrackUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TrackSearchResultsActivity extends AppCompatActivity implements TrackAdapter.TrackItemClickListener {
    @Bind(R.id.rv_tracks)
    RecyclerView rvTracks;
    @Bind(R.id.track_search_loader)
    ProgressBar trackSearchLoader;
    @Bind(R.id.error)
    TextView error;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_search_results_activty);
        ButterKnife.bind(this);
        String searchQuery = getIntent().getStringExtra("track");
        new SearchTask().execute(searchQuery);

    }

    private class SearchTask extends AsyncTask<String, Void, List<Track>> {
        @Override
        protected void onPreExecute() {
            trackSearchLoader.setVisibility(View.VISIBLE);
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
            if (null != tracks) {
                error.setVisibility(View.GONE);

                rvTracks.setAdapter(new TrackAdapter(tracks, TrackSearchResultsActivity.this));
                int gridSize = 1;
                rvTracks.setLayoutManager(new GridLayoutManager(TrackSearchResultsActivity.this, gridSize));
                rvTracks.setHasFixedSize(true);
                trackSearchLoader.setVisibility(View.GONE);
            } else {
                trackSearchLoader.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }

            super.onPostExecute(tracks);
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
