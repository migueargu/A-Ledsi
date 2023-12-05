package com.example.a_ledsi;

public class BluetoothConnectionHolder {
    private static BluetoothConnectionManager bluetoothManager;

    public static BluetoothConnectionManager getBluetoothManager() {
        return bluetoothManager;
    }

    public static void setBluetoothManager(BluetoothConnectionManager manager) {
        bluetoothManager = manager;
    }
}
