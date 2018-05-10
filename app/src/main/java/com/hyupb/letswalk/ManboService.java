package com.hyupb.letswalk;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by alfo6-18 on 2018-04-27.
 */

public class ManboService extends Service implements SensorEventListener {

    int count = StepCount.Step;
    private long lastTime;
    private float speed;
    private float lastX;
    private float lastY;
    private float lastZ;

    private float x, y, z;
    private static final int SHAKE_THRESHOLD = 800;

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;

    private Calendar selectionCa;
    private Calendar todayCa;
    private boolean save = true;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("onCreate", "IN");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        selectionCa = Calendar.getInstance();
        selectionCa.set(Calendar.HOUR_OF_DAY,23);
    } // end of onCreate

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i("onStartCommand", "IN");
        if (accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        } // end of if

        return START_STICKY;
    } // end of onStartCommand

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("onDestroy", "IN");
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            StepCount.Step = 0;
        } // end of if
    } // end of onDestroy

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.i("onSensorChanged", "IN");
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime);

            if (gabOfTime > 100) { //  gap of time of step count
                Log.i("onSensorChanged_IF", "FIRST_IF_IN");
                lastTime = currentTime;

                x = event.values[0];
                y = event.values[1];
                z = event.values[2];

                speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000;

                if(!StepCount.flag){
                    if (speed > SHAKE_THRESHOLD) {
                        Log.i("onSensorChanged_IF", "SECOND_IF_IN");
                        Intent myFilteredResponse = new Intent("com.hyupb.letswalk");


                        StepCount.Step = count++;

                        String msg = StepCount.Step / 2 + "";
                        myFilteredResponse.putExtra("stepService", msg);

                        sendBroadcast(myFilteredResponse);
                    }

                } // end of if
                lastX = event.values[0];
                lastY = event.values[1];
                lastZ = event.values[2];

                //24시 될때마다 저장
                todayCa = Calendar.getInstance();


                if(save) {
                    if (selectionCa.get(Calendar.HOUR_OF_DAY) == todayCa.get(Calendar.HOUR_OF_DAY)) {

                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Data",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putInt(todayCa.get(Calendar.MONTH)+"month",sharedPreferences.getInt(todayCa.get(Calendar.MONTH)+"month",0)+StepCount.todayStep);
                        editor.putInt((todayCa.get(Calendar.DAY_OF_WEEK)-1)+"day",StepCount.todayStep);
                        editor.commit();

                        for(int i=0;i<7;i++){
                            if(todayCa.get(Calendar.DAY_OF_WEEK) == (i+1)){
                                StepCount.days[i] = StepCount.todayStep;

                                StepCount.Step=0;
                                break;
                            }
                        }

                        save = false;
                    }
                }else {
                    if(selectionCa.get(Calendar.HOUR_OF_DAY) != todayCa.get(Calendar.HOUR_OF_DAY)){
                        save = true;
                    }
                }

            } // end of if
        } // end of if

    } // end of onSensorChanged

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
