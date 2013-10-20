package com.breezing.health.ui.activity;


import com.breezing.health.R;
import com.breezing.health.providers.Breezing;
import com.breezing.health.providers.Breezing.Account;
import com.breezing.health.providers.Breezing.EnergyCost;
import com.breezing.health.providers.Breezing.WeightChange;
import com.breezing.health.tools.IntentAction;
import com.breezing.health.transation.DataReceiver;
import com.breezing.health.transation.DataTaskService;
import com.breezing.health.util.DateFormatUtil;
import com.breezing.health.util.LocalSharedPrefsUtil;


import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;

import java.util.ArrayList;



public class LauncherActivity extends BaseActivity {
    private final String TAG = "LauncherActivity";
    private final int MSG_AUTO = 110;

    private Handler mHandler;
    private final StringBuilder  mStringBuilder = new StringBuilder();

    private int mMetabolism = 0;
    private int mSport = 0;
    private int mDigest = 0;
    private int mEnergyDate = 0;
    
    private ContentResolver mContentResolver;
    private boolean mDateLoadFinish = false;
    private  boolean mDateWaitFinish = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitApplication();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        mDateLoadFinish = false;
        mDateWaitFinish = false;
        mContentResolver = this.getContentResolver();
        IntentFilter filter = new IntentFilter();
        
        filter.addAction(IntentAction.BROADCAST_TASK_SERVICE);
     //   registerReceiver(mBroadcastReceiver, filter);

        //表示数据导入开始
        LocalSharedPrefsUtil.saveSharedPrefsValueInt(this,
                LocalSharedPrefsUtil.PREFS_LOADING_FINISH, 0);

        sendBroadcast(new Intent(DataTaskService.ACTION_IMPORT_DATA,
                null,
                this,
                DataReceiver.class));

        mHandler = new Handler() {

            @Override
            public void dispatchMessage(Message msg) {
                final int what = msg.what;
                switch(what) {
                    case MSG_AUTO:
                        Log.d(TAG, "onCreate dispatchMessage");
//                        mDateWaitFinish = true;
//
//                        if (mDateLoadFinish) {
//                            String  action = verifyLocalAccountInfo();
//                            Intent intent = new Intent(action);
//                            startActivity(intent);
//                            finish();
//                        }

                        String  action = verifyLocalAccountInfo();
                        Intent intent = new Intent(action);
                        startActivity(intent);
                        finish();

                        return;
                }

                super.dispatchMessage(msg);
            }

        };

