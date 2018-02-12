package com.example.user.guitarlessons;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
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
    final int CREATE_USER_FRAGMENT=2;

    TextView createAccTextView;
    EditText email;
    EditText password;
    Button logInButton;
    final static String TAG = "mylog";
    ViewSwitcher viewSwitcher;
    GoogleSignInOptions gSignInOptions;
    GoogleApiClient gApiClient;
    final static int SIGN_IN = 1;
    final static int REQUEST_AUTHORIZATION = 2;
    SignInButton signInButton;
    final String SERVER_CLIENT_ID="964645203843-isd2idnvj807sn7sudj6q33rrnkqbtgo.apps.googleusercontent.com";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_fragment_layout, container, false);
        email = rootView.findViewById(R.id.email);
        password = rootView.findViewById(R.id.password);

        logInButton = rootView.findViewById(R.id.login_button);
        logInButton.setOnClickListener(this);

        viewSwitcher = rootView.findViewById(R.id.viewSwitcher);
        viewSwitcher.setDisplayedChild(1);

        createAccTextView = rootView.findViewById(R.id.create_account);
        createAccTextView.setOnClickListener(this);

        signInButton = rootView.findViewById(R.id.sign_in_google);
        signInButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );

        FragmentActivity fragmentActivity = (FragmentActivity)this.getActivity();

        gSignInOptions = new GoogleSignInOptions.Builder( GoogleSignInOptions.DEFAULT_SIGN_IN )
                .requestEmail().requestProfile().requestId()
                .requestIdToken(SERVER_CLIENT_ID)
                .build();


        GoogleApiClient.Builder apiCliBuilder = new GoogleApiClient.Builder( fragmentActivity );
        gApiClient = apiCliBuilder
                .enableAutoManage( fragmentActivity, this )
                .addApi( Auth.GOOGLE_SIGN_IN_API, gSignInOptions ).build();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((LogInActivity)getActivity()).setBackButtonStatus(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty())
                    logIn(email.getText().toString(), password.getText().toString());
                break;
            case R.id.create_account:
                ((LogInActivity)getActivity()).putFragments(CREATE_USER_FRAGMENT,R.id.content_main);
                ((LogInActivity)getActivity()).setBackButtonStatus(true);
                break;
            case R.id.sign_in_google:
                googleLogIn();

        }
    }

    private void logIn(final String userEmail, String userPassword) {
        Backendless.UserService.login(userEmail, userPassword, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser user) {
                Log.d(TAG, "user email" + user.getEmail());
                viewSwitcher.setDisplayedChild(0);
                goToMainActivity();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG, "failed log in" + fault.getCode());
                Toast.makeText(LoginFragment.this.getActivity(), "failed log in",
                        Toast.LENGTH_SHORT).show();
            }
        }, true);
    }

    public void goToMainActivity() {
        MainActivity.start(this.getActivity());
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void googleLogIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(gApiClient);
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
        Log.d(TAG, "handleSignInResult: try login to backendless");
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
        Log.d(TAG, "idToken: " + idToken + ", accessToken: " + accessToken);

        Map<String, String> googlePlusFieldsMapping = new HashMap<String, String>();
        googlePlusFieldsMapping.put("given_name", "gp_given_name");
        googlePlusFieldsMapping.put("family_name", "gp_family_name");
        googlePlusFieldsMapping.put("gender", "gender");
        googlePlusFieldsMapping.put("email", "email");
        List<String> permissions = new ArrayList<String>();

        if (idToken != null && accessToken != null)
            Backendless.UserService.loginWithGooglePlusSdk(idToken,
                    accessToken,
                    googlePlusFieldsMapping,
                    permissions,
                    new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser backendlessUser) {
                            Log.i(TAG, "Logged in to backendless, user id is: " + backendlessUser.getObjectId());
                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {
                            Log.e(TAG, "Could not login to backendless: " +
                                    backendlessFault.getMessage() +
                                    " code: " + backendlessFault.getCode());
                        }
                    },true);
    }

}

