package com.breezing.health.ui.activity;

import android.os.Bundle;

import com.breezing.health.R;
import com.breezing.health.entity.ActionItem;

public class HistoryActivity extends ActionBarActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_history);
        initValues();
        initViews();
        valueToView();
        initListeners();
    }

    private void initValues() {
        
    }

    private void initViews() {
        setActionBarTitle(R.string.history);
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));
    }

    private void valueToView() {
        
    }

    private void initListeners() {
        
    }
}
