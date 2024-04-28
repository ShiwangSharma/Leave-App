package com.shiwangapp.leaveapp;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameEt, erpIdEt, passwordEt, rePasswordEt, emailEt;
    private Button signUpBt;
    private TextView signInBt;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setUpListeners();
    }

    private void setUpListeners() {

        nameEt = findViewById(R.id.nameEt);
        erpIdEt = findViewById(R.id.erpIdEt);
        passwordEt = findViewById(R.id.passwordEt);
        rePasswordEt = findViewById(R.id.rePasswordEt);
        signUpBt = findViewById(R.id.signUpBt);
        signInBt = findViewById(R.id.signInBt);
        progressBar = findViewById(R.id.progressBar);
        emailEt = findViewById(R.id.emailEt);

        signInBt.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });


        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = firebaseAuth -> {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user!=null){
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;

            }
        };

        signUpBt.setOnClickListener(view -> {
            if (isValidSignUpDetails()){

                signUp();

            }
        });






    }

    private void signUp() {
        loading(true);

        String name = nameEt.getText().toString();
        String pass = passwordEt.getText().toString();
        String erp = erpIdEt.getText().toString();
        String email = emailEt.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if (!task.isSuccessful()){
                loading(false);
                Toast.makeText(this, "Error in signUp!", Toast.LENGTH_SHORT).show();
            }else {
                String userId = mAuth.getCurrentUser().getUid();
                FirebaseFirestore database = FirebaseFirestore.getInstance();

                HashMap<String, Object> userInfo = new HashMap<>();
                userInfo.put("ErpId", erp);
                userInfo.put("Name", name);
                userInfo.put("Email", email);

                database.collection("Students").document(userId).set(userInfo).addOnSuccessListener(unused -> {
                    loading(false);
                    //preferenceManager.putString("userId",documentReference.getId());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }).addOnFailureListener(e -> {
                    loading(false);
                    Toast.makeText(this, "Error in adding database", Toast.LENGTH_SHORT).show();
                });

            }
        });
    }

    private boolean isValidSignUpDetails() {
        if (emailEt.getText().toString().trim().isEmpty()){
            emailEt.setError("Please enter email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailEt.getText().toString()).matches()) {
            emailEt.setError("Enter valid email");
            return false;
        }else if (nameEt.getText().toString().trim().isEmpty()){
            nameEt.setError("Please enter name");
            return false;
        }else if (erpIdEt.getText().toString().trim().isEmpty()) {
            erpIdEt.setError("Please enter erpId");
            return false;
        }else if (passwordEt.getText().toString().trim().isEmpty()) {
            passwordEt.setError("Please enter password");
            return false;
        }else if(rePasswordEt.getText().toString().trim().isEmpty()){
            rePasswordEt.setError("Please re enter password");
            return false;
        } else if (!rePasswordEt.getText().toString().equals(passwordEt.getText().toString())) {
            rePasswordEt.setError("password does not match");
            return false;
        }
        else {
            return true;
        }
    }

    private void loading(boolean isLoading){
        if (isLoading){
            signUpBt.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }else {
            signUpBt.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

    }
}