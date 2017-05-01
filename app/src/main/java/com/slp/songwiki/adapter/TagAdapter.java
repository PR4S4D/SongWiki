package com.slp.songwiki.adapter;

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

    public TagAdapter(List<String> tags){
        this.tags = tags;
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
    }

    @Override
    public int getItemCount() {
        if (null != tags)
            return tags.size();
        return 0;
    }

    public class TagViewHolder extends RecyclerView.ViewHolder {
        TextView tag;
        public TagViewHolder(View itemView) {
            super(itemView);
            tag = (TextView) itemView.findViewById(R.id.tag);
        }
    }
}
