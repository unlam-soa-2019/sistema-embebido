package smarttrashcan;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import java.lang.reflect.Type;

import smarttrashcan.bluetooth.TypeBluetoothThread;

public class accion_CambieBolsa extends BluetoothActivity implements SensorEventListener {
    private final static float ACC = 20;
    private SensorManager sensor;
    private boolean signalWasSend = false;

    private void registerSensor()
    {
        boolean done;
        done = sensor.registerListener(this, sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        if (!done)
        {
            Toast.makeText(this, "Acelerómetro no soportado", Toast.LENGTH_SHORT).show();
        }

        Log.i("sensor", "register");
    }

    private void unregisterSensor() {
        sensor.unregisterListener(this);
        Log.i("sensor", "unregister");
    }

    protected TypeBluetoothThread GetTypeOfBluetoothOperation() {
        return TypeBluetoothThread.Write;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accion__cambie_bolsa);
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
        Log.i("sensor", String.valueOf(values[1]));
        Log.i("sensor", String.valueOf(values[2]));

        if (sensorType == Sensor.TYPE_ACCELEROMETER)
        {
            if ((Math.abs(values[0]) > ACC || Math.abs(values[1]) > ACC || Math.abs(values[2]) > ACC))
            {
                Log.i("sensor", "running");
                try {
                    if (!signalWasSend) {
                        mConnectedThread.addMessageToQueue("c");
                        ((Switch)findViewById(R.id.switch_cambie_bolsa)).setChecked(true);
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
