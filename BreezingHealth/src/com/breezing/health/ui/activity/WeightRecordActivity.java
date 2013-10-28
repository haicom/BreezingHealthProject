package com.breezing.health.ui.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.breezing.health.R;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.entity.WeightInfoEntity;
import com.breezing.health.providers.Breezing;
import com.breezing.health.providers.Breezing.Account;
import com.breezing.health.providers.Breezing.HeatConsumption;
import com.breezing.health.providers.Breezing.Information;
import com.breezing.health.providers.Breezing.UnitSettings;
import com.breezing.health.providers.Breezing.WeightChange;
import com.breezing.health.tools.IntentAction;
import com.breezing.health.ui.fragment.BaseDialogFragment;
import com.breezing.health.ui.fragment.DialogFragmentInterface;
import com.breezing.health.ui.fragment.ExceptedWeightPickerDialogFragment;
import com.breezing.health.ui.fragment.WeightPickerDialogFragment;
import com.breezing.health.util.BLog;
import com.breezing.health.util.BreezingQueryViews;
import com.breezing.health.util.DateFormatUtil;
import com.breezing.health.util.ExtraName;
import com.breezing.health.util.LocalSharedPrefsUtil;

public class WeightRecordActivity extends ActionBarActivity implements OnClickListener {
    
    private final String TAG = getClass().getName();
    
    private TextView mNotice;
    private TextView mDifferentValue;
    
    private TextView mWeightUnit;
    private TextView mExpectedUnit;
    
    private EditText mWeight;
    private EditText mExpectedWeight;
    
    private Button mRecord;
    private Button mConnect;
    private SeekBar mSeekBar;
    
    private String mErrorInfo;
    
    private float mWeightValue;
    private float mEveryWeight;
    private float mHopeWeightValue;
    
    private float mActualWeight;
    private float mActualHope;
    private float mActualEvery;
    
    private int mSelectedDate;
    private int mCurrentDate;
    
    private String mWeightUnitValue;
    private String mDate;
    
