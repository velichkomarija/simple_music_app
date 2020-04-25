package com.velichkomarija4.simplemusicapp.views.auth;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.velichkomarija4.simplemusicapp.ApiUtils;
import com.velichkomarija4.simplemusicapp.R;
import com.velichkomarija4.simplemusicapp.model.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RegistrationFragment extends Fragment {

    private TextInputEditText emailEditText;
    private TextInputEditText nameEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText againPasswordEditText;
    private Button registrateButton;

    static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    @SuppressLint("CheckResult")
    private View.OnClickListener mOnRegistrationClickListener = view -> {

        if (isEmailValid()) {
            // Проверка была заранее.
            User user = new User(
                    emailEditText.getText().toString(),
                    nameEditText.getText().toString(),
                    passwordEditText.getText().toString());

            ApiUtils.getApi()
                    .registration(user)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                                showMessage(R.string.response_code_204);
                                FragmentManager fragmentManager = getFragmentManager();
                                if (fragmentManager != null) {
                                    fragmentManager.popBackStack();
                                }
                            }, throwable -> {
                                showMessage(R.string.request_error);
                                Log.d("ERORR", throwable.getMessage());
                            }
                    );
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // do nothing
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // do nothing
        }

        @Override
        public void afterTextChanged(Editable editable) {

            if (TextUtils.isEmpty(emailEditText.getText()) ||
                    TextUtils.isEmpty(passwordEditText.getText()) ||
                    TextUtils.isEmpty(againPasswordEditText.getText()) ||
                    TextUtils.isEmpty(nameEditText.getText())) {

                registrateButton.setEnabled(false);
            } else {

                if (passwordEditText.getText().toString()
                        .equals(againPasswordEditText.getText().toString())) {
                    passwordEditText.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
                    againPasswordEditText.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
                    registrateButton.setEnabled(true);
                } else {
                    passwordEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                    againPasswordEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                    registrateButton.setEnabled(false);
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailEditText = view.findViewById(R.id.editText_email);
        nameEditText = view.findViewById(R.id.editText_name);
        passwordEditText = view.findViewById(R.id.editText_password);
        againPasswordEditText = view.findViewById(R.id.editText_againPassword);
        registrateButton = view.findViewById(R.id.button_register);

        emailEditText.addTextChangedListener(textWatcher);
        nameEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);
        againPasswordEditText.addTextChangedListener(textWatcher);
        registrateButton.setOnClickListener(mOnRegistrationClickListener);
    }

    private boolean isEmailValid() {

        if (emailEditText.getText() != null) {

            if (!Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
                emailEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                return false;
            } else {
                emailEditText.getBackground().invalidateSelf();
                return true;
            }
        }
        return false;
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

}
