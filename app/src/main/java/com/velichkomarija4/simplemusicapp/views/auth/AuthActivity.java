package com.velichkomarija4.simplemusicapp.views.auth;

import com.velichkomarija4.simplemusicapp.MainActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class AuthActivity extends MainActivity {
    @Override
    protected Fragment getFragment() {
        return AuthFragment.newInstance();
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
