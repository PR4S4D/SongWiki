package com.slp.songwiki.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.slp.songwiki.R;
import com.slp.songwiki.adapter.TagAdapter;
import com.slp.songwiki.adapter.TrackAdapter;
import com.slp.songwiki.model.Artist;
import com.slp.songwiki.model.Track;
import com.slp.songwiki.utilities.LastFmUtils;
import com.slp.songwiki.utilities.SongWikiConstants;
import com.slp.songwiki.utilities.TrackUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TrackActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Void>, View.OnClickListener, TrackAdapter.TrackItemClickListener, SongWikiConstants {

    private static final int TRACK_LOADER = 456;
    private Track track;
    private List<Track> similarTracks;
    @Bind(R.id.track_image)
    ImageView trackImage;
    @Bind(R.id.artist)
    TextView artist;
    @Bind(R.id.rv_tags)
    RecyclerView rvTags;
    @Bind(R.id.collapsing_tool_bar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.loading_frame)
    FrameLayout loadingFrame;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.rv_similar_tracks)
    RecyclerView rvTracks;
    @Bind(R.id.artist_card)
    CardView artistCard;
    @Bind(R.id.similar_tracks_label)
    TextView similarTracksLabel;
    @Bind(R.id.content)
    TextView content;
    private int backgroundColor = Color.GRAY;
    private int textColor = Color.BLACK;
    private Palette.PaletteAsyncListener paletteListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        ButterKnife.bind(this);
        track = getIntent().getParcelableExtra(TRACK);
        setToolbar();
        setTrackInfo();
        try {
            Log.i("similar Artist", String.valueOf(LastFmUtils.getSimilarTracksUrl(track)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        getSupportLoaderManager().initLoader(TRACK_LOADER, null, this);

    }

    private void setToolbar() {
        collapsingToolbarLayout.setTitle(track.getTitle());
        toolbar.setTitle(track.getTitle());
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    private void setTrackInfo() {
        trackImage.setTransitionName(track.getArtist());
        artist.setText(track.getArtist());
        if (TextUtils.isEmpty(track.getImageLink())) {
            Picasso.with(getApplicationContext()).load(R.drawable.loading).into(trackImage);
        } else {
            Picasso.with(getApplicationContext()).load(track.getImageLink()).placeholder(R.drawable.loading).into(trackImage,
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
                textColor = palette.getDarkMutedColor(defaultColor);
                backgroundColor = palette.getLightMutedColor(defaultColor);

                Palette.Swatch vibrant = palette.getVibrantSwatch();
                if (vibrant != null) {
                    backgroundColor = vibrant.getRgb();
                    textColor = vibrant.getTitleTextColor();


                }
                artistCard.setBackgroundColor(backgroundColor);
                artist.setTextColor(textColor);
                collapsingToolbarLayout.setCollapsedTitleTextColor(textColor);
                collapsingToolbarLayout.setBackgroundColor(backgroundColor);
                collapsingToolbarLayout.setStatusBarScrimColor(backgroundColor);
                collapsingToolbarLayout.setContentScrimColor(backgroundColor);
                if (null != track.getTags())
                    showTags();

            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(this)
                        .setType("text/plain")
                        .setText(track.getTrackLink())
                        .getIntent(), track.getTitle()));
                break;
            case android.R.id.home:
                finish();
                break;
        }
        if (item.getItemId() == R.id.share) {
            startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setText(track.getTrackLink())
                    .getIntent(), track.getTitle()));
        }

        return super.onOptionsItemSelected(item);
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
                    similarTracks = TrackUtils.getSimilarTracks(track);

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    private void initializeRecyclerView() {
        if (null != similarTracks && similarTracks.size() > 0) {
            similarTracksLabel.setVisibility(View.VISIBLE);
            rvTracks.setAdapter(new TrackAdapter(similarTracks, this));
            int gridSize = getResources().getInteger(R.integer.track_grid);
            rvTracks.setLayoutManager(new GridLayoutManager(this, gridSize));
            rvTracks.setHasFixedSize(true);
        }
    }


    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        loadingFrame.setVisibility(View.GONE);
        initializeRecyclerView();
        if (null != track.getContent()) {
            content.setText(getTextFromHtml(track.getContent()));

            makeLinkClickable(content);
        }

        showTags();
    }

    private void makeLinkClickable(TextView textView) {
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setClickable(true);
    }

    private Spanned getTextFromHtml(String htmlContent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(htmlContent);
        }
    }

    private void showTags() {


        rvTags.setAdapter(new TagAdapter(track.getTags(), backgroundColor, textColor));
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);

        rvTags.setLayoutManager(flowLayoutManager);

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

    @Override
    public void onTrackItemClick(int position) {
        if (null != similarTracks) {
            Intent trackIntent = new Intent(this, TrackActivity.class);
            Track clickedTrack = ((TrackAdapter) rvTracks.getAdapter()).getItem(position);
            TrackAdapter.TrackViewHolder viewHolder = (TrackAdapter.TrackViewHolder) rvTracks.findViewHolderForAdapterPosition(position);
            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<>(viewHolder.getTrackImage(), viewHolder.getArtist().getText());
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, pairs);
            trackIntent.putExtra(TRACK, clickedTrack);
            startActivity(trackIntent, options.toBundle());
        }

    }
}
