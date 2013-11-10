package com.breezing.health.ui.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.breezing.health.R;
import com.breezing.health.bean.BaseInformationOutput;
import com.breezing.health.entity.AccountEntity;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.providers.Breezing;
import com.breezing.health.providers.Breezing.Account;
import com.breezing.health.providers.Breezing.Information;
import com.breezing.health.providers.Breezing.UnitSettings;
import com.breezing.health.providers.Breezing.WeightChange;
import com.breezing.health.tools.Tools;
import com.breezing.health.ui.fragment.BaseDialogFragment;
import com.breezing.health.ui.fragment.DatePickerDialogFragment;
import com.breezing.health.ui.fragment.DialogFragmentInterface;
import com.breezing.health.ui.fragment.ExceptedWeightPickerDialogFragment;
import com.breezing.health.ui.fragment.HeightPickerDialogFragment;
import com.breezing.health.ui.fragment.JobTypePickerDialogFragment;
import com.breezing.health.ui.fragment.WeightPickerDialogFragment;
import com.breezing.health.util.BLog;
import com.breezing.health.util.BreezingQueryViews;
import com.breezing.health.util.ChangeUnitUtil;
import com.breezing.health.util.DateFormatUtil;
import com.breezing.health.util.LocalSharedPrefsUtil;



public class EditInformationActivity extends ActionBarActivity implements OnClickListener {
    private final String TAG = "EditInformationActivity";
    private String mErrorInfo;
    private String mYear;
    private String mMonth;
    private String mDay;
   
    private EditText mUserName;
    private EditText mBornDate;
    private EditText mJobType;
    private EditText mHeight;
    private EditText mWeight;
    private EditText mHopeWeight;

    private RadioGroup mSexGroup;
    private RadioButton mRadioMale;
    private RadioButton mRadioFemale;

    private TextView mHeightUnit;
    private TextView mWeightUnit;
    private TextView mHopeWeightUnit;
    
    private Intent mIntent;
    
    private BaseInformationOutput mBaseInformationOutput;
    
    private ArrayList<ContentProviderOperation> mOps;
    
    private Button mModify;
   
    private TextView mWeightNotice;
    private TextView mExpectedWeightNotice;
    
    private float mWeightValue = 0;
    private float mExpectedWeightValue = 0;
    private double mStandardWeightValue = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {       
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_edit_information);
        initViews();
        initValues();
        valueToView();
        initListeners();
    }

    private void initValues() {       
        AccountEntity account;
        int accountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this, LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        BreezingQueryViews query = new BreezingQueryViews( this );
        account = query.queryBaseInfoViews(accountId);
        mJobType.setText( getString( ChangeUnitUtil.
                changeCustomUtilToId( account.getCustom() ) )  );
        mHeightUnit.setText( account.getHeightUnit() );
        mWeightUnit.setText( account.getWeightUnit() );
        mHopeWeightUnit.setText( account.getWeightUnit() );

        mOps = new ArrayList<ContentProviderOperation>();
    }

    private void initViews() {
        Bundle bundle = getIntent().getExtras();
        
        mBaseInformationOutput = bundle.getParcelable(AccountDetailActivity.BASE_INFO_PARCELABLE);
        
        setActionBarTitle(R.string.title_edit_personal_information);
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));
        
