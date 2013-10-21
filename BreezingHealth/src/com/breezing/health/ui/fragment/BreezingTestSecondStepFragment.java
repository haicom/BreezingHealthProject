package com.breezing.health.ui.fragment;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.breezing.health.R;
import com.breezing.health.entity.AccountEntity;
import com.breezing.health.providers.Breezing;
import com.breezing.health.providers.Breezing.EnergyCost;
import com.breezing.health.ui.activity.BreezingTestActivity;
import com.breezing.health.ui.activity.MainActivity;
import com.breezing.health.util.BreezingQueryViews;
import com.breezing.health.util.DateFormatUtil;
import com.breezing.health.util.LocalSharedPrefsUtil;

public class BreezingTestSecondStepFragment extends BaseFragment implements OnClickListener {
    private static final String TAG = "BreezingTestSecondStepFragment";
    private View mFragmentView;
    private ImageButton mBegin;
    private String mTestInfo;
    private AccountEntity mAccount;
    private float mUnifyUnit;
    private String mCaloricUnit;

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

    private void showBluetoothSettingDialog() {
        BreezingTestBTSettingFragment setting = (BreezingTestBTSettingFragment) getActivity().getSupportFragmentManager().findFragmentByTag("bluetoothSetting");
        if (setting != null) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(setting);
        }
        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null);

        setting = BreezingTestBTSettingFragment.newInstance();
        setting.show(getActivity().getSupportFragmentManager(), "bluetoothSetting");
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
        Log.d(TAG, " queryEnergyCost accountId = " + accountId);
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

        return count;
    }


    private String appendEnergyCost() {
        String result;

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int accountId = LocalSharedPrefsUtil.
                        getSharedPrefsValueInt(this.getActivity(), LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        ops.add(ContentProviderOperation.newInsert(EnergyCost.CONTENT_URI)
                .withValue(EnergyCost.ACCOUNT_ID, accountId)
                .withValue(EnergyCost.METABOLISM, ENERGY_COST_METABOLISM)
                .withValue(EnergyCost.SPORT, ENERGY_COST_SPORT)
                .withValue(EnergyCost.DIGEST, ENERGY_COST_DIGEST)
                .withValue(EnergyCost.TRAIN, 0)
                .withValue(EnergyCost.ENERGY_COST_DATE, DateFormatUtil.simpleDateFormat("yyyyMMdd") )
                .build());
        try {
            getActivity().getContentResolver().applyBatch(Breezing.AUTHORITY, ops);
            result =  getActivity().getString(R.string.input_infomation, 
                    ENERGY_COST_METABOLISM, ENERGY_COST_METABOLISM, ENERGY_COST_DIGEST);                   
        } catch (Exception e) {
            result = getResources().getString(R.string.data_error);
            // Log exception
            Log.e(TAG, "Exceptoin encoutered while inserting contact: " + e);
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
        
        ops.add(ContentProviderOperation.newUpdate(EnergyCost.CONTENT_URI)
                .withSelection(stringBuilder.toString(),  
                        new String[] { 
                        String.valueOf( accountId ) , 
                        String.valueOf( date ) } )               
                .withValue(EnergyCost.METABOLISM, ENERGY_COST_METABOLISM)                     
                .build() );      
        try {
            getActivity().getContentResolver().applyBatch(Breezing.AUTHORITY, ops);
            result =  getActivity().getString(R.string.input_infomation, 
                    ENERGY_COST_METABOLISM, ENERGY_COST_METABOLISM, ENERGY_COST_DIGEST);                   
        } catch (Exception e) {
            result = getResources().getString(R.string.data_error);
            // Log exception
            Log.e(TAG, "Exceptoin encoutered while inserting contact: " + e);
        }

        return result;
    }

    private static final int ENERGY_COST_METABOLISM = 3500;
    private static final int ENERGY_COST_SPORT = 160;
    private static final int ENERGY_COST_DIGEST = ENERGY_COST_METABOLISM / 20;
}
