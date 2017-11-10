package com.example.company.myplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.company.myplanner.utils.Budget;
import com.example.company.myplanner.utils.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AddBudgetActivity extends AppCompatActivity {
    private EditText commitEditText;
    private EditText valueEditText;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference userReference;
    String userId;
    double balance = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget);
        commitEditText = (EditText) findViewById(R.id.commitEditText);
        valueEditText = (EditText) findViewById(R.id.valueEditText);
        firebaseAuth = FirebaseAuth.getInstance();
        userReference = FirebaseDatabase.getInstance().getReference("users");
        userId = firebaseAuth.getCurrentUser().getUid();
        userReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                balance = Double.parseDouble(user.getBalance());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addBtnClicked(View view) {
        String commitment = commitEditText.getText().toString().trim();
        String value = valueEditText.getText().toString();
        double myValue = Double.parseDouble(value);
        balance =balance-myValue;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = FirebaseDatabase.getInstance().getReference("budget").push().getKey();
        Budget budget = new Budget();
        budget.setUserId(userId);
        budget.setCommitment(commitment);
        budget.setValue(value);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, budget.toFirebaseObject());
        database.getReference("budget").updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Map<String, Object> updatedData = new HashMap<>();
                    updatedData.put("balance", String.valueOf(balance));
                    userReference.child(userId).updateChildren(updatedData);
                    finish();
                }
            }
        });
         }
}
