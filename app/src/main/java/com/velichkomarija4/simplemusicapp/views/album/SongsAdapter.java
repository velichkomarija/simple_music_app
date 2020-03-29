package com.velichkomarija4.simplemusicapp.views.album;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.velichkomarija4.simplemusicapp.R;
import com.velichkomarija4.simplemusicapp.model.Song;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SongsAdapter extends RecyclerView.Adapter<SongsHolder> {

    @NonNull
    private final List<Song> mSongs = new ArrayList<>();

    @NotNull
    @Override
    public SongsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_song, parent, false);
        return new SongsHolder(view);
    }

    @Override
    public void onBindViewHolder(SongsHolder holder, int position) {
        Song song = mSongs.get(position);
        holder.bind(song);
    }

    @Override
    public int getItemCount() {
        return mSongs.size();
    }

    void addData(List<Song> data,
                 boolean isRefreshed) {
        if (isRefreshed) {
            mSongs.clear();
        }

        mSongs.addAll(data);
        notifyDataSetChanged();
    }
}
