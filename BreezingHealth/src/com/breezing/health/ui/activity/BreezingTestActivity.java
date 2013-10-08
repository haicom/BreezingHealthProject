package com.breezing.health.ui.activity;

import android.bluetooth.BluetoothDevice;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.breezing.health.R;
import com.breezing.health.adapter.BreezingTestPagerAdapter;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.providers.Breezing.Account;
import com.breezing.health.providers.Breezing.EnergyCost;
import com.breezing.health.ui.fragment.BreezingTestResultFragment;
import com.breezing.health.util.LocalSharedPrefsUtil;
import com.breezing.health.widget.CustomViewPager;

public class BreezingTestActivity extends ActionBarActivity {

    private final String TAG = getClass().getName();
    
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

        int accountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this,
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        String accountPass = null;
        accountPass = LocalSharedPrefsUtil.getSharedPrefsValueString(this, String.valueOf(accountId) );
        if ( (accountId != 0) && (accountPass != null) ) {
            int count = queryAccountInfo(accountId, accountPass);
            if (count == 1) {
                if ( queryEnergyCost(accountId) ) {
                    addRightActionItem(new ActionItem(ActionItem.ACTION_BREEZING_TEST_HISTORY));
                }
            }
        }
        
        mViewPager = (CustomViewPager) findViewById(R.id.viewPager);
        
        mStepLayout = findViewById(R.id.step_layout);
        mStepOne = (TextView) findViewById(R.id.step_one);
        mStepOne.setSelected(true);
        mStepTwo = (TextView) findViewById(R.id.step_two);
        mStepTwo.setSelected(true);
    }
    
    /***
     * Through accountName and accountPass query account info
     * @param accountName
     * @param accountPass
     */
    private int queryAccountInfo(final int accountId, final String accountPass) {
        int count = 0;
        Log.d(TAG, " queryAccountInfo accountId = " + accountId + " accountPass = " + accountPass);
        StringBuilder  stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(Account.ACCOUNT_ID + " = " + accountId + " AND ");
        stringBuilder.append(Account.ACCOUNT_PASSWORD + "= ?");
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(Account.CONTENT_URI,
                    new String[] {Account.ACCOUNT_ID},
                    stringBuilder.toString(),
                    new String[] { accountPass},
                    null);

            if (cursor != null) {
               count = cursor.getCount();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return count;
    }
    
    /***
     * Through accountName and accountPass query account info
     * @param accountName
     * @param accountPass
     */
    private boolean queryEnergyCost(final int accountId) {
        boolean result = false;
        Log.d(TAG, " queryAccountInfo accountId = " + accountId );
        String sortOrder = EnergyCost.ENERGY_COST_DATE + " DESC";

        StringBuilder  stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(EnergyCost.ACCOUNT_ID + " = ? ");
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(EnergyCost.CONTENT_URI,
                    new String[] {EnergyCost.METABOLISM},
                    stringBuilder.toString(),
                    new String[] { String.valueOf(accountId) },
                    sortOrder);

            if (cursor != null) {
                if ( cursor.getCount() > 0 ) {
                    cursor.moveToPosition(0);
                    result = true;
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.d(TAG, " queryEnergyCost result = " + result);

        return result;
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
