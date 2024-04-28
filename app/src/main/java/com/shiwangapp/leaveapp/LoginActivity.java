package com.shiwangapp.leaveapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private Button signInBt;
    private TextView signUpBt;
    private EditText emailEt, passwordEt;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setUpListeners();

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = firebaseAuth -> {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user !=null){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };


    }

    private void setUpListeners() {
        signInBt = findViewById(R.id.signIn_bt);
        signUpBt = findViewById(R.id.signUp_bt);
        progressBar = findViewById(R.id.progressBar);
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);


        signUpBt.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            finish();
        });

        signInBt.setOnClickListener(view -> {
            if (isValidDetails()){
                loading(true);
                final String email = emailEt.getText().toString();
                final String password = passwordEt.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, task -> {
                    if(task.isSuccessful()){
                        loading(false);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }else {
                        loading(false);
                        Toast.makeText(LoginActivity.this, "sign in error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private boolean isValidDetails() {
        if (emailEt.getText().toString().trim().isEmpty()){
            emailEt.setError("Please enter email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailEt.getText().toString()).matches()) {
            emailEt.setError("Enter valid email");
            return false;
        } else if (passwordEt.getText().toString().trim().isEmpty()) {
            passwordEt.setError("Please enter password");
            return false;
        }
        else {
            return true;
        }
    }

    private void loading(boolean isLoading){
        if (isLoading){
            signInBt.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }else {
            signInBt.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}