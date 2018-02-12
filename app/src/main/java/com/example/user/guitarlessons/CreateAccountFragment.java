package com.example.user.guitarlessons;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

/**
 * Created by user on 07.02.2018.
 */

public class CreateAccountFragment extends Fragment implements View.OnClickListener {
    public static CreateAccountFragment newInstance() {

        Bundle args = new Bundle();

        CreateAccountFragment fragment = new CreateAccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    EditText email;
    EditText password;
    EditText userName;
    Button confirmButton;
    final static String TAG = "mylog";
    ViewSwitcher viewSwitcher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.create_account_fragment, container, false);
        userName = rootView.findViewById(R.id.user_name);
        email = rootView.findViewById(R.id.user_email);
        password = rootView.findViewById(R.id.user_password);
        confirmButton = rootView.findViewById(R.id.ok_button);
        confirmButton.setOnClickListener(this);

        viewSwitcher = rootView.findViewById(R.id.viewSwitcher);
        viewSwitcher.setDisplayedChild(1);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok_button:
                if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()
                        &&!userName.getText().toString().isEmpty())
                    registrateUser(email.getText().toString(), password.getText().toString()
                            ,userName.getText().toString());
                break;
        }

    }

    private void registrateUser(String userEmail, String userPassword,String userName) {
        BackendlessUser user = new BackendlessUser();
        user.setProperty("email", userEmail);
        user.setProperty("name",userName);
        user.setPassword(userPassword);
        Log.d(TAG, "email" + userEmail);
        Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser response) {
                Toast.makeText(getActivity(), response.getEmail(),
                        Toast.LENGTH_SHORT).show();
                Log.d(TAG, "success");
                viewSwitcher.setDisplayedChild(0);
                goToMainActivity();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(getActivity(), "registration failed",
                        Toast.LENGTH_SHORT).show();
                Log.e(TAG, "registration failed " + fault.getCode());
            }
        });


    }

    private void goToMainActivity() {
        MainActivity.start(this.getActivity());
    }
}
