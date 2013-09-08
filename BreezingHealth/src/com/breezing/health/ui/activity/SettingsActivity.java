package com.breezing.health.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import br.com.dina.ui.model.GroupIndex;
import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.OnItemClickListener;

import com.breezing.health.R;
import com.breezing.health.tools.IntentAction;

public class SettingsActivity extends ActionBarActivity {

    private UITableView mTableView;
    
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
        
    }

    private void initViews() {
        setActionBarTitle(R.string.settings);
        mTableView = (UITableView) findViewById(R.id.tableView);
        createList();
        mTableView.commit();
    }

    private void valueToView() {
        
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
        mTableView.addBasicItem(R.drawable.user_image, "test name", "13616042050", IntentAction.ACTIVITY_ACCOUNT_DETAIL);
        mTableView.addBasicItem(getString(R.string.modify_password), getString(R.string.modify_password_summary), IntentAction.ACTIVITY_MODIFY_PASSWORD);
        mTableView.addHeaderView("");
        mTableView.addBasicItem(getString(R.string.account_management), getString(R.string.account_management_summary), IntentAction.ACTIVITY_ACCOUNT_MANAGEMENT);
        mTableView.addHeaderView("");
        mTableView.addBasicItem(getString(R.string.unit_settings), getString(R.string.unit_settings_summary), IntentAction.ACTIVITY_UNIT_SETTINGS);
        mTableView.addBasicItem(getString(R.string.bluetooth_device_settings), getString(R.string.bluetooth_device_settings_summary), IntentAction.ACTIVITY_BTDEVICE_SETTINGS);
    }
    
}
