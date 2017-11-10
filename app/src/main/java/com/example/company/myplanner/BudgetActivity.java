package com.example.company.myplanner;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.company.myplanner.utils.Budget;
import com.example.company.myplanner.utils.BudgetAdapter;
import com.example.company.myplanner.utils.Todo;
import com.example.company.myplanner.utils.TodoAdapter;
import com.example.company.myplanner.utils.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BudgetActivity extends AppCompatActivity {
    ArrayList<Budget> budgetList;
    private FirebaseAuth firebaseAuth;
    private BudgetAdapter mAdapter;
    private FirebaseDatabase database;
    private DatabaseReference userReference;
    double salary = 0.0;
    double balance = 0.0;
    private TextView salary_balanceTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        salary_balanceTV=(TextView)findViewById(R.id.salary_balanceTV);
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userReference = FirebaseDatabase.getInstance().getReference("users");
        userReference.child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                salary = Double.parseDouble(user.getSalary());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(BudgetActivity.this, AddBudgetActivity.class);
                startActivity(newIntent);
            }
        });

        budgetList = new ArrayList<>();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.myrecycleView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new BudgetAdapter(budgetList, BudgetActivity.this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userReference.child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                balance = Double.parseDouble(user.getBalance());
                salary_balanceTV.setText("Salary: "+salary+" - Balance: "+balance);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        database.getReference("budget").orderByChild("userId").equalTo(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        budgetList.clear();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Budget budget = data.getValue(Budget.class);
                            budget.setKey(data.getKey());
                            budgetList.add(budget);
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                    }
                });

    }

    public void deleteBudgetItem(int position, String key) {
        database.getReference("budget").child(key).removeValue();
        String value = budgetList.get(position).getValue();
        balance += Double.parseDouble(value);
        budgetList.remove(position);
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("balance", String.valueOf(balance));
        userReference.child(firebaseAuth.getCurrentUser().getUid()).updateChildren(updatedData);
        mAdapter.notifyDataSetChanged();
        salary_balanceTV.setText("Salary: "+salary+" - Balance: "+balance);
    }
}
