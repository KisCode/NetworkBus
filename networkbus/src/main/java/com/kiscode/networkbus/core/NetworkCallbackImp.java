package com.kiscode.networkbus.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.kiscode.networkbus.NetworkBus;
import com.kiscode.networkbus.type.NetType;
import com.kiscode.networkbus.util.NetUtil;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Description: 网络监听回调
 * Author: keno
 * Date : 2020/9/4 18:49
 **/
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkCallbackImp extends ConnectivityManager.NetworkCallback {
    private static final String TAG = "NetworkCallbackImp";
    private static final int TIME_DISTANCE = 2000;
    //标记当前网络状态， 当接收到新网络状态时进行比对
    private final AtomicReference<NetType> currentNetTypeAtomicReference;
    private Handler mHandler = new Handler();
    private Context context;

    public NetworkCallbackImp(Context context) {
        this.context = context;
        currentNetTypeAtomicReference = new AtomicReference<>();
    }

    @Override
    public void onAvailable(@NonNull Network network) {
        super.onAvailable(network);
        //网络已连接
    }

    @Override
    public void onLost(@NonNull Network network) {
        super.onLost(network);
        Log.i(TAG, Thread.currentThread().getName() + "\t onLost " + NetUtil.isNetworkAvailable(context));

        if (NetUtil.isNetworkAvailable(context)) {
            return;
        }

        if (currentNetTypeAtomicReference.get() == NetType.WIFI) {
            //延迟2秒再次检测网络状态 并发送状态
            mHandler.postDelayed(() -> {
                if (!NetUtil.isNetworkAvailable(context)) {
                    post(NetType.NONE);
                }
            }, TIME_DISTANCE);
        } else {
            //网络断开连接
            post(NetType.NONE);
        }
    }

    @Override
    public void onUnavailable() {
        super.onUnavailable();
    }

    @Override
    public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        //网络发生变化回调 执行在ConnectivityThread 的子线程
        Log.i(TAG, "onCapabilitiesChanged 可以上网：" + networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET));
//        NetworkCapabilities.NET_CAPABILITY_INTERNET
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                //Wifi
                post(NetType.WIFI);
            } else {
                //mobile
                post(NetType.MOBILE);
            }
        }
    }

    private void post(NetType netType) {
        Log.i(TAG, "onCapabilitiesChanged:" + netType + "\t in " + Thread.currentThread().getName());
        mHandler.post(() -> {
            currentNetTypeAtomicReference.set(netType);
            NetworkBus.getDefault().post(netType);
        });
    }
}