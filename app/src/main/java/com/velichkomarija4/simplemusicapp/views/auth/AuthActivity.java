package com.velichkomarija4.simplemusicapp.views.auth;

import com.velichkomarija4.simplemusicapp.MainActivity;

import androidx.fragment.app.Fragment;

public class AuthActivity extends MainActivity {
    @Override
    protected Fragment getFragment() {
        return AuthFragment.newInstance();
    }
}
