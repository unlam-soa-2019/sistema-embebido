package smarttrashcan;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import smarttrashcan.bluetooth.ConnectedThread;
import smarttrashcan.bluetooth.TypeBluetoothThread;

public class accion_ObtenePesoActual extends BluetoothActivity {
    private Handler bluetoothIn;
    private TextView txtPeso;
    private StringBuilder recDataString = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accion__obtene_peso_actual);
        bluetoothIn = Handler_Msg_Hilo_Principal();
        txtPeso = findViewById(R.id.textPesoActual);
    }

    @Override
    public void onResume() {
        super.onResume();
        mConnectedThread.readSignalToSend = "p";
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private Handler Handler_Msg_Hilo_Principal () {
        return new Handler() {
            public void handleMessage(android.os.Message msg) {
                //si se recibio un msj del hilo secundario
                if (msg.what == ConnectedThread.btMessageReceived) {
                    //voy concatenando el msj
                    String readMessage = (String) msg.obj;
                    recDataString.append(readMessage);
                    int endOfLineIndex = recDataString.indexOf("%");

                    //cuando recibo toda una linea la muestro en el layout
                    if (endOfLineIndex > 0) {
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);
                        Float value = Float.valueOf(dataInPrint);
                        if (value < 0) {
                            txtPeso.setText("0 gramos");
                        } else {
                            txtPeso.setText(dataInPrint + " gramos");
                        }
                        recDataString.delete(0, recDataString.length());
                    }
                }
            }
        };
    }

    protected TypeBluetoothThread GetTypeOfBluetoothOperation() {
        return TypeBluetoothThread.Read;
    }

    protected Handler GetBluetoothHandler() {
        return bluetoothIn;
    }

}
