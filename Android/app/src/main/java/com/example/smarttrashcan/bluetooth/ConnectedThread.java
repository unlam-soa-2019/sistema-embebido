package com.example.smarttrashcan.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ConnectedThread extends Thread {

    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private final Handler handlerBluetooth;
    public static final int handlerState = 0; // used to identify handler message

    public ConnectedThread(BluetoothSocket socket, Handler bluetoothIn)
    {
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try
        {
            // Create I/O streams for connection
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
        handlerBluetooth = bluetoothIn;
    }

    // metodo run del hilo, que va a entrar en una espera activa para recibir los msjs del HC05
    public void run()
    {
        if (handlerBluetooth != null)
        {
            byte[] buffer = new byte[256];
            int bytes;

            //el hilo secundario se queda esperando mensajes del HC05
            while (true)
            {
                try
                {
                    //se leen los datos del Bluethoot
                    bytes = mmInStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);

                    // se envía al handler el mensaje obtenido del HC05
                    handlerBluetooth.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
    }

    // write method
    // si lanzo excepción lo trato desde donde lo esté llamando
    public void write(String input) throws IOException {
        byte[] msgBuffer = input.getBytes(); //converts entered String into bytes
        mmOutStream.write(msgBuffer); //write bytes over BT connection via outstream
    }
}
