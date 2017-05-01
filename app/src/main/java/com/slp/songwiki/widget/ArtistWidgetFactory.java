package com.slp.songwiki.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;


import com.slp.songwiki.R;
import com.slp.songwiki.model.Artist;
import com.slp.songwiki.utilities.ArtistUtils;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class ArtistWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    public Context context;
    private List<Artist> artists;
    private Intent intent;
    private List<Bitmap> artistImages;

    public ArtistWidgetFactory(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
        artistImages = new ArrayList<>();
    }

    @Override
    public void onCreate() {
        new GetPopularArtists().execute();
    }

    @Override
    public void onDataSetChanged() {


        try {
            artists = new GetPopularArtists().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    class GetPopularArtists extends AsyncTask<String, Void, List<Artist>> {

        @Override
        protected List<Artist> doInBackground(String... params) {
            try {
                List<Artist> topChartArtists = ArtistUtils.getTopChartArtists();
                for(Artist artist : topChartArtists){
                    artistImages.add(downloadUrl(artist.getImageLink()));
                }
                return topChartArtists;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Artist> data) {
            artists = data;


            Log.i("onPostExecute: ",String.valueOf(artists));
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.artist_widget_item);
            /*Log.i("updating the widget in ",String.valueOf(intent.getIntArrayExtra("appWidgetIds")));
            appWidgetManager.updateAppWidget(intent.getIntArrayExtra("appWidgetIds"),remoteViews);*/
            super.onPostExecute(artists);
        }
    }

    private Bitmap downloadUrl(String strUrl) throws IOException{

        Bitmap bitmap=null;
        InputStream iStream = null;
        try{
            URL url = new URL(strUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(iStream);

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            iStream.close();
        }
        return bitmap;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (null != artists){
            Log.i("returning ","artist size");
            return artists.size();
        }
        Log.i( "getCount: ","0");
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if(null == artists)
            return null;

        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.artist_widget_item);
        final Artist artist = artists.get(position);
        Log.i("getViewAt: ",artist.getName());
        remoteViews.setTextViewText(R.id.artist_name, artist.getName());
        remoteViews.setImageViewBitmap(R.id.widget_image,artistImages.get(position));

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("artist",artist);
        remoteViews.setOnClickFillInIntent(R.id.artist_item,fillInIntent);

        return  remoteViews;

    }

    @Override
    public RemoteViews getLoadingView() {
        return  new RemoteViews(context.getPackageName(), R.layout.artist_widget);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
