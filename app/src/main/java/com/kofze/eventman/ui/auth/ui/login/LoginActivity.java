package com.kofze.eventman.ui.auth.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kofze.eventman.MainActivity;
import com.kofze.eventman.R;
import com.kofze.eventman.SignupActivity;

import java.util.regex.Pattern;


public class LoginActivity extends AppCompatActivity {

    private TextInputEditText username , password;
    private Button login,forgotpassword,signup;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        init();


    }

    private void init() {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        TextInputLayout usernamebox = findViewById(R.id.usernamebox);
        TextInputLayout passwordbox = findViewById(R.id.passwordbox);
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signUpButton);
        forgotpassword = findViewById(R.id.forgotPassword);

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
                boolean validEmail = Pattern.compile(regexPattern)
                        .matcher(s)
                        .matches();

                if (s.length()<6 || !validEmail) {
                    usernamebox.setErrorEnabled(true);
                    if (!validEmail) usernamebox.setError("Invalid Email");
                    usernamebox.setErrorIconDrawable(R.drawable.ic_baseline_error_outline_24);
                }
                else{
                    usernamebox.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()<8) {
                    passwordbox.setErrorEnabled(true);
                    passwordbox.setError("Too small");
                    passwordbox.setErrorIconDrawable(R.drawable.ic_baseline_error_outline_24);
                }
                else if (s.length()>16) {
                    passwordbox.setErrorEnabled(true);
                    passwordbox.setError("Too large");
                    passwordbox.setErrorIconDrawable(R.drawable.ic_baseline_error_outline_24);
                }
                else{
                    passwordbox.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent();
                intent.setClass(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        FirebaseUser account = FirebaseAuth.getInstance().getCurrentUser();
        if (account!=null && getIntent().getBooleanExtra("autoLogin",true)){
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}