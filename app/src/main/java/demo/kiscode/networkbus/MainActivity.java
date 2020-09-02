package demo.kiscode.networkbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kiscode.networkbus.NetworkBus;
import com.kiscode.networkbus.listener.NetChangeListener;
import com.kiscode.networkbus.type.NetType;
import com.kiscode.networkbus.util.Constant;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetworkBus.getDefault().setOnChangeListener(new NetChangeListener() {
            @Override
            public void onChange(NetType netType) {
                Log.i(Constant.LOG, "MainActivity onChange :" + netType);
            }
        });
    }
}