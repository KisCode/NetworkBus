package com.kiscode.networkbus.type;

/**
 * Description:网络类型过滤器
 * Author: keno
 * Date : 2020/9/2 22:08
 **/
public enum NetTypeFilter {
    // 只要网络发生变化就监听
    ALL,

    // 仅监听WIFI网络：连接or断开
    WIFI,

    // 仅监听移动网络：连接or断开
    MOBILE,

    // 仅监听网络断开
    NONE
} 