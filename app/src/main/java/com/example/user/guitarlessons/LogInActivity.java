package com.example.user.guitarlessons;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

/**
 * Created by user on 05.02.2018.
 */

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{
    EditText email;
    EditText password;
    Button logInButton;
    final static String TAG="mylog";
    ViewSwitcher viewSwitcher;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        email=(EditText) findViewById(R.id.email);
        password=(EditText) findViewById(R.id.password);

        logInButton=(Button)findViewById(R.id.login_button);
        logInButton.setOnClickListener(this);

        viewSwitcher=(ViewSwitcher) findViewById(R.id.viewSwitcher);
        viewSwitcher.setDisplayedChild(1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_button:
                if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty())
                    logIn(email.getText().toString(),password.getText().toString());
                break;

        }
    }

    private void logIn(final String userEmail, String userPassword) {
        Backendless.UserService.login(userEmail,userPassword, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser user) {
                Log.d(TAG,"user email"+user.getEmail());
                Toast.makeText(LogInActivity.this,"log in "+user.getEmail(),
                        Toast.LENGTH_SHORT).show();
                viewSwitcher.setDisplayedChild(0);
                goToUserProfile();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG,"failed log in" + fault.getCode());
                Toast.makeText(LogInActivity.this,"failed log in",
                        Toast.LENGTH_SHORT).show();
            }
        },true);
    }
    public void goToUserProfile(){

        Intent i =new Intent(LogInActivity.this,UserProfile.class);
        startActivity(i);
        finish();
    }
}
