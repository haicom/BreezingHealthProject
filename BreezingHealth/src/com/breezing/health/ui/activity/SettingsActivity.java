package com.breezing.health.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import br.com.dina.ui.model.BasicItem;
import br.com.dina.ui.model.GroupIndex;
import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.OnItemClickListener;

import com.breezing.health.R;
import com.breezing.health.application.SysApplication;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.providers.Breezing.Account;
import com.breezing.health.tools.IntentAction;
import com.breezing.health.ui.fragment.BaseDialogFragment;
import com.breezing.health.ui.fragment.BreezingDialogFragment;
import com.breezing.health.ui.fragment.DialogFragmentInterface;
import com.breezing.health.util.BLog;
import com.breezing.health.util.LocalSharedPrefsUtil;

public class SettingsActivity extends ActionBarActivity implements View.OnClickListener {
    
    private static final String TAG = "SettingsActivity";
    
    private UITableView mTableView;
    
    private StringBuilder mStringBuilder; 
    
    private int mAccountId;
    
    private BasicItem mNameItem;
    
    private Button mButton;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_settings);
        initValues();
        initViews();        
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
        mButton = (Button) findViewById(R.id.button);
        mButton.setText(R.string.quit_current_account);
       
       
        
    }
    
    @Override
    protected void onResume() {        
        super.onResume();
        valueToView();
       
    }

    private void valueToView() {
        String accountName = queryAccountName();         
        mTableView.clear();        
        createList();
        mNameItem.setTitle(accountName);
        mTableView.commit();
        
        Log.d(TAG, "onResume mTableView.getChildCount() =  " + mTableView.getChildCount() );
    }

    private void initListeners() {
        mButton.setOnClickListener(this);
        
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
        mNameItem = new BasicItem(R.drawable.user_image,
                    null,
                    null,
                    IntentAction.ACTIVITY_ACCOUNT_DETAIL );
        mTableView.addBasicItem(mNameItem);
        
        mTableView.addBasicItem(getString(R.string.modify_password), 
                   getString(R.string.modify_password_summary), 
                   IntentAction.ACTIVITY_MODIFY_PASSWORD);
        
        mTableView.addHeaderView("");
        mTableView.addBasicItem(getString(R.string.account_management), 
                   getString(R.string.account_management_summary), 
                   IntentAction.ACTIVITY_ACCOUNT_MANAGEMENT);
        mTableView.addHeaderView("");
        mTableView.addBasicItem(getString(R.string.unit_settings), 
                   getString(R.string.unit_settings_summary), 
                   IntentAction.ACTIVITY_UNIT_SETTINGS);
        mTableView.addBasicItem(getString(R.string.bluetooth_device_settings), 
                   getString(R.string.bluetooth_device_settings_summary), 
                   IntentAction.ACTIVITY_BTDEVICE_SETTINGS);
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
    
    private void showDialog() {       
        BreezingDialogFragment dialog = (BreezingDialogFragment) getSupportFragmentManager().findFragmentByTag(ACCOUNT_LOGIN_DIALOG);
        if (dialog != null) {
            getSupportFragmentManager().beginTransaction().remove(dialog);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(null);      
       
        
        dialog = BreezingDialogFragment.newInstance( this.getString(R.string.account_quit_confirm) );       
        dialog.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {
            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {                
                SysApplication.getInstance().exit();          
                Intent intent = new Intent(IntentAction.ACTIVITY_LOGIN);
                dialog.startActivity(intent);
               
            }
        });

        dialog.show(getSupportFragmentManager(), ACCOUNT_LOGIN_DIALOG);
    }
    
    @Override
    public void onClick(View v) {
        showDialog();        
    }
    
    private static final String ACCOUNT_LOGIN_DIALOG = "dialog";    
    
}
