package com.breezing.health.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.breezing.health.R;
import com.breezing.health.entity.ActionItem;

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
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));
        addRightActionItem(new ActionItem(ActionItem.ACTION_DONE));
    }

    private void valueToView() {
        
    }

    private void initListeners() {
        
    }
    
    private boolean checkInputValues() {
        
        return false;
    }
    
    @Override
    public void onClickActionBarItems(ActionItem item, View v) {
        switch(item.getActionId()) {
        
        case ActionItem.ACTION_DONE:
            final boolean result = checkInputValues();
            if (result) {
                
            }
            return ;
        
        }
        super.onClickActionBarItems(item, v);
    }
    
}
