package com.breezing.health.ui.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import br.com.dina.ui.model.ViewItem;
import br.com.dina.ui.widget.UITableView;

import com.breezing.health.R;
import com.breezing.health.entity.ActionItem;

public class AboutActivity extends ActionBarActivity {

    private UITableView mTableView;
    
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
        
        View versionView = inflater.inflate(R.layout.uitableview_custom_item, null);
        TextView versionTitle = (TextView) versionView.findViewById(R.id.title);
        TextView versionContent = (TextView) versionView.findViewById(R.id.content);
        versionTitle.setText(R.string.version_number);
        PackageManager pm = getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(getPackageName(), 0);
            versionContent.setText(pi.versionName);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        ViewItem heightUnitViewItem = new ViewItem(versionView);
        mTableView.addViewItem(heightUnitViewItem);
        
        View officialView = inflater.inflate(R.layout.uitableview_custom_item, null);
        TextView officialTitle = (TextView) officialView.findViewById(R.id.title);
        TextView officialContent = (TextView) officialView.findViewById(R.id.content);
        officialTitle.setText(R.string.official_webside);
        officialContent.setText(R.string.breezing_webside);
        ViewItem weightUnitViewItem = new ViewItem(officialView);
        mTableView.addViewItem(weightUnitViewItem);
        
        View hotLineView = inflater.inflate(R.layout.uitableview_custom_item, null);
        TextView hotLineTitle = (TextView) hotLineView.findViewById(R.id.title);
        TextView hotLineContent = (TextView) hotLineView.findViewById(R.id.content);
        hotLineTitle.setText(R.string.hot_line);
        hotLineContent.setText(R.string.breezing_hot_line);
        ViewItem distanceUnitViewItem = new ViewItem(hotLineView);
        mTableView.addViewItem(distanceUnitViewItem);
        
    }
    
}
