package com.velichkomarija4.simplemusicapp;

import com.velichkomarija4.simplemusicapp.views.albums.AlbumsFragment;

import androidx.fragment.app.Fragment;

public class AlbumsActivity extends MainActivity{

    @Override
    protected Fragment getFragment() {
        return AlbumsFragment.newInstance();
    }
}
