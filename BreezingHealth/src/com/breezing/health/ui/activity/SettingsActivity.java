package com.breezing.health.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import br.com.dina.ui.model.GroupIndex;
import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.OnItemClickListener;

import com.breezing.health.R;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.providers.Breezing.Account;
import com.breezing.health.tools.IntentAction;
import com.breezing.health.util.BLog;
import com.breezing.health.util.LocalSharedPrefsUtil;

public class SettingsActivity extends ActionBarActivity {
    
    private static final String TAG = "SettingsActivity";
    
    private UITableView mTableView;
    
    private StringBuilder mStringBuilder; 
    
    private int mAccountId;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_settings);
        initValues();
        initViews();
        valueToView();
        initListeners();
    }

    private void initValues() {
        mAccountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this,
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        mStringBuilder = new StringBuilder();
    }

    private void initViews() {
        setActionBarTitle(R.string.settings);
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));
        
        mTableView = (UITableView) findViewById(R.id.tableView);
       
        
    }
    
    @Override
    protected void onResume() {        
        super.onResume();
       
    }

    private void valueToView() {
        createList();
        mTableView.commit();
    }

    private void initListeners() {
        mTableView.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onClick(View view, ViewGroup contentView, String action, GroupIndex index) {
                if (action != null) {
                    Intent intent = new Intent(action);
                    startActivity(intent);
                    return; 
                }
            }
        });
    }
    
    /**
     * create UITableView items
     */
    private void createList() {
        
        String accountName = queryAccountName(); 
        
        mTableView.addBasicItem(R.drawable.user_image, accountName, null, IntentAction.ACTIVITY_ACCOUNT_DETAIL);
        mTableView.addBasicItem(getString(R.string.modify_password), getString(R.string.modify_password_summary), IntentAction.ACTIVITY_MODIFY_PASSWORD);
        mTableView.addHeaderView("");
        mTableView.addBasicItem(getString(R.string.account_management), getString(R.string.account_management_summary), IntentAction.ACTIVITY_ACCOUNT_MANAGEMENT);
        mTableView.addHeaderView("");
        mTableView.addBasicItem(getString(R.string.unit_settings), getString(R.string.unit_settings_summary), IntentAction.ACTIVITY_UNIT_SETTINGS);
        mTableView.addBasicItem(getString(R.string.bluetooth_device_settings), getString(R.string.bluetooth_device_settings_summary), IntentAction.ACTIVITY_BTDEVICE_SETTINGS);
    }
    
    /***
     * Through accountName and accountPass query account info
     * @param accountName
     * @param accountPass
     */
    private String queryAccountName( ) {
        
        String accountName = null;
        
        mStringBuilder.setLength(0);
        mStringBuilder.append(Account.ACCOUNT_ID + " =? ");
        
        Cursor cursor = null;
        
        try {
            cursor = getContentResolver().query(Account.CONTENT_URI,
                    new String[] {Account.ACCOUNT_NAME},
                    mStringBuilder.toString(),
                    new String[] { String.valueOf(mAccountId) },
                    null);

            if (cursor != null) {
                if ( cursor.getCount() > 0 ) {
                    cursor.moveToPosition(0);
                    accountName = cursor.getString(0);
                }
                
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.d(TAG, " queryAccountName accountName = " + accountName);

        return accountName;
    }
    
}
