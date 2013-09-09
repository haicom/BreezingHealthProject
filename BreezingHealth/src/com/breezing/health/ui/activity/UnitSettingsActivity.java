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

public class UnitSettingsActivity extends ActionBarActivity {

    private UITableView mTableView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_unit_settings);
        initValues();
        initViews();
        valueToView();
        initListeners();
    }

    private void initValues() {
        
    }

    private void initViews() {
        setActionBarTitle(R.string.unit_settings);
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
        
        View heightUnitView = inflater.inflate(R.layout.uitableview_custom_item, null);
        TextView heightUnitTitle = (TextView) heightUnitView.findViewById(R.id.title);
        TextView heightUnitContent = (TextView) heightUnitView.findViewById(R.id.content);
        heightUnitTitle.setText(R.string.height_unit);
        heightUnitContent.setText("");
        ViewItem heightUnitViewItem = new ViewItem(heightUnitView);
        mTableView.addViewItem(heightUnitViewItem);
        
        View weightUnitView = inflater.inflate(R.layout.uitableview_custom_item, null);
        TextView weightUnitTitle = (TextView) weightUnitView.findViewById(R.id.title);
        TextView weightUnitContent = (TextView) weightUnitView.findViewById(R.id.content);
        weightUnitTitle.setText(R.string.weight_unit);
        weightUnitContent.setText("");
        ViewItem weightUnitViewItem = new ViewItem(weightUnitView);
        mTableView.addViewItem(weightUnitViewItem);
        
        View distanceUnitView = inflater.inflate(R.layout.uitableview_custom_item, null);
        TextView distanceUnitTitle = (TextView) distanceUnitView.findViewById(R.id.title);
        TextView distanceUnitContent = (TextView) distanceUnitView.findViewById(R.id.content);
        distanceUnitTitle.setText(R.string.distance_unit);
        distanceUnitContent.setText("");
        ViewItem distanceUnitViewItem = new ViewItem(distanceUnitView);
        mTableView.addViewItem(distanceUnitViewItem);
        
    }
    
}
