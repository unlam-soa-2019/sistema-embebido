package smarttrashcan.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.os.Handler;

public class ConexionBluetooth {
    private static BluetoothAdapter bluetoothAdapter;

    public static BluetoothAdapter getBluetoothAdapter() {
        if (bluetoothAdapter == null) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        return bluetoothAdapter;
    }

    public static ConnectedThread getConnectedThreadToBluetoothDevice(String address, Handler handler, Handler handlerError)
        throws Exception {
        try {
            // Se establece la conexión con el módulo bluetooth según la dirección
            ConnectedThread mConnectedThread = new ConnectedThread(address, handler, handlerError);
            mConnectedThread.start();
            return mConnectedThread;
        }
        catch (Exception e) {
            throw new Exception("¡Ops! Ocurrió un error al intentar establecer conexión con el bluetooth (" + e.getMessage() + ")");
        }
    }

}
