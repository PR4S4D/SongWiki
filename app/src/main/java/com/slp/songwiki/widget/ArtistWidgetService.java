package com.slp.songwiki.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by lshivaram on 4/29/2017.
 */

public class ArtistWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new ArtistWidgetFactory(getApplicationContext(), intent);
    }
}
