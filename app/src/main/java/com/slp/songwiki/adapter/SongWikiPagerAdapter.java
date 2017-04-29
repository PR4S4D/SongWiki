package com.slp.songwiki.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.text.TextUtilsCompat;

import com.slp.songwiki.R;
import com.slp.songwiki.ui.fragment.TopAlbumsFragment;
import com.slp.songwiki.ui.fragment.TopArtistsFragment;
import com.slp.songwiki.ui.fragment.SongWikiFragmentable;
import com.slp.songwiki.ui.fragment.TopTracksFragment;

/**
 * Created by lshivaram on 4/29/2017.
 */

public class SongWikiPagerAdapter extends FragmentPagerAdapter {
    private SongWikiFragmentable currentFragment;
    private Context context;
    public SongWikiPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                currentFragment = new TopTracksFragment();
                break;
            case 1:
                currentFragment = new TopArtistsFragment();
                break;
            case 2:
                currentFragment = new TopAlbumsFragment();
                break;

        }

        return  (Fragment) currentFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String pageTitle = "";
        switch (position){
            case 0:
                pageTitle = context.getString(R.string.track_title);
            break;
            case 1:
                pageTitle = context.getString(R.string.artist_title);
                break;
            case 2:
                pageTitle = context.getString(R.string.album_title);
            break;
        }
        return pageTitle;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
