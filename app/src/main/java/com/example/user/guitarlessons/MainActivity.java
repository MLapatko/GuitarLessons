package com.example.user.guitarlessons;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.persistence.local.UserTokenStorageFactory;

public class MainActivity extends AppCompatActivity  {
    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }
    final String TAG="mylog";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(getApplicationContext(), Defaults.APPLICATION_ID, Defaults.API_KEY);

        toolbar =findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

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
            UserProfileActivity.start(this);
        }
    }
}
