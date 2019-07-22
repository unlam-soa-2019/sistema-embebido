package smarttrashcan.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;


public class ConnectedThread extends Thread {

    private InputStream mmInStream;
    private OutputStream mmOutStream;
    private BluetoothSocket btSocket = null;
    private boolean hasTriedToInitialize = false;
    private String deviceAddress = null;
    private Queue<String> messagesQueue = new LinkedList<String>();

    private final Handler handlerBluetooth;
    private final Handler handlerError;

    public static final int btMessageReceived = 0; // used to identify handler message
    public static final int btErrorHandler = 1;

    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device)
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

    private void connectSocket(BluetoothDevice device, BluetoothSocket btSocket)
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

    private void testConnection() throws Exception {
        try {
            addMessageToQueue("x");
        } catch (Exception e) {
            throw new Exception("Ocurrió un error enviando una cadena de prueba al bluetooth");
        }
    }

    private void establishConnectionToDevice(String address) {
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = btAdapter.getRemoteDevice(address);
        Log.d("ConexionBluetooth", "MAC a la que intento conectar " + address);

        // se realiza la conexion del Bluethoot crea y se conecta a a traves de un socket
        try {
            btSocket = createBluetoothSocket(device);
            if (btSocket != null) {
                // se establece la conexión al socket
                connectSocket(device, btSocket);
                testConnection(); // envio un char de prueba
            }
        } catch (Exception e) {
            Log.e("ConnectedThread", "Error al establecer conexión (" + e.getMessage() + ")");
        }

        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        try
        {
            // Create I/O streams for connection
            tmpIn = btSocket.getInputStream();
            tmpOut = btSocket.getOutputStream();
        } catch (IOException e) { }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
        hasTriedToInitialize = true;
    }

    // write method
    private void write(String input) throws IOException {
        byte[] msgBuffer = input.getBytes(); //converts entered String into bytes
        mmOutStream.write(msgBuffer); //write bytes over BT connection via outstream
    }

    public ConnectedThread(String address, Handler bluetoothIn, Handler handlerError)
    {
        deviceAddress = address;
        handlerBluetooth = bluetoothIn;
        this.handlerError = handlerError;
    }

    // metodo run del hilo, que va a entrar en una espera activa para recibir los msjs del HC05
    public void run()
    {
        if (hasTriedToInitialize == false) {
            establishConnectionToDevice(deviceAddress);
        }

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

        // lectura
        if (handlerBluetooth != null && btSocket.isConnected())
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

    public void close() {
        if (btSocket != null && btSocket.isConnected()) {
            try {
                btSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // addMessageToQueue method
    // si lanzo excepción lo trato desde donde lo esté llamando
    public void addMessageToQueue(String input) throws IOException, InterruptedException {
        messagesQueue.add(input);
    }

}
