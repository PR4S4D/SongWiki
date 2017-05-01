package com.slp.songwiki.adapter;

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
import com.slp.songwiki.model.Track;
import com.slp.songwiki.utilities.TrackUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lshivaram on 5/1/2017.
 */

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> implements Filterable {

    final private TrackItemClickListener onClickListener;
    private List<Track> tracks;
    private List<Track> tracksOld;

    public TrackAdapter(List<Track> tracks, TrackItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.tracks = tracks;
        this.tracksOld = tracks;
    }


    public interface TrackItemClickListener {
        void onTrackItemClick(int position);
    }

    public Track getItem(int position) {
        if (null != tracks)
            return tracks.get(position);
        return null;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                tracks = (List<Track>) results.values;
                TrackAdapter.this.notifyDataSetChanged();

            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Track> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = tracksOld;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

    protected List<Track> getFilteredResults(String constraint) {
        List<Track> results = new ArrayList<>();

        for (Track track : tracks) {
            if (track.getTitle().toLowerCase().contains(constraint)) {
                results.add(track);
            }
        }
        return results;
    }

    @Override
    public TrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int trackLayout = R.layout.track_item;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(trackLayout, parent, false);
        return new TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrackViewHolder holder, int position) {
        if (null != tracks) {
            Track currentTrack = tracks.get(position);
            holder.trackTitle.setText(currentTrack.getTitle());
            holder.artist.setText(currentTrack.getArtist());
            holder.getTrackImage().setTransitionName(currentTrack.getArtist());
            if (!TextUtils.isEmpty(currentTrack.getImageLink()))
                Picasso.with(holder.trackImage.getContext()).load(currentTrack.getImageLink()).into(holder.trackImage);

        }
    }

    @Override
    public int getItemCount() {
        if (null != tracks)
            return tracks.size();
        return 0;
    }

    public class TrackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView trackCardView;
        private TextView trackTitle;
        private TextView artist;

        public TextView getArtist() {
            return artist;
        }

        private ImageView trackImage;

        public TextView getTrackTitle() {
            return trackTitle;
        }

        public ImageView getTrackImage() {
            return trackImage;
        }


        public TrackViewHolder(View itemView) {
            super(itemView);
            trackCardView = (CardView) itemView.findViewById(R.id.track_card);
            trackTitle = (TextView) itemView.findViewById(R.id.track_title);
            artist = (TextView) itemView.findViewById(R.id.artist);
            trackImage = (ImageView) itemView.findViewById(R.id.track_image);
            trackCardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onClickListener.onTrackItemClick(getAdapterPosition());
        }
    }
}
