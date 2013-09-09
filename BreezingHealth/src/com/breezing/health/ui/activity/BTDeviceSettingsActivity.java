package com.breezing.health.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.com.dina.ui.model.GroupIndex;
import br.com.dina.ui.model.ViewItem;
import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.OnItemClickListener;

import com.breezing.health.R;
import com.breezing.health.entity.ActionItem;

public class BTDeviceSettingsActivity extends ActionBarActivity {
    
    private UITableView mTableView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_btdevice_settings);
        initValues();
        initViews();
        valueToView();
        initListeners();
    }

    private void initValues() {
        
    }

    private void initViews() {
        setActionBarTitle(R.string.bluetooth_device_settings);
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));
        
        mTableView = (UITableView) findViewById(R.id.tableView);
        createList();
        mTableView.commit();
    }

    private void valueToView() {
        
    }

    private void initListeners() {
        mTableView.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onClick(View view, ViewGroup contentView, String intent, GroupIndex index) {
                
            }
        });
    }
    
    /**
     * create UITableView items
     */
    private void createList() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        View breezingView = inflater.inflate(R.layout.uitableview_custom_item, null);
        TextView breezingTitle = (TextView) breezingView.findViewById(R.id.title);
        TextView breezingContent = (TextView) breezingView.findViewById(R.id.content);
        breezingTitle.setText(R.string.my_breezing);
        breezingContent.setText("");
        ViewItem breezingViewItem = new ViewItem(breezingView);
        mTableView.addViewItem(breezingViewItem);
        
        View pedometerView = inflater.inflate(R.layout.uitableview_custom_item, null);
        TextView pedometerTitle = (TextView) pedometerView.findViewById(R.id.title);
        TextView pedometerContent = (TextView) pedometerView.findViewById(R.id.content);
        pedometerTitle.setText(R.string.my_pedometer);
        pedometerContent.setText("");
        ViewItem pedometerViewItem = new ViewItem(pedometerView);
        mTableView.addViewItem(pedometerViewItem);
        
        View weighingScaleView = inflater.inflate(R.layout.uitableview_custom_item, null);
        TextView weighingScaleTitle = (TextView) weighingScaleView.findViewById(R.id.title);
        TextView weighingScaleContent = (TextView) weighingScaleView.findViewById(R.id.content);
        weighingScaleTitle.setText(R.string.my_weighing_scale);
        weighingScaleContent.setText("");
        ViewItem weighingScaleViewItem = new ViewItem(weighingScaleView);
        mTableView.addViewItem(weighingScaleViewItem);
    }
    
}