//        addRightActionItem(new ActionItem(ActionItem.ACTION_DONE) );    

        mModify = (Button) findViewById(R.id.modify);
        
        mSexGroup = (RadioGroup) findViewById(R.id.sex_radiogroup);
        mRadioMale = (RadioButton) findViewById(R.id.male);
        mRadioFemale = (RadioButton) findViewById(R.id.female);
        
        if ( mBaseInformationOutput.getGender() == ChangeUnitUtil.GENDER_MALE ) {
            mRadioMale.setChecked(true);
        } else if (mBaseInformationOutput.getGender() == ChangeUnitUtil.GENDER_FEMALE ) {
            mRadioFemale.setChecked(true);
        }
        
        mUserName = (EditText)findViewById(R.id.user_name);
        mUserName.setText( mBaseInformationOutput.getName() );
        
        mBornDate = (EditText) findViewById(R.id.date);
        getDateByBirthday( mBaseInformationOutput.getAge() );
        
        mBornDate.setFocusable(false);
        mBornDate.setText( getString(R.string.birthday, mYear, mMonth, mDay) );                        
               
        mJobType = (EditText) findViewById(R.id.jobType);
        mJobType.setFocusable(false);
        mJobType.setText( ChangeUnitUtil.changeCustomUtilToId( mBaseInformationOutput.getCustom() ) );
        
        mHeight = (EditText) findViewById(R.id.height);
        
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
                
        mHeight.setText( heightString );
        
        mHeightUnit = (TextView) findViewById(R.id.height_unit);
        mHeightUnit.setText( mBaseInformationOutput.getHeightUnit() );        
        mHeight.setFocusable(false);
        
        mWeight = (EditText) findViewById(R.id.weight);
        
        float  weight = queryUnitObtainData(mBaseInformationOutput.getWeight(), 
                getString(R.string.weight_type),
                mBaseInformationOutput.getWeightUnit() );
        
        DecimalFormat weightFormat = new DecimalFormat("#.0");
        String weightString = weightFormat.format(weight);
        
        mWeight.setText( weightString ); 
        
        mWeightUnit = (TextView) findViewById(R.id.weight_unit);
        mWeightUnit.setText( mBaseInformationOutput.getWeightUnit() );        
        mWeight.setFocusable(false);
        
        float  hopeWeight = queryUnitObtainData( mBaseInformationOutput.getExpectedWeight(), 
                getString(R.string.weight_type),
                mBaseInformationOutput.getWeightUnit() );      
        
        DecimalFormat hopeWeightFormat = new DecimalFormat("#.0");
        String hopeWeightString = hopeWeightFormat.format(hopeWeight);
        
        mHopeWeight = (EditText) findViewById(R.id.hopeWeight);
        mHopeWeight.setText( hopeWeightString ); 
        
        mHopeWeightUnit = (TextView) findViewById(R.id.hope_weight_unit);
        mHopeWeightUnit.setText( mBaseInformationOutput.getWeightUnit() );      
        mHopeWeight.setFocusable(false);
        
        mWeightNotice = (TextView) findViewById(R.id.weight_notice);
        mExpectedWeightNotice = (TextView) findViewById(R.id.hope_weight_notice);
        
        BreezingQueryViews query = new BreezingQueryViews(EditInformationActivity.this);
        mWeightValue = query.queryUnitUnifyData(
                Float.parseFloat(mWeight.getText().toString()) ,
                getResources().getString(R.string.weight_type),
                mWeightUnit.getText().toString());
        mExpectedWeightValue = query.queryUnitUnifyData(
                Float.parseFloat(mHopeWeight.getText().toString()) ,
                getResources().getString(R.string.weight_type),
                mWeightUnit.getText().toString());
        BLog.v("mWeightValue =" + mWeightValue + "mExpectedWeightValue =" + mExpectedWeightValue);
        caculateStandard();
        updateWeightNotice();
        updateExpectedWeightNotice();
    }

    private void valueToView() {


    }

    private void initListeners() {
       
        mBornDate.setOnClickListener(this);
        mJobType.setOnClickListener(this);
        mWeight.setOnClickListener(this);
        mHopeWeight.setOnClickListener(this);
        mHeight.setOnClickListener(this);
        mModify.setOnClickListener(this);
        mSexGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				caculateStandard();
                updateWeightNotice();
                updateExpectedWeightNotice();
			}
		});
    }

    @Override
    public void onClick(View v) {
        
        if (v == mBornDate) {
            showDatePicker();
            return ;
        } else if (v == mWeight) {
            showWeightPicker();
            return ;
        } else if (v == mHopeWeight) {
            showHopeWeightPicker();
            return ;
        } else if (v == mHeight) {
            showHeightPicker();
            return ;
        } else if (v == mJobType) {
            showJobTypePicker();
            return ;
        } else if (v == mModify) {
            updateAccountInfo();             
            this.finish();
            return ;
        }
    }

    private void showJobTypePicker() {
        
        JobTypePickerDialogFragment datePicker = (JobTypePickerDialogFragment) 
                getSupportFragmentManager().findFragmentByTag("jobTypePicker");
        
        if (datePicker != null) {
            getSupportFragmentManager().beginTransaction().remove(datePicker);
        }
        
        getSupportFragmentManager().beginTransaction().addToBackStack(null);
        
        int custom;
        String jobType = mJobType.getText().toString();
        
        if ( jobType.isEmpty() ) {
            custom  = 0;
        } else {
            custom = ChangeUnitUtil.changeCustomUtil(this, mJobType.getText().toString() );
        }
       
        datePicker = JobTypePickerDialogFragment.newInstance( custom );
        datePicker.setTitle(getString(R.string.title_select_job_type));
        datePicker.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {
            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                mJobType.setText(params[0].toString());
            }
        });

        datePicker.show(getSupportFragmentManager(), "jobTypePicker");
    }

    private void showDatePicker() {
        int year = 0;
        int month = 0;
        int day = 0;
        
        DatePickerDialogFragment datePicker = (DatePickerDialogFragment) 
                getSupportFragmentManager().findFragmentByTag("datePicker");
        
        if (datePicker != null) {
            getSupportFragmentManager().beginTransaction().remove(datePicker);
        }
        
        getSupportFragmentManager().beginTransaction().addToBackStack(null);
        
        if ( !mYear.isEmpty() ) {
            year = Integer.valueOf(mYear);
        }
        
        if ( !mMonth.isEmpty() ) {
            month = Integer.valueOf(mMonth);
        }
        
        if ( !mDay.isEmpty() ) {
            day = Integer.valueOf(mDay);
        }
        
        datePicker = DatePickerDialogFragment.newInstance( year,
                     month, 
                     day );
        datePicker.setTitle(getString(R.string.title_select_born_date));
        datePicker.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {
            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                mYear = String.valueOf(params[0]);
                mMonth = String.valueOf(params[1]);
                mDay = String.valueOf(params[2]);
                mBornDate.setText( getString(R.string.birthday, mYear, mMonth, mDay) );
            }
        });

        datePicker.show(getSupportFragmentManager(), "datePicker");
    }

    private void showWeightPicker() {
        
        WeightPickerDialogFragment weightPicker = (WeightPickerDialogFragment) 
                getSupportFragmentManager().findFragmentByTag("weightPicker");
        
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
        
        
        weightPicker = WeightPickerDialogFragment.newInstance(weight, mWeightUnit.getText().toString() );
        weightPicker.setTitle(getString(R.string.title_select_weight));
        weightPicker.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {
            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                mWeight.setText(String.valueOf(params[0]));
                mWeightUnit.setText(String.valueOf(params[1]));
                mHopeWeightUnit.setText(String.valueOf(params[1]));
                
                BreezingQueryViews query = new BreezingQueryViews(EditInformationActivity.this);
                mWeightValue = query.queryUnitUnifyData(
                        Float.parseFloat(mWeight.getText().toString()) ,
                        getResources().getString(R.string.weight_type),
                        mWeightUnit.getText().toString());
                updateWeightNotice();
            }

        });

        weightPicker.show(getSupportFragmentManager(), "weightPicker");
    }

    private void showHopeWeightPicker() {
        ExceptedWeightPickerDialogFragment weightPicker = (ExceptedWeightPickerDialogFragment) 
                getSupportFragmentManager().findFragmentByTag("hopeWeightPicker");
        
        if (weightPicker != null) {
            getSupportFragmentManager().beginTransaction().remove(weightPicker);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(null);
       
        float hopeWeight = 0;
        String hopeWeightString = mHopeWeight.getText().toString();
        
        if ( hopeWeightString.isEmpty() ) {
            hopeWeight = 0;
        } else {
            hopeWeight = Float.valueOf(hopeWeightString);
        }
        
        weightPicker = ExceptedWeightPickerDialogFragment.newInstance(hopeWeight, mHopeWeightUnit.getText().toString() );
        weightPicker.setTitle(getString(R.string.title_select_hope_weight));
        weightPicker.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {

            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                mHopeWeight.setText(String.valueOf(params[0]));
                
                BreezingQueryViews query = new BreezingQueryViews(EditInformationActivity.this);
                mExpectedWeightValue = query.queryUnitUnifyData(
                        Float.parseFloat(mHopeWeight.getText().toString()) ,
                        getResources().getString(R.string.weight_type),
                        mWeightUnit.getText().toString());
                updateExpectedWeightNotice();
            }

        });

        weightPicker.show(getSupportFragmentManager(), "hopeWeightPicker");
    }

    private void showHeightPicker() {
        HeightPickerDialogFragment heightPicker = (HeightPickerDialogFragment) 
                getSupportFragmentManager().findFragmentByTag("heightPicker");
        if (heightPicker != null) {
            getSupportFragmentManager().beginTransaction().remove(heightPicker);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(null);
        
        float height = 0; 
        
        String heightString = mHeight.getText().toString();
        
        if ( heightString.isEmpty() ) {
            height = 0;
        } else {
            height = Float.valueOf(heightString);
        }
        
        heightPicker = HeightPickerDialogFragment.
                newInstance( height, 
                mHeightUnit.getText().toString() );
        heightPicker.setTitle( getString(R.string.title_select_height) );
        heightPicker.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {
            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                mHeight.setText(String.valueOf(params[0]));
                mHeightUnit.setText(String.valueOf(params[1]));
                
                caculateStandard();
                updateWeightNotice();
                updateExpectedWeightNotice();
            }

        });

        heightPicker.show(getSupportFragmentManager(), "heightPicker");
    }
    
    
    @Override
    public void onClickActionBarItems(ActionItem item, View v) {
        switch( item.getActionId() ) {
            case ActionItem.ACTION_DONE: {
                updateAccountInfo();             
                this.finish();                
                return;
            }
        }
        
        super.onClickActionBarItems(item, v);
    }

    /***
     * 验证输入信息，并生成帐户信息,插入相应的数据
     */
    private boolean updateAccountInfo() {
        boolean result = false;

        if ( !checkFillInputInfo() ) {
            return false;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(mYear);
        if ( (Integer.parseInt(mMonth) < 10) && (mMonth.length() == 1) ) {            
            stringBuilder.append("0").append(mMonth);
        } else {
            stringBuilder.append(mMonth);
        }

        if ( (Integer.parseInt(mDay) < 10) && (mDay.length() == 1) ) {
            stringBuilder.append("0").append(mDay);
        } else {
            stringBuilder.append(mDay);
        }



        int date = DateFormatUtil.simpleDateFormat("yyyyMMdd", stringBuilder.toString());
        int accountId = mBaseInformationOutput.getAccount();

        updateAccount(mOps, mUserName.getText().toString(), accountId,
                      DEFAULT_PASSWORD);

        BreezingQueryViews query = new BreezingQueryViews(EditInformationActivity.this);
        float height =  query.queryUnitUnifyData(
                           Float.parseFloat(mHeight.getText().toString()) ,
                           getResources().getString(R.string.height_type),
                           mHeightUnit.getText().toString());

        float exceptedWeight = query.queryUnitUnifyData(
                                Float.parseFloat(mHopeWeight.getText().toString()) ,
                                getResources().getString(R.string.weight_type),
                                mWeightUnit.getText().toString());

        float weight = query.queryUnitUnifyData(
                       Float.parseFloat(mWeight.getText().toString()) ,
                       getResources().getString(R.string.weight_type),
                       mWeightUnit.getText().toString());
        
        
        Log.d(TAG, "updateAccountInfo height = " + height + " exceptedWeight = " + exceptedWeight
                + " weight = " + weight);

        int gender = 0;

        if ( mRadioMale.isChecked() ) {
            gender = ChangeUnitUtil.GENDER_MALE;
        } else if ( mRadioFemale.isChecked() ) {
            gender = ChangeUnitUtil.GENDER_FEMALE;
        }

        int custom = ChangeUnitUtil.changeCustomUtil(this, mJobType.getText().toString());
        
        String[] distanceUnits = getResources().getStringArray(R.array.distance_units);
        
        
        updateInformation(mOps, accountId, gender,
                          height, date, custom,
                          mHeightUnit.getText().toString(),
                          mWeightUnit.getText().toString(),
                          distanceUnits[0]);

        updateWeightChange(mOps, accountId, weight, exceptedWeight);

        try {
            getContentResolver().applyBatch(Breezing.AUTHORITY, mOps);            
            result = true;
        } catch (Exception e) {
            result = false;
            mErrorInfo = getResources().getString(R.string.data_error);
            // Log exception
            Log.e(TAG, "Exceptoin encoutered while inserting contact: " + e);
        }

        return result;
    }

    private boolean checkFillInputInfo() {
        boolean bResult = true;
        mErrorInfo = null;
        if (mUserName.getText().length() == 0) {
            mErrorInfo = getResources().getString(R.string.info_prompt)
                       + getResources().getString(R.string.edittext_hint_username) ;
            bResult = false;
        } else if (mBornDate.getText().length() == 0) {
            mErrorInfo = getResources().getString(R.string.info_prompt)
                    + getResources().getString(R.string.edittext_hint_born_date) ;
            bResult = false;
        } else if (mHeight.getText().length() == 0) {
            mErrorInfo = getResources().getString(R.string.info_prompt)
                    + getResources().getString(R.string.edittext_hint_height) ;
            bResult = false;
        } else if (mWeight.getText().length() == 0) {
            mErrorInfo = getResources().getString(R.string.info_prompt)
                    + getResources().getString(R.string.edittext_hint_weight) ;
            bResult = false;
        } else if (mHopeWeight.getText().length() == 0) {
            mErrorInfo = getResources().getString(R.string.info_prompt)
                    + getResources().getString(R.string.edittext_hint_hope_weight) ;
            bResult = false;
        }

        return bResult;
    }



    /***
     * 添加信息到 TABLE_ACCOUNT 表中
     * @param accountName
     * @param accountId
     * @param accountPass
     */
    private void updateAccount( ArrayList<ContentProviderOperation> ops, String accountName, int accountId, String accountPass) {
        Log.d(TAG, " appendAccount accountName = " + accountName + " accountPass = " + accountPass);
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(Account.ACCOUNT_ID + " = ? ");
     

        ops.add(ContentProviderOperation.newUpdate(Account.CONTENT_URI)
                .withSelection(stringBuilder.toString(),  
                        new String[] { String.valueOf(accountId) } )
                .withValue(Account.ACCOUNT_NAME, accountName)
                .withValue(Account.ACCOUNT_ID, accountId)
                .withValue(Account.ACCOUNT_PASSWORD, accountPass)
                .build());
    }

    /***
     * 添加信息到 TABLE_INFORMATION 表中
     * @param accountId
     * @param gender
     * @param height
     * @param birthday
     * @param custom
     * @param heightUnit
     * @param weightUnit
     * @param distanceUnit
     */
    private void updateInformation( ArrayList<ContentProviderOperation> ops, int accountId,
                                    int gender, float height, int birthday,
                                    int custom, String heightUnit, String weightUnit ,
                                    String distanceUnit) {
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(Information.ACCOUNT_ID + " = ? ");
        
        ops.add(ContentProviderOperation.newUpdate(Information.CONTENT_URI)
                .withSelection(stringBuilder.toString(),  new String[] { String.valueOf(accountId) } )                
                .withValue(Information.GENDER, gender)
                .withValue(Information.HEIGHT, height)
                .withValue(Information.BIRTHDAY, birthday)
                .withValue(Information.CUSTOM, custom)
                .withValue(Information.HEIGHT_UNIT, heightUnit)
                .withValue(Information.WEIGHT_UNIT, weightUnit)                
                .build());
    }

    /**
     * 添加信息到 TABLE_WEIGHT 的表中
     * @param accountId
     * @param weight
     * @param expectedWeight
     */
    private void updateWeightChange(ArrayList<ContentProviderOperation> ops, int accountId,
                                    float weight, float expectedWeight) {
        
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(WeightChange.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(WeightChange.DATE + " = ? ");
        Log.d(TAG, " updateWeightChange accountId = " + accountId + " mBaseInformationOutput.getDate() = " + mBaseInformationOutput.getDate()
                + " weight = " + weight + " expectedWeight = " + expectedWeight ) ;
        ops.add(ContentProviderOperation.newUpdate(WeightChange.CONTENT_URI)
                .withSelection(stringBuilder.toString(),  
                        new String[] { 
                        String.valueOf(accountId) , 
                        String.valueOf( mBaseInformationOutput.getDate() ) } )               
                .withValue(WeightChange.WEIGHT, weight)
                .withValue(WeightChange.EVERY_WEIGHT, weight)
                .withValue(WeightChange.EXPECTED_WEIGHT, expectedWeight)               
                .build());
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

    public String getWeightUnit() {
        return mWeightUnit.getText().toString();
    }
    
    private void getDateByBirthday(int date) {        
        String dateString = String.valueOf(date);        
        mYear = dateString.subSequence(0, WEEK_PICKER_YEAR).toString();
        mMonth = dateString.subSequence(WEEK_PICKER_YEAR, WEEK_PICKER_YEAR + WEEK_PICKER_MONTH).toString();
        mDay = dateString.subSequence(WEEK_PICKER_YEAR + WEEK_PICKER_MONTH, dateString.length() ).toString();       
       
    }
    
    private void caculateStandard() {
    	if (mHeight.getText().toString().equals("")) {
    		return ;
    	}
    	final float inputHeight = Float.parseFloat(mHeight.getText().toString());
    	BreezingQueryViews query = new BreezingQueryViews(EditInformationActivity.this);
    	float height =  query.queryUnitUnifyData(
    			inputHeight ,
                getResources().getString(R.string.height_type),
                mHeightUnit.getText().toString());
    	
    	mStandardWeightValue = Tools.checkWeight(height, mRadioFemale.isChecked());
    	BLog.v(TAG, "caculateStandard() mStandardWeightValue =" + mStandardWeightValue);
    }
    
    public void updateWeightNotice() {
    	BLog.v("updateWeightNotice() mWeightValue = " + mWeightValue);
    	if (mWeightValue == 0 || mStandardWeightValue == 0) {
    		return ;
    	}
    	
    	mWeightNotice.setVisibility(View.VISIBLE);
    	if (mWeightValue > mStandardWeightValue) {
    		mWeightNotice.setText(R.string.more_than_standard_weight_notice);
    	} else if (mWeightValue < mStandardWeightValue) {
    		mWeightNotice.setText(R.string.less_than_standard_weight_notice);
    	} else {
    		mWeightNotice.setText(R.string.equal_standard_weight_notice);
    	}
    	
    }
    
    public void updateExpectedWeightNotice() {
    	BLog.v("updateExpectedWeightNotice() mExpectedWeightValue = " + mExpectedWeightValue);
    	if (mExpectedWeightValue == 0 || mStandardWeightValue == 0) {
    		return ;
    	}
    	
    	mExpectedWeightNotice.setVisibility(View.VISIBLE);
    	if (mExpectedWeightValue > mStandardWeightValue) {
    		mExpectedWeightNotice.setText(R.string.more_than_standard_weight_notice);
    	} else if (mExpectedWeightValue < mStandardWeightValue) {
    		mExpectedWeightNotice.setText(R.string.less_than_standard_weight_notice);
    	} else {
    		mExpectedWeightNotice.setText(R.string.equal_standard_weight_notice);
    	}
    	
    }

    private static final String DEFAULT_PASSWORD = "888888";
    
    private static final int WEEK_PICKER_DATE_LEN           = 8;
    private static final int WEEK_PICKER_YEAR               = 4;
    private static final int WEEK_PICKER_MONTH              = 2;


}



