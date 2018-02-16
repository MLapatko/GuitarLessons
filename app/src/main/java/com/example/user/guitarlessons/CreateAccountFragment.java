package com.example.user.guitarlessons;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.backendless.BackendlessUser;

/**
 * Created by user on 07.02.2018.
 */

public class CreateAccountFragment extends BaseFragment implements View.OnClickListener {
    public static CreateAccountFragment newInstance() {

        Bundle args = new Bundle();

        CreateAccountFragment fragment = new CreateAccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static final String PASSWORD_ERROR = "password";
    public static final String EMAIL_ERROR = "email";
    public static final String CONF_PASSWORD = "conf password";

    EditText mEmail;
    EditText mPassword;
    EditText mConfPassword;
    Button mConfirmButton;
    final static String TAG = "mylog";
    ViewSwitcher mViewSwitcher;

    TextInputLayout mEmailInputLayout;
    TextInputLayout mPasswordInputLayout;
    TextInputLayout mConfPasswordLayout;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEmail = view.findViewById(R.id.user_email);
        mPassword = view.findViewById(R.id.user_password);
        mConfPassword=view.findViewById(R.id.confirm_password);
        mConfirmButton = view.findViewById(R.id.ok_button);
        mConfirmButton.setOnClickListener(this);

        mViewSwitcher = view.findViewById(R.id.viewSwitcher);
        mViewSwitcher.setDisplayedChild(1);

        mEmailInputLayout = view.findViewById(R.id.user_email_input);
        mPasswordInputLayout = view.findViewById(R.id.user_password_input);
        mConfPasswordLayout=view.findViewById(R.id.confirm_password_input);

        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mEmailInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mPasswordInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mConfPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mConfPasswordLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok_button:
                if (!AuthValidation.checkEmail(mEmail.getText())) {
                    focusError(getActivity().getResources().getString(R.string.error_3040),EMAIL_ERROR);
                } else if (!AuthValidation.checkPasswordLength(mPassword.getText())) {
                    focusError(getActivity().getResources().getString(R.string.short_password),PASSWORD_ERROR);
                } else if (!AuthValidation.comperePassword(mPassword.getText(), mConfPassword.getText())) {
                    focusError(getActivity().getResources().getString(R.string.different_passwords),
                            CONF_PASSWORD);
                } else {
                    registrateUser(mEmail.getText().toString(), mPassword.getText().toString());
                }
                break;
        }

    }

    private void registrateUser(String userEmail, String userPassword) {
        mViewSwitcher.setDisplayedChild(0);
        UserAuthManager.getInstance().registrateUser(userEmail, userPassword,
                new UserAuthManager.AuthListener<BackendlessUser>() {
                    @Override
                    public void onSuccess(BackendlessUser user) {
                        goToMainActivity();
                    }

                    @Override
                    public void onError(String massage, String errorType) {
                        mViewSwitcher.setDisplayedChild(1);
                        focusError(massage,errorType);
                    }
                });
    }

    private void focusError(String massage,String errorType) {
        switch (errorType) {
            case EMAIL_ERROR:
                mEmailInputLayout.setError(massage);
                mEmail.requestFocus();
                break;
            case PASSWORD_ERROR:
                mPasswordInputLayout.setError(massage);
                mPassword.requestFocus();
                break;
            case CONF_PASSWORD:
                mConfPasswordLayout.setError(massage);
                mConfPassword.requestFocus();
            default: Toast.makeText(getActivity(), massage, Toast.LENGTH_SHORT).show();
        }
    }

    private void goToMainActivity() {
        MainActivity.start(getContext());
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.create_account_fragment;
    }
}
