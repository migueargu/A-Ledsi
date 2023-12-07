package com.example.a_ledsi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Imagen extends AppCompatActivity {

    ListView listImg;
    BluetoothConnectionManager bluetoothManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen);

        String[] items = {
                "Pacman",
                "Img2",
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, items);

        listImg = findViewById(R.id.listviewImg);
        listImg.setAdapter(adapter);

        listImg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bluetoothManager = BluetoothConnectionHolder.getBluetoothManager();

                if (bluetoothManager != null) {
                    String clickedText = items[position];
                    bluetoothManager.sendMessage(clickedText);
                    Toast.makeText(Imagen.this, "Sonido " + clickedText
                            , Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Menu", "Menu dice: STATE_CONNECTION_FAILED");
                    Toast.makeText(Imagen.this, "La conexión Bluetooth fallo",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
    }



}