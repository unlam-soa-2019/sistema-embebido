package smarttrashcan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import smarttrashcan.bluetooth.ConexionBluetooth;
import smarttrashcan.bluetooth.ConnectedThread;

public class BluetoothActivity extends AppCompatActivity {
    protected ConnectedThread mConnectedThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected Handler GetBluetoothHandler() {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String address = intent.getExtras().getString(DispositivosBluetooth.extra_device_address);

        try {
            mConnectedThread = ConexionBluetooth.getConnectedThreadToBluetoothDevice(address, GetBluetoothHandler());
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Ocurrió un error al intentar establecer conexión con el módulo bluetooth", Toast.LENGTH_SHORT).show();
            Log.e("connectedthread", e.getMessage());
        }
    }


}
