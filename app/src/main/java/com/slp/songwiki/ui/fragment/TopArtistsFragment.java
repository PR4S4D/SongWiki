package com.slp.songwiki.ui.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.graphics.Palette;
import android.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.slp.songwiki.BuildConfig;
import com.slp.songwiki.R;
import com.slp.songwiki.adapter.ArtistAdapter;
import com.slp.songwiki.model.Artist;
import com.slp.songwiki.ui.activity.ArtistActivity;
import com.slp.songwiki.ui.activity.ArtistSearchResultsActivity;
import com.slp.songwiki.ui.activity.TrackSearchResultsActivity;
import com.slp.songwiki.utilities.ArtistUtils;
import com.slp.songwiki.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * Created by lshivaram on 4/30/2017.
 */

public class TopArtistsFragment extends Fragment implements SongWikiFragmentable, LoaderManager.LoaderCallbacks<List<Artist>>, ArtistAdapter.ListItemClickListener {
    private static final int TOP_ARTISTS = 321;
    private View rootView;
    private LoaderManager loaderManager;
    private List<Artist> topArtists;
    @Bind(R.id.rv_artists)
    RecyclerView rvArtists;
    @Bind(R.id.artist_loader)
    ProgressBar artistLoader;
    @Bind(R.id.error)
    TextView error;
    private SearchView searchView;
    private FirebaseAnalytics firebaseAnalytics;
    private FirebaseRemoteConfig mFBConfig;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.i("onCreateView: ", String.valueOf(topArtists));
        super.onCreateView(inflater, container, savedInstanceState);
        if (null == rootView)
            rootView = inflater.inflate(R.layout.fragment_artist_top, container, false);
        ButterKnife.bind(this, rootView);
        if(NetworkUtils.isNetworkAvailable(getActivity())){
            error.setVisibility(View.GONE);
            loaderManager = getActivity().getSupportLoaderManager();
            loaderManager.initLoader(TOP_ARTISTS, null, this);
            setupFB();
            setHasOptionsMenu(true);
        }else{
            error.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    private void setupFB() {
        mFBConfig = FirebaseRemoteConfig.getInstance();
        firebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFBConfig.setConfigSettings(configSettings);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.artist_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_artist);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint(getString(R.string.artist_title));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                ((ArtistAdapter) rvArtists.getAdapter()).getFilter().filter(query);
                Intent intent = new Intent(getActivity(), ArtistSearchResultsActivity.class);
                intent.putExtra("artist", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ((ArtistAdapter) rvArtists.getAdapter()).getFilter().filter(newText);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public Loader<List<Artist>> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<List<Artist>>(getActivity()) {
            List<Artist> artists;

            @Override
            protected void onStartLoading() {
                if (null != artists) {
                    deliverResult(artists);
                } else {
                    artistLoader.setVisibility(View.VISIBLE);

                    forceLoad();
                }
            }

            @Override
            public List<Artist> loadInBackground() {
                try {
                    artists = ArtistUtils.getTopChartArtists();
                    Log.i("loadInBackground: ", artists.toString());
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                return artists;
            }

            @Override
            public void deliverResult(List<Artist> data) {
                artists = data;
                super.deliverResult(data);
            }
        };
    }


    @Override
    public void onLoadFinished(Loader<List<Artist>> loader, List<Artist> artists) {
        artistLoader.setVisibility(View.GONE);
        initializeRecyclerView(artists);

    }

    @Override
    public void onLoaderReset(Loader<List<Artist>> loader) {
        topArtists = null;

    }

    private void initializeRecyclerView(List<Artist> artists) {
        if (null != artists) {
            topArtists = artists;
            rvArtists.setAdapter(new ArtistAdapter(artists, this));
            int gridSize = 2;
            rvArtists.setLayoutManager(new GridLayoutManager(getActivity(), gridSize));
            rvArtists.setHasFixedSize(true);
        }


    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        if (null != topArtists) {
            Intent artistIntent = new Intent(getActivity(), ArtistActivity.class);
            Artist clickedArtist = ((ArtistAdapter) rvArtists.getAdapter()).getItem(clickedItemIndex);
            ArtistAdapter.ArtistViewHolder viewHolder = (ArtistAdapter.ArtistViewHolder) rvArtists.findViewHolderForAdapterPosition(clickedItemIndex);
            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<>(viewHolder.getArtistImage(), viewHolder.getArtistName().getText());
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
            artistIntent.putExtra("artist", clickedArtist);
            Bundle params = new Bundle();
            params.putString(FirebaseAnalytics.Param.ITEM_NAME, clickedArtist.getName());
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, params);
            startActivity(artistIntent, options.toBundle());

        }
    }


}
