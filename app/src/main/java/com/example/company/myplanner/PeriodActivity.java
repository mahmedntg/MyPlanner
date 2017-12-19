package com.example.company.myplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;


import com.example.company.myplanner.utils.SharedPreferenceUtil;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PeriodActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private PieChart pieChart;
    ArrayList<Entry> entries;
    ArrayList<String> PieEntryLabels;
    PieDataSet pieDataSet;
    PieData pieData;
    int year, month, day;
    private SharedPreferences preferences;
    private FirebaseAuth firebaseAuth;
    private String userId;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period);
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        SimpleDateFormat format = new SimpleDateFormat("MMM dd");
        Calendar calendar = Calendar.getInstance();
        String date = format.format(calendar.getTime());
        preferences = SharedPreferenceUtil.getInstance(this).getSharedPreferences();
        key = preferences.getString(userId, null);
        int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        String todayDate = format.format(today);
        int firstDay = 1;
        if (key != null) {
            firstDay = Integer.parseInt(key.split(" ")[1]);
            if (!key.split(" ")[0].equalsIgnoreCase(date.split(" ")[0])) {
                today += 30;
            }
        }

        int todayDay = today - firstDay;
        if (todayDay > 28) {
            SharedPreferenceUtil.getInstance(this).clearSharedPreferences();
            key = null;
        }
        int remainingDays=28-todayDay;
        todayDay = todayDay * 100 / 28;
        pieChart = (PieChart) findViewById(R.id.chart);
        pieChart.setUsePercentValues(true);
        entries = new ArrayList<>();
        PieEntryLabels = new ArrayList<String>();
        if (key != null) {
            entries.add(new BarEntry(todayDay, 0));
            entries.add(new BarEntry(100 - todayDay, 1));
            PieEntryLabels.add(key);
            PieEntryLabels.add(date);

        } else {
            entries.add(new BarEntry(0, 0));
            PieEntryLabels.add("No Period Selected");
            remainingDays=0;
        }
        pieChart.setUsePercentValues(false);
        pieDataSet = new PieDataSet(entries, "");
        pieDataSet.setValueTextSize(20f);
        pieDataSet.setSliceSpace(2f);
        pieData = new PieData(PieEntryLabels, pieDataSet);
        if (key != null) pieDataSet.setColors(new int[]{Color.RED, Color.GREEN});
        else pieDataSet.setColors(new int[]{Color.YELLOW});
        pieChart.setData(pieData);
        pieChart.animateY(3000);
        pieChart.setDescription("Track Your Period");
        String remainingString=(remainingDays +" days remaining");
        if(remainingDays<=0){
            remainingString="";
        }

        pieChart.setCenterText("click here to start period \n " +
               remainingString);
        pieChart.setCenterTextSize(15f);
        pieChart.setClickable(true);
        pieChart.setOnTouchListener(null);
        pieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(PeriodActivity.this, PeriodActivity.this, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        this.year = year;
        this.month = month + 1;
        this.day = dayOfMonth;
        SimpleDateFormat format = new SimpleDateFormat("MMM dd");
        calendar.set(year, month, day);
        String date = format.format(calendar.getTime());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(userId, date);
        editor.commit();
        Intent refreshIntenet = new Intent(this, PeriodActivity.class);
        startActivity(refreshIntenet);
        finish();
    }
}

