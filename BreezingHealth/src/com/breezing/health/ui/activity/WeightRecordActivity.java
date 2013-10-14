package com.breezing.health.ui.activity;

import java.text.DecimalFormat;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.breezing.health.R;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.providers.Breezing.Account;
import com.breezing.health.providers.Breezing.Information;
import com.breezing.health.providers.Breezing.UnitSettings;
import com.breezing.health.providers.Breezing.WeightChange;
import com.breezing.health.ui.fragment.BaseDialogFragment;
import com.breezing.health.ui.fragment.DialogFragmentInterface;
import com.breezing.health.ui.fragment.WeightPickerDialogFragment;
import com.breezing.health.util.BLog;
import com.breezing.health.util.LocalSharedPrefsUtil;

public class WeightRecordActivity extends ActionBarActivity implements OnClickListener {
    
    private final String TAG = getClass().getName();
    
    private TextView mNotice;
//    private TextView mHopeWeight;
    private TextView mDifferentValue;
    private EditText mWeight;
    private TextView mWeightUnit;
    private Button mRecord;
    private Button mConnect;
    private SeekBar mSeekBar;
    
    private String mErrorInfo;
    
    private float mWeightValue;
    private float mHopeWeightValue;
    private String mWeightUnitValue;
    private String mDate;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_weight_record);
        initValues();
        initViews();
        valueToView();
        initListeners();
    }

    private void initValues() {
        queryBaseInfoViews();
    }

    private void initViews() {
        setActionBarTitle(R.string.update_weight);
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));
        
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mNotice = (TextView) findViewById(R.id.notice);
//        mHopeWeight = (TextView) findViewById(R.id.hopeWeight);
        mDifferentValue = (TextView) findViewById(R.id.different_value);
        
        mWeight = (EditText) findViewById(R.id.weight);
        mWeightUnit = (TextView) findViewById(R.id.weight_unit);
        mWeight.setFocusable(false);
        
        mRecord = (Button) findViewById(R.id.record);
        mConnect = (Button) findViewById(R.id.connect);
    }

    private void valueToView() {
//        final String hopeWeight = getString(R.string.hope_weight_notice, 120);
//        mHopeWeight.setText(hopeWeight);
        
        refreshWeightInfo();
        
        final String[] weightUnits = getResources().getStringArray(R.array.weightUnits);
        mWeightUnit.setText(weightUnits[0]);
    }

    private void initListeners() {
        mWeight.setOnClickListener(this);
        mRecord.setOnClickListener(this);
        mConnect.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        if (v == mWeight) {
            showWeightPicker();
            return ;
        } else if (v == mRecord) {
            if (checkInputWeight()) {
                
            } else {
                Toast.makeText(this, mErrorInfo, Toast.LENGTH_SHORT).show();
            }
            return ;
        } else if (v == mConnect) {
            return ;
        }
    }
    
    private void showWeightPicker() {
        WeightPickerDialogFragment weightPicker = (WeightPickerDialogFragment) getSupportFragmentManager().findFragmentByTag("weightPicker");
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
        
        
        weightPicker = WeightPickerDialogFragment.newInstance( weight, mWeightUnit.getText().toString() );
        weightPicker.setTitle(getString(R.string.title_select_weight));
        weightPicker.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {
            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                mWeight.setText(String.valueOf(params[0]));
                mWeightUnit.setText(String.valueOf(params[1]));
            }

        });

        weightPicker.show(getSupportFragmentManager(), "weightPicker");
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
    
    private void refreshWeightInfo() {
        float  weight = queryUnitObtainData(mWeightValue, 
                getString(R.string.weight_type),
                mWeightUnitValue );
        
        DecimalFormat weightFormat = new DecimalFormat("#0.0");
        final String notice = getString(R.string.weight_update_notice,
                mDate.subSequence(0, 4),
                mDate.subSequence(4, 6),
                mDate.subSequence(6, 8),
                weightFormat.format(weight), mWeightUnitValue);
        mNotice.setText(notice);
        
        float  hopeWeight = queryUnitObtainData(mHopeWeightValue, 
                getString(R.string.weight_type),
                mWeightUnitValue );
        
        final String differentValue = getString(R.string.different_weight_value_notice, weightFormat.format(weight - hopeWeight), mWeightUnitValue);
        mDifferentValue.setText(differentValue);
        
        mSeekBar.setMax((int) Math.max(weight, hopeWeight));
        mSeekBar.setProgress((int) Math.min(weight, hopeWeight));
    }
    
    /**
     * 查询基本信息视图列表
     */
    private static final String[] PROJECTION_BASE_INFO = new String[] {
        Information.WEIGHT_UNIT,        
        WeightChange.WEIGHT,
        WeightChange.EXPECTED_WEIGHT,
        WeightChange.DATE
    };
    
    private static final int INFO_WEIGHT_UNIT_INDEX = 0;
    private static final int INFO_WEIGHT_INDEX = 1;
    private static final int INFO_EXPECTED_WEIGHT_INDEX = 2;
    private static final int INFO_DATE_INDEX = 3;
    
    /**
     * 根据某一个帐户id查询基本信息视图
     */
    public void queryBaseInfoViews( ) {
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
                
                if ( cursor.getCount() > 0 ) {
                    
                    cursor.moveToPosition(0);
                    mWeightUnitValue = cursor.getString(INFO_WEIGHT_UNIT_INDEX);
                    mWeightValue = cursor.getFloat(INFO_WEIGHT_INDEX);
                    mHopeWeightValue = cursor.getFloat(INFO_EXPECTED_WEIGHT_INDEX);
                    mDate = String.valueOf(cursor.getInt(INFO_DATE_INDEX));
                }    
                
            }
        } finally {
            cursor.close();
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

}
