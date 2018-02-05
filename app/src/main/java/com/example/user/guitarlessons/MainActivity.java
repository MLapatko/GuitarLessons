package com.example.user.guitarlessons;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText email;
    EditText password;
    Button confirmButton;
    Button logInButton;
    final static String TAG="mylog";
    BackendlessUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Backendless.setUrl( Defaults.SERVER_URL );
        Backendless.initApp( getApplicationContext(), Defaults.APPLICATION_ID, Defaults.API_KEY );

        email=(EditText) findViewById(R.id.user_email);
        password=(EditText) findViewById(R.id.user_password);
        confirmButton=(Button) findViewById(R.id.ok_button);
        confirmButton.setOnClickListener(this);
        logInButton=(Button)findViewById(R.id.login_button);
        logInButton.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser=Backendless.UserService.CurrentUser();
        if (currentUser!=null){
            Log.d(TAG,"current user"+ currentUser.getEmail());
            Intent i=new Intent(MainActivity.this,UserProfile.class);
            startActivity(i);
        }
    }

    private void registrateUser(String userEmail, String userPassword){
        BackendlessUser user=new BackendlessUser();
        user.setProperty("email",userEmail);
        user.setPassword(userPassword);
        Log.d(TAG,"email"+userEmail);
        Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                Toast.makeText(MainActivity.this,response.getEmail(),
                        Toast.LENGTH_SHORT).show();
                Log.d(TAG,"success");
                Intent i=new Intent(MainActivity.this,UserProfile.class);
                startActivity(i);
                finish();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(MainActivity.this,"registration failed",
                        Toast.LENGTH_SHORT).show();
                Log.e(TAG,"registration failed "+fault.getCode());
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ok_button:
                if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty())
                registrateUser(email.getText().toString(),password.getText().toString());
                break;
                case R.id.login_button:
                    Intent i=new Intent(MainActivity.this,LogInActivity.class);
                    startActivity(i);
                    break;
        }
    }
}
