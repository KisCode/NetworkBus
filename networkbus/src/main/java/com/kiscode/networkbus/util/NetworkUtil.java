package com.kiscode.networkbus.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.kiscode.networkbus.NetworkBus;
import com.kiscode.networkbus.type.NetType;

/**
 * Description:网络处理工具
 * Author: keno
 * Date : 2020/9/2 18:04
 **/
public class NetworkUtil {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    public static NetType getNetType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (null == networkInfo) {
            return NetType.NONE;
        }
        int networkType = networkInfo.getType();
        if (networkType == ConnectivityManager.TYPE_WIFI) {
            return NetType.WIFI;
        } else if (networkType == ConnectivityManager.TYPE_MOBILE) {
            return NetType.MOBILE;
        } else {
            return NetType.NONE;
        }
    }

    public static boolean isWiFiActive(Context context) {
        WifiManager mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
        return mWifiManager.isWifiEnabled() && ipAddress != 0;
    }
} 