package com.example.amyge.watertracker;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private static int waterCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeWaterCount();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("waterCount", String.valueOf(waterCount)).apply();
        prefsEditor.putString("lastUpdated", getCurrentDateString()).apply();
    }

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private void initializeWaterCount() {
        prefs = getSharedPreferences("waterTracker", 0);
        String countString = prefs.getString("waterCount", "0");
        String lastUpdatedString = prefs.getString("lastUpdated", "1970-01-01");

        if (!getCurrentDateString().equals(lastUpdatedString)) {
            countString = "0";
        }

        waterCount = Integer.valueOf(countString);
        updateWaterCountDisplay();
    }

    private String getCurrentDateString(){
        Date today = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(today);
    }

    public void addToWaterCount(View view){
        modifyWaterCount(1);
    }

    public void removeFromWaterCount(View view){
        if (waterCount > 0) {
            modifyWaterCount(-1);
        }
    }

    private void modifyWaterCount(int amount){
        waterCount += amount;

        updateWaterCountDisplay();

        if (waterCount >= 8){
            displaySuccessMessage();
        } else if (waterCount == 7){
            hideSuccessMessage();
        }
    }

    private void updateWaterCountDisplay() {
        TextView textView = (TextView) findViewById(R.id.waterDisplay);
        textView.setText(String.valueOf(waterCount));
    }

    private void displaySuccessMessage(){
        TextView textView = (TextView) findViewById(R.id.successDisplay);
        textView.setText(R.string.successMessage);
    }

    private void hideSuccessMessage() {
        TextView textView = (TextView) findViewById(R.id.successDisplay);
        textView.setText("");
    }
}
