package com.kiscode.networkbus;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;

import com.kiscode.networkbus.core.NetworkCallbackImp;
import com.kiscode.networkbus.core.NetworkReceiver;
import com.kiscode.networkbus.listener.NetChangeListener;
import com.kiscode.networkbus.type.NetType;
import com.kiscode.networkbus.util.NetUtil;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Description: 网络监听总线控制器
 * 所有页面Activity/Fragment均通过单例的NetworkBus进行注册
 * 当网络发生变化时，通知所有注册的页面，通过反射方法回调监听方法
 * Author: keno
 * Date : 2020/9/2 13:54
 **/
public class NetworkBus {
    private static NetworkBus instance;
    //网络监听集合
    private final List<NetChangeListener> netChangeListenerList;
    //标记当前网络状态， 当接收到新网络状态时进行比对
    private final AtomicReference<NetType> currentNetTypeAtomicReference;
    private Application application;

    private NetworkBus(Application application) {
        currentNetTypeAtomicReference = new AtomicReference<>(NetUtil.getNetType(application));
        netChangeListenerList = new CopyOnWriteArrayList<>();
    }

    public static NetworkBus getDefault() {
        if (instance == null) {
            throw new RuntimeException("NetworkBus is not init!");
        }
        return instance;
    }


    public static void init(Application application) {
        instance = new NetworkBus(application);

        //在Android 5.0之后版本新增了NetworkManager监听网络变化
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetworkCallbackImp networkCallbackImp = new NetworkCallbackImp(application.getApplicationContext());
            NetworkRequest networkRequest = new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .build();
            ConnectivityManager connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connectivityManager != null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                connectivityManager.registerDefaultNetworkCallback(networkCallbackImp);
            } else {
                connectivityManager.registerNetworkCallback(networkRequest, networkCallbackImp);
            }
        } else {
            //注册广播
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            NetworkReceiver networkReceiver = new NetworkReceiver();
            application.registerReceiver(networkReceiver, intentFilter);
        }
    }

    public Application getApplication() {
        if (application == null) {
            throw new RuntimeException("NetworkBus is not init!");
        }
        return application;
    }

    public void post(NetType currentNetType) {
        NetType lastNetType = currentNetTypeAtomicReference.get();
        if (lastNetType == currentNetType) {
            return;
        }

        currentNetTypeAtomicReference.set(currentNetType);
        //通知 总线发送
        for (NetChangeListener netChangeListener : netChangeListenerList) {
            netChangeListener.onChange(lastNetType, currentNetType);
        }
    }

    public void register(NetChangeListener netChangeListener) {
        if (netChangeListenerList.contains(netChangeListener)) {
            return;
        }

        synchronized (this) {
            netChangeListenerList.add(netChangeListener);
        }
    }

    public void unregister(NetChangeListener netChangeListener) {
        if (!netChangeListenerList.contains(netChangeListener)) {
            return;
        }
        netChangeListenerList.remove(netChangeListener);
    }
}
