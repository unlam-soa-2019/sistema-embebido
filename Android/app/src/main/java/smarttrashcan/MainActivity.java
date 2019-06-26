package smarttrashcan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnConectarBluetooth = findViewById(R.id.btnConectar);
        btnConectarBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent verDispositivos = new Intent(getApplicationContext(), DispositivosBluetooth.class);
                startActivity(verDispositivos);
            }
        });
    }
}
