package com.example.anirudh.testapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    static String newIP;
    static int ipCounter=0;
    static Button  button;

    private ProgressBar mProgress;
    private int mProgressStatus = 0;

    private Handler mHandler = new Handler();


    public static void tryToConnect(){
        String connectIp = newIP+"."+MainActivity.ipCounter+"";
        Log.i("wifi",connectIp);
        try{
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(connectIp,12001), 500);
            Log.i("wifi4","conected");
        } catch (Exception e) {
            Log.i("wifi", e.toString());
            ipCounter++;
            tryToConnect();
        }
        }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //startActivity(new Intent(MainActivity.this,Prog.class));

        try{mProgress = (ProgressBar) findViewById(R.id.progressBar);}catch (Exception e){
            Log.i("myexcep",e.toString());
        }
        Log.i("myprogress",mProgress.toString());
        new Thread(new Runnable() {
            public void run() {
                while (mProgressStatus < 100) {
                    mProgressStatus=mProgressStatus + 5;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        public void run() {
                            mProgress.setProgress(mProgressStatus);
                        }
                    });
                }
            }
        }).start();

        /*WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
        wifiManager.setWifiEnabled(false);


        Log.i("hotspot",ApManager.isApOn(MainActivity.this)+"");
        Log.i("hotspot",ApManager.configApState(MainActivity.this)+"");*/

        /*try {
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        /*WifiManager newWifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        WifiInfo newWifiInfo = newWifiManager.getConnectionInfo();
        Log.i("SSID",newWifiInfo.getSSID());*/

        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.i("wifiinfo",wifiInfo.toString());


        /*WifiConfiguration apConfig = null;
        Method method = null;
        try {
            method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            method.invoke(wifiManager, apConfig, true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/

        int ipAddress = wifiInfo.getIpAddress();

        String ip = null;

        Log.i("wifi", String.valueOf(ipAddress));
        ip = String.format("%d.%d.%d.%d",
                (ipAddress & 0xff),
                (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff),
                (ipAddress >> 24 & 0xff));

        Log.i("wifi1",ip);

        newIP = ip.substring(0,ip.lastIndexOf('.'));
        Log.i("wifi2",newIP);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //new Socket("192.168.0.1",12001);
                    MainActivity.tryToConnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        })/*.start()*/;

    }
}
