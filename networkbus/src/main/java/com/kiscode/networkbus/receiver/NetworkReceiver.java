package com.kiscode.networkbus.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.kiscode.networkbus.NetworkBus;
import com.kiscode.networkbus.type.NetType;
import com.kiscode.networkbus.util.Constant;
import com.kiscode.networkbus.util.NetworkUtil;

/**
 * Description:
 * Author: keno
 * Date : 2020/9/2 13:56
 **/
public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }

        if (ConnectivityManager.CONNECTIVITY_ACTION.equalsIgnoreCase(intent.getAction())) {
            NetType netType = NetworkUtil.getNetType(context);
//            Log.i(Constant.LOG, "network change:" + netType);
            NetworkBus.getDefault().post(netType);
        }
    }
}