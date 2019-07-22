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

    public static ConnectedThread getConnectedThreadToBluetoothDevice(String address, Handler handler, Handler handlerError, TypeBluetoothThread type)
        throws Exception {
        try {
            // Se establece la conexión con el módulo bluetooth según la dirección
            ConnectedThread mConnectedThread;
            switch (type) {
                case Read:
                    mConnectedThread = new ReadBluetoothThread(address, handler, handlerError);
                    break;
                case Write:
                    mConnectedThread = new WriteBluetoothThread(address, handler, handlerError);
                    break;
                    default:
                        throw new Exception("Tipo no definido");
            }
            mConnectedThread.start();
            return mConnectedThread;
        }
        catch (Exception e) {
            throw new Exception("¡Ops! Ocurrió un error al intentar establecer conexión con el bluetooth (" + e.getMessage() + ")");
        }
    }

}
