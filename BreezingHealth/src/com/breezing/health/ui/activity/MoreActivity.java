package com.breezing.health.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import br.com.dina.ui.model.GroupIndex;
import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.OnItemClickListener;

import com.breezing.health.R;
import com.breezing.health.entity.ActionItem;

public class MoreActivity extends ActionBarActivity {

    private UITableView mTableView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_more);
        initValues();
        initViews();
        valueToView();
        initListeners();
    }

    private void initValues() {
        
    }

    private void initViews() {
        setActionBarTitle(R.string.more);
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));
        
        mTableView = (UITableView) findViewById(R.id.tableView);
        createList();
        mTableView.commit();
    }

    private void valueToView() {
        mTableView.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onClick(View view, ViewGroup contentView, String action,
                    GroupIndex index) {
                // TODO Auto-generated method stub
                
            }
        });
    }

    private void initListeners() {
        mTableView.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onClick(View view, ViewGroup contentView, String action, GroupIndex index) {
                
            }
        });
    }
    
    /**
     * create UITableView items
     */
    private void createList() {
        mTableView.addBasicItem(getString(R.string.user_instructions), getString(R.string.user_instructions_summary), null);
        mTableView.addBasicItem(getString(R.string.related_links), getString(R.string.related_links_summary), null);
        mTableView.addBasicItem(getString(R.string.about_us), getString(R.string.about_us_summary), null);
    }
    
}
