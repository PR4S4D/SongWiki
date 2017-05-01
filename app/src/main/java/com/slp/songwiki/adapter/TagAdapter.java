package com.slp.songwiki.adapter;

import android.support.v7.widget.RecyclerView;
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

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(TagViewHolder holder, int position) {

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
