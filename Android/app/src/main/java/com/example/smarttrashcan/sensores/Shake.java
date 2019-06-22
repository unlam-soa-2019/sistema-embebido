package com.example.smarttrashcan.sensores;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
/*
public class Shake implements SensorEventListener, OnCheckedChangeListener {

    public void onCreate()
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensor = (SensorManager) getSystemService(SENSOR_SERVICE);

        switchButton = (CheckBox) findViewById(R.id.checkBox1);
        switchButton.setOnCheckedChangeListener(this);

        mplayer = MediaPlayer.create(this, R.raw.lightsaber);

        mplayer.setOnPreparedListener (
                new OnPreparedListener()
                {
                    public void onPrepared(MediaPlayer arg0)
                    {
                        Log.e("ready!","ready!");
                        mplayer.setVolume(1.0f, 1.0f);
                    }
                }
        );
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
*/