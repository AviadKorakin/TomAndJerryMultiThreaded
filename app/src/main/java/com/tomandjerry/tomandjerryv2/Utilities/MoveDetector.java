package com.tomandjerry.tomandjerryv2.Utilities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.tomandjerry.tomandjerryv2.Interfaces.MoveCallback;


public class MoveDetector {

    private final SensorManager sensorManager;
    private final Sensor sensor;
    private SensorEventListener sensorEventListener;
    private boolean clickLeft=false;
    private boolean clickRight=true;
    private int cycleLeft=0;
    private int cycleRight=0;




    private long timestamp = 0L;

    private final MoveCallback moveCallback;

    public MoveDetector(Context context,MoveCallback moveCallback) {
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.moveCallback = moveCallback;
        initEventListener();
    }
    private void initEventListener() {
        this.sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                calculateMove(x,y);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // pass
            }
        };
    }

    private void calculateMove(float x, float y) {
        long time=System.currentTimeMillis() - timestamp ;
        if (time > 80) {
            timestamp = System.currentTimeMillis();
            cycleLeft++;
            cycleRight++;
            if (x > 0.8) {
                if (moveCallback != null) {
                    if (!clickLeft)
                    {
                        moveCallback.moveLeft();
                        clickLeft = true;
                        clickRight = false;
                        cycleLeft=1;
                    }
                }
            } else if (x < -0.8 ) {
                if (moveCallback != null) {
                    if (!clickRight) {
                        moveCallback.moveRight();
                        clickRight = true;
                        clickLeft = false;
                        cycleRight=1;
                    }
                }

            }
            if(clickLeft && cycleLeft>2)clickLeft=false;
            if(clickRight && cycleRight>2)clickRight=false;

            if (y < 5) {
                if (moveCallback != null) {
                    moveCallback.moveUp();
                }
            } else  {
                if (moveCallback != null) {
                    moveCallback.moveDown();
                }

            }
        }
    }


    public void start(){
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    public void stop(){
        sensorManager.unregisterListener(
                sensorEventListener,
                sensor
        );
    }
}