    private ArrayList<WeightInfoEntity> mWeightInfos;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_weight_record);
        
        initValues();
        initViews();
        valueToView();
        initListeners();
    }
   
    @Override
    protected void onResume() {       
        super.onResume();      
        obtainWeightInfo();
        refreshWeightInfo();
    }
    
    private void obtainWeightInfo() {
        queryBaseInfoViews();
        WeightInfoEntity weightInfo;
        weightInfo = mWeightInfos.get(0);
        Log.d(TAG, " obtainWeightInfo mSelectedDate = " + mSelectedDate + " weightInfo.getDate() = " + weightInfo.getDate() );
        if (mSelectedDate > weightInfo.getDate() ) {
            mWeightUnitValue = weightInfo.getWeightUnit();          
            mWeightValue = weightInfo.getWeight();
            mEveryWeight = weightInfo.getEveryWeight();
            mHopeWeightValue = weightInfo.getExpectedWeight();
        } else {
            weightInfo = mWeightInfos.get( mWeightInfos.size() - 1 );
            if ( weightInfo.getDate() >  mSelectedDate) {
                mWeightUnitValue = weightInfo.getWeightUnit();
                mWeightValue = 0;
                mEveryWeight = 0;
                mHopeWeightValue = 0;
            } else {
                for (WeightInfoEntity weight: mWeightInfos) {
                    if ( weight.getDate() ==  mSelectedDate ) {
                        mWeightUnitValue = weight.getWeightUnit();
                        mWeightValue = weight.getWeight();
                        mEveryWeight = weight.getEveryWeight();
                        mHopeWeightValue = weight.getExpectedWeight();
                    }
                }
            }
        }
    }
    
    private void initValues() {
        mSelectedDate = this.getIntent().getIntExtra(MainActivity.MAIN_DATE, 0);
        mCurrentDate = DateFormatUtil.simpleDateFormat("yyyyMMdd");
        mWeightInfos = new ArrayList<WeightInfoEntity>();
        mActualWeight = 0;
        mActualHope = 0;
        mActualEvery = 0;
    }

    private void initViews() {
        setActionBarTitle(R.string.update_weight);
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));
        addRightActionItem(new ActionItem(ActionItem.ACTION_HISTORY));
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mNotice = (TextView) findViewById(R.id.notice);

        mDifferentValue = (TextView) findViewById(R.id.different_value);
        
        mWeight = (EditText) findViewById(R.id.weight);
        mWeightUnit = (TextView) findViewById(R.id.weight_unit);        
        mWeight.setFocusable(false);
        
        mExpectedWeight = (EditText) findViewById(R.id.expected_weight);
        mExpectedUnit = (TextView) findViewById(R.id.expected_weight_unit);
        mExpectedWeight.setFocusable(false);
        
        mRecord = (Button) findViewById(R.id.record);
        mConnect = (Button) findViewById(R.id.connect);
    }

    private void valueToView() { 
        
         
        
    }

    private void initListeners() {
        mWeight.setOnClickListener(this);
        mExpectedWeight.setOnClickListener(this);
        mRecord.setOnClickListener(this);
        mConnect.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        
        if ( v == mExpectedWeight) {
             showHopeWeightPicker();
         } else if (v == mWeight) {
            showWeightPicker();
            return ;
        } else if (v == mRecord) {
            if ( checkInputWeight() ) {
                updateWeightData();
                this.finish();
            } else {
                Toast.makeText(this, mErrorInfo, Toast.LENGTH_SHORT).show();
            }
            return ;
        } else if (v == mConnect) {
            return ;
        }
    }
    
    private void showWeightPicker() {
        ExceptedWeightPickerDialogFragment weightPicker = (ExceptedWeightPickerDialogFragment) getSupportFragmentManager().findFragmentByTag("weightPicker");
        if (weightPicker != null) {
            getSupportFragmentManager().beginTransaction().remove(weightPicker);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(null);
        
        
        float weight = 0;        
        String weightString = mWeight.getText().toString();
        if ( weightString.isEmpty() ) {
            weight = 0;
        } else {
            weight = Float.valueOf(weightString);
        }        
        
        
        weightPicker = ExceptedWeightPickerDialogFragment.newInstance( weight, mWeightUnit.getText().toString() );
        weightPicker.setTitle(getString(R.string.title_select_weight));
        weightPicker.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {
            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                mWeight.setText( String.valueOf(params[0]) );
            }
        });

        weightPicker.show(getSupportFragmentManager(), "weightPicker");
    }
    
    private void showHopeWeightPicker() {
        ExceptedWeightPickerDialogFragment weightPicker = (ExceptedWeightPickerDialogFragment) getSupportFragmentManager().findFragmentByTag("hopeWeightPicker");
        if (weightPicker != null) {
            getSupportFragmentManager().beginTransaction().remove(weightPicker);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(null);
        
        float hopeWeight = 0;
        String hopeWeightString = mExpectedWeight.getText().toString();
        
        if ( hopeWeightString.isEmpty() ) {
            hopeWeight = 0;
        } else {
            hopeWeight = Float.valueOf(hopeWeightString);
        }
        
        weightPicker = ExceptedWeightPickerDialogFragment.newInstance( hopeWeight, mWeightUnit.getText().toString() );
        weightPicker.setTitle(getString(R.string.title_select_hope_weight));
        weightPicker.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {

            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                mExpectedWeight.setText(String.valueOf(params[0]));
            }

        });

        weightPicker.show(getSupportFragmentManager(), "hopeWeightPicker");
    }
    
    private boolean checkInputWeight() {
        boolean bResult = true;
        mErrorInfo = null;
        if (mWeight.getText().length() == 0) {
            mErrorInfo = getResources().getString(R.string.info_prompt)
                    + getResources().getString(R.string.edittext_hint_weight) ;
            bResult = false;
        }

        return bResult;
    }
    
    public String getWeightUnit() {
        return mWeightUnit.getText().toString();
    }
    
    @Override
    public void onClickActionBarItems(ActionItem item, View v) {

        switch( item.getActionId() ) {
            case ActionItem.ACTION_HISTORY: {
              
                Intent intent = new Intent(IntentAction.ACTIVITY_WEIGHT_HISTORY);
                     
                startActivity(intent);
                return ;
            }                
        
        }
        super.onClickActionBarItems(item, v);
    }
    
    private void refreshWeightInfo() {
        BreezingQueryViews query = new BreezingQueryViews(this);
        float unifyUnit =  query.queryUnitObtainData(getString(R.string.weight_type), mWeightUnitValue);
        mActualWeight = unifyUnit * mWeightValue;
        mActualHope = unifyUnit * mHopeWeightValue;
        mActualEvery = unifyUnit * mEveryWeight;
      
       
        Log.d(TAG, "refreshWeightInfo mWeightValue = " + mWeightValue + " mHopeWeightValue =  " + mHopeWeightValue
                + " mEveryWeight = " + mEveryWeight);
        
        DecimalFormat weightFormat = new DecimalFormat("#0.0");
        final String notice = getString(R.string.weight_update_notice,
                String.valueOf(mSelectedDate).subSequence(0, 4),
                String.valueOf(mSelectedDate).subSequence(4, 6),
                String.valueOf(mSelectedDate).subSequence(6, 8),
                weightFormat.format(mActualEvery), mWeightUnitValue);
        
        SpannableString spannable = new SpannableString(notice);
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)),
                notice.length() - (weightFormat.format(mActualEvery) + mWeightUnitValue).length(),
                notice.length() - mWeightUnitValue.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new RelativeSizeSpan(1.1f),
                notice.length() - (weightFormat.format(mActualEvery) + mWeightUnitValue).length(),
                notice.length() - mWeightUnitValue.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        
        mNotice.setText(spannable);
        String actualString = weightFormat.format(mActualEvery);
        String actualHope =  weightFormat.format(mActualHope);
        final String differentValue = getString(R.string.different_weight_value_notice, 
                actualString, actualHope, mWeightUnitValue);
        final int index = differentValue.indexOf( actualString );
        SpannableString diffValueSpan = new SpannableString(differentValue);
        diffValueSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.green)),
                index ,
                index + actualString.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        diffValueSpan.setSpan(new RelativeSizeSpan(1.2f),
                index ,
                index + actualHope.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        
        mDifferentValue.setText(diffValueSpan);
        
        mSeekBar.setMax( (int) Math.abs( mActualWeight - mActualHope) );
        mSeekBar.setProgress( (int) Math.abs( mActualWeight - mActualEvery) );
        
        Log.d(TAG, " refreshWeightInfo mActualEvery = " + mActualEvery + " mActualHope = " + mActualHope + " mActualWeight =" + mActualWeight);
        mWeight.setText( weightFormat.format(mActualEvery) );
        mExpectedWeight.setText( weightFormat.format(mActualHope) );
        mWeightUnit.setText(mWeightUnitValue);
        mExpectedUnit.setText(mWeightUnitValue);
    }
    
    /**
     * 查询基本信息视图列表
     */
    private static final String[] PROJECTION_BASE_INFO = new String[] {
        Information.WEIGHT_UNIT,        
        WeightChange.WEIGHT,
        WeightChange.EVERY_WEIGHT,
        WeightChange.EXPECTED_WEIGHT,
        WeightChange.DATE
    };
    
    private static final int INFO_WEIGHT_UNIT_INDEX = 0;
    private static final int INFO_WEIGHT_INDEX = 1;
    private static final int INFO_EVERY_WEIGHT_INDEX = 2;
    private static final int INFO_EXPECTED_WEIGHT_INDEX = 3;
    private static final int INFO_DATE_INDEX = 4;
    
    /**
     * 根据某一个帐户id查询基本信息视图
     */
    public void queryBaseInfoViews( ) {
        String weightVaule = null;
        float  weight = 0;
        float  everyWeight = 0;
        float  hopeWeight = 0;
        int    date = 0;
        BLog.d(TAG, "queryBaseInfoView");
        
        String accountClause =  Account.ACCOUNT_ID + " = ?";
        String sortOrder = WeightChange.DATE + " DESC";
        
        int accountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this,
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        String[] args = new String[] { String.valueOf(accountId) };
        
        Cursor cursor  = getContentResolver().query(Information.CONTENT_BASE_INFO_URI,
                PROJECTION_BASE_INFO, accountClause, args, sortOrder);
        
        if (cursor == null) {
            BLog.d(TAG, " testBaseInfoView cursor = " + cursor);
        }

  
        try {
            if (cursor != null) {
                mWeightInfos.clear();
                cursor.moveToPosition(-1);
                while (cursor.moveToNext() ) {
                    WeightInfoEntity weightInfo = new WeightInfoEntity();
                    weightVaule = cursor.getString(INFO_WEIGHT_UNIT_INDEX);                    
                    weight = cursor.getFloat(INFO_WEIGHT_INDEX);
                    everyWeight = cursor.getFloat(INFO_EVERY_WEIGHT_INDEX);
                    hopeWeight = cursor.getFloat(INFO_EXPECTED_WEIGHT_INDEX);
                    date = cursor.getInt(INFO_DATE_INDEX);
                    weightInfo.setWeightUnit(weightVaule);
                    weightInfo.setWeight(weight);
                    weightInfo.setEveryWeight(everyWeight);
                    weightInfo.setExpectedWeight(hopeWeight);
                    weightInfo.setDate(date);
                    mWeightInfos.add(weightInfo);
                }                
            }
        } finally {
            cursor.close();
        }
    }
    
    private void updateWeightData() {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int accountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this,
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);        
        int count = queryWeightInfoByDate(accountId);
        
        float weightText = Float.valueOf( mWeight.getText().toString() );
        float expectedText = Float.valueOf( mExpectedWeight.getText().toString() );
        DecimalFormat weightFormat = new DecimalFormat("#0.0");
        Log.d(TAG, "updateWeightData weightText = " + weightText + " expectedText = " + expectedText 
                + "weightFormat.format(mActualHope) = " + weightFormat.format(mActualHope) 
                + " weightFormat.format(mActualWeight) = " + weightFormat.format(mActualWeight));
        float updateWeight = queryUnitUnifyData(weightText, 
                getString(R.string.weight_type),
                mWeightUnit.getText().toString() );
        
        float updateExpected = queryUnitUnifyData(expectedText, 
                getString(R.string.weight_type),
                mWeightUnit.getText().toString() );
        if (count > 0 ) {
            if (expectedText != Float.valueOf(weightFormat.format(mActualHope) ) ) {
                if (weightText != 0) {
                    updateExpectedWeightChange(ops, accountId, updateWeight, updateExpected);
                }
            } else if (weightText != Float.valueOf(weightFormat.format(mActualWeight) ) ) {
                updateWeightChange(ops, accountId, updateWeight);
            }
        } else {
            appendWeightChange(ops, accountId, updateWeight, updateExpected);
        }
        
        try {
            getContentResolver().applyBatch(Breezing.AUTHORITY, ops);            
        } catch (Exception e) {          
            mErrorInfo = getResources().getString(R.string.data_error);
            // Log exception
            Log.e(TAG, "Exceptoin encoutered while inserting contact: " + e);
        }
        
    }
    
    private int queryWeightInfoByDate(int accountId) {
        int count = 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(WeightChange.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(WeightChange.DATE + " = ? ");
        
      
        String[] args = new String[] { String.valueOf(accountId), String.valueOf(mSelectedDate) };
        
        Cursor cursor  = getContentResolver().query(Information.CONTENT_BASE_INFO_URI,
                PROJECTION_BASE_INFO, stringBuilder.toString(), args, null);
        
        if (cursor == null) {
            BLog.d(TAG, " testBaseInfoView cursor = " + cursor);
        }

  
        try {
            if (cursor != null) {                
                count =  cursor.getCount();                
            }
        } finally {
            cursor.close();
        }
        
        return count;
    }
    
    
    /**
     * 添加信息到 TABLE_WEIGHT 的表中
     * @param accountId
     * @param weight
     * @param expectedWeight
     */
    private void appendWeightChange(ArrayList<ContentProviderOperation> ops, int accountId,
                                    float weight, float expectedWeight) {
        ops.add(ContentProviderOperation.newInsert(WeightChange.CONTENT_URI)
                .withValue(WeightChange.ACCOUNT_ID, accountId)
                .withValue(WeightChange.WEIGHT, weight)
                .withValue(WeightChange.EVERY_WEIGHT, weight)
                .withValue(WeightChange.EXPECTED_WEIGHT, expectedWeight)
                .build());
    }
    
    /**
     * 添加信息到 TABLE_WEIGHT 的表中
     * @param accountId
     * @param weight
     * @param expectedWeight
     */
    private void updateExpectedWeightChange(ArrayList<ContentProviderOperation> ops, int accountId,
                                    float weight, float expectedWeight) {
        
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(WeightChange.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(WeightChange.DATE + " = ? ");
        Log.d(TAG, " updateWeightChange accountId = " + accountId + " mSelectedDate = " + mSelectedDate
                + " weight = " + weight + " expectedWeight = " + expectedWeight ) ;
        ops.add(ContentProviderOperation.newUpdate(WeightChange.CONTENT_URI)
                .withSelection(stringBuilder.toString(),  
                        new String[] { 
                        String.valueOf( accountId ) , 
                        String.valueOf( mSelectedDate ) } )               
                .withValue(WeightChange.WEIGHT, weight)
                .withValue(WeightChange.EVERY_WEIGHT, weight)
                .withValue(WeightChange.EXPECTED_WEIGHT, expectedWeight)               
                .build());
    }
    
    /**
     * 添加信息到 TABLE_WEIGHT 的表中
     * @param accountId
     * @param weight
     * @param expectedWeight
     */
    private void updateWeightChange(ArrayList<ContentProviderOperation> ops, int accountId,
                                    float weight) {
        
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(WeightChange.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(WeightChange.DATE + " = ? ");
        Log.d(TAG, " updateWeightChange accountId = " + accountId + " mSelectedDate = " + mSelectedDate
                + " weight = " + weight  ) ;
        ops.add(ContentProviderOperation.newUpdate(WeightChange.CONTENT_URI)
                .withSelection(stringBuilder.toString(),  
                        new String[] { 
                        String.valueOf( accountId ) , 
                        String.valueOf( mSelectedDate ) } )               
                .withValue(WeightChange.EVERY_WEIGHT, weight)                         
                .build());
    }
    
    /***
     * 查询统计信息换算单位，把单位改为统计信息取得数据，比如 重量 输入 磅 换算成斤存储，然后根据磅获取
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

        return unifyUnit;
    }
    
    
    /***
     * 查询统计信息换算单位，把单位改为统计信息存储，比如 重量 输入 磅 换算成斤存储
     * @param data
     * @param unitType
     * @param unitName
     * @return
     */
    public float queryUnitUnifyData(float data, String unitType, String unitName) {
        float unifyUnit = 0;
        Log.d(TAG, " queryUnitUnifyData unitType = " + unitType + " unitName = " + unitName);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(UnitSettings.UNIT_TYPE + " = ? AND ");
        stringBuilder.append(UnitSettings.UNIT_NAME + "= ?");
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(UnitSettings.CONTENT_URI,
                    new String[] {UnitSettings.UNIT_UNIFY_DATA},
                    stringBuilder.toString(),
                    new String[] {unitType, unitName},
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

        Log.d(TAG, " queryUnitUnifyData  data = " + data + " unifyUnit = " + unifyUnit);

        return data * unifyUnit;
    }
    
    

}
