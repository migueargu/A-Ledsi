package com.example.a_ledsi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;

public class Menu extends AppCompatActivity {

    Button btnConecta, btnExit;
    Switch swtPanel;
    FirebaseAuth mAuth;
    BluetoothConnectionManager bluetoothManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mAuth=FirebaseAuth.getInstance();

        btnConecta = (Button)findViewById(R.id.btnConectar);
        swtPanel = (Switch) findViewById(R.id.switchPanel);
        btnExit=(Button)findViewById(R.id.btnSalir);

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

        btnExit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mAuth.signOut();
                finish();
                startActivity(new Intent(Menu.this, login.class));
            }
        });
    }





}