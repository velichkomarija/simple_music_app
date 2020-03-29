package com.velichkomarija4.simplemusicapp.views.album;

import android.view.View;
import android.widget.TextView;

import com.velichkomarija4.simplemusicapp.R;
import com.velichkomarija4.simplemusicapp.model.Song;

import androidx.recyclerview.widget.RecyclerView;

class SongsHolder extends RecyclerView.ViewHolder {

    private TextView mTitle;
    private TextView mDuration;

    SongsHolder(View itemView) {
        super(itemView);
        mTitle = itemView.findViewById(R.id.textView_title);
        mDuration = itemView.findViewById(R.id.textView_date);
    }

    void bind(Song item) {
        mTitle.setText(item.getName());
        mDuration.setText(item.getDuration());
    }
}
