package com.slp.songwiki.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.slp.songwiki.R;
import com.slp.songwiki.adapter.TrackAdapter;
import com.slp.songwiki.model.Track;
import com.slp.songwiki.ui.activity.TrackActivity;
import com.slp.songwiki.ui.activity.TrackSearchResultsActivty;
import com.slp.songwiki.utilities.TrackUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by lshivaram on 4/30/2017.
 */

public class TopTracksFragment extends Fragment implements SongWikiFragmentable, LoaderManager.LoaderCallbacks<List<Track>>, TrackAdapter.TrackItemClickListener {
    private View rootView;
    private LoaderManager loaderManager;
    private List<Track> topTracks;
    private RecyclerView rvTracks;
    private static final int TOP_TRACKS = 345;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_track_top, container, false);
        rvTracks = (RecyclerView) rootView.findViewById(R.id.rv_tracks);
        loaderManager = getActivity().getSupportLoaderManager();
        loaderManager.initLoader(TOP_TRACKS, null, this);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.artist_menu, menu);
        MenuItem menuItem=  menu.findItem(R.id.search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint(getString(R.string.track_title));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                ((TrackAdapter)rvTracks.getAdapter()).getFilter().filter(query);
                Intent intent = new Intent(getActivity(), TrackSearchResultsActivty.class);
                intent.putExtra("artist",query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ((TrackAdapter)rvTracks.getAdapter()).getFilter().filter(newText);
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
                forceLoad();
            }

            @Override
            public List<Track> loadInBackground() {
                try {
                    tracks = TrackUtils.getTopChartTracks();
                } catch (IOException | JSONException e){
                    e.printStackTrace();
                }
                return tracks;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Track>> loader, List<Track> data) {
        Log.i(TAG, "onLoadFinished: finishedloadingtracks "+data);
        initializeRecyclerView(data);
    }

    private void initializeRecyclerView(List<Track> tracks) {
        if (null != tracks) {
            topTracks = tracks;
            rvTracks.setAdapter(new TrackAdapter(tracks, this));
            int gridSize = 1;
            rvTracks.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            rvTracks.setHasFixedSize(true);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Track>> loader) {

    }

    @Override
    public void onTrackItemClick(int position) {
        Intent intent = new Intent(getActivity(),TrackActivity.class);
        intent.putExtra("track", topTracks.get(position));
        startActivity(intent);
    }
}
