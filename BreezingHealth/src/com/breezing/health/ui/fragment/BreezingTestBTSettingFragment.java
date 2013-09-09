package com.breezing.health.ui.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.breezing.health.R;
import com.breezing.health.adapter.BluetoothDeviceAdapter;
import com.breezing.health.tools.IntentAction;
import com.breezing.health.ui.activity.BreezingTestActivity;

public class BreezingTestBTSettingFragment extends BaseDialogFragment implements OnClickListener {
    
    private View mFragmentView;
    private ListView mListView;
    private ProgressBar mProgressBar;
    private Button mRefresh;
    private TextView mTitle;
    private BluetoothDeviceAdapter mAdapter;
    
    public static BreezingTestBTSettingFragment newInstance() {
        BreezingTestBTSettingFragment fragment = new BreezingTestBTSettingFragment();
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {       
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {       
        mFragmentView = inflater.inflate(R.layout.fragment_breezing_test_bt_setting, null);
        mListView = (ListView) mFragmentView.findViewById(R.id.list);
        mProgressBar = (ProgressBar) mFragmentView.findViewById(R.id.progressBar);
        mRefresh = (Button) mFragmentView.findViewById(R.id.refresh);
        mTitle = (TextView) mFragmentView.findViewById(R.id.bluetoothTitle);
    
        mRefresh.setOnClickListener(this);
        
        mAdapter = new BluetoothDeviceAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {               
                ((BreezingTestActivity)getActivity()).setBluetooth(mAdapter.getItem(arg2));
                dismiss();
            }
        });
        
        getDialog().getWindow().setBackgroundDrawable(new
                ColorDrawable(Color.TRANSPARENT));
        
        registerSearchReceiver();
        doDiscovery();
        
        return mFragmentView;
    }
    
    private void registerSearchReceiver() {        
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        getActivity().registerReceiver(mSearchBroadcast, intentFilter);
    }

    @Override
    public void onClick(View v) {        
        if (v == mRefresh) {            
            doDiscovery();
            return ;
        }
    }
    
    public boolean doDiscovery() {
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter != null && btAdapter.isEnabled()) {
            if (!btAdapter.isDiscovering()) {
                btAdapter.startDiscovery();
                mProgressBar.setVisibility(View.VISIBLE);
                mTitle.setText(R.string.on_discovery_bluetooth);
                return true;
            }
        } else {
            btAdapter.enable();
            mTitle.setText(R.string.on_opening_bluetooth);
        }
        
        return false;
    }
    
    private void stopDiscovery() {
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter != null && btAdapter.isDiscovering()) {
            btAdapter.cancelDiscovery();
        }
    }
    
    @Override
    public void onDestroy() {       
        getActivity().unregisterReceiver(mSearchBroadcast);
        stopDiscovery();
        super.onDestroy();
    }
    
    private BroadcastReceiver mSearchBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) {
                    String address = device.getAddress();
                    if (address.length() != 0) {
                        boolean isExist = false;
                        final int size = mAdapter.getCount();
                        for (int i = 0; i < size; i++) {
                            if (device.getAddress().equals(mAdapter.getItem(i).getAddress())) {
                                isExist = true;
                                break;
                            }
                        }
                        
                        if (!isExist) {
                            mAdapter.addItem(device);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                mProgressBar.setVisibility(View.GONE);
                mTitle.setText(R.string.bluetooth_device);
            }
            
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                if (state == BluetoothAdapter.STATE_ON) {
                    doDiscovery();
                } else if (state == BluetoothAdapter.STATE_OFF) {
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        }
    };
}
