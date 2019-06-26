package smarttrashcan.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.UUID;

public class ConexionBluetooth {
    private static BluetoothAdapter bluetoothAdapter;
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static BluetoothSocket createBluetoothSocket(BluetoothDevice device)
            throws Exception {
        try {
            if(Build.VERSION.SDK_INT >= 10){
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, BTMODULEUUID);
            }
            return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        } catch (Exception e) {
            throw new Exception("Error al crear el socket bluetooth " + e.getMessage());
        }
    }

    private static void connectSocket(BluetoothDevice device, BluetoothSocket btSocket)
        throws Exception {
        try {
            try {
                btSocket.connect();
            } catch (Exception e) {
                btSocket =(BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
                btSocket.connect();
            }
        } catch (Exception e) {
            throw new Exception("Error al tratar de establecer conexión con el socket bluetooth " + e.getMessage());
        }
    }

    private static void testConnection(ConnectedThread thread)
        throws Exception {
        try {
            thread.write("x");
        } catch (Exception e) {
            throw new Exception("Ocurrió un error enviando una cadena de prueba al bluetooth");
        }
    }

    public static BluetoothAdapter getBluetoothAdapter() {
        if (bluetoothAdapter == null) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        return bluetoothAdapter;
    }

    public static ConnectedThread getConnectedThreadToBluetoothDevice(String address, Handler handler)
        throws Exception {
        try {
            BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice device = btAdapter.getRemoteDevice(address);
            Log.d("ConexionBluetooth", "MAC a la que intento conectar " + address);
            BluetoothSocket btSocket;
            ConnectedThread mConnectedThread;

            // se realiza la conexion del Bluethoot crea y se conecta a a traves de un socket
            btSocket = createBluetoothSocket(device);

            // se establece la conexión al socket
            connectSocket(device, btSocket);



            // Una establecida la conexion con el Hc05 se crea el hilo secundario, el cual va a recibir
            // los datos de Arduino atraves del bluethoot
            mConnectedThread = new ConnectedThread(btSocket, handler);
            mConnectedThread.start();

            //I send a character when resuming.beginning transmission to check device is connected
            //If it is not an exception will be thrown in the write method and finish() will be called
            testConnection(mConnectedThread);
            return mConnectedThread;
        }
        catch (Exception e) {
            throw new Exception("¡Ops! Ocurrió un error al intentar establecer conexión con el bluetooth (" + e.getMessage() + ")");
        }
    }

}
