package com.example.chase.iot_user;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class User_reg extends AppCompatActivity {

    public Socket sock;
    public BufferedWriter bw;
    public BufferedReader br;
    public Socket_handler sh;

    public void main(){
        Bundle extras = getIntent().getExtras();
        sh =extras.getParcelable("sockethandler");
        sock = Socket_handler.getSocket();
        try {
            bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            bw.write("reg_new_usr_grp");
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final EditText username = (EditText)findViewById(R.id.username);
        final EditText password = (EditText)findViewById(R.id.Password);
        final EditText Aadhar = (EditText)findViewById(R.id.UID);
        Button Submit = (Button)findViewById(R.id.Submit);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = String.valueOf(username.getText());
                String pass = String.valueOf(password.getText());
                String UID = String.valueOf(Aadhar.getText());

                String data = user + ":" + pass + ":" + UID;

                Transmit_data td = new Transmit_data();
                String result="";
                try {
                    result = td.execute(data).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if(result.equals("1")){
                    Toast.makeText(getApplicationContext(), "failure", Toast.LENGTH_LONG).show();
                    Intent fourth = new Intent(User_reg.this, User_grp_admin.class);
                    fourth.putExtra("sockethandler", (Serializable) sh);
                    startActivity(fourth);
                }else{
                    Toast.makeText(getApplicationContext(), "failure", Toast.LENGTH_LONG).show();
                    Intent fourth = new Intent(User_reg.this, User_grp_admin.class);
                    fourth.putExtra("sockethandler", (Serializable) sh);
                    startActivity(fourth);
                }
            }
        });


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reg);
        main();
    }

    public class Transmit_data extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
           String temp1="";
            try {
                bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
                bw.write(params[0]);
                bw.newLine();
                bw.flush();
                BufferedReader br;
                br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                temp1 = br.readLine();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return temp1;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String temp = "exit";
            String Result = "";
            Transmit_data td = new Transmit_data();
            try {
                Result = td.execute(temp).get();
//                Toast.makeText(getApplicationContext(), Result, Toast.LENGTH_LONG).show();
                Intent fourth = new Intent(User_reg.this, User_grp_admin.class);
                fourth.putExtra("sockethandler", (Serializable) sh);
                startActivity(fourth);
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        return super.onKeyDown(keyCode, event);
    }
    }
