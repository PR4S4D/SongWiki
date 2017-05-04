package com.slp.songwiki.ui.activity;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.slp.songwiki.R;
import com.slp.songwiki.adapter.ArtistAdapter;
import com.slp.songwiki.data.FavouriteArtistContract;
import com.slp.songwiki.model.Artist;
import com.slp.songwiki.utilities.ArtistUtils;
import com.slp.songwiki.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArtistActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, SearchView.OnQueryTextListener, ArtistAdapter.ListItemClickListener {

    private Artist artist;
    private List<Artist> similarArtists;
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
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_tool_bar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.loading_frame)
    FrameLayout loadingFrame;
    @Bind(R.id.rv_similar_artists)
    RecyclerView rvSimilarArtists;
    @Bind(R.id.similar_artists_label)
    TextView similarArtistsLabel;
    @Bind(R.id.make_favourite)
    FloatingActionButton favFab;
    @Bind(R.id.artist_info)
    NestedScrollView artistInfo;


    private boolean basicInfoSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        ButterKnife.bind(this);
        artist = getIntent().getParcelableExtra("artist");
        collapsingToolbarLayout.setTitle(artist.getName());
        toolbar.setTitle(artist.getName());
        showArtistInfo();
        setFavouriteIcon();
    }

    private void setFavouriteIcon() {
        if (ArtistUtils.isFavourite(getApplicationContext(), artist.getName())) {
            favFab.setImageResource(R.drawable.ic_favorite);
        } else {
            favFab.setImageResource(R.drawable.make_favourite);
        }
    }

    private void showArtistInfo() {
        if (ArtistUtils.isArtistImageSet(artist))
            showArtistBasicInfo();
        if (getIntent().getBooleanExtra("isFavourite", false) && !NetworkUtils.isNetworkAvailable(this)) {
            showArtistBasicInfo();
            showArtistAdvancedInfo();
        } else {

            getSupportLoaderManager().initLoader(12, null, this);
        }
    }


    private void showArtistBasicInfo() {
        basicInfoSet = true;
        Picasso.with(this).load(artist.getImageLink()).into(artistImage);

        artistImage.setTransitionName(artist.getName());
        artistName.setText(artist.getName());
    }

    private void showArtistAdvancedInfo() {
        if (null != artist.getSummary()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                summary.setText(Html.fromHtml(artist.getSummary(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                summary.setText(Html.fromHtml(artist.getSummary()));
            }
            summary.setMovementMethod(LinkMovementMethod.getInstance());
            summary.setClickable(true);
        }
        listeners.setText(String.valueOf(artist.getListeners()));
        publishDate.setText(artist.getPublishedOn());
    }


    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String>(getApplicationContext()) {
            @Override
            protected void onStartLoading() {
                loadingFrame.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                try {
                    ArtistUtils.setArtistDetails(artist);

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return "";
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.i("onLoadFinished: ", artist.toString());
        loadingFrame.setVisibility(View.GONE);
        if (!basicInfoSet)
            showArtistBasicInfo();
        showArtistAdvancedInfo();
        showSimilarArtists();
    }

    private void showSimilarArtists() {
        if (artist.getSimilarArtists() != null && artist.getSimilarArtists().size() > 0) {
            similarArtists = artist.getSimilarArtists();
            rvSimilarArtists.setAdapter(new ArtistAdapter(similarArtists, this));
            // int gridSize = 2;
            LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            layout.setInitialPrefetchItemCount(2);
            rvSimilarArtists.setLayoutManager(layout);
            rvSimilarArtists.setHasFixedSize(true);
        }
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

    public void addToFavourites(View view) {
        checkPermissions();
        if (isWritePermissionGranted()) {
            if (ArtistUtils.isFavourite(getApplicationContext(), artist.getName())) {
                Uri uri = FavouriteArtistContract.ArtistEntry.CONTENT_URI;
                String[] args = new String[]{String.valueOf(artist.getName())};
                int id = getContentResolver().delete(uri, FavouriteArtistContract.ArtistEntry.ARTIST_NAME + "=?", args);
                if (id > 0) {
                    Toast.makeText(this, "removed from favourites", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.i("addToFavourites: ", artist.getName());
                ContentValues artistContent = ArtistUtils.getArtistContent(artist);
                Uri uri = getContentResolver().insert(FavouriteArtistContract.ArtistEntry.CONTENT_URI, artistContent);
                if (uri == null) {
                    Log.i("Insert failed", artistContent.toString());
                } else {
                    ArtistUtils.saveImage(artist, getApplicationContext());
                    Toast.makeText(this, "made favourite", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "write permission not granted ", Toast.LENGTH_SHORT).show();
        }
        setFavouriteIcon();
    }

    private void checkPermissions() {
        if (!isWritePermissionGranted())
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    3);
    }

    private boolean isWritePermissionGranted() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        if (null != similarArtists) {
            Intent artistIntent = new Intent(this, ArtistActivity.class);
            Artist clickedArtist = ((ArtistAdapter) rvSimilarArtists.getAdapter()).getItem(clickedItemIndex);
            ArtistAdapter.ArtistViewHolder viewHolder = (ArtistAdapter.ArtistViewHolder) rvSimilarArtists.findViewHolderForAdapterPosition(clickedItemIndex);
            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<>(viewHolder.getArtistImage(), viewHolder.getArtistName().getText());
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, pairs);
            artistIntent.putExtra("artist", clickedArtist);
            startActivity(artistIntent, options.toBundle());

        }

    }
}