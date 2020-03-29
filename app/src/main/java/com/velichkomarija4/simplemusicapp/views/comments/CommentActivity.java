package com.velichkomarija4.simplemusicapp.views.comments;

import com.velichkomarija4.simplemusicapp.MainActivity;

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
        finish();
    }
}
