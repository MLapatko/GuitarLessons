package com.example.user.guitarlessons.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.backendless.BackendlessUser;
import com.example.user.guitarlessons.BaseFragment;
import com.example.user.guitarlessons.FragmentsInterface;
import com.example.user.guitarlessons.MainActivity;
import com.example.user.guitarlessons.R;
import com.example.user.guitarlessons.managers.UserAuthManager;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 07.02.2018.
 */

public class LoginFragment extends BaseFragment implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener, TextWatcher {

    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    final int CREATE_USER_FRAGMENT = 2;
    final int REQUEST_AUTHORIZATION = 2;
    public static final int RESTORE_PASSWORD_FRAGMENT = 3;
    final int SIGN_IN = 1;
    final String TAG = "mylog";
    final String SERVER_CLIENT_ID = "964645203843-isd2idnvj807sn7sudj6q33rrnkqbtgo.apps.googleusercontent.com";
    public static final String PASSWORD_ERROR = "password";
    public static final String EMAIL_ERROR = "email";

    TextView mCreateAccTextView;
    EditText mEmail;
    EditText mPassword;
    Button mLogInButton;
    SignInButton mSignInButton;

    ViewSwitcher mViewSwitcher;
    GoogleSignInOptions mgSignInOptions;
    GoogleApiClient mgApiClient;

    TextInputLayout mEmailInputLayout;
    TextInputLayout mPasswordInputLayout;

    private TextView mRestorePasswordTextView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEmail = view.findViewById(R.id.email);
        mEmail.requestFocus();
        mPassword = view.findViewById(R.id.password);

        mLogInButton = view.findViewById(R.id.login_button);
        mLogInButton.setOnClickListener(this);

        mViewSwitcher = view.findViewById(R.id.viewSwitcher);
        mViewSwitcher.setDisplayedChild(1);

        mCreateAccTextView = view.findViewById(R.id.create_account);
        mCreateAccTextView.setOnClickListener(this);

        mRestorePasswordTextView = view.findViewById(R.id.restore_password);
        mRestorePasswordTextView.setOnClickListener(this);

        mSignInButton = view.findViewById(R.id.sign_in_google);
        mSignInButton.setOnClickListener(this);

        mEmailInputLayout = view.findViewById(R.id.email_input);
        mPasswordInputLayout = view.findViewById(R.id.password_input);

        mEmail.addTextChangedListener(this);
        mPassword.addTextChangedListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity fragmentActivity = this.getActivity();

        mgSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile().requestId()
                .requestIdToken(SERVER_CLIENT_ID)
                .build();


        GoogleApiClient.Builder apiCliBuilder = new GoogleApiClient.Builder(fragmentActivity);
        mgApiClient = apiCliBuilder
                .enableAutoManage(fragmentActivity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mgSignInOptions).build();
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.login_fragment_layout;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                if (!AuthValidation.checkEmail(mEmail.getText())) {
                    focusError(getActivity().getString(R.string.error_3040), EMAIL_ERROR);
                } else if (!AuthValidation.checkPasswordLength(mPassword.getText())) {
                    focusError(getActivity().getString(R.string.short_password), PASSWORD_ERROR);
                } else {
                    logIn(mEmail.getText().toString(), mPassword.getText().toString());
                }
                break;
            case R.id.create_account:
                if (getActivity() instanceof FragmentsInterface) {
                    ((FragmentsInterface) getActivity()).putFragments(CREATE_USER_FRAGMENT);
                }
                break;
            case R.id.sign_in_google:
                googleLogIn();
                break;
            case R.id.restore_password:
                if (getActivity() instanceof FragmentsInterface) {
                    ((FragmentsInterface) getActivity()).putFragments(RESTORE_PASSWORD_FRAGMENT);
                }
                break;

        }

    }

    private void logIn(final String userEmail, String userPassword) {
        mViewSwitcher.setDisplayedChild(0);
        UserAuthManager.getInstance().logIn(userEmail, userPassword,
                new UserAuthManager.AuthListener<BackendlessUser>() {
                    @Override
                    public void onSuccess(BackendlessUser user) {
                        goToMainActivity();
                    }

                    @Override
                    public void onError(String massage, String errorCode) {
                        mViewSwitcher.setDisplayedChild(1);
                        focusError(massage, errorCode);
                    }
                });
    }

    public void goToMainActivity() {
        MainActivity.start(getContext());
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void googleLogIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mgApiClient);
        this.startActivityForResult(signInIntent, SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                loginInBackendless(result.getSignInAccount());
            }

        }
    }

    private void loginInBackendless(final GoogleSignInAccount acct) {
        final LogInActivity logInActivity = (LogInActivity) this.getActivity();
        final String accountName = acct.getEmail();
        final String scopes = "oauth2:" + Scopes.PLUS_LOGIN + " " +
                Scopes.PLUS_ME + " " + Scopes.PROFILE + " " +
                Scopes.EMAIL;

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String token = null;
                try {
                    token = GoogleAuthUtil.getToken(logInActivity, accountName, scopes);
                    GoogleAuthUtil.invalidateToken(logInActivity, token);
                    handleAccessTokenInBackendless(acct.getIdToken(), token);
                } catch (UserRecoverableAuthException e) {
                    Log.e(TAG, e.toString());
                    startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                return token;
            }
        };

        task.execute();
    }

    private void focusError(String massage, String errorType) {
        switch (errorType) {
            case EMAIL_ERROR:
                mEmailInputLayout.setError(massage);
                mEmail.requestFocus();
                break;
            case PASSWORD_ERROR:
                mPasswordInputLayout.setError(massage);
                mPassword.requestFocus();
                break;
            default:
                Toast.makeText(getActivity(), massage, Toast.LENGTH_SHORT).show();
        }
    }


    private void handleAccessTokenInBackendless(String idToken, String accessToken) {
        Map<String, String> googlePlusFieldsMapping = new HashMap<String, String>();
        googlePlusFieldsMapping.put("given_name", "gp_given_name");
        googlePlusFieldsMapping.put("family_name", "gp_family_name");
        googlePlusFieldsMapping.put("gender", "gender");
        googlePlusFieldsMapping.put("email", "email");
        List<String> permissions = new ArrayList<String>();

        UserAuthManager.getInstance().handleAccessTokenInBackendless(idToken, accessToken,
                permissions, googlePlusFieldsMapping, new UserAuthManager.AuthListener<BackendlessUser>() {
                    @Override
                    public void onSuccess(BackendlessUser user) {
                        goToMainActivity();
                    }

                    @Override
                    public void onError(String massage, String errorCode) {
                        Toast.makeText(getActivity(), massage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (mEmail.isFocused()) {
            mEmailInputLayout.setError(null);
        } else if (mPassword.isFocused()) {
            mPasswordInputLayout.setError(null);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}

