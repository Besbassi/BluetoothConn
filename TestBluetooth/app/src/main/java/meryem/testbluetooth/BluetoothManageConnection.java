package meryem.testbluetooth;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by meryem on 09/04/16.
 */
public class BluetoothManageConnection  extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    protected static final String TAG = "TAG";
    public static final int MESSAGE_READ = 222390;
    public static final int MESSAGE_WRITE = 232290;

    public BluetoothManageConnection(BluetoothSocket socket) {
        Log.d(TAG, "create ConnectedThread ... ");
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            System.out.println("yes input");
            tmpOut = socket.getOutputStream();
            System.out.println("yes output");
        } catch (IOException e) {
            System.out.println("no input nor output");
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        Log.i(TAG, "BEGIN mConnectedThread");
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream
                bytes = mmInStream.read(buffer);
                System.out.println("read buffer");
                // Send the obtained bytes to the UI activity
                BluetoothDemo.handleSeacrh.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                System.out.println("send ui");

            } catch (IOException e) {
                System.out.println("no buffer nor ui");
                break;
            }
        }
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] buffer) {

        try {
            mmOutStream.write(buffer);
            BluetoothDemo.handleSeacrh.obtainMessage(MESSAGE_WRITE, -1, -1, buffer).sendToTarget();

            System.out.println("writing");
        } catch (IOException e) {
            Log.e(TAG, "Exception during write", e);}
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "close() of connect socket failed", e);
        }
    }

}
