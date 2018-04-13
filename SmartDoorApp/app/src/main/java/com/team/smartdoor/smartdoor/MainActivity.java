package com.team.smartdoor.smartdoor;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter btAdapter = null;
    final int REQUEST_ENABLE_BT = 1;
    private Set<BluetoothDevice> pairedDevices;
    private ListView listPairedDevices;
    private ArrayList<String> nbDevices;
    private ListView listDiscoveredDevices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listPairedDevices = (ListView)findViewById(R.id.listPairedDevices);
        listDiscoveredDevices = (ListView)findViewById(R.id.listDiscoveredDevices);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        nbDevices = new ArrayList<>();
        if(btAdapter == null){
            Log.e("Error","Il Bluetooth non è compatibile");
            finish();
        }
        if (!btAdapter.isEnabled()) {
            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(i, REQUEST_ENABLE_BT);
        }
        registerReceiver(br, new IntentFilter(BluetoothDevice. ACTION_FOUND));



    }

    @Override
    public void onStart(){
        super.onStart();
        getPairedDevices();
        nbDevices.clear();
        btAdapter.startDiscovery();


    }

    @Override
    public void onStop(){
        super.onStop();
        btAdapter.cancelDiscovery();
    }

    @Override
    public void onActivityResult(int reqID, int res, Intent data){
        if(reqID == REQUEST_ENABLE_BT && res == Activity.RESULT_OK){
            Toast.makeText(getApplicationContext(), "Bluetooth Attivo", Toast.LENGTH_LONG).show();
        }
        if(reqID == REQUEST_ENABLE_BT && res == Activity.RESULT_CANCELED){ //BT enabling process aborted
            Toast.makeText(getApplicationContext(), "Il bluetooth non è stato attivato", Toast.LENGTH_LONG).show();
        }
    }

    public void getPairedDevices(){
        pairedDevices = btAdapter.getBondedDevices();
        ArrayList list = new ArrayList();
        for(BluetoothDevice bt : pairedDevices) list.add(bt.getName());
        Toast.makeText(getApplicationContext(), "Showing Paired Devices",Toast.LENGTH_SHORT).show();
        final ArrayAdapter adapter = new  ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        listPairedDevices.setAdapter(adapter);
    }

    private final BroadcastReceiver br = new BroadcastReceiver(){
       @Override
        public void onReceive(Context context , Intent intent){
            BluetoothDevice device = null;
            if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction())){
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                nbDevices.add(device.getName());
                final ArrayAdapter adapter = new  ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, nbDevices);
                listDiscoveredDevices.setAdapter(adapter);
            }
        }
    };



}

