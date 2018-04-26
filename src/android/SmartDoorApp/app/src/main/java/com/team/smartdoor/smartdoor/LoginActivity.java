package com.team.smartdoor.smartdoor;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnLogIn;
    DialogFragment waitAuthFragment = new AuthFragment();

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String tempMsg = (message.obj).toString();
            waitAuthFragment.dismiss();
            if(tempMsg.equals("A")){
                Intent myIntent = new Intent(getApplicationContext(), WelcomeActivity.class);
                startActivity(myIntent);
                Toast.makeText(getApplicationContext(), "Attenzione ti sei allontanato troppo!", Toast.LENGTH_LONG).show();
            }
            else if(tempMsg.equals("Y")){
                Intent myIntent = new Intent(getApplicationContext(), WorkingActivity.class);
                startActivity(myIntent);
            }
            else if(tempMsg.equals("K")){
                txtPassword.setText("");
                txtUsername.setText("");
                Toast.makeText(getApplicationContext(), "Username o password sbagliate", Toast.LENGTH_LONG).show();
            }
            else if(tempMsg.equals("P")){
                Intent myIntent = new Intent(getApplicationContext(), WelcomeActivity.class);
                startActivity(myIntent);
                Toast.makeText(getApplicationContext(), "Attenzione presenza non rilevata!", Toast.LENGTH_LONG).show();
            }
            return true;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtUsername = (EditText)findViewById(R.id.username);
        txtPassword = (EditText)findViewById(R.id.password);
        btnLogIn = (Button)findViewById(R.id.logIn);

        btnLogIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                SendReceive.getInstance().write(username+"/"+ password);
                waitAuthFragment.show(getFragmentManager(), "auth");
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

    public static class AuthFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage(getString(R.string.auth));
            dialog.setIndeterminate(true);
            return dialog;
        }
    }
}

