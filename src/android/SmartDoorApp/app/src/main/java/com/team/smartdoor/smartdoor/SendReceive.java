package com.team.smartdoor.smartdoor;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class SendReceive extends Thread {
    private BluetoothSocket btSocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Handler handler;
    private boolean stop;
    private static SendReceive instance = null;
    private SendReceive(){
        stop = true;
    }

    public static SendReceive getInstance(){
        if(instance == null){
            instance = new SendReceive();
        }
        return instance;
    }

    public void setChannel(BluetoothSocket socket){
        this.btSocket = socket;
        InputStream tempIn = null;
        OutputStream tempOut = null;
        try {
            tempIn = btSocket.getInputStream();
            tempOut = btSocket.getOutputStream();
        } catch (IOException ex){
            ex.printStackTrace();
        }
        this.inputStream = tempIn;
        this.outputStream = tempOut;
        stop = false;
    }

    public void setHandler(Handler handler){
        this.handler = handler;
    }

    public void run(){
        byte[] buffer = new byte[1024];
        int bytes;
        while (!stop){
            try{
                bytes = inputStream.read(buffer);
                String message = new String(buffer,0,bytes);
                handler.obtainMessage(-1,bytes,-1,message).sendToTarget();
            } catch (IOException ex){
                stop = true;
            }
        }
    }

    public void write(String data){
        try {
            outputStream.write(data.getBytes());
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void cancel(){
        try{
            btSocket.close();
        }catch (IOException ex) {
            stop = true;
        }
    }
}
