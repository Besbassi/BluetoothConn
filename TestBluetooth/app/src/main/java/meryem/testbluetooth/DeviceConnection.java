package meryem.testbluetooth;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class DeviceConnection extends Activity implements Runnable
{
    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    Button mScan;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;

    @Override
    public void onCreate(Bundle mSavedInstanceState)
    {
        super.onCreate(mSavedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        mScan = (Button) findViewById(R.id.ok_button);
        mScan.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View mView)
            {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null)
                {
                    Toast.makeText(DeviceConnection.this, "Message1", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if (!mBluetoothAdapter.isEnabled())
                    {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    }
                    else
                    {
                        ListPairedDevices();
                        Intent connectIntent = new Intent(DeviceConnection.this, DeviceListActivity.class);
                        startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                    }
                }
            }
        });

        mScan.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View mView)
            {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null)
                {
                    Toast.makeText(DeviceConnection.this, "Message2", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if (!mBluetoothAdapter.isEnabled())
                    {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    }
                    else
                    {
                        ListPairedDevices();
                        Intent connectIntent = new Intent(DeviceConnection.this, DeviceListActivity.class);
                        startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                    }
                }
            }
        });
    }// onCreate

    public void onActivityResult(int mRequestCode, int mResultCode, Intent mDataIntent)
    {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode)
        {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK)
                {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(mDeviceAddress);
                    mBluetoothConnectProgressDialog = ProgressDialog.show(this, "Connecting...", mBluetoothDevice.getName() + " : " + mBluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                    //pairToDevice(mBluetoothDevice); This method is replaced by progress dialog with thread
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK)
                {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(DeviceConnection.this, DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                }
                else
                {
                    Toast.makeText(DeviceConnection.this, "Message", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void ListPairedDevices()
    {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter.getBondedDevices();
        if (mPairedDevices.size() > 0)
        {
            for (BluetoothDevice mDevice : mPairedDevices)
            {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + " " + mDevice.getAddress());
            }
        }
    }

    public void run()
    {
        try
        {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
        }
        catch (IOException eConnectException)
        {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            return;
        }
    }

    private void closeSocket(BluetoothSocket nOpenSocket)
    {
        try
        {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        }
        catch (IOException ex)
        {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(DeviceConnection.this, "DeviceConnected", Toast.LENGTH_LONG).show();
        }
    };
}