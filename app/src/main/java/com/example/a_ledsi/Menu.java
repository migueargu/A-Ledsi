package com.example.a_ledsi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class Menu extends AppCompatActivity {

    Button btnConecta;
    Switch swtPanel;
    BluetoothConnectionManager bluetoothManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnConecta = (Button)findViewById(R.id.btnConectar);
        swtPanel = (Switch) findViewById(R.id.switchPanel);

        btnConecta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, Conecta.class);
                startActivity(intent);
            }
        });

        swtPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = swtPanel.isChecked() ? "ON" : "OFF";
                bluetoothManager.sendMessage(message);
                //msg_box.setText("Sensor " + (send.isChecked() ? "encendido" : "apagado"));
            }
        });
    }
}