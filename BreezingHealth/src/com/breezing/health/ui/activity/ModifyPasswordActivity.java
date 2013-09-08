package com.breezing.health.ui.activity;

import android.os.Bundle;

import com.breezing.health.R;

public class ModifyPasswordActivity extends ActionBarActivity {
    
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
    }

    private void valueToView() {
        
    }

    private void initListeners() {
        
    }
    
}
