package smarttrashcan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class accion_ConfigurarPesoMaximo extends BluetoothActivity {
    private Button btnEnviar;
    private RadioGroup radioGroup;
    private RadioButton radioBtnUno;
    private RadioButton radioBtnDos;
    private RadioButton radioBtnTres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accion__configurar_peso_maximo);

        btnEnviar = findViewById(R.id.btn_enviar_peso_embebido);
        radioGroup = findViewById(R.id.radioGroup);

        radioBtnUno = findViewById(R.id.cien_gramos);
        radioBtnDos = findViewById(R.id.quinientos_gramos);
        radioBtnTres = findViewById(R.id.un_kilo);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idSelected = radioGroup.getCheckedRadioButtonId();
                try {
                    if (idSelected == R.id.cien_gramos) {
                        mConnectedThread.write("1");
                    } else if (idSelected == R.id.quinientos_gramos) {
                        mConnectedThread.write("2");
                    } else {
                        mConnectedThread.write("3");
                    }

                    btnEnviar.setEnabled(false);
                    radioGroup.setEnabled(false);
                    Toast.makeText(getBaseContext(), "Se envió la señal", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "No se pudo enviar, reintente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mConnectedThread.close();
    }

}
