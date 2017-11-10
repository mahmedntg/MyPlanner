package com.example.company.myplanner;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.company.myplanner.utils.MyAlertDialog;
import com.example.company.myplanner.utils.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.MessageFormat;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private EditText nameET, dobET, userNameET, passwordET, confirmPasswordET,salaryEditText;
    private RadioGroup genderRG;
    private RadioGroup typeRG;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private AlertDialog alertDialog;
    int year, month, day, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        progressDialog = new ProgressDialog(this);
        String alertTitle = getString(R.string.validation_title_alert);
        alertDialog = MyAlertDialog.createAlertDialog(this, alertTitle);
        nameET = (EditText) findViewById(R.id.edit_name);
        dobET = (EditText) findViewById(R.id.edit_dob);
        userNameET = (EditText) findViewById(R.id.edit_userName);
        passwordET = (EditText) findViewById(R.id.edit_password);
        salaryEditText = (EditText) findViewById(R.id.edit_salary);
        genderRG = (RadioGroup) findViewById(R.id.genderRG);
        typeRG = (RadioGroup) findViewById(R.id.typeRG);
        ((RadioButton) findViewById(R.id.maleRadioBtn)).setChecked(true);
        ((RadioButton) findViewById(R.id.studentRadioBtn)).setChecked(true);
        dobET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, RegisterActivity.this, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    public void registerUser(View view) {
        final String name = nameET.getText().toString().trim();
        final String dob = dobET.getText().toString().trim();
        final String userName = userNameET.getText().toString().trim();
        final String password = passwordET.getText().toString().trim();
        final String salary=salaryEditText.getText().toString();
        int genderId = genderRG.getCheckedRadioButtonId();
        int typeId = typeRG.getCheckedRadioButtonId();
        final String gender = ((RadioButton) findViewById(genderId)).getText().toString();
        final String type = ((RadioButton) findViewById(typeId)).getText().toString();
        String message = getString(R.string.value_required_msg).trim();
        User user = new User(name, userName, password, dob, gender, type,salary,"");
        if (!isDataValid(message, user)) {
            return;
        }
        progressDialog.setMessage(getString(R.string.register_user));
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(userName, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String userId = firebaseAuth.getCurrentUser().getUid();
                    DatabaseReference ref = databaseReference.child(userId);
                    ref.child("name").setValue(name);
                    ref.child("dob").setValue(dob);
                    ref.child("gender").setValue(gender);
                    ref.child("type").setValue(type);
                    ref.child("userName").setValue(userName);
                    ref.child("salary").setValue(salary);
                    ref.child("balance").setValue(salary);
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                } else {
                    alertDialog.setMessage(task.getException().getMessage());
                    alertDialog.show();
                }
                progressDialog.dismiss();
            }
        });
    }

    private boolean isDataValid(String message, User user) {
        boolean valid = true;
        if (TextUtils.isEmpty(user.getEmail())) {
            message = MessageFormat.format(message, "userName");
            alertDialog.setMessage(message);
            alertDialog.show();
            valid = false;
        } else if (TextUtils.isEmpty(user.getName())) {
            message = MessageFormat.format(message, "Name");
            alertDialog.setMessage(message);
            alertDialog.show();
            valid = false;
        } else if (TextUtils.isEmpty(user.getDob())) {
            message = MessageFormat.format(message, "Date of birth");
            alertDialog.setMessage(message);
            alertDialog.show();
            valid = false;
        } else if (TextUtils.isEmpty(user.getType())) {
            message = MessageFormat.format(message, "type");
            alertDialog.setMessage(message);
            alertDialog.show();
            valid = false;
        } else if (TextUtils.isEmpty(user.getGender())) {
            message = MessageFormat.format(message, "Gender");
            alertDialog.setMessage(message);
            alertDialog.show();
            valid = false;
        } else if (TextUtils.isEmpty(user.getPassword())) {
            message = MessageFormat.format(message, "Password");
            alertDialog.setMessage(message);
            alertDialog.show();
            valid = false;
        }
        return valid;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        year = year;
        month = month + 1;
        dayOfMonth = dayOfMonth;
        dobET.setText(day + "/" + month + "/" + year);
    }
}
