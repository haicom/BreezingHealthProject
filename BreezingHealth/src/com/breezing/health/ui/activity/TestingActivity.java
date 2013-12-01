package com.breezing.health.ui.activity;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.breezing.health.R;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.ui.fragment.TestingBTConnectionFragment;
import com.breezing.health.ui.fragment.TestingBTScanFragment;
import com.breezing.health.ui.fragment.TestingBreezingFragment;
import com.breezing.health.ui.fragment.TestingPreparativelyFragment;
import com.breezing.health.ui.fragment.TestingResultFragment;

public class TestingActivity extends ActionBarActivity implements OnClickListener {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_testing);
        initValue();
        initView();
        valueToView();
        initListener();
        createPreparativelyFragment();
    }

    private void initValue() {
        
    }

    private void initView() {
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));
    }

    private void valueToView() {
        
    }

    private void initListener() {

    }
    
    private void createPreparativelyFragment() {
        setActionBarTitle(R.string.my_breezing_test);
        TestingPreparativelyFragment fragment = TestingPreparativelyFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }
    
    public void createBTScanFragment() {
        setActionBarTitle(R.string.bluetooth_connection);
        TestingBTScanFragment fragment = TestingBTScanFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }
    
    public void createBTConnectionFragment(BluetoothDevice device) {
        setActionBarTitle(R.string.bluetooth_connection);
        TestingBTConnectionFragment fragment = TestingBTConnectionFragment.newInstance(device);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }
    
    public void createBreezingFragment() {
        setActionBarTitle(R.string.breezing_initialize);
        TestingBreezingFragment fragment = TestingBreezingFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }
    
    public void createResultFragment() {
        setActionBarTitle(R.string.energy_params);
        TestingResultFragment fragment = TestingResultFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }
    
    @Override
    public void onClick(View arg0) {
        
    }

}
