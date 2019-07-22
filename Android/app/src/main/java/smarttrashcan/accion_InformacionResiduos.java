package smarttrashcan;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import smarttrashcan.bluetooth.ConnectedThread;

public class accion_InformacionResiduos extends BluetoothActivity {
    private Handler bluetoothIn;
    private TextView txtEstadoResiduos;
    private StringBuilder recDataString = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accion__informacion_residuos);
        bluetoothIn = Handler_Msg_Hilo_Principal();
        txtEstadoResiduos = findViewById(R.id.textEstadoResiduos);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            mConnectedThread.addMessageToQueue("r");
        }
        catch (Exception e) {
            Toast.makeText(getBaseContext(), "No se pudo comunicar con SmartTrashCan", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    private Handler Handler_Msg_Hilo_Principal ()
    {
        return new Handler() {
            public void handleMessage(android.os.Message msg)
            {
                //si se recibio un msj del hilo secundario
                if (msg.what == ConnectedThread.btMessageReceived)
                {
                    //voy concatenando el msj
                    String readMessage = (String) msg.obj;
                    recDataString.append(readMessage);
                    int endOfLineIndex = recDataString.indexOf("%");

                    //cuando recibo toda una linea la muestro en el layout
                    if (endOfLineIndex > 0)
                    {
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);
                        txtEstadoResiduos.setText(dataInPrint);
                        recDataString.delete(0, recDataString.length());
                    }
                }
            }
        };

    }

    protected Handler GetBluetoothHandler() {
        return bluetoothIn;
    }

}
