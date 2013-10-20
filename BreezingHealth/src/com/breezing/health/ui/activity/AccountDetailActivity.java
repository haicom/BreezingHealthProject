package com.breezing.health.ui.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import br.com.dina.ui.model.ViewItem;
import br.com.dina.ui.widget.UITableView;

import com.breezing.health.R;
import com.breezing.health.application.SysApplication;
import com.breezing.health.bean.BaseInformationOutput;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.providers.Breezing;
import com.breezing.health.providers.Breezing.Account;
import com.breezing.health.providers.Breezing.Information;
import com.breezing.health.providers.Breezing.UnitSettings;
import com.breezing.health.providers.Breezing.WeightChange;
import com.breezing.health.tools.IntentAction;
import com.breezing.health.ui.fragment.BaseDialogFragment;
import com.breezing.health.ui.fragment.BreezingDialogFragment;
import com.breezing.health.ui.fragment.DialogFragmentInterface;

import com.breezing.health.util.BLog;
import com.breezing.health.util.CalendarUtil;
import com.breezing.health.util.ChangeUnitUtil;
import com.breezing.health.util.LocalSharedPrefsUtil;

public class AccountDetailActivity extends ActionBarActivity implements View.OnClickListener {
    private static final String TAG = "AccountDetailActivity";
    
    private ContentResolver mContentResolver;
    private UITableView mTableView;
    
    private BaseInformationOutput mBaseInformationOutput;
    
    private int mAccountId;
    
    private TextView mNameContent;
    private TextView mSexContent;
    private TextView mAgeContent;
    private TextView mJobTypeContent;
    private TextView mHeightContent;
    private TextView mWeightContent;
    private TextView mHopeWeightContent;
    
