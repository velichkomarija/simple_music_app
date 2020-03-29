package com.velichkomarija4.simplemusicapp;

import com.velichkomarija4.simplemusicapp.views.auth.AuthFragment;

import androidx.fragment.app.Fragment;

public class AuthActivity extends MainActivity {
    @Override
    protected Fragment getFragment() {
        return AuthFragment.newInstance();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
