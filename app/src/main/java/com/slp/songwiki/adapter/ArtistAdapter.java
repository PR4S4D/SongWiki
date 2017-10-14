package com.slp.songwiki.adapter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lakshmiprasad on 4/30/2017.
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
        void onArtistItemClick(int clickedItemIndex);
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
        Artist currentArtist = artists.get(position);
        holder.artistName.setText(currentArtist.getName());
        String imageLink = currentArtist.getImageLink();
        holder.artistImage.setContentDescription(currentArtist.getName());

        if (TextUtils.isEmpty(imageLink)) {

            Picasso.with(holder.artistImage.getContext()).load(R.drawable.ic_artist).error(R.drawable.ic_artist).into(holder.artistImage);
        } else {

            Picasso.with(holder.artistImage.getContext()).load(imageLink).error(R.drawable.ic_artist).into(holder.artistImage, new Callback() {
                @Override
                public void onSuccess() {
                    int backgroundColor = Color.GRAY;
                    int textColor = Color.BLACK;

                    Bitmap bitmap = ((BitmapDrawable) holder.artistImage.getDrawable()).getBitmap();
                    Palette palette = Palette.from(bitmap).generate();

                    textColor = palette.getDarkMutedColor(textColor);
                    backgroundColor = palette.getLightMutedColor(backgroundColor);

                    Palette.Swatch vibrant = palette.getVibrantSwatch();
                    if (vibrant != null) {
                        backgroundColor = vibrant.getRgb();
                        if (backgroundColor != vibrant.getTitleTextColor())
                            textColor = vibrant.getTitleTextColor();


                    }
                    holder.artistCard.setCardBackgroundColor(backgroundColor);
                    holder.artistName.setTextColor(textColor);
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
            onClickListener.onArtistItemClick(getAdapterPosition());
        }
    }
}
