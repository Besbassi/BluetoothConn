package meryem.testbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by meryem on 09/04/16.
 */
 public class BluetoothConnectionClient extends Thread {

    protected static final String TAG = "TAG";
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    BluetoothAdapter bluetoothAdapter;
    BluetoothManageConnection bluetoothManageConnection;
    BluetoothDemo bluetoothDemo;
    private UUID DEFAULT_UUID;
    static UUID mon_uuid;

    public BluetoothConnectionClient(BluetoothDevice device) {
        // Use a temporary object that is later assigned to mmSocket because mmSocket is final
        BluetoothSocket tmp = null;
        mmDevice = device;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // UUID is the app's UUID string, also used by the server code
    // Get a BluetoothSocket to connect with the given BluetoothDevice. This code below show how to do it and handle the case that the UUID from the device is not found and trying a default UUID.
    // Default UUID
      DEFAULT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    try {
        // Use the UUID of the device that discovered // TODO Maybe need extra device object
        if (mmDevice != null)
        {
            BluetoothConnectionClient blue = new BluetoothConnectionClient(device);

            System.out.println("In BluetoothConnectionClient");

            Log.i(TAG, "Device Name: " + mmDevice.getName());
            Log.i(TAG, "Device UUID: " + mmDevice.getUuids()[0].getUuid());
            mon_uuid=mmDevice.getUuids()[0].getUuid();
            tmp = device.createRfcommSocketToServiceRecord(mon_uuid);
            run();
            /*mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);*/

        }
        else Log.d(TAG, "Device is null.");
    }
    catch (NullPointerException e)
    {
        Log.d(TAG, " UUID from device is null, Using Default UUID, Device name: " + device.getName());
        try {
            tmp = device.createRfcommSocketToServiceRecord(DEFAULT_UUID);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

        } catch (IOException e) { }
        mmSocket = tmp;

    }

    public void run() {
        System.out.println("In Run");
        // Cancel discovery because it will slow down the connection
       //bluetoothAdapter.cancelDiscovery();

        try {
            System.out.println("Connecting...");
            // Connect the device through the socket. This will block until it succeeds or throws an exception
            mmSocket.connect();
            System.out.println("Connected");
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            try {
                mmSocket.close();
                System.out.println("Not Connected");
            } catch (IOException closeException) { }
            return;
        }
        // Do work to manage the connection (in a separate thread)
        System.out.println("je vais manager ma connection");
        bluetoothManageConnection =new BluetoothManageConnection(mmSocket);
        bluetoothManageConnection.run();
    }

    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}