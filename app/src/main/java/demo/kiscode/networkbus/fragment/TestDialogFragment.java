package demo.kiscode.networkbus.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
//        NetworkBus.getDefault().register(this);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
//        NetworkBus.getDefault().unregister(this);
    }
}