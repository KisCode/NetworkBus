package demo.kiscode.networkbus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kiscode.networkbus.NetworkBus;
import com.kiscode.networkbus.listener.NetChangeListener;
import com.kiscode.networkbus.listener.SimpleNetChangeListener;
import com.kiscode.networkbus.util.Constant;

import demo.kiscode.networkbus.R;
import demo.kiscode.networkbus.fragment.TestDialogFragment;

public class MainActivity extends AppCompatActivity {

    private NetChangeListener netChangeListener = new SimpleNetChangeListener() {
        @Override
        protected void onChange(boolean isConnected) {
            Log.i(Constant.LOG, "MainActivity onChange isConnected:" + isConnected);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (netChangeListener != null) {
            NetworkBus.getDefault().unregister(netChangeListener);
            netChangeListener = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        NetworkBus.getDefault().register(netChangeListener);
    }

    private void initViews() {
        findViewById(R.id.btnSecond).setOnClickListener(v -> {
            startActivity(new Intent(this, SecondActivity.class));
        });
        findViewById(R.id.btnDialogFragment).setOnClickListener(v -> {
            TestDialogFragment editDialogFragment = new TestDialogFragment();
            editDialogFragment.show(getSupportFragmentManager(), "TestDialogFragment");
        });
    }
}