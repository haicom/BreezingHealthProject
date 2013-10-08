package com.breezing.health.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.breezing.health.R;
import com.breezing.health.adapter.AccountAdapter;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.tools.IntentAction;

public class AccountManagementActivity extends ActionBarActivity {

	private ListView mListView;
	private AccountAdapter mAccountAdapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_account_management);
        initValues();
        initViews();
        valueToView();
        initListeners();
    }

    private void initValues() {
        
    }

    private void initViews() {
        setActionBarTitle(R.string.account_management);
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));
        addRightActionItem(new ActionItem(ActionItem.ACTION_ADD_ACCOUNT));
        
        mListView = (ListView) findViewById(R.id.list);
    }

    private void valueToView() {
    	mAccountAdapter = new AccountAdapter(this);
    	mListView.setAdapter(mAccountAdapter);
    }

    private void initListeners() {
    	mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position,
					long id) {
				mAccountAdapter.setSelected(position);
				mAccountAdapter.notifyDataSetChanged();
				return ;
			}
		});
    }
    
    @Override
    public void onClickActionBarItems(ActionItem item, View v) {
    	
    	switch(item.getActionId()) {
    	case ActionItem.ACTION_ADD_ACCOUNT: {
    		Intent intent = new Intent(IntentAction.ACTIVITY_FILLIN_INFORMATION);
    		startActivity(intent);
    		finish();
    		return ;
    	}
    	}
    	
    	super.onClickActionBarItems(item, v);
    }
}
