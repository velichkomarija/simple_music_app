package com.velichkomarija4.simplemusicapp.views.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.velichkomarija4.simplemusicapp.AcademyApi;
import com.velichkomarija4.simplemusicapp.AlbumsActivity;
import com.velichkomarija4.simplemusicapp.ApiUtils;
import com.velichkomarija4.simplemusicapp.AuthActivity;
import com.velichkomarija4.simplemusicapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AuthFragment extends Fragment {
    private EditText emailEditText;
    private EditText passwordEditText;
    AuthActivity authActivity;
    SharedPreferences sharedPreferences;

    private View.OnClickListener mOnEnterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isEmailValid() && isPasswordValid()) {

                AcademyApi loginService =
                        ApiUtils.createService(AcademyApi.class,
                                emailEditText.getText().toString(),
                                passwordEditText.getText().toString());

                loginService.auth()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(user -> {
                            sharedPreferences.edit().putString("USER_NAME", user.getName()).apply();
                            startActivity(new Intent(authActivity, AlbumsActivity.class));
                            authActivity.finish();
                        }, throwable -> showMessage(R.string.request_error));

            }
        }
    };

    private View.OnClickListener mOnRegisterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, RegistrationFragment.newInstance())
                    .addToBackStack(RegistrationFragment.class.getName())
                    .commit();
        }
    };

    public static AuthFragment newInstance() {
        Bundle args = new Bundle();

        AuthFragment fragment = new AuthFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        authActivity = (AuthActivity)context;
        sharedPreferences = authActivity.getSharedPreferences("APP_PREFERENCES",
                Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_auth, container, false);

        emailEditText = v.findViewById(R.id.editText_email);
        passwordEditText = v.findViewById(R.id.editText_password);
        Button enterButton = v.findViewById(R.id.button_enter);
        Button registerButton = v.findViewById(R.id.button_register);

        enterButton.setOnClickListener(mOnEnterClickListener);
        registerButton.setOnClickListener(mOnRegisterClickListener);
        return v;
    }

    private boolean isEmailValid() {
        if (TextUtils.isEmpty(emailEditText.getText())
                && !Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText()).matches()) {
            emailEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            if (TextUtils.isEmpty(passwordEditText.getText())) {
                passwordEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                return false;
            } else {
                passwordEditText.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
                return false;
            }
        } else {
            emailEditText.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
            return true;
        }
    }

    private boolean isPasswordValid() {

        if (TextUtils.isEmpty(passwordEditText.getText())) {
            passwordEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            return false;
        } else {
            passwordEditText.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
            return true;
        }
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(authActivity, string, Toast.LENGTH_LONG).show();
    }

}
