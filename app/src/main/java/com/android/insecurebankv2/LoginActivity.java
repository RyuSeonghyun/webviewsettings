package com.android.insecurebankv2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;

import java.io.IOException;


import android.os.Environment;
import android.view.View;
import android.widget.Toast;



public class LoginActivity extends Activity {

    private final String BROADCAST_MESSAGE = "com.example.fileaccess_pathtraversal.intent";
    private BroadcastReceiver mReceiver = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_main);
	}

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver();

    }
    private void unregisterReceiver() {
        if(mReceiver != null){
            this.unregisterReceiver(mReceiver);
            mReceiver = null;
        }

    }

    private void registerReceiver(){
        if(mReceiver != null) return;

        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction(BROADCAST_MESSAGE);

        this.mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String receviedData = intent.getStringExtra("intent");
                if(intent.getAction().equals(BROADCAST_MESSAGE)){
                    //Toast.makeText(context, "경로 : " + receviedData, Toast.LENGTH_SHORT).show();
                    try {
                        saveFile(receviedData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        this.registerReceiver(this.mReceiver, theFilter);

    }
    public void saveFile(String filename) throws IOException {
        StringBuffer buffer = new StringBuffer();
        //write file
	    String pathString = Environment.getExternalStorageDirectory() + "/"+Environment.DIRECTORY_DOWNLOADS+ "/";
        File file = new File(pathString, filename);

        FileWriter fw = new FileWriter(file, false);
        fw.write("abc"+"\n");
        fw.close();

        ///read file
        BufferedReader eReader = new BufferedReader(new FileReader(pathString+filename));
        String data = eReader.readLine();
        while(data != null)
        {
            buffer.append(data);
            data = eReader.readLine();
        }
        Toast.makeText(getApplicationContext(), buffer.toString()+"\n", Toast.LENGTH_LONG).show();
        eReader.close();


    }

    public void clickMethod(View v){
        /** 1. 전달할 메세지를 담은 인텐트 생성
         * 2. DATA를 잘 전달받는지 확인할 수 있게 Key, value 넣기
         * 3. sendBroadcast(intent); 메서드를 이용해서 전달할 intent를 넣고, 브로드캐스트한다. */

        String filename = "a.txt";
        Intent intent = new Intent(BROADCAST_MESSAGE);
        intent.putExtra("intent", filename);
        sendBroadcast(intent);

    }

}