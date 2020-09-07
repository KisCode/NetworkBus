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
import com.kiscode.networkbus.util.NetworkUtil;

/**
 * Description: 网络监听回调
 * Author: keno
 * Date : 2020/9/4 18:49
 **/
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkCallbackImp extends ConnectivityManager.NetworkCallback {
    private static final String TAG = "NetworkCallbackImp";
    private Handler mHandler = new Handler();
    private Context context;

    public NetworkCallbackImp(Context context) {
        this.context = context;
    }

    @Override
    public void onAvailable(@NonNull Network network) {
        super.onAvailable(network);
        //网络已连接
//        Log.i(TAG, Thread.currentThread().getName() + "\t onAvailable" );
    }

    @Override
    public void onLost(@NonNull Network network) {
        super.onLost(network);
//        Log.i(TAG, Thread.currentThread().getName() + "\t onLost" );
        if (NetworkUtil.isNetworkAvailable(context)) {
            return;
        }
        //网络断开连接
        post(NetType.NONE);
    }

    @Override
    public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        //网络发生变化回调 执行在ConnectivityThread 的子线程
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
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
        mHandler.post(() -> NetworkBus.getDefault().post(netType));
    }
}