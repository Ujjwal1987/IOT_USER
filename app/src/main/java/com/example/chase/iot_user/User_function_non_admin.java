package com.example.chase.iot_user;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class User_function_non_admin extends AppCompatActivity {

    public Button Control_devices;
    public Socket_handler sh;
    Socket sock;
    public String admin = "non-admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_function_non_admin);
        Control_devices = (Button)findViewById(R.id.Control_devices);
        Bundle extras = getIntent().getExtras();
        sh =extras.getParcelable("sockethandler");
        sock = Socket_handler.getSocket();

        Control_devices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Transmit_data td = new Transmit_data();
                td.execute("cntrl_dev");
                Intent third = new Intent(User_function_non_admin.this, Agreegatorlist.class);
                third.putExtra("sockethandler", (Serializable) sh);
                third.putExtra("admin",admin);
                startActivity(third);
                finish();
            }
        });
    }

    public class Transmit_data extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            BufferedWriter bw;
            try {
                bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
                bw.write(params[0]);
                bw.newLine();
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
    public class Transmit_data_inter extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String Result = new String();
            try {
                BufferedWriter bw;
                bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
                bw.write(params[0]);
                bw.newLine();
                bw.flush();
                BufferedReader br;
                br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                Result = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Result;
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            String temp = "exit";
            String Result = "";
            Transmit_data_inter tdi = new Transmit_data_inter();
            try {
                Result = tdi.execute(temp).get();
//                Toast.makeText(getApplicationContext(), Result, Toast.LENGTH_LONG).show();
                Intent third = new Intent(User_function_non_admin.this, Group_List.class);
                third.putExtra("sockethandler", (Serializable) sh);
                startActivity(third);
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
