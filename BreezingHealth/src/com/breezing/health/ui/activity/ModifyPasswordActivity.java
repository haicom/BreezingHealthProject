package com.breezing.health.ui.activity;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.breezing.health.R;
import com.breezing.health.entity.AccountEntity;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.providers.Breezing;
import com.breezing.health.providers.Breezing.Account;
import com.breezing.health.util.BreezingQueryViews;
import com.breezing.health.util.LocalSharedPrefsUtil;

public class ModifyPasswordActivity extends ActionBarActivity implements OnClickListener {
    
    private EditText mOldPassord;
    private EditText mNewPassword;
    private EditText mCheckPassword;
    private Button mModify;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_modify_password);
        initValues();
        initViews();
        valueToView();
        initListeners();
    }

    private void initValues() {
        
    }

    private void initViews() {
        setActionBarTitle(R.string.modify_password);
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));
//        addRightActionItem(new ActionItem(ActionItem.ACTION_DONE));
    }

    private void valueToView() {
        mOldPassord = (EditText) findViewById(R.id.old_password);
        mNewPassword = (EditText) findViewById(R.id.new_password);
        mCheckPassword = (EditText) findViewById(R.id.check_password);
        mModify = (Button) findViewById(R.id.modify);
    }

    private void initListeners() {
        mModify.setOnClickListener(this);
    }
    
    private boolean checkInputValues() {
        if (mOldPassord.getText().toString().equals("")) {
            mOldPassord.setError(getString(R.string.old_password_cant_be_null));
            return false;
        } else if (mNewPassword.getText().toString().equals("")) {
            mNewPassword.setError(getString(R.string.new_password_cant_be_null));
            return false;
        } else if (!mNewPassword.getText().toString().equals(mCheckPassword.getText().toString())) {
            mCheckPassword.setError(getString(R.string.check_password_failure));
            return false;
        } else if (!checkLoginPassword()) {
            mOldPassord.setError(getString(R.string.old_paasword_input_failure));
            return false;
        }
        return true;
    }
    
    private boolean checkLoginPassword() {
        final int accountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this,
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        BreezingQueryViews query = new BreezingQueryViews(this);
        final AccountEntity account = query.queryBaseInfoViews(accountId);
        if (mOldPassord.getText().toString().equals(account.getAccountPass())) {
            return true;
        }
        
        return false;
    }
    
    @Override
    public void onClickActionBarItems(ActionItem item, View v) {
        switch(item.getActionId()) {
        
        case ActionItem.ACTION_DONE:
            final boolean result = checkInputValues();
            if (result) {
                updateAccountPassword();
            }
            return ;
        
        }
        super.onClickActionBarItems(item, v);
    }

    @Override
    public void onClick(View v) {
        
        if (v == mModify) {
            if (checkInputValues()) {
                updateAccountPassword();
                finish();
                return ;
            }
        }
        
    }
    
    /***
     * update password to TABLE_ACCOUNT
     * @param accountId
     * @param accountPass
     */
    private void updateAccountPassword() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(Account.ACCOUNT_ID + " = ? ");
     

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        final int accountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this,
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        ops.add(ContentProviderOperation.newUpdate(Account.CONTENT_URI)
                .withSelection(stringBuilder.toString(),  
                        new String[] { String.valueOf(accountId) } )
                .withValue(Account.ACCOUNT_PASSWORD, mNewPassword.getText().toString())
                .build());
        
        try {
            getContentResolver().applyBatch(Breezing.AUTHORITY, ops);
            LocalSharedPrefsUtil.saveSharedPrefsAccount(this, accountId, mNewPassword.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.modify_password_failure), Toast.LENGTH_LONG).show();
            return ;
        }
        
        Toast.makeText(this, getString(R.string.modify_password_success), Toast.LENGTH_LONG).show();
    }
    
}
