package com.velichkomarija4.simplemusicapp.views.albums;

import com.velichkomarija4.simplemusicapp.MainActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class AlbumsActivity extends MainActivity{

    @Override
    protected Fragment getFragment() {
        return AlbumsFragment.newInstance();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 0) {
            finish();
        } else {
            fragmentManager.popBackStack();
        }
    }
}
