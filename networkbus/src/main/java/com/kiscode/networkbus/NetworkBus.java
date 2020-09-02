package com.kiscode.networkbus;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.kiscode.networkbus.listener.NetChangeListener;
import com.kiscode.networkbus.receiver.NetworkReceiver;

/**
 * Description:
 * Author: keno
 * Date : 2020/9/2 13:54
 **/
public class NetworkBus {
    private static NetworkBus instance;
    private Application application;
    private NetworkReceiver networkBroadcastReceiver;

    private NetworkBus() {
    }

    public static NetworkBus getDefault() {
        if (instance == null) {
            synchronized (NetworkBus.class) {
                if (instance == null) {
                    instance = new NetworkBus();
                }
            }
        }
        return instance;
    }


    public void init(Application application) {
        this.application = application;

        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        networkBroadcastReceiver = new NetworkReceiver();
        application.registerReceiver(networkBroadcastReceiver, intentFilter);
    }

    public void setOnChangeListener(NetChangeListener listener) {
        if (networkBroadcastReceiver != null) {
            networkBroadcastReceiver.setOnChangeListener(listener);
        }
    }
}
