package com.breezing.health.ui.fragment;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.breezing.health.R;
import com.breezing.health.ui.activity.TestingActivity;

public class TestingBTConnectionFragment extends BaseFragment implements OnClickListener {

    private final int CONNCET_SUCCESS = 100;
    
    private View mFragmentView;
    private Button mStop;
    private BluetoothDevice mDevice;
    private Handler mHandler;
    
    public static TestingBTConnectionFragment newInstance(BluetoothDevice device) {
        TestingBTConnectionFragment fragment = new TestingBTConnectionFragment();
        fragment.mDevice = device;
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_testing_btconnection, null);
        mStop = (Button) mFragmentView.findViewById(R.id.stop);
        mStop.setOnClickListener(this);
        
        mHandler = new Handler(getActivity().getMainLooper()) {
            @Override
            public void dispatchMessage(Message msg) {
                final int what = msg.what;
                switch(what) {
                case CONNCET_SUCCESS:
                    ((TestingActivity) getActivity()).createBreezingFragment();
                    break;
                }
                super.dispatchMessage(msg);
            }
        };
        mHandler.sendEmptyMessageDelayed(CONNCET_SUCCESS, 3 * 1000);
        
        return mFragmentView;
    }

    @Override
    public void onClick(View v) {
        if (v == mStop) {
            ((TestingActivity) getActivity()).createBTScanFragment();
            return ;
        }
    }
    
}
