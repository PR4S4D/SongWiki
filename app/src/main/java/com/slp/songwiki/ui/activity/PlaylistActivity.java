package com.slp.songwiki.ui.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.slp.songwiki.R;
import com.slp.songwiki.adapter.TrackAdapter;
import com.slp.songwiki.model.Track;
import com.slp.songwiki.utilities.TrackUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlaylistActivity extends AppCompatActivity implements TrackAdapter.TrackItemClickListener {

    @Bind(R.id.rv_tracks)
    RecyclerView rvTracks;
    private List<Track> tracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        ButterKnife.bind(this);
        new GetPlaylist().execute();
    }

    @Override
    public void onTrackItemClick(int position) {

    }

    private class GetPlaylist extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            tracks = TrackUtils.getTracksFromPlaylist(getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            Log.i("onPostExecute: "
                    + tracks, "");
            rvTracks.setAdapter(new TrackAdapter(tracks, PlaylistActivity.this));
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
            rvTracks.setLayoutManager(layoutManager);
            rvTracks.setHasFixedSize(true);
            super.onPostExecute(o);
        }
    }
}
