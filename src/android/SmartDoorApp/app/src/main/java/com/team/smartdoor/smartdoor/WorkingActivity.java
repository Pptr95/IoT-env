package com.team.smartdoor.smartdoor;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class WorkingActivity extends AppCompatActivity {

    Button btnLogout;
    TextView txtTemp;
    TextView txtLedValue;
    SeekBar barLedValue;
    int progress=0;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String tempMsg = (message.obj).toString();
            if(tempMsg.contains("L")){
               restartApp();
            }
            else{
                txtTemp.setText(tempMsg + " °C");
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working);
        txtLedValue = (TextView) findViewById(R.id.txtLedValue);
        txtTemp = (TextView) findViewById(R.id.txtTemp);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        barLedValue = (SeekBar) findViewById(R.id.barLedValue);
        txtLedValue.setText(String.valueOf(progress));
        txtTemp.setText("- °C");

          barLedValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress = i;
                txtLedValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SendReceive.getInstance().write(Integer.toString(progress));
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               restartApp();
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
       SendReceive.getInstance().setHandler(handler);
    }

    @Override
    public void onBackPressed() {
        return;
    }

    public void restartApp(){
        SendReceive.getInstance().write("L");
        SendReceive.getInstance().cancel();
        Intent i = new Intent(this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("restart",true);
        startActivity(i);
        finish();
    }
}


