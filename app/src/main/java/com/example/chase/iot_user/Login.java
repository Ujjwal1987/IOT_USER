package com.example.chase.iot_user;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity {

    private String username, password;
    private Button Login;
    private EditText user,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        main();
    }

    public void main(){

        Login = (Button)findViewById(R.id.button_login);
        user = (EditText)findViewById(R.id.editText_username);
        pass = (EditText)findViewById(R.id.editText_password);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = String.valueOf(user.getText());
                String password = String.valueOf(pass.getText());

                if((username.equals(""))||(password.equals(""))){
                    Toast.makeText(getApplicationContext(), "Username or Password field cannot be empty", Toast.LENGTH_LONG).show();
                }
                else
                {
                    String result = "";
                    Transmit_data td = new Transmit_data();
                    try {
                        result = td.execute(username,password,"a").get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    String []temp = result.split(":");

                    if(temp[0].equals("1")){
                        startservice(v, temp[1]);
                        Intent first = new Intent(Login.this, Group_List.class);
                        Socket_handler sh = null;
                        first.putExtra("sockethandler", (Serializable) sh);
                        startActivity(first);
                        finish();
                    } else{
                        Toast.makeText(getApplicationContext(), "Incorrect Username or Password", Toast.LENGTH_LONG).show();
                    }
                }

            }
       });

    }

    public class Transmit_data extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String transmit_data = params[0] + ":" + params[1];
            String ipaddress_string = "192.168.1.4";
            InetAddress ipaddress = null;
            int port = 8887;
            Socket sock = null;
            String Result = "";
            BufferedWriter bw;
            BufferedReader br;
            try {
                ipaddress = InetAddress.getByName(ipaddress_string);
                try {
                    sock = new Socket(ipaddress, port);
                    Socket_handler.setSocket(sock);
                    bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
                    bw.write(transmit_data);
                    bw.newLine();
                    bw.flush();

                    br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                    Result = br.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

            return Result;
        }
    }

        public void startservice(View v, String UID){
            Intent i = new Intent(this,Myservice.class);
            i.putExtra("UID", UID);
            startService(i);
}


}



