package com.example.smarttrashcan;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class DispositivosBluetooth extends AppCompatActivity {

    ListView lista;
    private static final String tag = "DispositivosBluetooth";
    private BluetoothAdapter bluetoothAdapter;

    private void conectarBluetooth() {
        // chequea el bluetooth del dispositivo e intenta habilitarlo
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getBaseContext(), "¡Ops! Este dispositivo no cuenta con conexión bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (bluetoothAdapter.isEnabled()) {
                Log.d(tag, "El bluetooth ya está activado");
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispositivos_bluetooth);

        lista = findViewById(R.id.listaDispositivos);
    }

    @Override
    public void onResume() {
        super.onResume();

        conectarBluetooth();
    }
}
