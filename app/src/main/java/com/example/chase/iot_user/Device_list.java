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

public class Device_list extends AppCompatActivity {

    Socket sock;
    List<Device> device_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        Bundle extras = getIntent().getExtras();
        final Socket_handler sh =extras.getParcelable("sockethandler");
        sock = Socket_handler.getSocket();
        Transmit_data td = new Transmit_data();
        device_data = new ArrayList<>();
        try {
            device_data = td.execute("send_dev_data").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ListView lv = (ListView)findViewById(R.id.Device);
        Device_adapter da = new Device_adapter(device_data,getApplicationContext());
        lv.setAdapter(da);
        registerForContextMenu(lv);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Device dev = device_data.get(position);
                String device_id = dev.getDev_id();
                device_id = "/devid/"+device_id;
                String Device_type = dev.getDev_type();
                Transmit_data_inter tdi = new Transmit_data_inter();
                String rs = null;
                try {
                    rs = tdi.execute(device_id).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if(rs.equals("1")){
                    if(Device_type.startsWith("Lighting")) {
                        //new intent for lighting
//                        String []temp = Device_type.split(".");
//                        if(temp[1].equals("8")){
//                            {
                                Intent Third = new Intent(Device_list.this, Dev_cntrl.class);
                                Third.putExtra("sockethandler", (Serializable) sh);
                                startActivity(Third);
                                finish();
//                            }
//                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Device type not yest supported by the Application", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Device not online", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public class Transmit_data extends AsyncTask<String, Void, ArrayList<Device>> {

        @Override
        protected ArrayList<Device> doInBackground(String... params) {
            ArrayList<Device> Result = new ArrayList<Device>();
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
                        Result.add(new Device(temp1,temp2));
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
            tdi.execute(temp);
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }
}
