package com.breezing.health.ui.activity;

import android.os.Bundle;

import com.breezing.health.R;

public class AccountDetailActivity extends ActionBarActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_account_detail);
        initValues();
        initViews();
        valueToView();
        initListeners();
    }

    private void initValues() {
        
    }

    private void initViews() {
        setActionBarTitle(R.string.account_detail);
    }

    private void valueToView() {
        
    }

    private void initListeners() {
        
    }
    
}
