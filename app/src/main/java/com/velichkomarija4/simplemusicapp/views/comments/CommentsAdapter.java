package com.velichkomarija4.simplemusicapp.views.comments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.velichkomarija4.simplemusicapp.R;
import com.velichkomarija4.simplemusicapp.model.Comment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsHolder> {

    @NonNull
    private final List<Comment> mComment = new ArrayList<>();

    @Override
    public CommentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_comment, parent, false);
        return new CommentsHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentsHolder holder, int position) {
        Comment comment = mComment.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return mComment.size();
    }

    boolean addData(List<Comment> data, boolean isRefreshed) {
        if (mComment.size() != data.size()) {
            if (isRefreshed) {
                mComment.clear();
            }

            mComment.addAll(data);
            notifyDataSetChanged();
            return true;
        } else {
            return false;
        }
    }
}
