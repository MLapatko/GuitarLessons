package com.example.user.guitarlessons.auth;

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

import com.example.user.guitarlessons.BaseFragment;
import com.example.user.guitarlessons.R;
import com.example.user.guitarlessons.managers.UserAuthManager;

import static com.example.user.guitarlessons.managers.UserAuthManager.EMAIL_ERROR;

/**
 * Created by user on 16.03.2018.
 */

public class RestorePasswordFragment extends BaseFragment implements View.OnClickListener, TextWatcher {
    public static RestorePasswordFragment newInstance() {
        Bundle args = new Bundle();

        RestorePasswordFragment fragment = new RestorePasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.request_password_fragment;
    }

    private EditText mEmail;
    private Button mSendButton;
    private TextInputLayout mEmailInputLayout;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEmail = view.findViewById(R.id.email);
        mEmailInputLayout = view.findViewById(R.id.email_input_layout);

        mSendButton = view.findViewById(R.id.send_email);
        mSendButton.setOnClickListener(this);
        mSendButton.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_email:
                if (!AuthValidation.checkEmail(mEmail.getText())) {
                    focusError(getActivity().getString(R.string.error_3040), EMAIL_ERROR);
                } else {
                    restorePassword(mEmail.getText().toString());
                }
                break;
        }
    }

    private void restorePassword(String email) {
        UserAuthManager.getInstance().restorePassword(email, new UserAuthManager.AuthListener<Void>() {
            @Override
            public void onSuccess(Void response) {
                Toast.makeText(getActivity(), getString(R.string.check_email), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String massage, String errorType) {
                focusError(massage, errorType);
            }
        });
    }

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

    private void focusError(String massage, String errorType) {
        switch (errorType) {
            case EMAIL_ERROR:
                mEmailInputLayout.setError(massage);
                mEmail.requestFocus();
                break;
            default:
                Toast.makeText(getActivity(), massage, Toast.LENGTH_SHORT).show();
        }
    }
}
