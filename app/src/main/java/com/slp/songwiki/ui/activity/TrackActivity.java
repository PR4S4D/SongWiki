package com.slp.songwiki.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.slp.songwiki.R;
import com.slp.songwiki.adapter.TagAdapter;
import com.slp.songwiki.adapter.TrackAdapter;
import com.slp.songwiki.model.Artist;
import com.slp.songwiki.model.Track;
import com.slp.songwiki.utilities.LastFmUtils;
import com.slp.songwiki.utilities.TrackUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TrackActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Void>, View.OnClickListener {

    private static final int TRACK_LOADER = 456;
    private Track track;
    private List<Track> similarTracks;
    @Bind(R.id.track_image)
    ImageView trackImage;
    @Bind(R.id.artist)
    TextView artist;
    @Bind(R.id.summary)
    TextView summary;
    @Bind(R.id.rv_tags)
    RecyclerView rvTags;
    @Bind(R.id.collapsing_tool_bar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.loading_frame)
    FrameLayout loadingFrame;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.artist_card)
    CardView artistCard;
    private Palette.PaletteAsyncListener paletteListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        ButterKnife.bind(this);
        track = getIntent().getParcelableExtra("track");
        collapsingToolbarLayout.setTitle(track.getTitle());
        toolbar.setTitle(track.getTitle());
        setTrackInfo();
        try {
            Log.i("similar Artist", String.valueOf(LastFmUtils.getSimilarTracksUrl(track)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        getSupportLoaderManager().initLoader(TRACK_LOADER, null, this);

    }

    private void setTrackInfo() {
        trackImage.setTransitionName(track.getArtist());
        //  Picasso.with(this).load(track.getImageLink()).into(trackImage);
        artist.setText(track.getArtist());

        Picasso.with(getApplicationContext()).load(track.getImageLink()).into(trackImage,
                new Callback() {
                    @Override
                    public void onSuccess() {
                        scheduleStartPostponedTransition(trackImage);
                        Bitmap bitmap = ((BitmapDrawable) trackImage.getDrawable()).getBitmap();
                        setPaleteListener();
                        Palette.from(bitmap).generate(paletteListener);
                    }

                    @Override
                    public void onError() {
                        Log.e("Track", "onError: loading image failed");
                    }
                });
    }

    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        TrackActivity.this.startPostponedEnterTransition();
                        return true;
                    }
                });
    }


    private void setPaleteListener() {
        paletteListener = new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                int defaultColor = 0x000000;
                int darkMutedColor = palette.getDarkMutedColor(defaultColor);
                int lightMutedColor = palette.getLightMutedColor(defaultColor);

                Palette.Swatch vibrant = palette.getVibrantSwatch();
                if (vibrant != null) {
                    artistCard.setBackgroundColor(vibrant.getRgb());
                    artist.setTextColor(vibrant.getTitleTextColor());
                    // bylineView.setTextColor(vibrant.getTitleTextColor());
                } else {
                    artistCard.setBackgroundColor(lightMutedColor);
                    artist.setTextColor(darkMutedColor);
                    //bylineView.setTextColor(darkMutedColor);
                }


            }
        };
    }

    @Override
    public Loader<Void> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Void>(getApplicationContext()) {
            @Override
            protected void onStartLoading() {
                if (null == track.getTags())
                    loadingFrame.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public Void loadInBackground() {
                try {
                    TrackUtils.addTrackInfo(track);
                    // similarTracks = TrackUtils.getSimilarTracks(track);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        loadingFrame.setVisibility(View.GONE);
        if (null != track.getSummary()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                summary.setText(Html.fromHtml(track.getSummary(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                summary.setText(Html.fromHtml(track.getSummary()));
            }
            summary.setMovementMethod(LinkMovementMethod.getInstance());
            summary.setClickable(true);
        }

        showTags();
    }

    private void showTags() {
        rvTags.setAdapter(new TagAdapter(track.getTags()));
        //int gridSize = 1;
        rvTags.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rvTags.setHasFixedSize(true);
    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {

    }

    public void goToArtist(View view) {
        Intent intent = new Intent(this, ArtistActivity.class);
        Artist artist = new Artist();
        artist.setName(track.getArtist());
        intent.putExtra("artist", artist);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }
}
