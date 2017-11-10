package com.example.company.myplanner;

import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.example.company.myplanner.utils.Gender;
import com.example.company.myplanner.utils.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PlannerBudgetActivity extends TabActivity {
    TabHost tabHostWindow;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userReference;
    private Gender gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner_budget);
        firebaseAuth = FirebaseAuth.getInstance();
        userReference = FirebaseDatabase.getInstance().getReference("users");
        Intent intent;
        tabHostWindow = (TabHost) findViewById(android.R.id.tabhost); // initiate TabHost
        TabSpec tabMenu1 = tabHostWindow.newTabSpec("planner");
        TabSpec tabMenu2 = tabHostWindow.newTabSpec("budget");
        tabMenu1.setIndicator("Planner"); // set the “Planner” as an indicator
        intent = new Intent(this, PlannerActivity.class);
        tabMenu1.setContent(intent);
        tabMenu2.setIndicator("Budget");
        intent = new Intent(this, BudgetActivity.class);
        tabMenu2.setContent(intent);
        tabHostWindow.addTab(tabMenu1);
        tabHostWindow.addTab(tabMenu2);
        tabHostWindow.setCurrentTab(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String userId = firebaseAuth.getCurrentUser().getUid();
        userReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                gender = Gender.valueOf(user.getGender().toUpperCase());
                if (gender.equals(Gender.FEMALE)) {
                    TabSpec tabMenu3 = tabHostWindow.newTabSpec("period");
                    tabMenu3.setIndicator("Period");
                    Intent intent = new Intent(PlannerBudgetActivity.this, PeriodActivity.class);
                    tabMenu3.setContent(intent);
                    tabHostWindow.addTab(tabMenu3);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
