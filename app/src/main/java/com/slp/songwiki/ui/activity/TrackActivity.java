package com.slp.songwiki.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.slp.songwiki.R;
import com.slp.songwiki.adapter.TagAdapter;
import com.slp.songwiki.adapter.TrackAdapter;
import com.slp.songwiki.model.Artist;
import com.slp.songwiki.model.Track;
import com.slp.songwiki.utilities.LastFmUtils;
import com.slp.songwiki.utilities.TrackUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TrackActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Void> {

    private static final int TRACK_LOADER = 456;
    private Track track;
    private List<Track> similarTracks;
    @Bind(R.id.track_title)
    TextView title;
    @Bind(R.id.track_image)
    ImageView trackImage;
    @Bind(R.id.artist)
    TextView artist;
    @Bind(R.id.album)
    TextView album;
    @Bind(R.id.summary)
    TextView summary;
    @Bind(R.id.rv_tags)
    RecyclerView rvTags;
    @Bind(R.id.loading_frame)
    FrameLayout loadingFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        ButterKnife.bind(this);
        track = getIntent().getParcelableExtra("track");
        setTrackInfo();
        try {
            Log.i("similar Artist", String.valueOf(LastFmUtils.getSimilarTracksUrl(track)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        getSupportLoaderManager().initLoader(TRACK_LOADER, null, this);

    }

    private void setTrackInfo() {
        trackImage.setTransitionName(track.getArtist());
        Picasso.with(this).load(track.getImageLink()).into(trackImage);
        title.setText(track.getTitle());
        artist.setText(track.getArtist());
    }

    @Override
    public Loader<Void> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Void>(getApplicationContext()) {
            @Override
            protected void onStartLoading() {
                loadingFrame.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public Void loadInBackground() {
                try {
                    TrackUtils.addTrackInfo(track);
                    similarTracks = TrackUtils.getSimilarTracks(track);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        loadingFrame.setVisibility(View.GONE);
        if (null != track.getSummary()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                summary.setText(Html.fromHtml(track.getSummary(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                summary.setText(Html.fromHtml(track.getSummary()));
            }
            summary.setMovementMethod (LinkMovementMethod.getInstance());
            summary.setClickable(true);
        }

        album.setText(track.getAlbum());
        showTags();
        Log.i("onLoadFinished: ", similarTracks.toString());
    }

    private void showTags() {
        rvTags.setAdapter(new TagAdapter(track.getTags()));
        //int gridSize = 1;
        rvTags.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        rvTags.setHasFixedSize(true);
    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {

    }

    public void goToArtist(View view) {
        Intent intent = new Intent(this, ArtistActivity.class);
        Artist artist = new Artist();
        artist.setName(track.getArtist());
        intent.putExtra("artist", artist);
        startActivity(intent);
    }
}
