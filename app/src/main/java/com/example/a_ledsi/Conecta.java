package com.example.a_ledsi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class Conecta extends AppCompatActivity {
    private static final int REQUEST_BLUETOOTH_CONNECT = 123;
    Button listen, listDevices;
    Switch send;
    ListView listView;
    TextView msg_box, status;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice[] btArray;
    int REQUEST_ENABLE_BLUETOOTH = 1;
    BluetoothConnectionManager bluetoothManager;
    private boolean isServiceBound = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BluetoothConnectionManager.LocalBinder binder = (BluetoothConnectionManager.LocalBinder) service;
            bluetoothManager = binder.getService();
            isServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceBound = false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conecta);

        findViewByIdes();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothManager = new BluetoothConnectionManager();

        Intent serviceIntent = new Intent(this, BluetoothConnectionManager.class);
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        if (bluetoothAdapter == null) {
        } else if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
        } else {
            implementListeners();
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.BLUETOOTH_CONNECT},
                    REQUEST_BLUETOOTH_CONNECT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isServiceBound) {
            unbindService(serviceConnection);
            isServiceBound = false;
        }
    }

    // Método para conectarse al dispositivo desde cualquier parte de tu actividad
    private void connectToDevice(BluetoothDevice device) {
        if (isServiceBound) {
            bluetoothManager.connectToDevice(device, new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // Manejar mensajes según sea necesario
                    switch (msg.what) {
                        case BluetoothConnectionManager.STATE_CONNECTED:
                            // El dispositivo se ha conectado exitosamente
                            showToast("Connected to the device");
                            break;
                        case BluetoothConnectionManager.STATE_CONNECTION_FAILED:
                            // La conexión ha fallado
                            showToast("Connection failed");
                            break;
                        // Otros casos según tus necesidades
                    }
                }
            });
        }
    }

    // Método para mostrar un Toast (mensaje emergente)
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Método para enviar mensajes desde cualquier parte de tu actividad
    private void sendMessage(String message) {
        if (isServiceBound) {
            bluetoothManager.sendMessage(message);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_BLUETOOTH_CONNECT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                implementListeners();
            } else {
                // Permiso denegado
                Toast.makeText(this, "Permiso de Bluetooth no otorgado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                // Bluetooth fue activado con éxito
                implementListeners();
            } else {
                Toast.makeText(this, "Permiso no otorgado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void implementListeners() {

        listDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<BluetoothDevice> bt=bluetoothAdapter.getBondedDevices();
                String[] strings=new String[bt.size()];
                btArray=new BluetoothDevice[bt.size()];
                int index=0;

                if( bt.size()>0)
                {
                    for(BluetoothDevice device : bt)
                    {
                        btArray[index]= device;
                        strings[index]=device.getName();
                        index++;
                    }
                    ArrayAdapter<String> arrayAdapter=new
                            ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,strings);
                    listView.setAdapter(arrayAdapter);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //ClientClass clientClass=new ClientClass(btArray[i]);
                //clientClass.start();
                bluetoothManager.connectToDevice(btArray[i], handler);

                status.setText("Conectando...");
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = send.isChecked() ? "ON" : "OFF";
                bluetoothManager.sendMessage(message);
                msg_box.setText("Sensor " + (send.isChecked() ? "encendido" : "apagado"));
            }
        });
    }

    private void findViewByIdes() {
        send = (Switch) findViewById(R.id.switchButtonSensor);
        listView = (ListView) findViewById(R.id.listview);
        status = (TextView) findViewById(R.id.status);
        listDevices = (Button) findViewById(R.id.listDevices);
        msg_box = (TextView) findViewById(R.id.msg);
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothConnectionManager.STATE_LISTENING:
                    status.setText("Listening");
                    break;
                case BluetoothConnectionManager.STATE_CONNECTING:
                    status.setText("Connecting");
                    break;
                case BluetoothConnectionManager.STATE_CONNECTED:
                    status.setText("Connected");
                    break;
                case BluetoothConnectionManager.STATE_CONNECTION_FAILED:
                    status.setText("Connection Failed");
                    break;
                case BluetoothConnectionManager.STATE_MESSAGE_RECEIVED:
                    byte[] readBuff = (byte[]) msg.obj;
                    String tempMsg = new String(readBuff, 0, msg.arg1);
                    msg_box.setText(tempMsg);
                    break;
            }
            return true;
        }
    });
}
