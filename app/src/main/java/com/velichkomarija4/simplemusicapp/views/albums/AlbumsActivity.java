package com.velichkomarija4.simplemusicapp.views.albums;

import com.velichkomarija4.simplemusicapp.MainActivity;

import androidx.fragment.app.Fragment;

public class AlbumsActivity extends MainActivity{

    @Override
    protected Fragment getFragment() {
        return AlbumsFragment.newInstance();
    }
}
