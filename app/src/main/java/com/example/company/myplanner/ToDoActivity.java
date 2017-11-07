package com.example.company.myplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.company.myplanner.service.MyClient;
import com.example.company.myplanner.utils.Todo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToDoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private FirebaseAuth firebaseAuth;
    String key = null;
    int year, month, day, hour, minute;
    int yearFinal, monthFinal, dayFinal, hourFinal, minuteFinal;
    private EditText nameEdtText, messageEditText;
    private TextView dateTimeTextView;
    private Button dateTimeBtn, addToDoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        nameEdtText = (EditText) findViewById(R.id.nameEditText);
        messageEditText = (EditText) findViewById(R.id.messageEditText);
        dateTimeBtn = (Button) findViewById(R.id.dateTimeBtn);
        addToDoBtn = (Button) findViewById(R.id.addToDoBtn);
        dateTimeTextView = (TextView) findViewById(R.id.dateTimeTextView);
        firebaseAuth = FirebaseAuth.getInstance();
        dateTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ToDoActivity.this, ToDoActivity.this, year, month, day);
                datePickerDialog.show();
            }
        });
        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            Todo todo = (Todo) extras.get("todo");
            String toDoKey = (String) extras.get("id");
            nameEdtText.setText(todo.getName());
            messageEditText.setText(todo.getMessage());
            dateTimeTextView.setText(todo.getDate());
            if (toDoKey != null) {
                key = extras.get("id").toString();
                addToDoBtn.setText("Update");
            } else if (toDoKey == null && todo != null) {

                nameEdtText.setEnabled(false);
                messageEditText.setEnabled(false);
                dateTimeTextView.setActivated(false);
                addToDoBtn.setVisibility(View.GONE);
                dateTimeBtn.setVisibility(View.GONE);
            }
        }
        addToDoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTodo();
            }
        });
    }

    void saveTodo() {
        //save it to the firebase db
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        key = key == null ? database.getReference("todoList").push().getKey() : key;
        Todo todo = new Todo();
        todo.setName(nameEdtText.getText().toString());
        todo.setMessage(messageEditText.getText().toString());
        todo.setDate(dateTimeTextView.getText().toString());
        todo.setUserId(firebaseAuth.getCurrentUser().getUid());
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, todo.toFirebaseObject());
        database.getReference("todoList").updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    finish();
                }
            }
        });
        MyClient m=new MyClient();
        m.execute(todo.getName(),todo.getMessage());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        yearFinal = year;
        monthFinal = month + 1;
        dayFinal = dayOfMonth;
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(ToDoActivity.this, ToDoActivity.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hourFinal = hourOfDay;
        minuteFinal = minute;
        dateTimeTextView.setText(dayFinal + "/" + monthFinal + "/" + yearFinal + " " + hourFinal + ":" + minuteFinal);
    }
}
