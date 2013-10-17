package com.breezing.health.ui.activity;

import android.bluetooth.BluetoothDevice;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.breezing.health.R;
import com.breezing.health.adapter.BreezingTestPagerAdapter;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.providers.Breezing.Account;
import com.breezing.health.providers.Breezing.EnergyCost;
import com.breezing.health.tools.Tools;
import com.breezing.health.ui.fragment.BreezingTestResultFragment;
import com.breezing.health.util.ExtraName;
import com.breezing.health.util.LocalSharedPrefsUtil;
import com.breezing.health.widget.CustomViewPager;

public class BreezingTestActivity extends ActionBarActivity {

    private final String TAG = getClass().getName();
    
    private CustomViewPager mViewPager;
    private BreezingTestPagerAdapter mBreezingTestPagerAdapter;
    
    private View mStepLayout;
    private TextView mStepOne;
    private TextView mStepTwo;
    
    private View mLastTestLayout;
    private TextView mLastTestNotice;
    private View mEnergyVane;
    
    private int mEnergyCost;
    private int mLastTestDate;
    
    private boolean mIsUpdate;
    private int mMetabolism;
    
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
        mIsUpdate = getIntent().getBooleanExtra(ExtraName.EXTRA_DATE, false);
    }

    private void initViews() {
        setActionBarTitle(R.string.my_energy_metabolism);

        if (!mIsUpdate) {
            int accountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this,
                    LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
            String accountPass = null;
            accountPass = LocalSharedPrefsUtil.getSharedPrefsValueString(this, String.valueOf(accountId) );
            if ( (accountId != 0) && (accountPass != null) ) {
                int count = queryAccountInfo(accountId, accountPass);
                if (count == 1) {
                    if ( queryEnergyCost(accountId) ) {
                        
                    }
                }
            }
            
            mStepLayout = findViewById(R.id.step_layout);
            mStepLayout.setVisibility(View.VISIBLE);
            mStepOne = (TextView) findViewById(R.id.step_one);
            mStepOne.setSelected(true);
            mStepTwo = (TextView) findViewById(R.id.step_two);
            mStepTwo.setSelected(true);
            
        } else {
            addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));
            addRightActionItem(new ActionItem(ActionItem.ACTION_BREEZING_TEST_HISTORY));
            
            mLastTestLayout = findViewById(R.id.last_test_result);
            mLastTestLayout.setVisibility(View.VISIBLE);
            mLastTestNotice = (TextView) findViewById(R.id.energyResult);
            mEnergyVane = findViewById(R.id.energy_vane);
            
            int accountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this,
                    LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
            final boolean isTested = queryEnergyCost(accountId);
            if (isTested) {
                Tools.refreshVane(mMetabolism, mEnergyVane);
                
                String year = String.valueOf(mLastTestDate).subSequence(0, ENERGY_COST_YEAR).toString();
                String month =  String.valueOf(mLastTestDate).subSequence(ENERGY_COST_YEAR ,
                        ENERGY_COST_YEAR + ENERGY_COST_MONTH ).toString();
                String day = String.valueOf(mLastTestDate).subSequence( ENERGY_COST_YEAR + ENERGY_COST_MONTH  ,
                        String.valueOf(mLastTestDate).length() ).toString();
                final String result = getString(R.string.last_breezing_test_result,
                        year, month , day);
                SpannableString spannable = new SpannableString(result);
                spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)),
                        1,
                        14,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new RelativeSizeSpan(1.1f),
                        1,
                        14,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mLastTestNotice.setText(spannable);
            }
            
            
            
        }
        
        mViewPager = (CustomViewPager) findViewById(R.id.viewPager);
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
    
    private final static int ENERGY_COST_METABOLISM_ENERGY = 0;
    private final static int ENERGY_COST_ENERGY_COST_DATE = 1;
    
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
                    new String[] {EnergyCost.METABOLISM, EnergyCost.ENERGY_COST_DATE},
                    stringBuilder.toString(),
                    new String[] { String.valueOf(accountId) },
                    sortOrder);

            if (cursor != null) {
                if ( cursor.getCount() > 0 ) {
                    cursor.moveToPosition(0);
                    mMetabolism = cursor.getInt(ENERGY_COST_METABOLISM_ENERGY);
                    mLastTestDate = cursor.getInt(ENERGY_COST_ENERGY_COST_DATE);
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
        if (mIsUpdate) {
            mViewPager.setCurrentItem(BreezingTestPagerAdapter.BREEZING_TEST_SECOND_STEP);
        }
    }

    public void setBluetooth(BluetoothDevice device) {
        mViewPager.setCurrentItem(BreezingTestPagerAdapter.BREEZING_TEST_RESULT);
    }  
    

    public void setTestResult() {
        if (!mIsUpdate) {
            mStepLayout.setVisibility(View.GONE);
        } else {
            mLastTestLayout.setVisibility(View.GONE);
        }
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
    
    private static final int ENERGY_COST_YEAR = 4;
    private static final int ENERGY_COST_MONTH = 2;
    private static final int ENERGY_COST_DAY = 2;
}
