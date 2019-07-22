package smarttrashcan;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

public class accion_CerrarBolsa extends BluetoothActivity implements SensorEventListener {
    private SensorManager sensor;
    private boolean signalWasSend = false;

    private void registerSensor()
    {
        boolean done;
        done = sensor.registerListener(this, sensor.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);

        if (!done)
        {
            Toast.makeText(this, "Sensor de proximidad no soportado", Toast.LENGTH_SHORT).show();
        }

        Log.i("sensor", "register");
    }

    private void unregisterSensor() {
        sensor.unregisterListener(this);
        Log.i("sensor", "unregister");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accion__cerrar_bolsa);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensor = (SensorManager) getSystemService(SENSOR_SERVICE);
        registerSensor();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();

        float[] values = event.values;
        Log.i("sensor", String.valueOf(values[0]));

        if (sensorType == Sensor.TYPE_PROXIMITY)
        {
            if (Math.abs(values[0]) == 0)
            {
                Log.i("sensor", "running");
                try {
                    if (!signalWasSend) {
                        mConnectedThread.addMessageToQueue("b");
                        ((Switch)findViewById(R.id.switch_cerre_bolsa)).setChecked(true);
                        signalWasSend = true;
                    }
                } catch (Exception e){
                    Toast.makeText(getBaseContext(), "No se pudo enviar la señal", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onStop()
    {
        unregisterSensor();
        super.onStop();
    }
}

