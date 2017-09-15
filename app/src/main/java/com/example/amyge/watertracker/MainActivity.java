package com.example.amyge.watertracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private static WaterCount waterCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeWaterCount();
        Calendar calendar = getMidnightCalendar();
        Context context = getApplicationContext();

        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                new Intent(context, WaterCount.class),PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("waterCount", String.valueOf(waterCount)).apply();
    }

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private void initializeWaterCount() {
        prefs = getSharedPreferences("waterTracker", 0);
        String countString = prefs.getString("waterCount", "0");

        if (countString.equals("")) {
            countString = "0";
        }

        waterCount = new WaterCount(Integer.valueOf(countString));
        updateWaterCountDisplay();
    }
    private Calendar getMidnightCalendar() {

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    public void addToWaterCount(View view){
        modifyWaterCount(1);
    }

    public void removeFromWaterCount(View view){
        modifyWaterCount(-1);
    }

    private void modifyWaterCount(int amount){
        waterCount.setValue(amount);
        updateWaterCountDisplay();

        if (waterCount.value() >= 8){
            displaySuccessMessage();
        } else if (waterCount.value() == 7){
            hideSuccessMessage();
        }
    }

    private void updateWaterCountDisplay() {
        TextView textView = (TextView) findViewById(R.id.waterDisplay);
        textView.setText(String.valueOf(waterCount.value()));
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
