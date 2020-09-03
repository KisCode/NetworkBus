package demo.kiscode.networkbus.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kiscode.networkbus.NetworkBus;
import com.kiscode.networkbus.annotation.NetSubscribe;
import com.kiscode.networkbus.type.NetType;
import com.kiscode.networkbus.type.NetTypeFilter;
import com.kiscode.networkbus.util.Constant;

import demo.kiscode.networkbus.R;

public class SecondeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seconde);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NetworkBus.getDefault().register(this);
    }

    @NetSubscribe(NetTypeFilter.NONE)
    private void netChange(NetType netType) {
        Log.i(Constant.LOG, "SecondeActivity network off");
    }
}