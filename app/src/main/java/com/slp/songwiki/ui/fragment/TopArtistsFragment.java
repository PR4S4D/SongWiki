package com.slp.songwiki.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
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

import com.slp.songwiki.R;
import com.slp.songwiki.adapter.ArtistAdapter;
import com.slp.songwiki.model.Artist;
import com.slp.songwiki.ui.activity.ArtistActivity;
import com.slp.songwiki.ui.activity.ArtistSearchResultsActivity;
import com.slp.songwiki.utilities.ArtistUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * Created by lshivaram on 4/30/2017.
 */

public class TopArtistsFragment extends Fragment implements SongWikiFragmentable, LoaderManager.LoaderCallbacks<List<Artist>>, ArtistAdapter.ListItemClickListener {
    private static final int TOP_ARTISTS = 321;
    private View rootView;
    private LoaderManager loaderManager;
    private List<Artist> topArtists;
    private RecyclerView rvArtists;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_artist_top, container, false);
        loaderManager = getActivity().getSupportLoaderManager();
        loaderManager.initLoader(TOP_ARTISTS, null, this);
        rvArtists = (RecyclerView) rootView.findViewById(R.id.rv_artists);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.artist_menu, menu);
        MenuItem menuItem=  menu.findItem(R.id.search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint(getString(R.string.artist_title));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                ((ArtistAdapter)rvArtists.getAdapter()).getFilter().filter(query);
                Intent intent = new Intent(getActivity(), ArtistSearchResultsActivity.class);
                intent.putExtra("artist",query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ((ArtistAdapter)rvArtists.getAdapter()).getFilter().filter(newText);
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

        initializeRecyclerView(artists);

    }

    @Override
    public void onLoaderReset(Loader<List<Artist>> loader) {

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
            artistIntent.putExtra("artist", clickedArtist);
            startActivity(artistIntent);

        }
    }
}
