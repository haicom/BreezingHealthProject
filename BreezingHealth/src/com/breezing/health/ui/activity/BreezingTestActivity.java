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
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_breezing_test);
        initValues();
        initViews();
        valueToView();
        initListeners();
    }

    private void initValues() {


    }

    private void initViews() {
        setActionBarTitle(R.string.my_energy_metabolism);
        mViewPager = (CustomViewPager) findViewById(R.id.viewPager);
    }

    private void valueToView() {
        mViewPager.setAdapter(new BreezingTestPagerAdapter( getSupportFragmentManager() ) );
    }

    public void setBluetooth(BluetoothDevice device) {
        mViewPager.setCurrentItem(BreezingTestPagerAdapter.BREEZING_TEST_RESULT);
    }

    public void setTestResult() {
        mViewPager.setCurrentItem(BreezingTestPagerAdapter.BREEZING_TEST_RESULT);
    }

    private void initListeners() {

        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if ( position == 1 ) {
                    mViewPager.setPagingEnabled(false);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
    }
}
