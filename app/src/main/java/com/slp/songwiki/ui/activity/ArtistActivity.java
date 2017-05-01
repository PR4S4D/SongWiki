package com.slp.songwiki.ui.activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.slp.songwiki.R;
import com.slp.songwiki.data.FavouriteArtistContract;
import com.slp.songwiki.model.Artist;
import com.slp.songwiki.utilities.ArtistUtils;
import com.slp.songwiki.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArtistActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, SearchView.OnQueryTextListener {

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
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_tool_bar)
    CollapsingToolbarLayout collapsingToolbarLayout;

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
    }

    private void showArtistInfo() {
        if (ArtistUtils.isArtistInfoAvailable(artist))
            showArtistBasicInfo();
        if(getIntent().getBooleanExtra("isFavourite",false) && ! NetworkUtils.isNetworkAvailable(this)){
            showArtistBasicInfo();
            showArtistAdvancedInfo();
        }else{

            getSupportLoaderManager().initLoader(12, null, this);
        }
    }


    private void showArtistBasicInfo() {
        basicInfoSet = true;
        Picasso.with(this).load(artist.getImageLink()).into(artistImage);
        listeners.setText(String.valueOf(artist.getListeners()));
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
            summary.setMovementMethod (LinkMovementMethod.getInstance());
            summary.setClickable(true);
        }
        publishDate.setText(artist.getPublishedOn());
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
        if (!basicInfoSet)
            showArtistBasicInfo();
        showArtistAdvancedInfo();
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
                Toast.makeText(this, "It's already a fav", Toast.LENGTH_SHORT).show();
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
}