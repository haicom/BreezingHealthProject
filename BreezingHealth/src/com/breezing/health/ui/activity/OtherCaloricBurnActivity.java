package com.breezing.health.ui.activity;

import android.os.Bundle;

import com.breezing.health.R;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.util.LocalSharedPrefsUtil;

public class OtherCaloricBurnActivity extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_other_caloric_burn);
        initValues();
        initViews();
        valueToView();
        initListeners();
    }

    private void initValues() {
        LocalSharedPrefsUtil.saveFirstTime(this);
    }

    private void initViews() {
        setActionBarTitle(R.string.other_caloric_burn);
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));
    }

    private void valueToView() {
    }

    private void initListeners() {
    }
    
}
