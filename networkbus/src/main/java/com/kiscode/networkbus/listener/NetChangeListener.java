package com.kiscode.networkbus.listener;

import com.kiscode.networkbus.type.NetType;

/**
 * Description:网络变化监听
 * Author: keno
 * Date : 2020/9/2 19:55
 **/
public interface NetChangeListener {
    /***
     *
     * @param lastNetType 上一个网络状态
     * @param currentNetType 当前网络状态
     */
    void onChange(NetType lastNetType, NetType currentNetType);
}
