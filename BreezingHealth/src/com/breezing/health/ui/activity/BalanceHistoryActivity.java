package com.breezing.health.ui.activity;

import android.os.Bundle;

import com.breezing.health.R;
import com.breezing.health.entity.ActionItem;

public class BalanceHistoryActivity extends ActionBarActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_balance_history);
        initValues();
        initViews();
        valueToView();
        initListeners();
    }

    private void initValues() {
        
    }

    private void initViews() {
        setActionBarTitle(R.string.caloric_balance_history);
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));
    }

    private void valueToView() {
        
    }

    private void initListeners() {
        
    }
}
