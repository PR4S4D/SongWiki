package com.slp.songwiki.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.slp.songwiki.R;

/**
 * Created by lshivaram on 4/30/2017.
 */

public class TopAlbumsFragment extends Fragment implements SongWikiFragmentable{
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_album_top,container,false);
        return rootView;
    }


}
