package com.breezing.health.ui.activity;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.breezing.health.R;
import com.breezing.health.adapter.BreezingTestPagerAdapter;
import com.breezing.health.widget.CustomViewPager;

public class BreezingTestActivity extends ActionBarActivity {
    
    private CustomViewPager mViewPager;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_breezing_test);
        initValues();
        initViews();
        valueToView();
        initListeners();
    }

    private void initValues() {
        // TODO Auto-generated method stub
        
    }

    private void initViews() {
        // TODO Auto-generated method stub
        setActionBarTitle(R.string.my_energy_metabolism);
        mViewPager = (CustomViewPager) findViewById(R.id.viewPager);
    }

    private void valueToView() {
        // TODO Auto-generated method stub
        mViewPager.setAdapter(new BreezingTestPagerAdapter(getSupportFragmentManager()));
    }
    
    public void setBluetooth(BluetoothDevice device) {
        mViewPager.setCurrentItem(2);
    }

    private void initListeners() {
        // TODO Auto-generated method stub
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            
            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                if (arg0 == 1) {
                    mViewPager.setPagingEnabled(false);
                }
            }
            
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
                
            }
        });
    }
}
