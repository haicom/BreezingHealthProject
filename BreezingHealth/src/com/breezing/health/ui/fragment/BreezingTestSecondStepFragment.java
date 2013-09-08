package com.breezing.health.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.breezing.health.R;
import com.breezing.health.tools.IntentAction;

public class BreezingTestSecondStepFragment extends BaseFragment implements OnClickListener {
    
    private View mFragmentView;
    private Button mBegin;
    
    public static BreezingTestSecondStepFragment newInstance() {
        BreezingTestSecondStepFragment fragment = new BreezingTestSecondStepFragment();
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mFragmentView = inflater.inflate(R.layout.fragment_breezing_test_second_step, null);
        mBegin = (Button) mFragmentView.findViewById(R.id.begin);
        
        mBegin.setOnClickListener(this);
        
        return mFragmentView;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == mBegin) {
//            showBluetoothSettingDialog();
            startActivity(new Intent(IntentAction.ACTIVITY_MAIN));
            getActivity().finish();
            return ;
        }
    }
    
    private void showBluetoothSettingDialog() {
        BreezingTestBTSettingFragment setting = (BreezingTestBTSettingFragment) getActivity().getSupportFragmentManager().findFragmentByTag("bluetoothSetting");
        if (setting != null) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(setting);
        }
        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null);
        
        setting = BreezingTestBTSettingFragment.newInstance();
        setting.show(getActivity().getSupportFragmentManager(), "bluetoothSetting");
    }
}
