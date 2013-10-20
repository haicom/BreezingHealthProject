package com.breezing.health.ui.activity;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import br.com.dina.ui.model.GroupIndex;
import br.com.dina.ui.model.ViewItem;
import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.OnItemClickListener;

import com.breezing.health.R;
import com.breezing.health.entity.AccountEntity;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.entity.UnitEntity;
import com.breezing.health.providers.Breezing;
import com.breezing.health.providers.Breezing.Information;
import com.breezing.health.ui.fragment.BaseDialogFragment;
import com.breezing.health.ui.fragment.DialogFragmentInterface;
import com.breezing.health.ui.fragment.UnitPickerDialogFragment;
import com.breezing.health.ui.fragment.UnitPickerDialogFragment.UnitType;
import com.breezing.health.util.BreezingQueryViews;
import com.breezing.health.util.LocalSharedPrefsUtil;

public class UnitSettingsActivity extends ActionBarActivity implements OnClickListener {
    private UITableView mTableView;
    private View mCaloricUnitView;
    private View mHeightUnitView;
    private View mWeightUnitView;
    private View mDistanceUnitView;
    
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
        addLeftActionItem( new ActionItem(ActionItem.ACTION_BACK) );
        
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
        
        int accountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this,
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        
        BreezingQueryViews query = new BreezingQueryViews(this);
        AccountEntity account = query.queryBaseInfoViews(accountId);
        
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        mCaloricUnitView = inflater.inflate(R.layout.uitableview_custom_item, null);
        TextView caloricUnitTitle = (TextView) mCaloricUnitView.findViewById(R.id.title);
        TextView caloricUnitContent = (TextView) mCaloricUnitView.findViewById(R.id.content);
        caloricUnitTitle.setText(R.string.caloric_unit);
        caloricUnitContent.setText( account.getCaloricUnit() );
        mCaloricUnitView.setOnClickListener(this);
        ViewItem caloricUnitViewItem = new ViewItem(mCaloricUnitView);
        mTableView.addViewItem(caloricUnitViewItem);
        
        mHeightUnitView = inflater.inflate(R.layout.uitableview_custom_item, null);
        TextView heightUnitTitle = (TextView) mHeightUnitView.findViewById(R.id.title);
        TextView heightUnitContent = (TextView) mHeightUnitView.findViewById(R.id.content);
        heightUnitTitle.setText(R.string.height_unit);
        heightUnitContent.setText( account.getHeightUnit() );
        mHeightUnitView.setOnClickListener(this);
        ViewItem heightUnitViewItem = new ViewItem(mHeightUnitView);
        mTableView.addViewItem(heightUnitViewItem);
        
        mWeightUnitView = inflater.inflate(R.layout.uitableview_custom_item, null);
        TextView weightUnitTitle = (TextView) mWeightUnitView.findViewById(R.id.title);
        TextView weightUnitContent = (TextView) mWeightUnitView.findViewById(R.id.content);
        weightUnitTitle.setText(R.string.weight_unit);
        weightUnitContent.setText( account.getWeightUnit() );
        mWeightUnitView.setOnClickListener(this);
        ViewItem weightUnitViewItem = new ViewItem(mWeightUnitView);
        mTableView.addViewItem(weightUnitViewItem);
        
        mDistanceUnitView = inflater.inflate(R.layout.uitableview_custom_item, null);
        TextView distanceUnitTitle = (TextView) mDistanceUnitView.findViewById(R.id.title);
        TextView distanceUnitContent = (TextView) mDistanceUnitView.findViewById(R.id.content);
        distanceUnitTitle.setText(R.string.distance_unit);
        distanceUnitContent.setText( account.getDistanceUnit() );
        mDistanceUnitView.setOnClickListener(this);
        ViewItem distanceUnitViewItem = new ViewItem(mDistanceUnitView);
        mTableView.addViewItem(distanceUnitViewItem);
        
    }
    
    private void showHopeWeightPicker(UnitType unitType) {
        
        UnitPickerDialogFragment weightPicker = (UnitPickerDialogFragment) getSupportFragmentManager().
                findFragmentByTag("hopeWeightPicker");
        
        if (weightPicker != null) {
            getSupportFragmentManager().beginTransaction().remove(weightPicker);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(null);
        
        weightPicker = UnitPickerDialogFragment.newInstance(unitType);
        weightPicker.setTitle(getString(R.string.please_select_unit));
        weightPicker.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {

            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                UnitType unitType = (UnitType) params[0];
                UnitEntity unit = (UnitEntity) params[1];
                switch(unitType) {
                
                    case CALORIC:
                        ( (TextView)mCaloricUnitView.findViewById( R.id.content) ).setText( unit.getUnitName() );
                        updateUnit(Information.CALORIC_UNIT, unit);
                        break;
                        
                    case HEIGHT:
                        ( (TextView)mHeightUnitView.findViewById(R.id.content) ).setText( unit.getUnitName() );
                        updateUnit(Information.HEIGHT_UNIT, unit);
                        break;
                        
                    case WEIGHT:
                        ( (TextView)mWeightUnitView.findViewById(R.id.content) ).setText( unit.getUnitName() );
                        updateUnit(Information.WEIGHT_UNIT, unit);
                        break;
                        
                    case DISTANCE:
                        ( (TextView)mDistanceUnitView.findViewById(R.id.content) ).setText( unit.getUnitName() );
                        updateUnit(Information.DISTANCE_UNIT, unit);
                        break;
                }
            }

        });

        weightPicker.show(getSupportFragmentManager(), "hopeWeightPicker");
    }

    @Override
    public void onClick(View view) {
        
        if (view == mCaloricUnitView) {
            showHopeWeightPicker(UnitType.CALORIC);
            return ;
        } else if (view == mHeightUnitView) {
            showHopeWeightPicker(UnitType.HEIGHT);
            return ;
        } else if (view == mWeightUnitView) {
            showHopeWeightPicker(UnitType.WEIGHT);
            return ;
        } else if (view == mDistanceUnitView) {
            showHopeWeightPicker(UnitType.DISTANCE);
            return ;
        }
        
    }
    
    private void updateUnit(String updateValue, UnitEntity unit) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(Information.ACCOUNT_ID + " = ? ");
     
        

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        final int accountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this,
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        ops.add(ContentProviderOperation.newUpdate(Information.CONTENT_URI)
                .withSelection(stringBuilder.toString(),  
                        new String[] { String.valueOf(accountId) } )
                .withValue(updateValue, unit.getUnitName())
                .build());
        
        try {
            getContentResolver().applyBatch(Breezing.AUTHORITY, ops);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.modify_unit_failure), Toast.LENGTH_LONG).show();
            return ;
        }
        
        Toast.makeText(this, getString(R.string.modify_unit_success), Toast.LENGTH_LONG).show();
    }
    
}
