package com.example.company.myplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.company.myplanner.utils.Todo;

public class PlannerInfoActivity extends AppCompatActivity {
    private TextView dateTimeTextView, messageTextView, nameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner_info);
        Bundle extras = getIntent().getExtras();
        Todo todo = (Todo) extras.get("todo");
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        messageTextView = (TextView) findViewById(R.id.messageTextView);
        dateTimeTextView = (TextView) findViewById(R.id.dateTimeTextView);
        nameTextView.setText(todo.getName());
        messageTextView.setText(todo.getMessage());
        dateTimeTextView.setText(todo.getDate());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Todo todo = (Todo) intent.getExtras().get("todo");
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        messageTextView = (TextView) findViewById(R.id.messageTextView);
        dateTimeTextView = (TextView) findViewById(R.id.dateTimeTextView);
        nameTextView.setText(todo.getName());
        messageTextView.setText(todo.getMessage());
        dateTimeTextView.setText(todo.getDate());
    }
}
