package com.kiscode.networkbus.bean;

import com.kiscode.networkbus.type.NetType;
import com.kiscode.networkbus.type.NetTypeFilter;

import java.lang.reflect.Method;

/**
 * Description: 网络注解方法的抽象对象
 * Author: keno
 * Date : 2020/9/3 23:19
 **/
public class NetSubcribeMethodModel {

    //注解参数值
    private NetTypeFilter netTypeFilter;

    //参数类型
    private Class<?> parameterType;

    //方法体
    private Method method;

    public NetSubcribeMethodModel(NetTypeFilter netType, Class<?> parameterType, Method method) {
        this.netTypeFilter = netType;
        this.parameterType = parameterType;
        this.method = method;
    }

    public NetTypeFilter getNetTypeFilter() {
        return netTypeFilter;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    public Method getMethod() {
        return method;
    }
}