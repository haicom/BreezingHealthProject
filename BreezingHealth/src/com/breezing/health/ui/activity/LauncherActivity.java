package com.breezing.health.ui.activity;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import com.breezing.health.R;
import com.breezing.health.providers.Breezing.Account;
import com.breezing.health.providers.Breezing.EnergyCost;
import com.breezing.health.tools.IntentAction;
import com.breezing.health.transation.DataReceiver;
import com.breezing.health.transation.DataTaskService;
import com.breezing.health.util.LocalSharedPrefsUtil;


public class LauncherActivity extends BaseActivity {
    private final String TAG = "LauncherActivity";
    private final int MSG_AUTO = 110;
    
    private Handler mHandler;
    private StringBuilder  mStringBuilder = new StringBuilder();
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
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
        
        mHandler = new Handler() {
            
            @Override
            public void dispatchMessage(Message msg) {
                // TODO Auto-generated method stub
                
                final int what = msg.what;
                switch(what) {
                    case MSG_AUTO:
                        String  action = verifyLocalAccountInfo();
                        Intent intent = new Intent(action);
                        startActivity(intent);                        
                        finish();
                        return ;
                }
                
                super.dispatchMessage(msg);
            }
            
        };
        
        mHandler.sendEmptyMessageDelayed(MSG_AUTO, 3 * 1000);
        sendBroadcast(new Intent(DataTaskService.ACTION_IMPORT_DATA,
                null,
                this,
                DataReceiver.class));
    }   
    
    private String verifyLocalAccountInfo() {        
        String action = IntentAction.ACTIVITY_FILLIN_INFORMATION;
        
        int accountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this, LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        String accountPass = null;        
        
        accountPass = LocalSharedPrefsUtil.getSharedPrefsValueString(this, String.valueOf(accountId) );
        
        if ( (accountId != 0) && (accountPass != null) ) {
            int count = queryAccountInfo(accountId, accountPass);
            if (count == 1) {
                action = IntentAction.ACTIVITY_BREEZING_TEST;
                if ( queryEnergyCost(accountId) > 0 ) {
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
    
    /***
     * Through accountName and accountPass query account info
     * @param accountName
     * @param accountPass
     */
    private int queryEnergyCost(final int accountId) {
        int count = 0;
        Log.d(TAG, " queryAccountInfo accountId = " + accountId );
        mStringBuilder.setLength(0);
        mStringBuilder.append(EnergyCost.ACCOUNT_ID + " = ? ");      
        Cursor cursor = null;
        try {            
            cursor = getContentResolver().query(EnergyCost.CONTENT_URI,
                    new String[] {EnergyCost.ACCOUNT_ID},
                    mStringBuilder.toString(),
                    new String[] { String.valueOf(accountId) },
                    null);

            if (cursor != null) {
               count = cursor.getCount();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        
        Log.d(TAG, " queryEnergyCost count = " + count);
        
        return count;
    }   
}
