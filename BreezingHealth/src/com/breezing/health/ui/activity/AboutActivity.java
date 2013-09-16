package com.breezing.health.ui.activity;

import android.os.Bundle;

import com.breezing.health.R;
import com.breezing.health.entity.ActionItem;

public class AboutActivity extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_about);
        initValues();
        initViews();
        valueToView();
        initListeners();
    }

    private void initValues() {
        
    }

    private void initViews() {
        setActionBarTitle(R.string.about_us);
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));
    }

    private void valueToView() {
        
    }

    private void initListeners() {
        
    }
    
}
