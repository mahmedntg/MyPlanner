package com.example.company.myplanner.service;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.example.company.myplanner.utils.Todo;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mohamed Sayed on 11/6/2017.
 */

public class MyClient extends AsyncTask<String, Void, Bitmap> {
    public static void sendRequest(String... parmas) {
        String result = "";
        try {

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Authorization", "key=AIzaSyDvLoIMnOpJurxUSNYcqCkZakVATCye4ZY");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            JSONObject jsonParam = new JSONObject();
            JSONObject jsonParam1 = new JSONObject();

            JSONObject jsonData = new JSONObject();
            jsonParam1.put("title", parmas[0]);
            jsonParam1.put("body", parmas[1]);
            jsonParam.put("to", FirebaseInstanceId.getInstance().getToken());
            jsonParam.put("notification", jsonParam1);

            jsonData.put("time", "18:08");
            jsonData.put("score", "5x1");
            jsonParam.put("data", jsonData);

            Log.i("JSON", jsonParam.toString());
            OutputStreamWriter wr = new OutputStreamWriter(
                    conn.getOutputStream());
            wr.write(jsonParam.toString());
            wr.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            result = "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            result = "FAILURE";
        }
        System.out.println("GCM Notification is sent successfully");

    }


    @Override
    protected Bitmap doInBackground(String... params) {
        sendRequest(params);
        return null;
    }
}
