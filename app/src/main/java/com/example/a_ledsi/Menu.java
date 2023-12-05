package com.example.a_ledsi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

public class Menu extends AppCompatActivity {

    Button btnConecta, btnSubir;
    Switch swtPanel;
    BluetoothConnectionManager bluetoothManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnConecta = (Button) findViewById(R.id.btnConectar);
        swtPanel = (Switch) findViewById(R.id.switchPanel);
        btnSubir = (Button) findViewById(R.id.btnSubir);

        btnConecta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, Conecta.class);
                startActivity(intent);
            }
        });

        btnSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, Imagen.class);
                startActivity(intent);
            }
        });


        swtPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothManager = BluetoothConnectionHolder.getBluetoothManager();

                if (bluetoothManager != null) {
                    String message = swtPanel.isChecked() ? "ON" : "OFF";
                    bluetoothManager.sendMessage(message);
                    Toast.makeText(Menu.this, "Sonido " + message
                            , Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Menu", "Menu dice: STATE_CONNECTION_FAILED");
                    Toast.makeText(Menu.this, "La conexión Bluetooth fallo",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
