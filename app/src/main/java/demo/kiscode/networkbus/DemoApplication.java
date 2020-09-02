package demo.kiscode.networkbus;

import android.app.Application;

import com.kiscode.networkbus.NetworkBus;

/**
 * Description:
 * Author: keno
 * Date : 2020/9/2 18:26
 **/
public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化网络监听总线
        NetworkBus.getDefault().init(this);
    }
}