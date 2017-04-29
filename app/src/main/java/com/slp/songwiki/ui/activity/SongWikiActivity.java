package com.slp.songwiki.ui.activity;

import android.content.Intent;
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

import com.slp.songwiki.R;
import com.slp.songwiki.adapter.ArtistAdapter;
import com.slp.songwiki.adapter.SongWikiPagerAdapter;

public class SongWikiActivity extends AppCompatActivity {

    private SongWikiPagerAdapter pagerAdapter;
    private ViewPager mViewPager;
    private SearchView searchView;

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

    }


  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.artist_menu, menu);
        MenuItem menuItem=  menu.findItem(R.id.search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        *//*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

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
        });*//*
        return true;
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    
  



}
