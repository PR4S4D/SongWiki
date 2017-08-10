package com.slp.songwiki.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.slp.songwiki.R;
import com.slp.songwiki.adapter.TrackAdapter;
import com.slp.songwiki.model.Artist;
import com.slp.songwiki.model.Track;
import com.slp.songwiki.utilities.SongWikiConstants;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VideoActivity extends YouTubeBaseActivity implements SongWikiConstants, TrackAdapter.TrackItemClickListener {

    private List<Track> similarTracks;
    private String videoId;
    private int backgroundColor;
    private  int textColor;
    private String artistName;
    private String track;
    private boolean fullScreen;
    private YouTubePlayer mYouTubePlayer;
    @Bind(R.id.track_video)
    YouTubePlayerView videoPlayer;
    @Bind(R.id.rv_similar_tracks)
    RecyclerView rvSimilarTracks;
    @Bind(R.id.artist_card)
    CardView artistCard;
    @Bind(R.id.artist)
    TextView artist;
    @Bind(R.id.track_title)
    TextView trackTitle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        similarTracks =  getIntent().getParcelableArrayListExtra(SIMILAR_TRACKS);
        videoId = getIntent().getStringExtra(TRACK_VIDEO_ID);
        textColor = getIntent().getIntExtra(TEXT_COLOR,0);
        backgroundColor = getIntent().getIntExtra(BACKGROUND_COLOR,0);
        artistName = getIntent().getStringExtra(ARTIST);
        artistCard.setBackgroundColor(backgroundColor);
        artist.setText(artistName);
        artist.setTextColor(textColor);
        track = getIntent().getStringExtra(TRACK);
        trackTitle.setText(track);
        trackTitle.setTextColor(textColor);

        Log.i(TAG, "onCreate: "+similarTracks);
        Log.i(TAG, "onCreate: videoId" + videoId);
        initializeRecyclerView();
        videoPlayer.initialize(YOUTUBE_PLAYER_API_KEY,getOnInitializedListener());
    }




    private void initializeRecyclerView() {
        if (null != similarTracks && similarTracks.size() > 0) {
            rvSimilarTracks.setAdapter(new TrackAdapter(similarTracks, this));
            rvSimilarTracks.setLayoutManager(new GridLayoutManager(this, 1));
            rvSimilarTracks.setHasFixedSize(true);
            rvSimilarTracks.setNestedScrollingEnabled(false);
        }}

    @NonNull
    private YouTubePlayer.OnInitializedListener getOnInitializedListener() {
        return new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, final boolean wasRestored) {
                if(! wasRestored) {
                    youTubePlayer.loadVideo(videoId);
                }

                youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                    @Override
                    public void onFullscreen(boolean b) {
                        fullScreen = b;
                    }
                });
                mYouTubePlayer = youTubePlayer;
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
    }

    public void goToArtist(View view) {
        Intent intent = new Intent(this, ArtistActivity.class);
        Artist artist = new Artist();
        artist.setName(artistName);
        intent.putExtra("artist", artist);
        startActivity(intent);
    }


    @Override
    public void onTrackItemClick(int position) {
        if (null != similarTracks) {
            Intent trackIntent = new Intent(this, TrackActivity.class);
            Track clickedTrack = ((TrackAdapter) rvSimilarTracks.getAdapter()).getItem(position);
            TrackAdapter.TrackViewHolder viewHolder = (TrackAdapter.TrackViewHolder) rvSimilarTracks.findViewHolderForAdapterPosition(position);
            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<>(viewHolder.getTrackImage(), viewHolder.getArtist().getText());
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, pairs);
            trackIntent.putExtra("track", clickedTrack);
            startActivity(trackIntent, options.toBundle());
        }
    }

    @Override
    public void onBackPressed() {
        if(fullScreen){
            mYouTubePlayer.setFullscreen(false);
        }else if(null!=mYouTubePlayer && mYouTubePlayer.isPlaying()){
            mYouTubePlayer.pause();
        }else{
            super.onBackPressed();
        }
    }
}