    private Button   mButton;
    
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
        mContentResolver = this.getContentResolver();
        mBaseInformationOutput = new BaseInformationOutput();
        mAccountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this,
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
    }

    private void initViews() {
        setActionBarTitle(R.string.account_detail);
        addLeftActionItem( new ActionItem(ActionItem.ACTION_BACK) );
        addRightActionItem(new ActionItem(ActionItem.ACTION_EDIT) );        
        mTableView = (UITableView) findViewById(R.id.tableView);
        mButton = (Button) findViewById(R.id.uitable_button);
        mButton.setText(R.string.delete_account);     
    }
    
    @Override
    protected void onResume() {        
        super.onResume();
        queryBaseInfoViews();
        fillInContent();
    }
    
    private void valueToView() {
        
        createList();
        mTableView.commit();
    }

    private void initListeners() {
        mButton.setOnClickListener(this);
    }
    

    
    private void fillInContent() {
        mNameContent.setText( mBaseInformationOutput.getName() );
        mSexContent.setText( getString( ChangeUnitUtil.changeGenderToId( mBaseInformationOutput.getGender() ) ) );
        mAgeContent.setText( String.valueOf( CalendarUtil.getAgeByDate( mBaseInformationOutput.getAge() ) ) );
        mJobTypeContent.setText(getString( ChangeUnitUtil.
                changeCustomUtilToId( mBaseInformationOutput.getCustom() ) ) );
        
        float  height = queryUnitObtainData(mBaseInformationOutput.getHeight(), 
                getString(R.string.height_type),
                mBaseInformationOutput.getHeightUnit() );
        
        DecimalFormat heightFormat;
        
        if (mBaseInformationOutput.getHeightUnit().equals(getString(R.string.height_meter) ) ) {
            heightFormat = new DecimalFormat("#.00");
        } else {
            heightFormat = new DecimalFormat("#.0");
        }
        
        String heightString = heightFormat.format(height);
        mHeightContent.setText( heightString + "  "  + mBaseInformationOutput.getHeightUnit() );
        
        float  weight = queryUnitObtainData(mBaseInformationOutput.getWeight(), 
                getString(R.string.weight_type),
                mBaseInformationOutput.getWeightUnit() );
        
        DecimalFormat weightFormat = new DecimalFormat("#.0");
        String weightString = weightFormat.format(weight);
        
        mWeightContent.setText( weightString 
                + "  " + mBaseInformationOutput.getWeightUnit() );
        
        float  hopeWeight = queryUnitObtainData( mBaseInformationOutput.getExpectedWeight(), 
                getString(R.string.weight_type),
                mBaseInformationOutput.getWeightUnit() );      
        
        DecimalFormat hopeWeightFormat = new DecimalFormat("#.0");
        String hopeWeightString = hopeWeightFormat.format(hopeWeight);
        
        mHopeWeightContent.setText( hopeWeightString + "  "  + mBaseInformationOutput.getWeightUnit() );
        
    }
    /**
     * create UITableView items
     */
    private void createList() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        View nameView = inflater.inflate(R.layout.uitableview_custom_item, null);
        TextView nameTitle = (TextView) nameView.findViewById(R.id.title);
        mNameContent = (TextView) nameView.findViewById(R.id.content);
        nameTitle.setText(R.string.name);
        
        ViewItem nameViewItem = new ViewItem(nameView);
        mTableView.addViewItem(nameViewItem);
        
        
        View sexView = inflater.inflate(R.layout.uitableview_custom_item, null);
        TextView sexTitle = (TextView) sexView.findViewById(R.id.title);
        mSexContent = (TextView) sexView.findViewById(R.id.content);
        sexTitle.setText(R.string.sex);
        
        ViewItem sexViewItem = new ViewItem(sexView);
        mTableView.addViewItem(sexViewItem);
        
        View ageView = inflater.inflate(R.layout.uitableview_custom_item, null);
        TextView ageTitle = (TextView) ageView.findViewById(R.id.title);
        mAgeContent = (TextView) ageView.findViewById(R.id.content);
        ageTitle.setText(R.string.age);
       
        ViewItem ageViewItem = new ViewItem(ageView);
        mTableView.addViewItem(ageViewItem);
        
        View jobTypeView = inflater.inflate(R.layout.uitableview_custom_item, null);
        TextView jobTypeTitle = (TextView) jobTypeView.findViewById(R.id.title);
        mJobTypeContent = (TextView) jobTypeView.findViewById(R.id.content);
        jobTypeTitle.setText(R.string.job_type);
       
        ViewItem jobTypeViewItem = new ViewItem(jobTypeView);
        mTableView.addViewItem(jobTypeViewItem);
        
        View heightView = inflater.inflate(R.layout.uitableview_custom_item, null);
        
        TextView heightTitle = (TextView) heightView.findViewById(R.id.title);
        mHeightContent = (TextView) heightView.findViewById(R.id.content);
        
        heightTitle.setText(R.string.height);        
        ViewItem heightViewItem = new ViewItem(heightView);
        mTableView.addViewItem(heightViewItem);
        
        View weightView = inflater.inflate(R.layout.uitableview_custom_item, null);
        
        TextView weightTitle = (TextView) weightView.findViewById(R.id.title);
        mWeightContent = (TextView) weightView.findViewById(R.id.content);
        
        weightTitle.setText(R.string.weight);
        
       
        
        ViewItem weightViewItem = new ViewItem(weightView);
        mTableView.addViewItem(weightViewItem);
        
        View hopeWeightView = inflater.inflate(R.layout.uitableview_custom_item, null);
        TextView hopeWeightTitle = (TextView) hopeWeightView.findViewById(R.id.title);
        mHopeWeightContent = (TextView) hopeWeightView.findViewById(R.id.content);
        
        hopeWeightTitle.setText(R.string.hope_weight);       
        
        ViewItem hopeWeightViewItem = new ViewItem(hopeWeightView);
        mTableView.addViewItem(hopeWeightViewItem);
        
       
        mTableView.addHeaderView("");
        
        
         
   
    }
    
    @Override
    public void onClickActionBarItems(ActionItem item, View v) {
        switch( item.getActionId() ) {
            case ActionItem.ACTION_EDIT: {
                
                Bundle bundle = new Bundle();
                bundle.putParcelable(BASE_INFO_PARCELABLE, mBaseInformationOutput);
                
                Intent intent = new Intent(IntentAction.ACTIVITY_EDIT_INFORMATION);                
                intent.putExtras(bundle);
                startActivity(intent);
                
                return;
            }
        }
        
        super.onClickActionBarItems(item, v);
    }
    
    /**
     * 查询基本信息视图列表
     */
    private static final String[] PROJECTION_BASE_INFO = new String[] {
        Account.ACCOUNT_ID,
        Account.ACCOUNT_NAME,          // 0
        Account.ACCOUNT_PASSWORD,      // 1
        Information.GENDER,    // 2
        Information.HEIGHT ,     //3
        Information.BIRTHDAY ,   //4
        Information.CUSTOM ,            //5
        Information.HEIGHT_UNIT,
        Information.WEIGHT_UNIT,        
        WeightChange.WEIGHT,            //6
        WeightChange.EXPECTED_WEIGHT,   //7
        WeightChange.DATE              //8
    };
    
    private static final int INFO_ACCOUNT_ID_INDEX = 0;
    private static final int INFO_ACCOUNT_NAME_INDEX = 1;
    private static final int INFO_ACCOUNT_PASSWORD_INDEX = 2;
    private static final int INFO_GENDER_INDEX = 3;
    private static final int INFO_HEIGHT_INDEX = 4;
    private static final int INFO_BIRTHDAY_INDEX = 5;
    private static final int INFO_CUSTOM_INDEX = 6;
    private static final int INFO_HEIGHT_UNIT_INDEX = 7;
    private static final int INFO_WEIGHT_UNIT_INDEX = 8;
    private static final int INFO_WEIGHT_INDEX = 9;
    private static final int INFO_EXPECTED_WEIGHT_INDEX = 10;
    private static final int INFO_DATE_INDEX = 11;
    
    /**
     * 根据某一个帐户id查询基本信息视图
     */
    public void queryBaseInfoViews( ) {
        BLog.d(TAG, "queryBaseInfoView");
        
        String accountClause =  Account.ACCOUNT_ID + " = ?";
        String sortOrder = WeightChange.DATE + " DESC";
        
        String[] args = new String[] { String.valueOf(mAccountId) };
        
        Cursor cursor  = mContentResolver.query(Information.CONTENT_BASE_INFO_URI,
                PROJECTION_BASE_INFO, accountClause, args, sortOrder);
        
        if (cursor == null) {
            BLog.d(TAG, " testBaseInfoView cursor = " + cursor);
        }

  
        try {
            if (cursor != null) {
                
                if ( cursor.getCount() > 0 ) {
                    
                    cursor.moveToPosition(0);
                    int  accountId = cursor.getInt(INFO_ACCOUNT_ID_INDEX);
                    String  accountName = cursor.getString(INFO_ACCOUNT_NAME_INDEX);
                    String  accountPass = cursor.getString(INFO_ACCOUNT_PASSWORD_INDEX);
                    int     gender = cursor.getInt(INFO_GENDER_INDEX);
                    float  height = cursor.getFloat(INFO_HEIGHT_INDEX);
                    int    birthday = cursor.getInt(INFO_BIRTHDAY_INDEX);
                    int    custom = cursor.getInt(INFO_CUSTOM_INDEX);
                    String heightUnit = cursor.getString(INFO_HEIGHT_UNIT_INDEX);
                    String weightUnit = cursor.getString(INFO_WEIGHT_UNIT_INDEX);
                    float  weight = cursor.getFloat(INFO_WEIGHT_INDEX);
                    float  expectedWeight = cursor.getFloat(INFO_EXPECTED_WEIGHT_INDEX);
                    int  date = cursor.getInt(INFO_DATE_INDEX);
                    
                    mBaseInformationOutput.setAccount(accountId);
                    mBaseInformationOutput.setName(accountName);
                    mBaseInformationOutput.setGender(gender);
                    mBaseInformationOutput.setAge(birthday);
                    mBaseInformationOutput.setHeight(height);
                    mBaseInformationOutput.setCustom(custom);
                    mBaseInformationOutput.setHeightUnit(heightUnit);
                    mBaseInformationOutput.setWeightUnit(weightUnit);
                    mBaseInformationOutput.setWeight(weight);
                    mBaseInformationOutput.setExpectedWeight(expectedWeight);
                    mBaseInformationOutput.setDate(date);
                    
                    BLog.d(TAG, " queryBaseInfoViews weight = " + weight + " expectedWeight = " + expectedWeight + " date = "
                            + date + " heightUnit = " + heightUnit + " weightUnit = " + weightUnit);
                }    
                
            }
        } finally {
            cursor.close();
        }
    }
    
    /***
     * 删除帐户信息信息到 TABLE_ACCOUNT 表中
     * @param accountName
     * @param accountId
     * @param accountPass
     */
    private void updateAccount( ) {
        
        ArrayList<ContentProviderOperation> opList = new ArrayList<ContentProviderOperation>();
        int accountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this,
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);                
    
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(Account.ACCOUNT_ID + " = ? ");

        opList.add(ContentProviderOperation.newUpdate(Account.CONTENT_URI)
                .withSelection(stringBuilder.toString(),  
                        new String[] { String.valueOf(accountId) } )              
                .withValue(Account.ACCOUNT_DELETED, 1)
                .build());
        
        try {
            getContentResolver().applyBatch(Breezing.AUTHORITY, opList);            
            LocalSharedPrefsUtil.saveSharedPrefsAccount(this, CLEAR_ACCOUNT_ID, String.valueOf(CLEAR_PASSWORD) );
        } catch (Exception e) {
           
            // Log exception
            Log.e(TAG, "Exceptoin encoutered while inserting contact: " + e);
        }
       
    }
    
    /***
     * 查询统计信息换算单位，把单位改为统计信息存储，比如 重量 输入 磅 换算成斤存储
     * @param data
     * @param unitType
     * @param unitName
     * @return
     */
    public float queryUnitObtainData(float data, String unitType, String unitName) {
        
        float unifyUnit = 0;
        
        BLog.d(TAG, " queryUnitObtainData unitType = " + unitType + " unitName = " + unitName);
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(UnitSettings.UNIT_TYPE + " = ? AND ");
        stringBuilder.append(UnitSettings.UNIT_NAME + "= ?");
        
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query( UnitSettings.CONTENT_URI,
                    new String[] { UnitSettings.UNIT_OBTAIN_DATA },
                    stringBuilder.toString(),
                    new String[] { unitType, unitName },
                    null);

            if (cursor != null) {
                if ( cursor.getCount() > 0 ) {
                    cursor.moveToPosition(0);
                    unifyUnit = cursor.getFloat(0);
                }
                
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        BLog.d(TAG, " queryUnitObtainData  unitType = " + unitType + " unitName = " + unitName);

        return data * unifyUnit;
    }
    
    public static final String BASE_INFO_PARCELABLE = "base_information_output";

    @Override
    public void onClick(View v) {
        if (v == mButton) {
            showDialog();
        }        
    }
    
    private void showDialog() {       
        BreezingDialogFragment dialog = (BreezingDialogFragment) getSupportFragmentManager().findFragmentByTag(ACCOUNT_DETAIL_DIALOG);
        if (dialog != null) {
            getSupportFragmentManager().beginTransaction().remove(dialog);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(null);      
       
        
        dialog = BreezingDialogFragment.newInstance(this.getString(R.string.account_delete_confirm));       
        dialog.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {
            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {                
                updateAccount();
                SysApplication.getInstance().exit();                
                Intent intent = new Intent(IntentAction.ACTIVITY_FILLIN_INFORMATION);
                dialog.startActivity(intent);
               
            }
        });

        dialog.show(getSupportFragmentManager(), ACCOUNT_DETAIL_DIALOG);
    }
    
    private static final int DIALOG_DELETE_ACCOUNT_INFO = 1;

    private static final String ACCOUNT_DETAIL_DIALOG = "dialog";    
    private static final int CLEAR_ACCOUNT_ID = 0;
    private static final int CLEAR_PASSWORD  = 0;
}
