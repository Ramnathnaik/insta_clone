package com.example.instaclone;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import es.dmoral.toasty.Toasty;

public class SignUpFragment extends Fragment {

    private EditText email, username, password, confirmPassword;
    private Button signUpBtn;
    private ProgressBar progressBar;
    private TextView alreadyHaveAnAccount;
    private FrameLayout parentFrameLayout;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        parentFrameLayout = getActivity().findViewById(R.id.register_framelayout);
        username = view.findViewById(R.id.username);
        email = view.findViewById(R.id.email_address);
        password = view.findViewById(R.id.password);
        confirmPassword = view.findViewById(R.id.confirm_password);
        signUpBtn = view.findViewById(R.id.signup_btn);
        progressBar = view.findViewById(R.id.progressbar);
        alreadyHaveAnAccount = view.findViewById(R.id.already_have_an_account_tv);

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

        email.addTextChangedListener(new TextWatcher() {
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

        confirmPassword.addTextChangedListener(new TextWatcher() {
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

        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                checkEmailAndPassword();
            }
        });
    }

    private void checkInputs() {
        if (username.getText().toString().equals("")) {
            username.setError("required");
        } else if (email.getText().toString().equals("")) {
            email.setError("required");
        } else if (password.getText().toString().equals("")) {
            password.setError("required");
        } else if (confirmPassword.getText().toString().equals("")) {
            confirmPassword.setError("required");
        }
    }

    private void checkEmailAndPassword() {
        if (email.getText().toString().matches(emailPattern)) {
            if (password.getText().toString().equals(confirmPassword.getText().toString())) {
                final ParseUser user = new ParseUser();
                user.setEmail(email.getText().toString());
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toasty.error(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT, true).show();
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toasty.success(getActivity().getApplicationContext(), "successfully signed up! login to account", Toasty.LENGTH_SHORT, true).show();
                            ParseUser.logOut();
                            setFragment(new SignInFragment());
                        }
                    }
                });
            } else {
                confirmPassword.setError("password does not match");
                progressBar.setVisibility(View.INVISIBLE);
            }
        } else {
            email.setError("invalid email");
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}