package com.mazing.com.cavasdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mazing.com.cavasdemo.service.LocalService;

/**
 * Created by user on 17/7/3.
 */

public class ServiceActivity extends AppCompatActivity {

//    LocalService mService;
    boolean mBound = false;

    static final int MSG_SAY_HELLO = 1;
    Messenger mService = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){
        Button stare = (Button)findViewById(R.id.stare);
        stare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               int number = mService.getRandomNumber();
//               Log.i("number ＝ ", number + "");

                if(!mBound)
                    return;
                Message msg = Message.obtain(null, MSG_SAY_HELLO, 0, 0);
                try {
                    mService.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent();
        intent.setAction("com.lypeer.messenger");
        intent.setPackage("com.mazing.com.cavasdemo");
        bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

//    private ServiceConnection mConnection = new ServiceConnection() {
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            LocalService.LocalBinder binder = (LocalService.LocalBinder) service;
//            mService = binder.getService();
//            mBound = true;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            mBound = false;
//        }
//    };


}
