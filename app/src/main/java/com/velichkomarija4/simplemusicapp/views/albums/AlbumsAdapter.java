package com.velichkomarija4.simplemusicapp.views.albums;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.velichkomarija4.simplemusicapp.R;
import com.velichkomarija4.simplemusicapp.model.Album;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsHolder> {

    @NonNull
    private final List<Album> albumList = new ArrayList<>();
    private final OnItemClickListener itemClickListener;

    AlbumsAdapter(OnItemClickListener onClickListener) {
        itemClickListener = onClickListener;
    }

    @NotNull
    @Override
    public AlbumsHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_album, parent, false);
        return new AlbumsHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumsHolder holder,
                                 int position) {
        Album album = albumList.get(position);
        holder.bind(album, itemClickListener);
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    void addData(List<Album> data,
                 boolean isRefreshed) {

        if (isRefreshed) {
            albumList.clear();
        }

        albumList.addAll(data);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Album album);
    }

}
