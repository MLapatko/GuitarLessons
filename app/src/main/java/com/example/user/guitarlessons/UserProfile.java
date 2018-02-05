package com.example.user.guitarlessons;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

/**
 * Created by user on 05.02.2018.
 */

public class UserProfile extends AppCompatActivity implements View.OnClickListener {
    Button logOutButton;
    final static String TAG="mylog";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        logOutButton=(Button)findViewById(R.id.log_out);
        logOutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.log_out:
                logOut();
                break;
        }
    }

    private void logOut() {
        Backendless.UserService.logout(new AsyncCallback<Void>() {
            @Override
            public void handleResponse(Void response) {
                Toast.makeText(UserProfile.this,"log out successfully",
                        Toast.LENGTH_SHORT).show();
                Log.d(TAG,"log out successfully");
                Intent i=new Intent(UserProfile.this,MainActivity.class);
                startActivity(i);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d(TAG,"log out problems " +fault.getCode());
            }
        });
    }
}
