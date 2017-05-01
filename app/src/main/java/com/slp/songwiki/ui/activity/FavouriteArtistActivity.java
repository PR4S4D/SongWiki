package com.slp.songwiki.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;

import com.slp.songwiki.R;
import com.slp.songwiki.adapter.ArtistAdapter;
import com.slp.songwiki.model.Artist;
import com.slp.songwiki.utilities.ArtistUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FavouriteArtistActivity extends AppCompatActivity implements ArtistAdapter.ListItemClickListener {
    @Bind(R.id.rv_artists)
    RecyclerView rvArtists;

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

    class QueryArtistTask extends AsyncTask<String, Void, List<Artist>> {

        @Override
        protected List<Artist> doInBackground(String... params) {

            return ArtistUtils.getFavouriteArtists(getApplicationContext());
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            rvArtists.setAdapter(new ArtistAdapter(artists, FavouriteArtistActivity.this));
            int gridSize = 2;
            rvArtists.setLayoutManager(new GridLayoutManager(FavouriteArtistActivity.this, gridSize));
          /*rvArtists.setLayoutManager(new
                  GridLayoutManager(getApplicationContext(), 1,GridLayoutManager.HORIZONTAL, false));*/
            rvArtists.setHasFixedSize(true);
            super.onPostExecute(artists);
            super.onPostExecute(artists);
        }
    }
}
