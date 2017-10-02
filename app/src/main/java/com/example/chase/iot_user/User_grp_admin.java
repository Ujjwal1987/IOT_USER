package com.example.chase.iot_user;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class User_grp_admin extends AppCompatActivity {

    public Button add_usr,cntrl_dev, add_exist_usr;
    Socket sock;
    public Socket_handler sh;


    public void main(){
        add_usr = (Button)findViewById((R.id.Add_new_usr_grp));
        add_exist_usr = (Button)findViewById(R.id.add_exist_user_grp);
        cntrl_dev = (Button)findViewById(R.id.Device_control);
        Bundle extras = getIntent().getExtras();
        sh =extras.getParcelable("sockethandler");
        sock = Socket_handler.getSocket();

        add_usr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent third = new Intent(User_grp_admin.this, User_reg.class);
                third.putExtra("sockethandler", (Serializable) sh);
                startActivity(third);
                finish();
            }
        });

        add_exist_usr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent third = new Intent(User_grp_admin.this, User_exist_grp_reg.class);
                third.putExtra("sockethandler", (Serializable) sh);
                startActivity(third);
                finish();
            }
        });

        cntrl_dev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transmit_data td = new Transmit_data();
                td.execute("cntrl_dev");
                Intent third = new Intent(User_grp_admin.this, Device_list.class);
                third.putExtra("sockethandler", (Serializable) sh);
                startActivity(third);
                finish();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_grp_admin);
        main();

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

    public class Transmit_data extends AsyncTask<String, Void, Void>{

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            String temp = "exit";
            String Result = "";
            Transmit_data_inter tdi = new Transmit_data_inter();
            try {
                Result = tdi.execute(temp).get();
//                Toast.makeText(getApplicationContext(), Result, Toast.LENGTH_LONG).show();
                Intent third = new Intent(User_grp_admin.this, Group_List.class);
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
