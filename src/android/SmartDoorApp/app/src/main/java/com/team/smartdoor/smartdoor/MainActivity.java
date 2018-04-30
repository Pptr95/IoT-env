package com.team.smartdoor.smartdoor;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    final int REQUEST_ENABLE_BT = 1;
    ArrayList<BluetoothDevice> pairedDevices;
    ArrayList<String> pairedNames;
    ListView listViewPairedDevices;
    ArrayList<BluetoothDevice> discoveredDevices;
    ArrayList<String> discoveredNames;
    ArrayAdapter<String> discoveredAdapter;
    ListView listViewDiscoveredDevices;
    BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    Button scanButton;
    UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    DialogFragment loadingFragment = new LoadingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewPairedDevices = (ListView)findViewById(R.id.listPairedDevices);
        listViewDiscoveredDevices = (ListView)findViewById(R.id.listDiscoveredDevices);
        scanButton = (Button)findViewById(R.id.scan_devices);
        discoveredDevices = new ArrayList<>();
        discoveredNames = new ArrayList<>();
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(br, intentFilter);
        discoveredAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.mytextview, discoveredNames);
        listViewDiscoveredDevices.setAdapter(discoveredAdapter);
        if(getIntent().getBooleanExtra("restart",false)){
            finish();
            int pid = android.os.Process.myPid();
            android.os.Process.killProcess(pid);
        }

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discoveredDevices.clear();
                discoveredNames.clear();
                discoveredAdapter.notifyDataSetChanged();
                btAdapter.startDiscovery();
            }
        });

        listViewDiscoveredDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                btAdapter.cancelDiscovery();
                loadingFragment.show( getFragmentManager() , "loading");
                new ClientClass(discoveredDevices.get(i)).execute();

            }
        });

        listViewPairedDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                btAdapter.cancelDiscovery();
                loadingFragment.show( getFragmentManager() , "loading");
                new ClientClass(pairedDevices.get(i)).execute();
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        btAdapter.startDiscovery();
        bluetoothSetup();
        discoveredNames.clear();
        discoveredAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop(){
        super.onStop();
        btAdapter.cancelDiscovery();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        SendReceive.getInstance().cancel();
    }

    @Override
    public void onActivityResult(int reqID, int res, Intent data){
        if(reqID == REQUEST_ENABLE_BT && res == Activity.RESULT_OK){
            Toast.makeText(getApplicationContext(), "Bluetooth attivato", Toast.LENGTH_LONG).show();
            getPairedDevices();
            btAdapter.startDiscovery();
        }
        if(reqID == REQUEST_ENABLE_BT && res == Activity.RESULT_CANCELED){ //BT enabling process aborted
            Toast.makeText(getApplicationContext(), "Bluetooth non attivato", Toast.LENGTH_LONG).show();
        }
    }

    public void bluetoothSetup(){
        if(btAdapter == null){
            Toast.makeText(getApplicationContext(), "Bluetooth non supportato", Toast.LENGTH_LONG).show();
            finish();
        }
        if (!btAdapter.isEnabled()) {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);
        } else {
            getPairedDevices();
            btAdapter.startDiscovery();
        }
    }

    public void getPairedDevices(){
        pairedDevices = new ArrayList<>(btAdapter.getBondedDevices());
        pairedNames = new ArrayList<>();
        for(BluetoothDevice bt : pairedDevices) {
            pairedNames.add(bt.getName());
        }
        ArrayAdapter<String> pairedAdapter = new  ArrayAdapter<>(getApplicationContext(), R.layout.mytextview, pairedNames);
        listViewPairedDevices.setAdapter(pairedAdapter);
    }

    private final BroadcastReceiver br = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context , Intent intent){
            if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction())){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(!discoveredDevices.contains(device) && device.getName() != null) {
                    discoveredDevices.add(device);
                }
                discoveredNames.clear();
                for(BluetoothDevice bt : discoveredDevices) {
                    discoveredNames.add(bt.getName());
                }
                discoveredAdapter.notifyDataSetChanged();
            }
        }
    };

    private class ClientClass extends AsyncTask<Void,Void,Void>{
        private BluetoothSocket socket;
        public ClientClass(BluetoothDevice device){
            try {
                socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... params){
            btAdapter.cancelDiscovery();
            try{
                socket.connect();
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void par){
            if(socket.isConnected()) {
                SendReceive sr = SendReceive.getInstance();
                sr.setChannel(socket);
                sr.start();
                Intent myIntent = new Intent(getApplicationContext(), WelcomeActivity.class);
                startActivity(myIntent);
            }
            loadingFragment.dismissAllowingStateLoss();
        }
    }


    public static class LoadingFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage(getString(R.string.loading));
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
