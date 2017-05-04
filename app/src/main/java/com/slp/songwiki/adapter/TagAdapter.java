package com.slp.songwiki.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.slp.songwiki.R;

import java.util.List;

/**
 * Created by lshivaram on 5/1/2017.
 */

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder> {
    List<String> tags;
    private int backgroundColor;
    private int textColor;

    public TagAdapter(List<String> tags, int backgroundColor, int textColor) {
        this.tags = tags;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int tagLayout = R.layout.tag;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(tagLayout, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TagViewHolder holder, int position) {
        holder.tag.setText(tags.get(position));
        holder.tagCard.setCardBackgroundColor(backgroundColor);
        holder.tag.setTextColor(textColor);
    }

    @Override
    public int getItemCount() {
        if (null != tags)
            return tags.size();
        return 0;
    }

    public class TagViewHolder extends RecyclerView.ViewHolder {
        CardView tagCard;
        TextView tag;
        public TagViewHolder(View itemView) {
            super(itemView);
            tag = (TextView) itemView.findViewById(R.id.tag);
            tagCard = (CardView) itemView.findViewById(R.id.tag_card);
        }
    }
}
