package com.example.company.myplanner;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.company.myplanner.service.DeviceBootReceiver;
import com.example.company.myplanner.utils.Todo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddPlannerActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
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
        setContentView(R.layout.activity_add_planner);
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddPlannerActivity.this, AddPlannerActivity.this, year, month, day);
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
        final Todo todo = new Todo();
        String dateTime = dateTimeTextView.getText().toString();
        String dateText = dateTime.split(" ")[0];
        String timeText = dateTime.split(" ")[1];
        monthFinal = Integer.parseInt(dateText.split("/")[1]);
        dayFinal = Integer.parseInt(dateText.split("/")[0]);
        yearFinal = Integer.parseInt(dateText.split("/")[2]);
        hourFinal = Integer.parseInt(timeText.split(":")[0]);
        minuteFinal = Integer.parseInt(timeText.split(":")[1]);
        todo.setName(nameEdtText.getText().toString());
        todo.setMessage(messageEditText.getText().toString());
        todo.setDate(dateTime);
        todo.setUserId(firebaseAuth.getCurrentUser().getUid());
        todo.setKey(key);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, todo.toFirebaseObject());
        database.getReference("todoList").updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    sendNotification(todo);
                    finish();
                }
            }
        });
    }

    private void sendNotification(Todo todo) {

        Intent alarmIntent = new Intent(this, AddPlannerActivity.class);
        Intent intent = new Intent(this, DeviceBootReceiver.class);
        intent.putExtra("title", todo.getName());
        intent.putExtra("message", todo.getMessage());
        intent.putExtra("date", todo.getDate());
        intent.putExtra("key", todo.getKey());
        final int _id = (int) System.currentTimeMillis();
        intent.putExtra("id",_id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,_id,
                intent, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.clear();
        calendar.set(yearFinal, monthFinal - 1, dayFinal, hourFinal, minuteFinal);
        manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        //manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 5000, pendingIntent);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        yearFinal = year;
        monthFinal = month + 1;
        dayFinal = dayOfMonth;
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddPlannerActivity.this, AddPlannerActivity.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hourFinal = hourOfDay;
        minuteFinal = minute;
        dateTimeTextView.setText(dayFinal + "/" + monthFinal + "/" + yearFinal + " " + hourFinal + ":" + minuteFinal);
    }
}
