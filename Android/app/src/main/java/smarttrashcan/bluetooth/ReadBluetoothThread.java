package smarttrashcan.bluetooth;

import android.os.Handler;

import java.io.IOException;

public class ReadBluetoothThread extends ConnectedThread {
    private boolean signalWasSend = false;

    public ReadBluetoothThread(String address, Handler bluetoothIn, Handler handlerError) {
        super(address, bluetoothIn, handlerError);
    }

    @Override
    public void run() {
        super.run();

        // envio de señal
        if (readSignalToSend != "" && btSocket.isConnected() && !signalWasSend) {
            try {
                write(messagesQueue.remove());
                signalWasSend = true;
            } catch (IOException e) {
                if (handlerError != null) {
                    handlerError.obtainMessage(btErrorHandler, e.getMessage()).sendToTarget();
                }
            }
        }

        // lectura
        if (handlerBluetooth != null && signalWasSend && btSocket.isConnected())
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
                    handlerBluetooth.obtainMessage(btMessageReceived, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    // Log.e("handlerBluetooth", e.getMessage());
                }
            }
        }
    }
}
