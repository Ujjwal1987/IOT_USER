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
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Agreegatorlist extends AppCompatActivity {

    Socket sock;
    List<Agreegator> agreegator_data;
    public Socket_handler sh;
    public String admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreegatorlist);
        Bundle extras = getIntent().getExtras();
        final Socket_handler sh =extras.getParcelable("sockethandler");
        admin = extras.getString("admin");
        sock = Socket_handler.getSocket();
        Transmit_data td = new Transmit_data();
        agreegator_data = new ArrayList<>();
        try {
            agreegator_data = td.execute("send_agree_data").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ListView lv = (ListView)findViewById(R.id.Agreegator);
        Agreegator_adapter aa = new Agreegator_adapter(agreegator_data,getApplicationContext());
        lv.setAdapter(aa);
        registerForContextMenu(lv);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Agreegator agree = agreegator_data.get(position);
                String agreegator_id = agree.getAgree_id();
                agreegator_id = "/agreeid/"+agreegator_id;
//                String Device_type = agree.getDev_type();
                Transmit_data_inter tdi = new Transmit_data_inter();
                String rs = null;
                try {
                    rs = tdi.execute(agreegator_id).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if(rs.equals("1")){
                        //new intent for lighting
//                        String []temp = Device_type.split(".");
//                        if(temp[1].equals("8")){
//                            {
                        Intent Third = new Intent(Agreegatorlist.this, Device_list.class);
                        Third.putExtra("sockethandler", (Serializable) sh);
                        Third.putExtra("admin", admin);
                        startActivity(Third);
                        finish();
//                            }
//                        }
                    }
                else{
                    Toast.makeText(getApplicationContext(), "Agreegator not online", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public class Transmit_data extends AsyncTask<String, Void, ArrayList<Agreegator>> {

        @Override
        protected ArrayList<Agreegator> doInBackground(String... params) {
            ArrayList<Agreegator> Result = new ArrayList<Agreegator>();
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
                        Result.add(new Agreegator(temp1,temp2));
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return Result;
        }

    }

    public class Transmit_data_inter extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            BufferedWriter bw;
            String Result = null;
            try {
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
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if(admin.equals("admin")){
                Intent third = new Intent(Agreegatorlist.this, User_grp_admin.class);
                third.putExtra("sockethandler", (Serializable) sh);
                startActivity(third);
            }
            else{
                Intent third = new Intent(Agreegatorlist.this, User_function_non_admin.class);
                third.putExtra("sockethandler", (Serializable) sh);
                startActivity(third);
            }
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }
}

