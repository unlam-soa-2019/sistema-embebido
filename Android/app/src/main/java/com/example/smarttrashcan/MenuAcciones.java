package com.example.smarttrashcan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuAcciones extends AppCompatActivity {

    private static String address = null;

    private void SetOnClickListener(Button btn, final Class activityClass) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), activityClass);
                intent.putExtra(DispositivosBluetooth.extra_device_address, address);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_acciones);

        Intent intent = getIntent();
        address = intent.getStringExtra(DispositivosBluetooth.extra_device_address);

        Button btnObtenerPesoActual = findViewById(R.id.btnPesoActual);
        SetOnClickListener(btnObtenerPesoActual, accion_ObtenePesoActual.class);

        Button btnConfigurarPesoMaximo = findViewById(R.id.btnPesoMaximo);
        SetOnClickListener(btnConfigurarPesoMaximo, accion_ConfigurarPesoMaximo.class);

        Button btnModoJuego = findViewById(R.id.btnModoJuego);
        SetOnClickListener(btnModoJuego, accion_ModoJuego.class);

        Button btnCerrarBolsa = findViewById(R.id.btnCerrarBolsa);
        SetOnClickListener(btnCerrarBolsa, accion_CerrarBolsa.class);
    }
}
