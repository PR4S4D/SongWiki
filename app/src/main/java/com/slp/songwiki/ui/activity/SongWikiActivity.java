package com.slp.songwiki.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.slp.songwiki.R;
import com.slp.songwiki.adapter.SongWikiPagerAdapter;
import com.slp.songwiki.utilities.PreferenceUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SongWikiActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{

    private SongWikiPagerAdapter pagerAdapter;
    private ViewPager mViewPager;
    @Bind(R.id.banner_ad)
    AdView adView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_wiki_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        pagerAdapter = new SongWikiPagerAdapter(getSupportFragmentManager(), this);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(pagerAdapter);
        setUpNavigationView();


        tabLayout.setupWithViewPager(mViewPager);
        adView = (AdView) findViewById(R.id.banner_ad);
        Log.i("Top artist limit ", PreferenceUtils.getTopArtistsLimit(this));
        showBannerAd();

    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.favourite_artists) {
            startActivity(new Intent(this, FavouriteArtistActivity.class));
            return true;
        } else if (id == R.id.song_wiki_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
