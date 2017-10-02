package com.example.chase.iot_user;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Group_List extends AppCompatActivity {

    public Socket sock;
    List<Group> grp_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group__list);
        Bundle extras = getIntent().getExtras();
        final Socket_handler sh =extras.getParcelable("sockethandler");
        sock = Socket_handler.getSocket();
        Transmit_data td = new Transmit_data();
        grp_data = new ArrayList<>();
        try {
            grp_data = td.execute("send_grp").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ListView lv = (ListView)findViewById(R.id.Group);
        GroupAdapter ga = new GroupAdapter(grp_data,getApplicationContext());
        lv.setAdapter(ga);
        registerForContextMenu(lv);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Group grp = grp_data.get(position);
                    String grp_ID = grp.getGrp_ID();
                    Transmit_data_inter tdi = new Transmit_data_inter();
                    String grp_admin = "";
                  try {
                      grp_admin = tdi.execute(grp_ID).get();

                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  } catch (ExecutionException e) {
                      e.printStackTrace();
                  }

                  if(grp_admin.equals("1")){
                      Intent second = new Intent(Group_List.this, User_grp_admin.class);
                      second.putExtra("sockethandler", (Serializable) sh);
                      startActivity(second);
                      finish();
                  }
                  else{
                      Intent second = new Intent(Group_List.this, User_function_non_admin.class);
                      second.putExtra("sockethandler", (Serializable) sh);
                      startActivity(second);
                      finish();
                  }
              }
        });



    }


    public class Transmit_data extends AsyncTask<String, Void, ArrayList<Group>> {

        @Override
        protected ArrayList<Group> doInBackground(String... params) {
            ArrayList<Group> Result = new ArrayList<Group>();
            try {
                BufferedWriter bw;
                bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
                bw.write(params[0]);
                bw.newLine();
                bw.flush();
                BufferedReader br;
                br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                while(true){
                    String temp1 = br.readLine();
                    if(temp1.equals("exit")){
                        break;
                    }
                    else {
                        String temp2 = br.readLine();
                        Result.add(new Group(temp1,temp2));
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return Result;
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
            String temp = "1";
            String Result = "";
            Transmit_data_inter tdi = new Transmit_data_inter();
            try {
                Result = tdi.execute(temp).get();
                Toast.makeText(getApplicationContext(), Result, Toast.LENGTH_LONG).show();
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
