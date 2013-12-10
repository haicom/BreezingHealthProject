package com.breezing.health.ui.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.breezing.health.R;
import com.breezing.health.entity.AccountEntity;
import com.breezing.health.entity.BreezingTestReport;
import com.breezing.health.providers.Breezing;
import com.breezing.health.providers.Breezing.EnergyCost;
import com.breezing.health.tools.IntentAction;
import com.breezing.health.ui.activity.BreezingTestActivity;
import com.breezing.health.util.BLog;
import com.breezing.health.util.BreezingQueryViews;
import com.breezing.health.util.DateFormatUtil;
import com.breezing.health.util.ExtraName;
import com.breezing.health.util.LocalSharedPrefsUtil;

public class BreezingTestSecondStepFragment extends BaseFragment implements OnClickListener {
	
	public final static int REQUEST_BREEZING_TEST = 0x111;
	
    private static final String TAG = "BreezingTestSecondStepFragment";
    private View mFragmentView;
    private ImageButton mBegin;
    private String mTestInfo;
    private AccountEntity mAccount;
    private float mUnifyUnit;
    private String mCaloricUnit;
    
    private BreezingTestReport mBreezingTestReport;

    public static BreezingTestSecondStepFragment newInstance() {
        BreezingTestSecondStepFragment fragment = new BreezingTestSecondStepFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_breezing_test_second_step, null);
        mBegin = (ImageButton) mFragmentView.findViewById(R.id.begin);
        mBegin.setOnClickListener(this);
      
        return mFragmentView;
    }
    
    @Override
    public void onResume() {       
        super.onResume();
        
        int accountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this.getActivity(),
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        BreezingQueryViews query = new BreezingQueryViews(this.getActivity());
        mAccount = query.queryBaseInfoViews(accountId);
        mUnifyUnit = query.queryUnitObtainData( this.getString(R.string.caloric_type), mAccount.getCaloricUnit() );
    }

    @Override
    public void onClick(View v) {
        if (v == mBegin) {
//            showBluetoothSettingDialog();
        	Intent intent = new Intent(IntentAction.ACTIVITY_TESTING);
        	startActivityForResult(intent, REQUEST_BREEZING_TEST);
            return ;
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == Activity.RESULT_OK) {
    		if (requestCode == REQUEST_BREEZING_TEST) {
    		  mBreezingTestReport = data.getParcelableExtra(ExtraName.EXTRA_DATA);
              int count = queryEnergyCost();
              if (count == 0 ) {
                  mTestInfo = appendEnergyCost();
              } else {
                  mTestInfo = updateEnergyCost();
              }
              
              ((BreezingTestActivity)getActivity()).setTestResult();
              Toast.makeText(getActivity(), mTestInfo, Toast.LENGTH_SHORT).show();
    		 return ;
    		}
    	}
    	super.onActivityResult(requestCode, resultCode, data);
    }
   
    /*** 
     * 查询能量消耗值
     */
    private int queryEnergyCost() {
        int count = 0;

        int accountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(getActivity(),
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        String sortOrder = EnergyCost.ENERGY_COST_DATE + " DESC";
        int  date = DateFormatUtil.simpleDateFormat("yyyyMMdd");
        BLog.d(TAG, " queryEnergyCost accountId = " + accountId);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);      
        stringBuilder.append(EnergyCost.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(EnergyCost.DATE + " = ?  ");
        Cursor cursor = null;
        try {
            cursor = getActivity().getContentResolver().query(EnergyCost.CONTENT_URI,
                    new String[] { EnergyCost._ID },
                    stringBuilder.toString(),
                    new String[] { String.valueOf(accountId), String.valueOf(date) },
                    sortOrder);

            if (cursor != null) {
                count =  cursor.getCount();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        BLog.d(TAG, " queryEnergyCost count = " + count);
        return count;
    }


    private String appendEnergyCost() {
        String result;

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int accountId = LocalSharedPrefsUtil.
                        getSharedPrefsValueInt(this.getActivity(), LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        
        final int currentDate = DateFormatUtil.simpleDateFormat("yyyyMMdd");
        BLog.v("appendEnergyCost currentDate =" + currentDate);
        ops.add(ContentProviderOperation.newInsert(EnergyCost.CONTENT_URI)
                .withValue(EnergyCost.ACCOUNT_ID, accountId)
                .withValue(EnergyCost.METABOLISM, mBreezingTestReport.getMetabolism())
                .withValue(EnergyCost.SPORT, mBreezingTestReport.getSport())
                .withValue(EnergyCost.DIGEST, mBreezingTestReport.getDigest())
                .withValue(EnergyCost.TRAIN, 0)
                .withValue(EnergyCost.ENERGY_COST_DATE, currentDate )
                .build());
        try {
            getActivity().getContentResolver().applyBatch(Breezing.AUTHORITY, ops);
            result =  getActivity().getString(R.string.input_infomation, 
            		mBreezingTestReport.getMetabolism(), mBreezingTestReport.getSport(), mBreezingTestReport.getDigest());                   
        } catch (Exception e) {
            result = getResources().getString(R.string.data_error);
            // Log exception
            BLog.e(TAG, "Exceptoin encoutered while inserting contact: " + e);
        }

        return result;
    }
    
    
    private String updateEnergyCost() {
        String result;

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int accountId = LocalSharedPrefsUtil.
                        getSharedPrefsValueInt(this.getActivity(), LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        int date = DateFormatUtil.simpleDateFormat("yyyyMMdd");
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(EnergyCost.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(EnergyCost.DATE + " = ? ");
        
        final int currentDate = DateFormatUtil.simpleDateFormat("yyyyMMdd");
        ops.add(ContentProviderOperation.newUpdate(EnergyCost.CONTENT_URI)
                .withSelection(stringBuilder.toString(),  
                        new String[] { 
                        String.valueOf( accountId ) , 
                        String.valueOf( date ) } )               
                .withValue(EnergyCost.METABOLISM, mBreezingTestReport.getMetabolism())
                .withValue(EnergyCost.SPORT, mBreezingTestReport.getSport())
                .withValue(EnergyCost.DIGEST, mBreezingTestReport.getDigest())
                .withValue(EnergyCost.TOTAL_ENERGY, mBreezingTestReport.getTotalEnerge())
                .withValue(EnergyCost.ENERGY_COST_DATE, currentDate )
                .build() );      
        try {
            getActivity().getContentResolver().applyBatch(Breezing.AUTHORITY, ops);
            result =  getActivity().getString(R.string.input_infomation, 
            		mBreezingTestReport.getMetabolism(), mBreezingTestReport.getSport(), mBreezingTestReport.getDigest());                   
        } catch (Exception e) {
            result = getResources().getString(R.string.data_error);
            // Log exception
            BLog.e(TAG, "Exceptoin encoutered while inserting contact: " + e);
        }

        return result;
    }

}
