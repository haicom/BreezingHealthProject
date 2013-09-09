package com.breezing.health.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import br.com.dina.ui.model.GroupIndex;
import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.OnItemClickListener;

import com.breezing.health.R;
import com.breezing.health.entity.ActionItem;

public class ShareActivity extends ActionBarActivity {
    
    private final String ACTION_MESSAGE = "message";
    private final String ACTION_EMAIL = "email";
    private final String ACTION_OTHER = "other";

    private UITableView mTableView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_share);
        initValues();
        initViews();
        valueToView();
        initListeners();
    }

    private void initValues() {
        
    }

    private void initViews() {
        setActionBarTitle(R.string.share);
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
            public void onClick(View view, ViewGroup contentView, String action,
                    GroupIndex index) {
                // TODO Auto-generated method stub
                
            }
        });
    }
    
    /**
     * create UITableView items
     */
    private void createList() {
        mTableView.addBasicItem(getString(R.string.share_by_message), getString(R.string.share_by_message_summary), ACTION_MESSAGE);
        mTableView.addBasicItem(getString(R.string.share_by_email), getString(R.string.share_by_email_summary), ACTION_EMAIL);
        mTableView.addBasicItem(getString(R.string.share_by_other), getString(R.string.share_by_other_summary), ACTION_OTHER);
    }
    
}
