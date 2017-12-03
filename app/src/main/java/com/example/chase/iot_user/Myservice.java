package com.example.chase.iot_user;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;


import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import java.io.OutputStreamWriter;
import java.net.Socket;


/**
 * Created by chase on 26-05-2017.
 */

public class Myservice extends Service {

    Socket sock1;
    String UID;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        UID = intent.getStringExtra("UID");
        Thread ServerSockethread = new Thread(new ServerSockethread(UID));
        ServerSockethread.start();
        //Toast.makeText(getApplicationContext(), String.valueOf(sock1), Toast.LENGTH_LONG).show();
 /*       new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    ReceiveData rd = new ReceiveData();
                    String Result = null;
                    try {
                        Result = rd.execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(), Result, Toast.LENGTH_LONG).show();
                }
            }
        }).start();*/

        return super.onStartCommand(intent, flags, startId);
        }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class ServerSockethread extends Thread{
        Socket sock;
        String uid;

        public ServerSockethread(String uid) {
            this.uid = uid;
        }

        @Override
        public void run() {
            try {
                sock = new Socket("192.168.1.4",8884);
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedWriter bw;
            try {
                bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
                bw.write(uid);
                bw.newLine();
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            while(true){
                try {

                    BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                    String result = br.readLine();
                    shownotification(result);
//                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void shownotification(String result){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Device Update");
        builder.setContentText(result);
        Intent i = new Intent(this, notification.class);
        TaskStackBuilder stackbuilder = TaskStackBuilder.create(this);
        stackbuilder.addParentStack(notification.class);
        stackbuilder.addNextIntent(i);
        PendingIntent pi = stackbuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0, builder.build());
    }

}
