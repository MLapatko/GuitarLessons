package com.example.user.guitarlessons;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.backendless.BackendlessUser;

/**
 * Created by user on 07.02.2018.
 */

public class CreateAccountFragment extends Fragment implements View.OnClickListener {
    public static CreateAccountFragment newInstance() {

        Bundle args = new Bundle();

        CreateAccountFragment fragment = new CreateAccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    EditText mEmail;
    EditText mPassword;
    EditText mUserName;
    Button mConfirmButton;
    final static String TAG = "mylog";
    ViewSwitcher mViewSwitcher;

    TextInputLayout mEmailInputLayout;
    TextInputLayout mPasswordInputLayout;
    TextInputLayout mLoginInputLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.create_account_fragment, container, false);
        mUserName = rootView.findViewById(R.id.user_name);
        mEmail = rootView.findViewById(R.id.user_email);
        mPassword = rootView.findViewById(R.id.user_password);
        mConfirmButton = rootView.findViewById(R.id.ok_button);
        mConfirmButton.setOnClickListener(this);

        mViewSwitcher = rootView.findViewById(R.id.viewSwitcher);
        mViewSwitcher.setDisplayedChild(1);

        mEmailInputLayout=rootView.findViewById(R.id.user_name_input);
        mPasswordInputLayout=rootView.findViewById(R.id.user_password_input);
        mLoginInputLayout=rootView.findViewById(R.id.user_name_input);

        mUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mLoginInputLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok_button:
                if (TextUtils.isEmpty(mUserName.getText())){
                    mLoginInputLayout.setError(getActivity().getResources().getString(R.string.empty_field));
                    mUserName.requestFocus();
                }
                else if (!AuthValidation.checkEmail(mEmail.getText()))
                {
                    mEmailInputLayout.setError(getActivity().getResources().getString(R.string.error_3040));
                    mEmail.requestFocus();
                }
                else if (!AuthValidation.checkPasswordLength(mPassword.getText())){
                    mPasswordInputLayout.setError(getActivity().getResources().getString(R.string.short_password));
                    mPassword.requestFocus();
                }
                else {
                    registrateUser(mEmail.getText().toString(), mPassword.getText().toString(),
                            mUserName.getText().toString());
                }
                break;
        }

    }

    private void registrateUser(String userEmail, String userPassword, String userName) {
        UserAuthManager.getInstance().registrateUser(userEmail, userPassword, userName,
                new UserAuthManager.AuthListener<BackendlessUser>() {
                    @Override
                    public void onSuccess(BackendlessUser user) {
                        mViewSwitcher.setDisplayedChild(0);
                        goToMainActivity();
                    }

                    @Override
                    public void onError(String massage) {
                        Toast.makeText(getActivity(), massage,Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToMainActivity() {
        MainActivity.start(getContext());
    }
}
