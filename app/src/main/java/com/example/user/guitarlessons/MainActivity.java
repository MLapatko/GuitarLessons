package com.example.user.guitarlessons;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.persistence.local.UserTokenStorageFactory;

public class MainActivity extends AppCompatActivity  {
    final String TAG="mylog";


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


    }

   @Override
    protected void onStart() {
        super.onStart();
        String userToken = UserTokenStorageFactory.instance().getStorage().get();
       Log.d(TAG,"userToken"+userToken);
        if (userToken != null && !userToken.equals("")) {
            Intent i = new Intent(MainActivity.this, UserProfile.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }
}
