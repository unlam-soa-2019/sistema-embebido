package smarttrashcan.bluetooth;

import android.os.Handler;

import java.io.IOException;

public class WriteBluetoothThread extends ConnectedThread {

    public WriteBluetoothThread(String address, Handler bluetoothIn, Handler handlerError) {
        super(address, bluetoothIn, handlerError);
    }

    @Override
    public void run() {
        super.run();

        // escritura
        while (!messagesQueue.isEmpty() && btSocket.isConnected()) {
            try {
                write(messagesQueue.remove());
            } catch (IOException e) {
                if (handlerError != null) {
                    handlerError.obtainMessage(btErrorHandler, e.getMessage()).sendToTarget();
                }
            }
        }

    }
}
