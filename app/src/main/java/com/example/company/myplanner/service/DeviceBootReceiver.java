package com.example.company.myplanner.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.example.company.myplanner.AddPlannerActivity;
import com.example.company.myplanner.PlannerInfoActivity;
import com.example.company.myplanner.R;
import com.example.company.myplanner.utils.Todo;

import java.util.Random;

/**
 * Created by Mohamed Sayed on 11/7/2017.
 */

public class DeviceBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // on device boot compelete, reset the alarm
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");
        String date = intent.getStringExtra("date");
        String key = intent.getStringExtra("key");
        int id = intent.getIntExtra("id", 0);
        createNotification(context, title, message, date, key, id);
    }

    private void createNotification(Context context, String messageTitle, String messageBody, String date, String key, int id) {
        Intent intent = new Intent(context, PlannerInfoActivity.class);
        Todo todo = new Todo(messageTitle, messageBody, date);
        intent.putExtra("key", key);
        intent.putExtra("todo", todo);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity(context, id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(id, mNotificationBuilder.build());
    }
}
