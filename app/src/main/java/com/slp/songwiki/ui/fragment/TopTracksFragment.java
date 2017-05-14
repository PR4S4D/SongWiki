package com.slp.songwiki.ui.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.slp.songwiki.R;
import com.slp.songwiki.adapter.TrackAdapter;
import com.slp.songwiki.model.Track;
import com.slp.songwiki.ui.activity.TrackActivity;
import com.slp.songwiki.ui.activity.TrackSearchResultsActivity;
import com.slp.songwiki.utilities.NetworkUtils;
import com.slp.songwiki.utilities.PreferenceUtils;
import com.slp.songwiki.utilities.TrackUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * Created by lshivaram on 4/30/2017.
 */

public class TopTracksFragment extends Fragment implements SongWikiFragmentable, LoaderManager.LoaderCallbacks<List<Track>>, TrackAdapter.TrackItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private View rootView;
    private LoaderManager loaderManager;
    private List<Track> topTracks;
    @Bind(R.id.rv_tracks)
    RecyclerView rvTracks;
    @Bind(R.id.track_loader)
    ProgressBar trackLoader;
    @Bind(R.id.error)
    TextView error;
    private static final int TOP_TRACKS = 345;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_track_top, container, false);
        ButterKnife.bind(this, rootView);
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            error.setVisibility(View.GONE);

            loaderManager = getActivity().getSupportLoaderManager();
            loaderManager.initLoader(TOP_TRACKS, null, this);
            setHasOptionsMenu(true);
        } else {
            Snackbar.make(getActivity().findViewById(R.id.toolbar),"No Connectivity!",Snackbar.LENGTH_LONG).show();
            Log.i(TAG, "onCreateView: " + "no network");
            error.setVisibility(View.VISIBLE);
        }
        PreferenceUtils.getPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
        return rootView;
    }

    @Override
    public void onDestroy() {
        PreferenceUtils.getPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);

        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.track_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_track);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint(getString(R.string.track_title));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                ((TrackAdapter) rvTracks.getAdapter()).getFilter().filter(query);
                Intent intent = new Intent(getActivity(), TrackSearchResultsActivity.class);
                intent.putExtra("track", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                TrackAdapter adapter = (TrackAdapter) rvTracks.getAdapter();
                if (null != adapter)
                    adapter.getFilter().filter(newText);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public Loader<List<Track>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Track>>(getActivity()) {
            List<Track> tracks;

            @Override
            protected void onStartLoading() {
                if (null != tracks) {
                    deliverResult(tracks);
                } else {
                    rvTracks.setVisibility(View.GONE);
                    trackLoader.setVisibility(View.VISIBLE);

                    forceLoad();
                }
            }

            @Override
            public List<Track> loadInBackground() {
                try {
                    tracks = TrackUtils.getTopChartTracks(getActivity());
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return tracks;
            }

            @Override
            public void deliverResult(List<Track> data) {
                tracks = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Track>> loader, List<Track> data) {
        Log.i(TAG, "onLoadFinished: finishedloadingtracks " + data);
        trackLoader.setVisibility(View.GONE);
        rvTracks.setVisibility(View.VISIBLE);
        initializeRecyclerView(data);
    }

    private void initializeRecyclerView(List<Track> tracks) {
        if (null != tracks) {
            topTracks = tracks;
            rvTracks.setAdapter(new TrackAdapter(tracks, this));
            int gridSize = getResources().getInteger(R.integer.track_grid);
            rvTracks.setLayoutManager(new GridLayoutManager(getActivity(), gridSize));
            rvTracks.setHasFixedSize(true);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Track>> loader) {
        topTracks = null;
    }

    @Override
    public void onTrackItemClick(int position) {
        if (null != topTracks) {
            Intent trackIntent = new Intent(getActivity(), TrackActivity.class);
            Track clickedTrack = ((TrackAdapter) rvTracks.getAdapter()).getItem(position);
            TrackAdapter.TrackViewHolder viewHolder = (TrackAdapter.TrackViewHolder) rvTracks.findViewHolderForAdapterPosition(position);
            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<>(viewHolder.getTrackImage(), viewHolder.getArtist().getText());
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
            trackIntent.putExtra("track", clickedTrack);
            startActivity(trackIntent, options.toBundle());
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("top_tracks_limit")|| key.equals("country")){
            loaderManager.restartLoader(TOP_TRACKS,null,this);
        }
    }
}
