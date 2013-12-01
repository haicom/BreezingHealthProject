package com.breezing.health.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.breezing.health.R;
import com.breezing.health.adapter.BluetoothDeviceAdapter;
import com.breezing.health.ui.activity.TestingActivity;

public class TestingBTScanFragment extends BaseFragment implements OnClickListener {

    private View mFragmentView;
    private ExpandableListView mExpandableListView;
    private BluetoothDeviceAdapter mAdapter;
    private Button mScan;
    
    public static TestingBTScanFragment newInstance() {
        TestingBTScanFragment fragment = new TestingBTScanFragment();
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_testing_btscan, null);
        mScan = (Button) mFragmentView.findViewById(R.id.scan);
        mScan.setOnClickListener(this);
        mExpandableListView = (ExpandableListView) mFragmentView.findViewById(R.id.device_list);
        mExpandableListView.setGroupIndicator(null);
        mAdapter = new BluetoothDeviceAdapter(getActivity());
        initBoundDevices();
        mExpandableListView.setAdapter(mAdapter);
        //展开所有group
        mExpandableListView.expandGroup(0);
        mExpandableListView.expandGroup(1);
        mExpandableListView.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                ((TestingActivity) getActivity()).createBTConnectionFragment(
                        mAdapter.getChild(groupPosition, childPosition));
                return true;
            }
        });
        return mFragmentView;
    }
    
    private void initBoundDevices() {
        mAdapter.addBoundDevices(null);
        mAdapter.addBoundDevices(null);
        mAdapter.addBoundDevices(null);
    }

    @Override
    public void onClick(View v) {
        if (v == mScan) {
            mScan.setText(R.string.refresh);
            scanDevice();
            return ;
        }
    }
    
    private void scanDevice() {
        mAdapter.addDevice(null);
        mAdapter.addDevice(null);
        mAdapter.notifyDataSetChanged();
    }

}
