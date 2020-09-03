package demo.kiscode.networkbus.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kiscode.networkbus.NetworkBus;
import com.kiscode.networkbus.annotation.NetSubscribe;
import com.kiscode.networkbus.type.NetType;
import com.kiscode.networkbus.type.NetTypeFilter;
import com.kiscode.networkbus.util.Constant;

import demo.kiscode.networkbus.R;

/**
 * Description: test DialogFragment
 * Author: keno
 * Date : 2020/9/3 22:59
 **/
public class TestDialogFragment extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_test, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        NetworkBus.getDefault().register(this);
    }

    @NetSubscribe(NetTypeFilter.WIFI)
    private void onWifiChange(NetType netType) {
        Log.i(Constant.LOG, "TestDialogFragment onWifiChange:" + netType);
    }

    @NetSubscribe(NetTypeFilter.MOBILE)
    private void onMobileNetChange(NetType netType) {
        Log.i(Constant.LOG, "TestDialogFragment onMobileNetChange: " + netType);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        NetworkBus.getDefault().unregister(this);
    }
}