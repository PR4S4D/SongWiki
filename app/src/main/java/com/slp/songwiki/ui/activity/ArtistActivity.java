package com.slp.songwiki.ui.activity;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.slp.songwiki.R;
import com.slp.songwiki.adapter.ArtistAdapter;
import com.slp.songwiki.adapter.TagAdapter;
import com.slp.songwiki.adapter.TrackAdapter;
import com.slp.songwiki.data.FavouriteArtistContract;
import com.slp.songwiki.model.Artist;
import com.slp.songwiki.model.Track;
import com.slp.songwiki.utilities.ArtistUtils;
import com.slp.songwiki.utilities.NetworkUtils;
import com.slp.songwiki.utilities.TrackUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import javax.net.ssl.SNIHostName;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArtistActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, SearchView.OnQueryTextListener, ArtistAdapter.ListItemClickListener, TrackAdapter.TrackItemClickListener {

    private Artist artist;
    private List<Artist> similarArtists;
    private List<Track> topTracks;
    @Bind(R.id.artist_image)
    ImageView artistImage;
    @Bind(R.id.artist_name)
    TextView artistName;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_tool_bar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.loading_frame)
    FrameLayout loadingFrame;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    @Bind(R.id.rv_similar_artists)
    RecyclerView rvSimilarArtists;
    @Bind(R.id.similar_artists_label)
    TextView similarArtistsLabel;
    @Bind(R.id.make_favourite)
    FloatingActionButton favFab;
    TextView summary;
    @Bind(R.id.rv_tags)
    RecyclerView rvTags;
    @Bind(R.id.artist_info)
    NestedScrollView artistInfo;
    @Bind(R.id.content)
    TextView content;
    private int backgroundColor = Color.GRAY;
    private int textColor = Color.BLACK;
    @Bind(R.id.artist_card)
    CardView artistCard;
    @Bind(R.id.rv_top_tracks)
    RecyclerView rvTopTracks;
    @Bind(R.id.top_tracks_label)
    TextView topTracksLabel;
    private Palette.PaletteAsyncListener paletteListener;

    private boolean basicInfoSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        ButterKnife.bind(this);
        artist = getIntent().getParcelableExtra("artist");
        collapsingToolbarLayout.setTitle(artist.getName());
        toolbar.setTitle(artist.getName());
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

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

    private void showTags() {
        rvTags.setAdapter(new TagAdapter(artist.getTags(), backgroundColor, textColor));
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        flowLayoutManager.canScrollHorizontally();

        rvTags.setLayoutManager(flowLayoutManager);

        rvTags.setHasFixedSize(true);
    }


    @Override
    public boolean onNavigateUp() {
        getSupportFragmentManager().popBackStack();
        return true;
    }

    private void showArtistBasicInfo() {
        basicInfoSet = true;
        if (TextUtils.isEmpty(artist.getImageLink())) {
            artistName.setText(artist.getName());
            Picasso.with(getApplicationContext()).load(R.drawable.loading).into(artistImage);
        } else {
            Picasso.with(getApplicationContext()).load(artist.getImageLink()).placeholder(R.drawable.loading).into(artistImage,
                    new Callback() {
                        @Override
                        public void onSuccess() {
                            scheduleStartPostponedTransition(artistImage);
                            Bitmap bitmap = ((BitmapDrawable) artistImage.getDrawable()).getBitmap();
                            setPaleteListener();
                            Palette.from(bitmap).generate(paletteListener);
                        }

                        @Override
                        public void onError() {
                            Log.e("Track", "onError: loading image failed");
                        }
                    });

            artistImage.setTransitionName(artist.getName());
            artistName.setText(artist.getName());
        }
    }

    private void showArtistAdvancedInfo() {
        if (null != artist.getContent()) {
            content.setText(getTextFromHtml(artist.getContent()));
            makeLinkClickable(content);
        }
        if (null != artist.getTags())
            showTags();

    }

    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        ArtistActivity.this.startPostponedEnterTransition();
                        return true;
                    }
                });
    }


    private void setPaleteListener() {
        paletteListener = new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                textColor = palette.getDarkMutedColor(textColor);
                backgroundColor = palette.getLightMutedColor(backgroundColor);

                Palette.Swatch vibrant = palette.getVibrantSwatch();
                if (vibrant != null) {
                    backgroundColor = vibrant.getRgb();
                    if (backgroundColor != vibrant.getTitleTextColor())
                        textColor = vibrant.getTitleTextColor();


                }
                artistCard.setBackgroundColor(backgroundColor);
                if (textColor != backgroundColor){
                    artistName.setTextColor(textColor);
                    collapsingToolbarLayout.setCollapsedTitleTextColor(textColor);
                }
                collapsingToolbarLayout.setBackgroundColor(backgroundColor);
                collapsingToolbarLayout.setStatusBarScrimColor(backgroundColor);
                collapsingToolbarLayout.setContentScrimColor(backgroundColor);
                favFab.setBackgroundTintList(ColorStateList.valueOf(palette.getDarkMutedColor(textColor)));
                progressBar.getIndeterminateDrawable().setColorFilter(textColor, PorterDuff.Mode.MULTIPLY);
                if (null != artist.getTags())
                    showTags();

            }
        };
    }

    private Spanned getTextFromHtml(String htmlContent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(htmlContent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        return true;
    }

    private void makeLinkClickable(TextView textView) {
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setClickable(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setText(artist.getArtistLink())
                    .getIntent(), getString(R.string.choose_one)));
        } else if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String>(getApplicationContext()) {
            @Override
            protected void onStartLoading() {
                if (null == artist.getContent())
                    loadingFrame.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                try {
                    ArtistUtils.setArtistDetails(artist);
                    topTracks = TrackUtils.getTopTracks(artist.getName());

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return "";
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        loadingFrame.setVisibility(View.GONE);
        if (!basicInfoSet)
            showArtistBasicInfo();
        showArtistAdvancedInfo();
        showSimilarArtists();
        showTopTracks();
        Log.i("onLoadFinished: ", String.valueOf(artist.getArtistLink()));
    }

    private void showTopTracks() {
        if (topTracks != null && topTracks.size() > 0) {
            topTracksLabel.setVisibility(View.VISIBLE);
            rvTopTracks.setAdapter(new TrackAdapter(topTracks, this));
            int gridSize = getResources().getInteger(R.integer.track_grid);
            rvTopTracks.setLayoutManager(new GridLayoutManager(this, gridSize));
            rvTopTracks.setHasFixedSize(true);
            rvTopTracks.setNestedScrollingEnabled(false);
        }
    }

    private void showSimilarArtists() {
        if (artist.getSimilarArtists() != null && artist.getSimilarArtists().size() > 0) {
            similarArtistsLabel.setVisibility(View.VISIBLE);
            similarArtists = artist.getSimilarArtists();
            rvSimilarArtists.setAdapter(new ArtistAdapter(similarArtists, this));
            LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            layout.setInitialPrefetchItemCount(2);
            rvSimilarArtists.setLayoutManager(layout);
            rvSimilarArtists.setHasFixedSize(true);
            rvSimilarArtists.setNestedScrollingEnabled(true);
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
                    Snackbar.make(view, R.string.removed_From_fav, Snackbar.LENGTH_SHORT).show();
                }
            } else {
                Log.i("addToFavourites: ", artist.getName());
                ContentValues artistContent = ArtistUtils.getArtistContent(artist);
                Uri uri = getContentResolver().insert(FavouriteArtistContract.ArtistEntry.CONTENT_URI, artistContent);
                if (uri == null) {
                    Log.i("Insert failed", artistContent.toString());
                } else {
                    ArtistUtils.saveImage(artist, getApplicationContext());
                    Snackbar.make(view, R.string.made_favourite, Snackbar.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, R.string.write_permission, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onTrackItemClick(int position) {
        if (null != topTracks) {
            Intent trackIntent = new Intent(this, TrackActivity.class);
            Track clickedTrack = ((TrackAdapter) rvTopTracks.getAdapter()).getItem(position);
            TrackAdapter.TrackViewHolder viewHolder = (TrackAdapter.TrackViewHolder) rvTopTracks.findViewHolderForAdapterPosition(position);
            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<>(viewHolder.getTrackImage(), viewHolder.getArtist().getText());
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, pairs);
            trackIntent.putExtra("track", clickedTrack);
            startActivity(trackIntent, options.toBundle());
        }

    }
}