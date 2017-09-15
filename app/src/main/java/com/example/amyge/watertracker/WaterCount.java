package com.example.amyge.watertracker;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by amyge on 9/10/2017.
 */

public class WaterCount extends Service{

    private int waterCount = 0;
    private final IBinder mBinder = new LocalBinder();

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    private class LocalBinder extends Binder {
        WaterCount getService() {
            return WaterCount.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public WaterCount(int origCount){
        waterCount = origCount;
    }

    public int value() {
        return waterCount;
    }

    public void setValue(int amount){
        waterCount += amount;

        if (waterCount < 0){
            waterCount = 0;
        }
    }

    private void resetWaterCount(){
        waterCount = 0;
    }
}
