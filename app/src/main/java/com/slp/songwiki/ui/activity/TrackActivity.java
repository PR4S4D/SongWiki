package com.slp.songwiki.ui.activity;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.slp.songwiki.R;
import com.slp.songwiki.model.Artist;
import com.slp.songwiki.model.Track;
import com.slp.songwiki.utilities.TrackUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TrackActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Void> {

    private static final int TRACK_LOADER = 456;
    private Track track;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        ButterKnife.bind(this);
        track = getIntent().getParcelableExtra("track");
        setTrackInfo();
        getSupportLoaderManager().initLoader(TRACK_LOADER,null,this);

    }

    private void setTrackInfo() {
        Picasso.with(this).load(track.getImageLink()).into(trackImage);
        title.setText(track.getTitle());
        artist.setText(track.getArtist());
    }

    @Override
    public Loader<Void> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Void>(getApplicationContext()) {
            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public Void loadInBackground() {
                try {
                    TrackUtils.addTrackInfo(track);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        summary.setText(track.getSummary());
        album.setText(track.getAlbum());
    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {

    }

    public void goToArtist(View view) {
        Intent intent = new Intent(this,ArtistActivity.class);
        Artist artist = new Artist();
        artist.setName(track.getArtist());
        intent.putExtra("artist",artist);
        startActivity(intent);
    }
}
