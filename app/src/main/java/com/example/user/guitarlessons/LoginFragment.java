package com.example.user.guitarlessons;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.backendless.BackendlessUser;
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

public class LoginFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    final int CREATE_USER_FRAGMENT = 2;

    TextView mCreateAccTextView;
    EditText mEmail;
    EditText mPassword;
    Button mLogInButton;
    final static String TAG = "mylog";
    ViewSwitcher mViewSwitcher;
    GoogleSignInOptions mgSignInOptions;
    GoogleApiClient mgApiClient;
    final static int SIGN_IN = 1;
    final static int REQUEST_AUTHORIZATION = 2;
    SignInButton mSignInButton;
    final String SERVER_CLIENT_ID = "964645203843-isd2idnvj807sn7sudj6q33rrnkqbtgo.apps.googleusercontent.com";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_fragment_layout, container, false);
        mEmail = rootView.findViewById(R.id.email);
        mPassword = rootView.findViewById(R.id.password);

        mLogInButton = rootView.findViewById(R.id.login_button);
        mLogInButton.setOnClickListener(this);

        mViewSwitcher = rootView.findViewById(R.id.viewSwitcher);
        mViewSwitcher.setDisplayedChild(1);

        mCreateAccTextView = rootView.findViewById(R.id.create_account);
        mCreateAccTextView.setOnClickListener(this);

        mSignInButton = rootView.findViewById(R.id.sign_in_google);
        mSignInButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                if (!TextUtils.isEmpty(mEmail.getText()) && !TextUtils.isEmpty(mPassword.getText())) {
                    logIn(mEmail.getText().toString(), mPassword.getText().toString());
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.error_3006),
                            Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.create_account:
                if (getActivity() instanceof FragmentsInterface) {
                    ((FragmentsInterface) getActivity()).putFragments(CREATE_USER_FRAGMENT);
                }
                break;
            case R.id.sign_in_google:
                googleLogIn();

        }

    }

    private void logIn(final String userEmail, String userPassword) {
        UserAuthManager.getInstance().logIn(userEmail, userPassword,
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
            Log.d(TAG, "google result" + result.isSuccess());
            if (result.isSuccess()) {
                loginInBackendless(result.getSignInAccount());
                goToMainActivity();
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

                    }

                    @Override
                    public void onError(String massage) {
                        Toast.makeText(getActivity(), massage,Toast.LENGTH_SHORT).show();
                    }
                });
    }

}

