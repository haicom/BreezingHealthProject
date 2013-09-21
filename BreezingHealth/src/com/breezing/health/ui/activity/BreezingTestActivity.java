package com.breezing.health.ui.activity;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.TextView;

import com.breezing.health.R;
import com.breezing.health.adapter.BreezingTestPagerAdapter;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.ui.fragment.BreezingTestResultFragment;
import com.breezing.health.widget.CustomViewPager;

public class BreezingTestActivity extends ActionBarActivity {

    private CustomViewPager mViewPager;
    private BreezingTestPagerAdapter mBreezingTestPagerAdapter;
    
    private View mStepLayout;
    private TextView mStepOne;
    private TextView mStepTwo;
    
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
        addRightActionItem(new ActionItem(ActionItem.ACTION_BREEZING_TEST_HISTORY));
        mViewPager = (CustomViewPager) findViewById(R.id.viewPager);
        
        mStepLayout = findViewById(R.id.step_layout);
        mStepOne = (TextView) findViewById(R.id.step_one);
        mStepOne.setSelected(true);
        mStepTwo = (TextView) findViewById(R.id.step_two);
        mStepTwo.setSelected(true);
    }

    private void valueToView() {
        mBreezingTestPagerAdapter = new BreezingTestPagerAdapter( getSupportFragmentManager() );
        mViewPager.setAdapter(mBreezingTestPagerAdapter);
    }

    public void setBluetooth(BluetoothDevice device) {
        mViewPager.setCurrentItem(BreezingTestPagerAdapter.BREEZING_TEST_RESULT);
    }  
    

    public void setTestResult() {
        mStepLayout.setVisibility(View.GONE);
        mViewPager.setCurrentItem(BreezingTestPagerAdapter.BREEZING_TEST_RESULT);
        BreezingTestResultFragment testResultFragment = (BreezingTestResultFragment)mBreezingTestPagerAdapter.
                getItem(BreezingTestPagerAdapter.BREEZING_TEST_RESULT);
        testResultFragment.showBreezingResult();
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
