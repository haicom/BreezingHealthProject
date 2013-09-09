package com.breezing.health.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import br.com.dina.ui.model.ViewItem;
import br.com.dina.ui.widget.UITableView;

import com.breezing.health.R;
import com.breezing.health.entity.ActionItem;

public class AccountDetailActivity extends ActionBarActivity {
    
    private UITableView mTableView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_account_detail);
        initValues();
        initViews();
        valueToView();
        initListeners();
    }

    private void initValues() {
        
    }

    private void initViews() {
        setActionBarTitle(R.string.account_detail);
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));
        
        mTableView = (UITableView) findViewById(R.id.tableView);
        createList();
        mTableView.commit();
    }

    private void valueToView() {
        
    }

    private void initListeners() {
        
    }
    
    /**
     * create UITableView items
     */
    private void createList() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        View nameView = inflater.inflate(R.layout.uitableview_custom_item, null);
        TextView nameTitle = (TextView) nameView.findViewById(R.id.title);
        TextView nameContent = (TextView) nameView.findViewById(R.id.content);
        nameTitle.setText(R.string.name);
        nameContent.setText("");
        ViewItem nameViewItem = new ViewItem(nameView);
        mTableView.addViewItem(nameViewItem);
        
        View sexView = inflater.inflate(R.layout.uitableview_custom_item, null);
        TextView sexTitle = (TextView) sexView.findViewById(R.id.title);
        TextView sexContent = (TextView) sexView.findViewById(R.id.content);
        sexTitle.setText(R.string.sex);
        sexContent.setText("");
        ViewItem sexViewItem = new ViewItem(sexView);
        mTableView.addViewItem(sexViewItem);
        
        View ageView = inflater.inflate(R.layout.uitableview_custom_item, null);
        TextView ageTitle = (TextView) ageView.findViewById(R.id.title);
        TextView ageContent = (TextView) ageView.findViewById(R.id.content);
        ageTitle.setText(R.string.age);
        ageContent.setText("");
        ViewItem ageViewItem = new ViewItem(ageView);
        mTableView.addViewItem(ageViewItem);
        
        View jobTypeView = inflater.inflate(R.layout.uitableview_custom_item, null);
        TextView jobTypeTitle = (TextView) jobTypeView.findViewById(R.id.title);
        TextView jobTypeContent = (TextView) jobTypeView.findViewById(R.id.content);
        jobTypeTitle.setText(R.string.job_type);
        jobTypeContent.setText("");
        ViewItem jobTypeViewItem = new ViewItem(jobTypeView);
        mTableView.addViewItem(jobTypeViewItem);
        
        View heightView = inflater.inflate(R.layout.uitableview_custom_item, null);
        TextView heightTitle = (TextView) heightView.findViewById(R.id.title);
        TextView heightContent = (TextView) heightView.findViewById(R.id.content);
        heightTitle.setText(R.string.height);
        heightContent.setText("");
        ViewItem heightViewItem = new ViewItem(heightView);
        mTableView.addViewItem(heightViewItem);
        
        View weightView = inflater.inflate(R.layout.uitableview_custom_item, null);
        TextView weightTitle = (TextView) weightView.findViewById(R.id.title);
        TextView weightContent = (TextView) weightView.findViewById(R.id.content);
        weightTitle.setText(R.string.weight);
        weightContent.setText("");
        ViewItem weightViewItem = new ViewItem(weightView);
        mTableView.addViewItem(weightViewItem);
        
        View hopeWeightView = inflater.inflate(R.layout.uitableview_custom_item, null);
        TextView hopeWeightTitle = (TextView) hopeWeightView.findViewById(R.id.title);
        TextView hopeWeightContent = (TextView) hopeWeightView.findViewById(R.id.content);
        hopeWeightTitle.setText(R.string.hope_weight);
        hopeWeightContent.setText("");
        ViewItem hopeWeightViewItem = new ViewItem(hopeWeightView);
        mTableView.addViewItem(hopeWeightViewItem);
    }
    
}
