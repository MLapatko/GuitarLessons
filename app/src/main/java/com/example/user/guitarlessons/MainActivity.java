package com.example.user.guitarlessons;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.backendless.Backendless;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {
    final String TAG="mylog";
  /*  EditText email;
    EditText password;
    Button confirmButton;
    Button logInButton;
    final static String TAG = "mylog";
    ViewSwitcher viewSwitcher;
    BackendlessUser currentUser;
    GoogleSignInOptions gSignInOptions;
    GoogleApiClient gApiClient;
    final static int SIGN_IN = 1;
    final static int REQUEST_AUTHORIZATION = 2;
    SignInButton signInButton;*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(getApplicationContext(), Defaults.APPLICATION_ID, Defaults.API_KEY);
        Fragment fragment=LoginFragment.newInstance();
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
       // ft.addToBackStack(LoginFragment.class.getSimpleName());
        ft.add(R.id.content_main,fragment,null);
        ft.commit();

        /*email = (EditText) findViewById(R.id.user_email);
        password = (EditText) findViewById(R.id.user_password);
        confirmButton = (Button) findViewById(R.id.ok_button);
        confirmButton.setOnClickListener(this);
        logInButton = (Button) findViewById(R.id.login_button);
        logInButton.setOnClickListener(this);

        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        viewSwitcher.setDisplayedChild(1);


        gSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile().requestId()
                .requestIdToken("964645203843-isd2idnvj807sn7sudj6q33rrnkqbtgo.apps.googleusercontent.com")
                .build();

        FragmentActivity fragmentActivity = (FragmentActivity) this;

        GoogleApiClient.Builder apiCliBuilder = new GoogleApiClient.Builder(fragmentActivity);
        gApiClient = apiCliBuilder
                .enableAutoManage(fragmentActivity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gSignInOptions).build();

        signInButton = (SignInButton) findViewById(R.id.sign_in_google);
        signInButton.setOnClickListener(this);*/


    }

   /* @Override
    protected void onStart() {
        super.onStart();
        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(getApplicationContext(), Defaults.APPLICATION_ID, Defaults.API_KEY);
        currentUser = Backendless.UserService.CurrentUser();
        String userToken = UserTokenStorageFactory.instance().getStorage().get();

        if (userToken != null && !userToken.equals("")) {
            Intent i = new Intent(MainActivity.this, UserProfile.class);
            startActivity(i);
        }
    }

    private void registrateUser(String userEmail, String userPassword) {
        BackendlessUser user = new BackendlessUser();
        user.setProperty("email", userEmail);
        user.setPassword(userPassword);
        Log.d(TAG, "email" + userEmail);
        Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                Toast.makeText(MainActivity.this, response.getEmail(),
                        Toast.LENGTH_SHORT).show();
                Log.d(TAG, "success");
                viewSwitcher.setDisplayedChild(0);
                Intent i = new Intent(MainActivity.this, UserProfile.class);
                startActivity(i);
                finish();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(MainActivity.this, "registration failed",
                        Toast.LENGTH_SHORT).show();
                Log.e(TAG, "registration failed " + fault.getCode());
            }
        });


    }*/

    @Override
    public void onClick(View view) {
      /* switch (view.getId()) {
            case R.id.ok_button:
                if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty())
                    registrateUser(email.getText().toString(), password.getText().toString());
                break;
            case R.id.login_button:
                Intent i = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(i);
                break;
            case R.id.sign_in_google:
                googleLogIn();
        }*/
    }

  /*  private void googleLogIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(gApiClient);
        this.startActivityForResult(signInIntent, SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d(TAG, "google result" + result.isSuccess());
            if (result.isSuccess()) {
                loginInBackendless(result.getSignInAccount());
                goToUserProfile();
            }

        }
    }

    private void loginInBackendless(final GoogleSignInAccount acct) {
        Log.d(TAG, "handleSignInResult: try login to backendless");
        final MainActivity mainActivity = (MainActivity) this;
        final String accountName = acct.getEmail();
        final String scopes = "oauth2:" + Scopes.PLUS_LOGIN + " " +
                Scopes.PLUS_ME + " " + Scopes.PROFILE + " " +
                Scopes.EMAIL;

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String token = null;
                try {
                    token = GoogleAuthUtil.getToken(mainActivity, accountName, scopes);
                    GoogleAuthUtil.invalidateToken(mainActivity, token);
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
                            //  refreshUIonLoginBackendless(LoginFragment.this.getActivity(), backendlessUser );
                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {
                            Log.e(TAG, "Could not login to backendless: " +
                                    backendlessFault.getMessage() +
                                    " code: " + backendlessFault.getCode());
                        }
                    });
    }

    public void goToUserProfile() {
        Intent i = new Intent(MainActivity.this, UserProfile.class);
        startActivity(i);
    }
*/
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
