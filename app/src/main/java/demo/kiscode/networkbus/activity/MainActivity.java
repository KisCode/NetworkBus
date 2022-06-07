package demo.kiscode.networkbus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kiscode.networkbus.NetworkBus;
import com.kiscode.networkbus.annotation.NetSubscribe;
import com.kiscode.networkbus.type.NetType;
import com.kiscode.networkbus.util.Constant;

import demo.kiscode.networkbus.R;
import demo.kiscode.networkbus.fragment.TestDialogFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
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

    private void initViews() {
        findViewById(R.id.btnSecond).setOnClickListener(v -> {
            startActivity(new Intent(this, SecondActivity.class));
        });
        findViewById(R.id.btnDialogFragment).setOnClickListener(v -> {
            TestDialogFragment editDialogFragment = new TestDialogFragment();
            editDialogFragment.show(getSupportFragmentManager(), "TestDialogFragment");
        });
    }

    @NetSubscribe
    private void onNetChange(NetType netType) {
        Log.i(Constant.LOG, "MainActivity onChange :" + netType);
    }
}