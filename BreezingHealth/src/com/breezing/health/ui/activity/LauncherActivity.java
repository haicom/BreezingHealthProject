package com.breezing.health.ui.activity;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import com.breezing.health.R;
import com.breezing.health.providers.Breezing.Account;
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
                        boolean result = verifyLocalAccountInfo();
                        if (!result) {
                            Intent intent = new Intent(IntentAction.ACTIVITY_FILLIN_INFORMATION);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(IntentAction.ACTIVITY_MAIN);
                            startActivity(intent);
                        }
                        
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
    
    private boolean verifyLocalAccountInfo() {
        String accountName = LocalSharedPrefsUtil.getSharedPrefsValue(this, LocalSharedPrefsUtil.PREFS_ACCOUNT_NAME);
        String accountPass = null;
        
        if (accountName == null) {
            return false;
        } else {
            accountPass = LocalSharedPrefsUtil.getSharedPrefsValue(this, accountName);
            if (accountPass == null) {
                return false;
            }
        }
        
        if ( (accountName != null) && (accountPass != null) ) {
            int count = queryAccountInfo(accountName, accountPass);
            if (count == 1) {
                return true;
            }
        }
        return false;
    }
    
    
    /***
     * Through accountName and accountPass query account info
     * @param accountName
     * @param accountPass
     */
    private int queryAccountInfo(final String accountName, final String accountPass) {
        int count = 0;
        mStringBuilder.setLength(0);
        mStringBuilder.append(Account.ACCOUNT_NAME + " = ? AND ");
        mStringBuilder.append(Account.ACCOUNT_PASSWORD + "= ?");
        Cursor cursor = null;
        try {            
            cursor = getContentResolver().query(Account.CONTENT_URI,
                    new String[] {Account.ACCOUNT_ID},
                    mStringBuilder.toString(),
                    new String[] {accountName, accountPass},
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
}
