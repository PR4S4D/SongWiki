package com.slp.songwiki.adapter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.slp.songwiki.R;
import com.slp.songwiki.model.Artist;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by lshivaram on 4/30/2017.
 */

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> implements Filterable {


    private List<Artist> artists;
    private List<Artist> artistsOld;
    final private ListItemClickListener onClickListener;


    @Override
    public Filter getFilter() {

        return new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                artists = (List<Artist>) results.values;
                ArtistAdapter.this.notifyDataSetChanged();

            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Artist> filteredResults ;
                if (constraint.length() == 0) {
                    filteredResults = artistsOld;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

    private List<Artist> getFilteredResults(String constraint) {
        List<Artist> results = new ArrayList<>();

        for (Artist artist : artists) {
            if (artist.getName().toLowerCase().contains(constraint)) {
                results.add(artist);
            }
        }
        return results;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public ArtistAdapter(List<Artist> artists, ListItemClickListener onClickListener) {
        this.artists = artists;
        this.artistsOld = artists;
        this.onClickListener = onClickListener;
    }

    @Override
    public ArtistAdapter.ArtistViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int artistLayout = R.layout.artist_item;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(artistLayout, viewGroup, false);
        return new ArtistViewHolder(view);

    }

    public Artist getItem(int position) {
        if (null != artists)
            return artists.get(position);
        return null;
    }

    @Override
    public void onBindViewHolder(final ArtistAdapter.ArtistViewHolder holder, int position) {
        holder.artistName.setText(artists.get(position).getName());
        String imageLink = artists.get(position).getImageLink();
        holder.artistImage.setTransitionName(artists.get(position).getName());
        holder.artistImage.setContentDescription(artists.get(position).getName());

        Log.i(TAG, "onBindViewHolder: " + imageLink);
        if (TextUtils.isEmpty(imageLink)) {

            Picasso.with(holder.artistImage.getContext()).load(R.drawable.loading).into(holder.artistImage);
        } else {

            Picasso.with(holder.artistImage.getContext()).load(imageLink).error(R.drawable.loading).into(holder.artistImage, new Callback() {
                @Override
                public void onSuccess() {
                    Bitmap bitmap = ((BitmapDrawable) holder.artistImage.getDrawable()).getBitmap();
                    Palette palette = Palette.from(bitmap).generate();
                    int defaultColor = Color.GRAY;
                    int darkMutedColor = palette.getDarkMutedColor(defaultColor);
                    int lightMutedColor = palette.getLightMutedColor(defaultColor);
                    Palette.Swatch vibrant = palette.getVibrantSwatch();
                    if (vibrant != null) {
                        Log.i(TAG, "onGenerated: vibrant is not null" + holder.artistName.getText());
                        holder.artistCard.setCardBackgroundColor(vibrant.getRgb());
                        holder.artistName.setTextColor(vibrant.getTitleTextColor());
                    } else {
                        Log.i(TAG, "onGenerated: vibrant is null" + holder.artistName.getText());
                        holder.artistCard.setCardBackgroundColor(lightMutedColor);
                        holder.artistName.setTextColor(darkMutedColor);
                    }
                }

                @Override
                public void onError() {

                }
            });
        }


    }


    @Override
    public int getItemCount() {
        if (null != artists)
            return artists.size();
        return 0;
    }


    public class ArtistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView getArtistImage() {
            return artistImage;
        }

        public TextView getArtistName() {
            return artistName;
        }

        ImageView artistImage;
        TextView artistName;
        CardView artistCard;

        private ArtistViewHolder(View itemView) {

            super(itemView);
            artistImage = (ImageView) itemView.findViewById(R.id.artist_image);
            artistName = (TextView) itemView.findViewById(R.id.artist_name);
            artistImage.setOnClickListener(this);
            artistCard = (CardView) itemView.findViewById(R.id.artist_card);
            artistCard.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onListItemClick(getAdapterPosition());
        }
    }
}
