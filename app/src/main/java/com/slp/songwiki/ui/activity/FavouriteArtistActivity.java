package com.slp.songwiki.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.slp.songwiki.R;
import com.slp.songwiki.adapter.ArtistAdapter;
import com.slp.songwiki.adapter.TrackAdapter;
import com.slp.songwiki.model.Artist;
import com.slp.songwiki.utilities.ArtistUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FavouriteArtistActivity extends AppCompatActivity implements ArtistAdapter.ListItemClickListener {
    @Bind(R.id.rv_artists)
    RecyclerView rvArtists;
    @Bind(R.id.no_favourites)
    TextView noFavourites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_artist);
        ButterKnife.bind(this);
        new QueryArtistTask().execute();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Artist clickedArtist = ((ArtistAdapter) rvArtists.getAdapter()).getItem(clickedItemIndex);
        Pair[] pairs = new Pair[1];
        ArtistAdapter.ArtistViewHolder viewHolder = (ArtistAdapter.ArtistViewHolder) rvArtists.findViewHolderForAdapterPosition(clickedItemIndex);
        pairs[0] = new Pair<>(viewHolder.getArtistImage(), viewHolder.getArtistName().getText());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, pairs);
        Intent intent = new Intent(this, ArtistActivity.class);

        intent.putExtra("artist", clickedArtist);
        intent.putExtra("isFavourite", true);
        startActivity(intent, options.toBundle());
    }

    private class QueryArtistTask extends AsyncTask<String, Void, List<Artist>> {

        @Override
        protected List<Artist> doInBackground(String... params) {

            return ArtistUtils.getFavouriteArtists(getApplicationContext());
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            if (null != artists && artists.size() > 0) {
                rvArtists.setAdapter(new ArtistAdapter(artists, FavouriteArtistActivity.this));
                int gridSize = getResources().getInteger(R.integer.artist_grid);
                rvArtists.setLayoutManager(new GridLayoutManager(FavouriteArtistActivity.this, gridSize));
                rvArtists.setHasFixedSize(true);
                super.onPostExecute(artists);
                super.onPostExecute(artists);
            } else {
                noFavourites.setVisibility(View.VISIBLE);
            }
        }
    }
}
