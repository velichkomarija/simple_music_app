package com.velichkomarija4.simplemusicapp.views.comments;

import android.content.Intent;

import com.velichkomarija4.simplemusicapp.MainActivity;
import com.velichkomarija4.simplemusicapp.views.albums.AlbumsActivity;

import androidx.fragment.app.Fragment;

public class CommentActivity extends MainActivity {

    private static final String ALBUM_KEY = "ALBUM_KEY";

    @Override
    protected Fragment getFragment() {
        int code = getIntent().getIntExtra(ALBUM_KEY, 0);
        return CommentFragment.newInstance(code);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AlbumsActivity.class);
        startActivity(intent);
        finish();
    }
}
