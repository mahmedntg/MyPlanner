package com.example.company.myplanner;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.company.myplanner.utils.MyAlertDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private EditText emailET, passwordET;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String alertTitle = getString(R.string.validation_title_alert);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        alertDialog = MyAlertDialog.createAlertDialog(this, alertTitle);
        emailET = (EditText) findViewById(R.id.loginEmailET);
        passwordET = (EditText) findViewById(R.id.loginPasswordET);
    }

    public void registerBtnClicked(View view) {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    public void loginBtnClicked(View view) {
        if (!checkUserExists()) {
            return;
        }
    }

    private boolean checkUserExists() {
        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        final boolean valid = true;
        if (TextUtils.isEmpty(email)) {
            alertDialog.setMessage("email is required!");
            alertDialog.show();
            return false;
        } else if (TextUtils.isEmpty(password)) {
            alertDialog.setMessage("password is required!");
            alertDialog.show();
            return false;
        }
        progressDialog.setMessage(getString(R.string.login_user));
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(MainActivity.this, PlannerActivity.class));
                } else {
                    alertDialog.setMessage(task.getException().getMessage());
                    alertDialog.show();
                }
                progressDialog.dismiss();
            }
        });
        return valid;
    }

}
