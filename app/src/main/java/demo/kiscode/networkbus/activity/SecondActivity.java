package demo.kiscode.networkbus.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import demo.kiscode.networkbus.R;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seconde);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        NetworkBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        NetworkBus.getDefault().register(this);
    }

}