package com.example.user.guitarlessons;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

/**
 * Created by user on 07.02.2018.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {
    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }
    TextView createAccTextView;
    EditText email;
    EditText password;
    Button logInButton;
    final static String TAG = "mylog";
    ViewSwitcher viewSwitcher;

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

        createAccTextView=rootView.findViewById(R.id.create_account);
        createAccTextView.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty())
                    logIn(email.getText().toString(), password.getText().toString());
                break;
            case R.id.create_account:
                Fragment fragment=CreateAccountFragment.newInstance();
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(CreateAccountFragment.class.getSimpleName());
                ft.replace(R.id.content_main,fragment,null);
                ft.commit();
                break;

        }
    }

    private void logIn(final String userEmail, String userPassword) {
        Backendless.UserService.login(userEmail, userPassword, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser user) {
                Log.d(TAG, "user email" + user.getEmail());
                viewSwitcher.setDisplayedChild(0);
                goToUserProfile();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e(TAG, "failed log in" + fault.getCode());
                Toast.makeText(LoginFragment.this.getActivity(), "failed log in",
                        Toast.LENGTH_SHORT).show();
            }
        }, true);
    }

    public void goToUserProfile() {
        Intent i = new Intent(getActivity(), UserProfile.class);
        getActivity().startActivity(i);
        getActivity().finish();
    }
}
