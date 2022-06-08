package com.kiscode.networkbus.listener;

import com.kiscode.networkbus.type.NetType;

/**
 * Description:简单的网络变化监听，仅监听网络断开连接
 * Author: keno
 * Date : 2020/9/2 19:55
 **/
public abstract class SimpleNetChangeListener implements NetChangeListener {
    /***
     *
     * @param lastNetType 上一个网络状态
     * @param currentNetType 当前网络状态
     */
    public void onChange(NetType lastNetType, NetType currentNetType) {
        if (lastNetType == currentNetType) {
            return;
        }

        if (lastNetType == NetType.NONE) {
            onChange(true);
        } else if (currentNetType == NetType.NONE) {
            onChange(false);
        }
    }

    /**
     * 网络连接上
     *
     * @param isConnected 网络是否连接上
     */
    protected abstract void onChange(boolean isConnected);
}
