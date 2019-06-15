package com.example.smarttrashcan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class DispositivosBluetooth extends AppCompatActivity {

    private static final String tag = "DispositivosBluetooth";
    public static final String extra_device_address = "device_address";
    private BluetoothAdapter bluetoothAdapter;
    private ArrayAdapter dispositivosVinculados;

    private BluetoothAdapter getBluetoothAdapter() {
        if (bluetoothAdapter == null) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        return bluetoothAdapter;
    }

    private void conectarBluetooth() {
        // chequea el bluetooth del dispositivo e intenta habilitarlo
        bluetoothAdapter = getBluetoothAdapter();
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

    private String getMACAddressFromView(View view) {
        // Obtiene la dirección MAC del dispositivo, que son los últimos 17 caracteres en la vista
        String viewText = ((TextView) view).getText().toString();
        return viewText.substring(viewText.length() - 17);
    }

    private AdapterView.OnItemClickListener onClickDevice = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView av, View v, int arg2, long arg3) {
            String address = getMACAddressFromView(((ConstraintLayout) v).getChildAt(0));

            // Realiza un intent para ir al menu de acciones, envía la MAC
            Intent intent = new Intent(getApplicationContext(), MenuAcciones.class);
            intent.putExtra(extra_device_address, address);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispositivos_bluetooth);
    }

    @Override
    public void onResume() {
        super.onResume();

        conectarBluetooth();

        dispositivosVinculados = new ArrayAdapter(this, R.layout.single_dispositivo, R.id.textView3);
        // Presenta los dispositivos vinculados en el ListView
        ListView listaDispositivos = findViewById(R.id.listaDispositivos);
        listaDispositivos.setAdapter(dispositivosVinculados);
        listaDispositivos.setOnItemClickListener(onClickDevice);

        // Agrega a la lista los dispositivos previamente emparejados
        Set<BluetoothDevice> pairedDevices = getBluetoothAdapter().getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            dispositivosVinculados.add(device.getName() + "\n" + device.getAddress());
        }
    }
}
