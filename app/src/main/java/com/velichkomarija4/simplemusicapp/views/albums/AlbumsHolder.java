package com.velichkomarija4.simplemusicapp.views.albums;

import android.view.View;
import android.widget.TextView;

import com.velichkomarija4.simplemusicapp.R;
import com.velichkomarija4.simplemusicapp.model.Album;

import androidx.recyclerview.widget.RecyclerView;

class AlbumsHolder extends RecyclerView.ViewHolder {

    private TextView title;
    private TextView releaseDate;

    AlbumsHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.textView_title);
        releaseDate = itemView.findViewById(R.id.textView_date);
    }

    void bind(Album item,
              AlbumsAdapter.OnItemClickListener onItemClickListener) {

        title.setText(item.getName());
        releaseDate.setText(item.getReleaseDate().substring(0,10));

        if (onItemClickListener != null) {
            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(item));
        }
    }

}
