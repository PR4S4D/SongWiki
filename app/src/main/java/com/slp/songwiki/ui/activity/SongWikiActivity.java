package com.slp.songwiki.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
   
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
  
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.slp.songwiki.R;
import com.slp.songwiki.adapter.ArtistAdapter;
import com.slp.songwiki.adapter.SongWikiPagerAdapter;

public class SongWikiActivity extends AppCompatActivity {

    private SongWikiPagerAdapter pagerAdapter;
    private ViewPager mViewPager;
    private SearchView searchView;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_wiki);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pagerAdapter = new SongWikiPagerAdapter(getSupportFragmentManager(),this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        adView = (AdView) findViewById(R.id.banner_ad);


        showBannerAd();
    }

    private void showBannerAd() {
        adView.loadAd(getAdRequest());
    }

    @NonNull
    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adView.loadAd(getAdRequest());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.favourite_artists) {
            startActivity(new Intent(this,FavouriteArtistActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_song_wiki,menu);
        return true;
    }
}
