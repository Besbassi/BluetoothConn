package meryem.testbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.sql.SQLOutput;

/**
 * Created by meryem on 09/04/16.
 */
public class BluetoothConnectionServer extends Thread {
    private final BluetoothServerSocket mmServerSocket;
    BluetoothManageConnection bluetoothManageConnection;
    BluetoothAdapter bluetoothAdapter;
    private String KillFly;


    public  BluetoothConnectionServer() {
        // Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final
        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code
            System.out.println("Server Listening ....");
            tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(KillFly, BluetoothConnectionClient.mon_uuid);
            System.out.println("Server Listening : okaaaaay");
        } catch (IOException e) {
            System.out.println("Server Listening failed");
        }
        mmServerSocket = tmp;
    }

    public void run() {
        System.out.println("Server Run");
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned
        while (true) {
            try {
                System.out.println("je vais accepter socket");
                socket = mmServerSocket.accept();
                System.out.println(" socket acceptée");
            } catch (IOException e) {
                System.out.println("Server socket accept failed");
                break;
            }
            // If a connection was accepted
            if (socket != null) {

                // Do work to manage the connection (in a separate thread)
               // manageConnectedSocket(socket);
                connected(socket, socket.getRemoteDevice(),
                        mSocketType);
              ;

                try {
                    mmServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    /** Will cancel the listening socket, and cause the thread to finish */
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) { }
    }

}
