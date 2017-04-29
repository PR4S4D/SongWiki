package com.slp.songwiki.ui.activity;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.widget.ImageView;
import android.widget.TextView;

import com.slp.songwiki.R;
import com.slp.songwiki.model.Artist;
import com.slp.songwiki.utilities.SongWikiUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArtistActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>,SearchView.OnQueryTextListener  {

    private Artist artist;
    @Bind(R.id.artist_image)
    ImageView artistImage;
    @Bind(R.id.listeners)
    TextView listeners;
    @Bind(R.id.artist_name)
    TextView artistName;
    @Bind(R.id.artist_summary)
    TextView summary;
    @Bind(R.id.publish_date)
    TextView publishDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        ButterKnife.bind(this);
        artist = getIntent().getParcelableExtra("artist");
        showArtistDetails();
        getSupportLoaderManager().initLoader(12, null, this);
    }

    private void showArtistDetails() {
        Picasso.with(this).load(artist.getImageLink()).into(artistImage);
        listeners.setText(String.valueOf(artist.getListeners()));
        artistName.setText(artist.getName());
    }


    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String>(getApplicationContext()) {
            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                try {
                    SongWikiUtils.setArtistDetails(artist);

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return "";
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        summary.setText(artist.getSummary());
        publishDate.setText(artist.getPublishedOn());
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //friendListAdapter.getFilter().filter(newText);

        return true;
    }
}