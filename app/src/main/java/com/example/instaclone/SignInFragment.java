package com.example.instaclone;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import es.dmoral.toasty.Toasty;

public class SignInFragment extends Fragment {

    private EditText username, password;
    private Button logInBtn;
    private ProgressBar progressBar;
    private TextView dontHaveAnAccount;
    private FrameLayout parentFrameLayout;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        parentFrameLayout = getActivity().findViewById(R.id.register_framelayout);
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        logInBtn = view.findViewById(R.id.login_btn);
        progressBar = view.findViewById(R.id.progressbar);
        dontHaveAnAccount = view.findViewById(R.id.dont_have_account_tv);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                checkUsernameAndPassword();
            }
        });

        dontHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignUpFragment());
            }
        });

    }

    private void checkInputs() {
        if (username.getText().toString().equals("")) {
            username.setError("required");
        } else if (password.getText().toString().equals("")) {
            password.setError("required");
        }
    }

    private void checkUsernameAndPassword() {
        ParseUser user = new ParseUser();
        user.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toasty.error(getActivity().getApplicationContext(), e.getMessage(), Toasty.LENGTH_SHORT, true).show();
                } else {
                    if (user.getBoolean("emailVerified")) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toasty.success(getActivity().getApplicationContext(), "logged in successfully!", Toasty.LENGTH_SHORT, true).show();
                        setIntent();
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toasty.info(getActivity().getApplicationContext(), "please verify your email", Toasty.LENGTH_SHORT, true).show();
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setIntent() {
        Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
        startActivity(homeIntent);
        getActivity().finish();
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}