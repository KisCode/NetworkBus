package com.kiscode.networkbus;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;

import com.kiscode.networkbus.annotation.NetSubscribe;
import com.kiscode.networkbus.bean.NetSubcribeMethodModel;
import com.kiscode.networkbus.core.NetworkCallbackImp;
import com.kiscode.networkbus.core.NetworkReceiver;
import com.kiscode.networkbus.exception.NetworkBusException;
import com.kiscode.networkbus.type.NetType;
import com.kiscode.networkbus.type.NetTypeFilter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 网络监听总线控制器
 * 所有页面Actvity/Fragment均通过单例的NetworkBus进行注册
 * 当网络发生变化时，通知所有注册的页面，通过反射方法回调监听方法
 * Author: keno
 * Date : 2020/9/2 13:54
 **/
public class NetworkBus {
    private static NetworkBus instance;
    private Application application;
    private NetworkReceiver networkReceiver;

    private Map<Object, List<NetSubcribeMethodModel>> netSubcribeMap;

    private NetworkBus() {
        netSubcribeMap = new HashMap<>();
    }

    public static NetworkBus getDefault() {
        if (instance == null) {
            synchronized (NetworkBus.class) {
                if (instance == null) {
                    instance = new NetworkBus();
                }
            }
        }
        return instance;
    }

    public void init(Application application) {
        this.application = application;
        //在Android 5.0之后版本新增了NetworkMannager监听网络变化
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            NetworkCallbackImp networkCallbackImp = new NetworkCallbackImp(application.getApplicationContext());
            NetworkRequest networkRequest = new NetworkRequest.Builder().build();
            ConnectivityManager connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connectivityManager != null;
            connectivityManager.registerNetworkCallback(networkRequest, networkCallbackImp);
        } else {
            //注册广播
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            networkReceiver = new NetworkReceiver();
            application.registerReceiver(networkReceiver, intentFilter);
        }
    }

    public Application getApplication() {
        if (application == null) {
            throw new RuntimeException("NetworkBus is not inint!");
        }
        return application;
    }

    public void post(NetType currentNetType) {
        //通知 总线发送
        for (Map.Entry<Object, List<NetSubcribeMethodModel>> entry : netSubcribeMap.entrySet()) {
            Object object = entry.getKey();
            List<NetSubcribeMethodModel> methodModelList = entry.getValue();
            for (NetSubcribeMethodModel netSubcribeMethodModel : methodModelList) {
                switch (netSubcribeMethodModel.getNetTypeFilter()) {
                    //触发条件
                    case ALL:
                        subscribeMethodInvoke(currentNetType, object, netSubcribeMethodModel);
                        break;
                    case WIFI:
                        if (NetType.WIFI == currentNetType || NetType.NONE == currentNetType) {
                            subscribeMethodInvoke(currentNetType, object, netSubcribeMethodModel);
                        }
                        break;
                    case MOBILE:
                        if (NetType.MOBILE == currentNetType || NetType.NONE == currentNetType) {
                            subscribeMethodInvoke(currentNetType, object, netSubcribeMethodModel);
                        }
                        break;
                    case NONE:
                        if (NetType.NONE == currentNetType) {
                            subscribeMethodInvoke(currentNetType, object, netSubcribeMethodModel);
                        }
                        break;
                }
            }
        }
    }

    /***
     * 通过反射执行网络监听方法
     * @param netType 网络类型
     * @param object 监听所在类对象
     * @param netSubcribeMethodModel 监听的方法对象
     */
    private void subscribeMethodInvoke(NetType netType, Object object, NetSubcribeMethodModel netSubcribeMethodModel) {
        try {
            Method method = netSubcribeMethodModel.getMethod();
            method.setAccessible(true); //私有方法进行授权
            method.invoke(object, netType);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void register(Object object) {
        if (netSubcribeMap == null || netSubcribeMap.containsKey(object)) {
            return;
        }
        List<NetSubcribeMethodModel> methodObjList = findNetSubscribeAnnotationMethod(object);

        netSubcribeMap.put(object, methodObjList);
    }

    public void unregister(Object object) {
        if (netSubcribeMap == null || !netSubcribeMap.containsKey(object)) {
            return;
        }
        netSubcribeMap.remove(object);
    }

    private List<NetSubcribeMethodModel> findNetSubscribeAnnotationMethod(Object object) {
        List<NetSubcribeMethodModel> netSubcribeMethodObjList = new ArrayList<>();
        //获取指定类中所有方法
        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method method : methods) {

            //遍历 所有被NetSubscribe注解的方法
            NetSubscribe netSubscribeAnnotation = method.getAnnotation(NetSubscribe.class);
            if (netSubscribeAnnotation == null) {
                continue;
            }

            String methodName = method.getDeclaringClass().getName() + "." + method.getName();

            //遍历 方法的参数长度
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                throw new NetworkBusException("@NetSubscribe method " + methodName +
                        "must have exactly 1 parameter but has " + parameterTypes.length);
            }

            Class<?> parameterType = parameterTypes[0];
            //方法参数类型未NetType
            if (!parameterType.isAssignableFrom(NetType.class)) {
                throw new NetworkBusException("@NetSubscribe method "
                        + methodName
                        +" is illegal, parameterType must isAssignableFrom NetType");
            }
            NetTypeFilter netTypeFilter = netSubscribeAnnotation.value();
            netSubcribeMethodObjList.add(new NetSubcribeMethodModel(netTypeFilter, parameterType, method));
        }
        return netSubcribeMethodObjList;
    }
}
