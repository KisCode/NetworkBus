package com.kiscode.networkbus.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import androidx.annotation.RequiresApi;

import com.kiscode.networkbus.type.NetType;

/**
 * Description:网络处理工具
 * Author: keno
 * Date : 2020/9/2 18:04
 **/
public class NetUtil {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Network activeNetwork = connectivityManager.getActiveNetwork();
            if (activeNetwork == null) return false;
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
            return networkCapabilities != null &&
                    (networkCapabilities.hasCapability(NetworkCapabilities.TRANSPORT_WIFI)
                            ||networkCapabilities.hasCapability(NetworkCapabilities.TRANSPORT_CELLULAR)
                            || networkCapabilities.hasCapability(NetworkCapabilities.TRANSPORT_ETHERNET));
        } else {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }

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
}