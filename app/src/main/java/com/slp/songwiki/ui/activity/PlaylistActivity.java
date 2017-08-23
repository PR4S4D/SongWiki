package com.slp.songwiki.ui.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.slp.songwiki.R;
import com.slp.songwiki.adapter.TrackAdapter;
import com.slp.songwiki.model.Track;
import com.slp.songwiki.utilities.SongWikiConstants;
import com.slp.songwiki.utilities.TrackUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlaylistActivity extends AppCompatActivity implements TrackAdapter.TrackItemClickListener, SongWikiConstants {

    @Bind(R.id.rv_tracks)
    RecyclerView rvTracks;
    private List<Track> tracks;
    private List<String> videoIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        ButterKnife.bind(this);
        new GetPlaylist().execute();
    }

    @Override
    public void onTrackItemClick(int position) {
        startActivity(YouTubeStandalonePlayer.createVideosIntent(this, YOUTUBE_PLAYER_API_KEY, videoIds, position, 0, true, false));
    }

    public void shuffle(View view) {
        List<String> shuffledVideoIds = new ArrayList<>(videoIds);

        Collections.shuffle(shuffledVideoIds);
        startActivity(YouTubeStandalonePlayer.createVideosIntent(this, YOUTUBE_PLAYER_API_KEY, shuffledVideoIds, 0, 0, true, false));
    }

    private class GetPlaylist extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            tracks = TrackUtils.getTracksFromPlaylist(getApplicationContext());
            videoIds = TrackUtils.getVideoIdsFromPlaylist(getApplicationContext());
            Log.i(TAG, "doInBackground: " + videoIds);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            Log.i("onPostExecute: "
                    + tracks, "");
            rvTracks.setAdapter(new TrackAdapter(tracks, PlaylistActivity.this));
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            rvTracks.setLayoutManager(layoutManager);
            rvTracks.setHasFixedSize(true);
            super.onPostExecute(o);
        }
    }
}
