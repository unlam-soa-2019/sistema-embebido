package smarttrashcan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import smarttrashcan.bluetooth.ConexionBluetooth;
import smarttrashcan.bluetooth.ConnectedThread;
import smarttrashcan.bluetooth.TypeBluetoothThread;

public class BluetoothActivity extends AppCompatActivity {
    protected ConnectedThread mConnectedThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected Handler GetBluetoothHandler() {
        return null;
    }

    protected Handler GetConnectivityError() {
        return new Handler() {
            public void handleMessage(android.os.Message msg)
            {
                if (msg.what == ConnectedThread.btErrorHandler) {
                    Toast.makeText(getBaseContext(), "Ocurrió un error al intentar establecer conexión con el módulo bluetooth", Toast.LENGTH_SHORT).show();
                    Log.e("connectedthread", msg.obj.toString());
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String address = intent.getExtras().getString(DispositivosBluetooth.extra_device_address);

        try {
            mConnectedThread = ConexionBluetooth.getConnectedThreadToBluetoothDevice(address, GetBluetoothHandler(), GetConnectivityError(), GetTypeOfBluetoothOperation());
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Ocurrió un error al intentar establecer conexión con el módulo bluetooth", Toast.LENGTH_SHORT).show();
            Log.e("connectedthread", e.getMessage());
        }
    }

    protected TypeBluetoothThread GetTypeOfBluetoothOperation() {
        return null;
    }

    @Override
    protected void onStop() {
        mConnectedThread.close();
        super.onStop();
    }

}