        mHandler.sendEmptyMessageDelayed(MSG_AUTO, LAUNCHER_DELAY_MILLIS);

    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
       // unregisterReceiver(mBroadcastReceiver);
    }

    private String verifyLocalAccountInfo() {

        if (LocalSharedPrefsUtil.isFirstTime(LauncherActivity.this)) {
            return IntentAction.ACTIVITY_HELPER;
        }

        String action = null;
        
        int userLogin = LocalSharedPrefsUtil.getSharedPrefsValueInt(this,
                LocalSharedPrefsUtil.PREFS_USER_LOGIN);
        
        if (userLogin == LocalSharedPrefsUtil.USER_NEEDFUL_LOGIN) {
            action = IntentAction.ACTIVITY_LOGIN;
        } else {
            action = IntentAction.ACTIVITY_FILLIN_INFORMATION;
        }
        
        appenAllEnergyCost();
        
        int accountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this,
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        String accountPass = null;

        accountPass = LocalSharedPrefsUtil.getSharedPrefsValueString(this, String.valueOf(accountId) );

        if ( (accountId != 0) && (accountPass != null) ) {
            int count = queryAccountInfo(accountId, accountPass);
            if (count == 1) {
                action = IntentAction.ACTIVITY_BREEZING_TEST;
                if ( queryEnergyCost(accountId) ) {                    
                    action = IntentAction.ACTIVITY_MAIN;
                }
            }
        }

        return action;
    }


    /***
     * Through accountName and accountPass query account info
     * @param accountName
     * @param accountPass
     */
    private int queryAccountInfo(final int accountId, final String accountPass) {
        int count = 0;
        Log.d(TAG, " queryAccountInfo accountId = " + accountId + " accountPass = " + accountPass);
        mStringBuilder.setLength(0);
        mStringBuilder.append(Account.ACCOUNT_ID + " = " + accountId + " AND ");
        mStringBuilder.append(Account.ACCOUNT_PASSWORD + "= ?");
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(Account.CONTENT_URI,
                    new String[] {Account.ACCOUNT_ID},
                    mStringBuilder.toString(),
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

        Log.d(TAG, " queryAccountInfo count = " + count);

        return count;
    }

    private static final String[] PROJECTION_ENERGY_COST = new String[] {
        EnergyCost.METABOLISM,      // 1
        EnergyCost.SPORT,    // 2
        EnergyCost.DIGEST,   //3
        EnergyCost.ENERGY_COST_DATE   //4
    };

    private final static int ENERGY_COST_METABOLISM_INDEX = 0;
    private final static int ENERGY_COST_SPORT_INDEX = 1;
    private final static int ENERGY_COST_DIGEST_INDEX = 2;
    private final static int ENERGY_COST_ENERGY_COST_DATE_INDEX = 3;

    /***
     * Through accountName and accountPass query account info
     * @param accountName
     * @param accountPass
     */
    private boolean queryEnergyCost(final int accountId) {
        boolean result = false;
        Log.d(TAG, " queryAccountInfo accountId = " + accountId );
        String sortOrder = EnergyCost.ENERGY_COST_DATE + " DESC";

        mStringBuilder.setLength(0);
        mStringBuilder.append(EnergyCost.ACCOUNT_ID + " = ? ");
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(EnergyCost.CONTENT_URI,
                    PROJECTION_ENERGY_COST,
                    mStringBuilder.toString(),
                    new String[] { String.valueOf(accountId) },
                    sortOrder);

            if (cursor != null) {
                if ( cursor.getCount() > 0 ) {
                    cursor.moveToPosition(0);
                    mMetabolism = cursor.getInt(ENERGY_COST_METABOLISM_INDEX);
                    mSport = cursor.getInt(ENERGY_COST_SPORT_INDEX);
                    mDigest = cursor.getInt(ENERGY_COST_DIGEST_INDEX);
                    mEnergyDate = cursor.getInt(ENERGY_COST_ENERGY_COST_DATE_INDEX);
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




    /***
     * 查询每个帐户每天能量消耗值的数量
     */
    private int queryEnergyCostEveryDay(final int accountId) {
        int count = 0;
        String sortOrder = EnergyCost.ENERGY_COST_DATE + " DESC";

        int date = DateFormatUtil.simpleDateFormat("yyyyMMdd");

        Log.d(TAG, " queryEnergyCostEveryDay ");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(EnergyCost.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(EnergyCost.DATE + " = ?  ");
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(EnergyCost.CONTENT_URI,
                    PROJECTION_ENERGY_COST,
                    stringBuilder.toString(),
                    new String[] { String.valueOf(accountId),  String.valueOf(date)},
                    sortOrder);

            if (cursor != null) {
                count = cursor.getCount();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.d(TAG, " queryEnergyCostEveryDay count = " + count );
        return count;
    }
    
    private static final String[] PROJECTION_WEIGHT_CHANGE = new String[] {
        WeightChange.ACCOUNT_ID,   // 0
        WeightChange.WEIGHT,   // 1
        WeightChange.DATE       // 2
    };


    private static final int ACCOUNT_ID_COLUMN_INDEX = 0;
    private static final int WEIGHT_COLUMN_INDEX = 1;
    private static final int DATE_COLUMN_INDEX = 2;
    
   /***
    * 因为every_weight 数据库的修改，需要填补EVERY_WEIGHT 这个字段的数值
    * @param ops
    */
    private void updateExpectedWeightChange(ArrayList<ContentProviderOperation> ops) {
        int   accountId = 0;
        float weight = 0;
        int  day = 0;
        
        StringBuilder stringBuilder = new StringBuilder();        
        stringBuilder.setLength(0);
        stringBuilder.append(WeightChange.EVERY_WEIGHT + " IS NULL ");

        String sortOrder = WeightChange.DATE + " ASC ";

        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(WeightChange.CONTENT_URI,
                    PROJECTION_WEIGHT_CHANGE,
                    stringBuilder.toString(),
                    null,
                    sortOrder );

            if (cursor == null) {
                Log.d(TAG, " fillInTotalEnergyInWeek cursor = " + cursor);
            }

            cursor.moveToPosition(-1);
            while (cursor.moveToNext() ) {
                accountId = cursor.getInt(ACCOUNT_ID_COLUMN_INDEX);
                weight = cursor.getFloat(WEIGHT_COLUMN_INDEX);
                day = cursor.getInt(DATE_COLUMN_INDEX);  
                
                stringBuilder.setLength(0);
                stringBuilder.append(WeightChange.ACCOUNT_ID + " = ? AND ");
                stringBuilder.append(WeightChange.DATE + " = ? ");
             
                ops.add(ContentProviderOperation.newUpdate(WeightChange.CONTENT_URI)
                        .withSelection(stringBuilder.toString(),  
                                new String[] { 
                                String.valueOf( accountId ) , 
                                String.valueOf( day ) } )               
                        .withValue(WeightChange.EVERY_WEIGHT, weight)                   
                        .build());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        
       
    }

    /***
     * 添加每个帐户每天能量消耗值数量
     */
    private void appendEnergyCostById(ArrayList<ContentProviderOperation> ops, int accountId) {
        int metabolism = 0;
        int sport = 0;
        int digest = 0;
        int energyDate = 0;
        
        Log.d(TAG, " appendEnergyCostById mMetabolism " + mMetabolism + " mSport = " + mSport + " mDigest = " + mDigest
                + " mEnergyDate = " + mEnergyDate );
        String sortOrder = EnergyCost.ENERGY_COST_DATE + " DESC";

        mStringBuilder.setLength(0);
        mStringBuilder.append(EnergyCost.ACCOUNT_ID + " = ? ");
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(EnergyCost.CONTENT_URI,
                    PROJECTION_ENERGY_COST,
                    mStringBuilder.toString(),
                    new String[] { String.valueOf(accountId) },
                    sortOrder);

            if (cursor != null) {
                if ( cursor.getCount() > 0 ) {
                    cursor.moveToPosition(0);
                    metabolism = cursor.getInt(ENERGY_COST_METABOLISM_INDEX);
                    sport = cursor.getInt(ENERGY_COST_SPORT_INDEX);
                    digest = cursor.getInt(ENERGY_COST_DIGEST_INDEX);
                    energyDate = cursor.getInt(ENERGY_COST_ENERGY_COST_DATE_INDEX);                    
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
       
        ops.add( ContentProviderOperation.newInsert(EnergyCost.CONTENT_URI)
                .withValue(EnergyCost.ACCOUNT_ID, accountId)
                .withValue(EnergyCost.METABOLISM, metabolism)
                .withValue(EnergyCost.SPORT, sport)
                .withValue(EnergyCost.DIGEST, digest)
                .withValue(EnergyCost.TRAIN, 0)
                .withValue(EnergyCost.ENERGY_COST_DATE, energyDate)
                .build() );
       
    }

//    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//
//            if (action.equals(IntentAction.BROADCAST_TASK_SERVICE)) {
//                mDateLoadFinish = true;
//
//                if (mDateWaitFinish) {
//                    String  verifyAction = verifyLocalAccountInfo();
//                    Intent  verifyIntent = new Intent(verifyAction);
//                    startActivity(verifyIntent);
//                    finish();
//                }
//            }
//
//        }
//    };
    /**
     * 考虑多帐户情况，每个帐户每天都需要添加一条新的信息，用于记录吹气的时间
     * @return
     */
    private String appenAllEnergyCost() {
        String result = null;
        ArrayList<Integer> accountList;
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        accountList = queryAllAcctounInfo();
        
        for (int accountId: accountList) {
            int countEnergyDay = queryEnergyCostEveryDay(accountId);
            if ( countEnergyDay == 0 ) {
                appendEnergyCostById(ops, accountId);
            }
        }
        
        updateExpectedWeightChange(ops);
        try {
            getContentResolver().applyBatch(Breezing.AUTHORITY, ops);
        } catch (Exception e) {
            result = getResources().getString(R.string.data_error);
            // Log exception
            Log.e(TAG, "Exceptoin encoutered while inserting contact: " + e);
        }

        return result;
    }
    
    /***
     * 查询所有帐户信息
     * @return
     */
    private ArrayList<Integer> queryAllAcctounInfo() {
        ArrayList<Integer> accountList = new ArrayList<Integer>();
        StringBuilder where = new StringBuilder();
        where.append(Account.ACCOUNT_DELETED + " =  ? ");

        String sortOrder = Account.ACCOUNT_NAME + " ASC ";

        Cursor cursor = this.getContentResolver().query(
                Account.CONTENT_URI,
                new String[] { Account.ACCOUNT_ID },
                where.toString(),
                new String[] { String.valueOf(0) },
                sortOrder);
        try {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext() ) {
                int  accountId = cursor.getInt(0); 
                accountList.add(accountId);
            }
        } finally {
            cursor.close();
        }
        
        return accountList;
    }

    private static final int LAUNCHER_DELAY_MILLIS = 3000;
}
