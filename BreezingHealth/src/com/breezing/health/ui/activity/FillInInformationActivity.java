package com.breezing.health.ui.activity;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Groups;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.breezing.health.R;
import com.breezing.health.providers.Breezing;
import com.breezing.health.providers.Breezing.Account;
import com.breezing.health.providers.Breezing.Information;
import com.breezing.health.providers.Breezing.UnitSettings;
import com.breezing.health.tools.IntentAction;
import com.breezing.health.ui.fragment.BaseDialogFragment;
import com.breezing.health.ui.fragment.DatePickerDialogFragment;
import com.breezing.health.ui.fragment.DialogFragmentInterface;
import com.breezing.health.ui.fragment.ExceptedWeightPickerDialogFragment;
import com.breezing.health.ui.fragment.HeightPickerDialogFragment;
import com.breezing.health.ui.fragment.JobTypePickerDialogFragment;
import com.breezing.health.ui.fragment.WeightPickerDialogFragment;
import com.breezing.health.util.ChangeUnitUtil;
import com.breezing.health.util.DateFormatUtil;
import com.breezing.health.providers.Breezing.WeightChange;



public class FillInInformationActivity extends ActionBarActivity implements OnClickListener {
    private final String TAG = "FillInInformationActivity";
    private String mErrorInfo;
    private String mYear;
    private String mMonth;
    private String mDay;

    private Button mNext;

    private EditText mUserName;
    private EditText mBornDate;
    private EditText mJobType;
    private EditText mHeight;
    private EditText mWeight;
    private EditText mHopeWeight;

    private RadioButton mRadioMale;
    private RadioButton mRadioFemale;

    private TextView mHeightUnit;
    private TextView mWeightUnit;
    private TextView mHopeWeightUnit;

    private ArrayList<ContentProviderOperation> mOps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_fillin_information);
        initViews();
        initValues();
        valueToView();
        initListeners();
    }

    private void initValues() {

        final String[] types = getResources().getStringArray(R.array.jobTypes);
        final String[] heightUnits = getResources().getStringArray(R.array.heightUnits);
        final String[] weightUnits = getResources().getStringArray(R.array.weightUnits);

        mJobType.setText(types[0]);
        mHeightUnit.setText(heightUnits[0]);
        mWeightUnit.setText(weightUnits[0]);
        mHopeWeightUnit.setText(weightUnits[0]);
        
        mOps = new ArrayList<ContentProviderOperation>();
    }

    private void initViews() {
        setActionBarTitle(R.string.title_fillin_personal_information);

        mRadioMale = (RadioButton) findViewById(R.id.male);
        mRadioFemale = (RadioButton) findViewById(R.id.female);

        mUserName = (EditText)findViewById(R.id.user_name);
        mBornDate = (EditText) findViewById(R.id.date);
        mBornDate.setFocusable(false);
        mJobType = (EditText) findViewById(R.id.jobType);
        mJobType.setFocusable(false);
        mHeight = (EditText) findViewById(R.id.height);
        mHeightUnit =(TextView) findViewById(R.id.height_unit);
        mHeight.setFocusable(false);
        mWeight = (EditText) findViewById(R.id.weight);
        mWeightUnit = (TextView) findViewById(R.id.weight_unit);
        mWeight.setFocusable(false);
        mHopeWeight = (EditText) findViewById(R.id.hopeWeight);
        mHopeWeightUnit = (TextView) findViewById(R.id.hope_weight_unit);
        mHopeWeight.setFocusable(false);
        mNext = (Button) findViewById(R.id.next);
    }

    private void valueToView() {


    }

    private void initListeners() {
        mNext.setOnClickListener(this);
        mBornDate.setOnClickListener(this);
        mJobType.setOnClickListener(this);
        mWeight.setOnClickListener(this);
        mHopeWeight.setOnClickListener(this);
        mHeight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mNext) {
            if ( createAccountInfo() ) {
                Intent intent = new Intent(IntentAction.ACTIVITY_BREEZING_TEST);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, mErrorInfo, Toast.LENGTH_SHORT).show();
            }
            return ;
        } else if (v == mBornDate) {
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
        }
    }

    private void showJobTypePicker() {
        JobTypePickerDialogFragment datePicker = (JobTypePickerDialogFragment) getSupportFragmentManager().findFragmentByTag("jobTypePicker");
        if (datePicker != null) {
            getSupportFragmentManager().beginTransaction().remove(datePicker);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(null);

        datePicker = JobTypePickerDialogFragment.newInstance();
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
        DatePickerDialogFragment datePicker = (DatePickerDialogFragment) getSupportFragmentManager().findFragmentByTag("datePicker");
        if (datePicker != null) {
            getSupportFragmentManager().beginTransaction().remove(datePicker);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(null);

        datePicker = DatePickerDialogFragment.newInstance();
        datePicker.setTitle(getString(R.string.title_select_born_date));
        datePicker.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {
            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                mYear = String.valueOf(params[0]);
                mMonth = String.valueOf(params[1]);
                mDay = String.valueOf(params[2]);
                mBornDate.setText(mYear + dialog.getString(R.string.year)
                                + mMonth + dialog.getString(R.string.month)
                                + mDay  + dialog.getString(R.string.day));
            }
        });

        datePicker.show(getSupportFragmentManager(), "datePicker");
    }

    private void showWeightPicker() {
        WeightPickerDialogFragment weightPicker = (WeightPickerDialogFragment) getSupportFragmentManager().findFragmentByTag("weightPicker");
        if (weightPicker != null) {
            getSupportFragmentManager().beginTransaction().remove(weightPicker);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(null);

        weightPicker = WeightPickerDialogFragment.newInstance();
        weightPicker.setTitle(getString(R.string.title_select_weight));
        weightPicker.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {
            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                mWeight.setText(String.valueOf(params[0]));
                mWeightUnit.setText(String.valueOf(params[1]));
                mHopeWeightUnit.setText(String.valueOf(params[1]));
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

        weightPicker = ExceptedWeightPickerDialogFragment.newInstance();
        weightPicker.setTitle(getString(R.string.title_select_hope_weight));
        weightPicker.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {

            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                mHopeWeight.setText(String.valueOf(params[0]));               
            }

        });

        weightPicker.show(getSupportFragmentManager(), "hopeWeightPicker");
    }

    private void showHeightPicker() {
        HeightPickerDialogFragment heightPicker = (HeightPickerDialogFragment) getSupportFragmentManager().findFragmentByTag("heightPicker");
        if (heightPicker != null) {
            getSupportFragmentManager().beginTransaction().remove(heightPicker);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(null);

        heightPicker = HeightPickerDialogFragment.newInstance();
        heightPicker.setTitle( getString(R.string.title_select_height) );
        heightPicker.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {
            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                mHeight.setText(String.valueOf(params[0]));
                mHeightUnit.setText(String.valueOf(params[1]));
            }

        });

        heightPicker.show(getSupportFragmentManager(), "heightPicker");
    }

    /***
     * 验证输入信息，并生成帐户信息,插入相应的数据
     */
    private boolean createAccountInfo() {
        boolean result = false;
        
        if ( !checkFillInputInfo() ) {
            return false;
        }
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(mYear);
        if ( Integer.parseInt(mMonth) < 10 ) {
            stringBuilder.append("0").append(mMonth);
        } else {
            stringBuilder.append(mMonth);
        }
        
        if ( Integer.parseInt(mDay) < 10 ) {
            stringBuilder.append("0").append(mDay);
        } else {
            stringBuilder.append(mDay);
        }       

    

        int date = DateFormatUtil.simpleDateFormat("yyyyMMdd", stringBuilder.toString());
        int accountId = createAccountId();

        appendAccount(mOps, mUserName.getText().toString(), accountId,
                      DEFAULT_PASSWORD);

        float height =  queryUnitUnifyData(
                           Float.parseFloat(mHeight.getText().toString()) ,
                           getResources().getString(R.string.height_type),
                           mHeightUnit.getText().toString());

        float exceptedHeight = queryUnitUnifyData(
                                Float.parseFloat(mHopeWeight.getText().toString()) ,
                                getResources().getString(R.string.height_type),
                                mHeightUnit.getText().toString());

        float weight = queryUnitUnifyData(
                       Float.parseFloat(mWeight.getText().toString()) ,
                       getResources().getString(R.string.weight_type),
                       mWeightUnit.getText().toString());

        int gender = 0;

        if ( mRadioMale.isChecked() ) {
            gender = ChangeUnitUtil.GENDER_MALE;
        } else if ( mRadioFemale.isChecked() ) {
            gender = ChangeUnitUtil.GENDER_FEMALE;
        }

        int custom = ChangeUnitUtil.changeCustomUtil(this, mJobType.getText().toString());
        String[] distanceUnits = getResources().getStringArray(R.array.distance_units);
        appendInformation(mOps, accountId, gender,
                          height, date, custom,
                          mHeightUnit.getText().toString(), mWeightUnit.getText().toString(),
                          distanceUnits[0]);

        appendWeightChange(mOps, accountId, weight, exceptedHeight);

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


    private final static int ACCOUNT_ID_INDEX = 0;

    /***
     * 生成帐户id
     * @return
     */
    private int createAccountId() {
        Cursor cursor = null;
        int accountId = 0;
        String sortOrder = Account.ACCOUNT_ID + " DESC";
        try {
            cursor = getContentResolver().query(Account.CONTENT_URI,
                    new String[] {Account.ACCOUNT_ID},
                    null,
                    null,
                    sortOrder);

            if (cursor != null) {
                if ( cursor.getCount() > 0 ) {
                    cursor.moveToPosition(0);
                    accountId = cursor.getInt(ACCOUNT_ID_INDEX);
                }
                
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return accountId + 1;
    }



    /***
     * 添加信息到 TABLE_ACCOUNT 表中
     * @param accountName
     * @param accountId
     * @param accountPass
     */
    private void appendAccount( ArrayList<ContentProviderOperation> ops, String accountName, int accountId, String accountPass) {
        Log.d(TAG, " appendAccount accountName = " + accountName + " accountPass = " + accountPass);
        ops.add(ContentProviderOperation.newInsert(Account.CONTENT_URI)
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
    private void appendInformation( ArrayList<ContentProviderOperation> ops, int accountId,
                                    int gender, float height, int birthday,
                                    int custom, String heightUnit, String weightUnit ,
                                    String distanceUnit) {
        ops.add(ContentProviderOperation.newInsert(Information.CONTENT_URI)
                .withValue(Information.ACCOUNT_ID, accountId)
                .withValue(Information.GENDER, gender)
                .withValue(Information.HEIGHT, height)
                .withValue(Information.BIRTHDAY, birthday)
                .withValue(Information.CUSTOM, custom)
                .withValue(Information.HEIGHT_UNIT, heightUnit)
                .withValue(Information.WEIGHT_UNIT, weightUnit)
                .withValue(Information.DISTANCE_UNIT, distanceUnit)
                .build());
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
                .withValue(WeightChange.EXPECTED_WEIGHT, expectedWeight)
                .build());
    }

    private final static int UNIT_SETTINGS_UNIFY_DATA = 0;

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
                cursor.moveToPosition(0);
                unifyUnit = cursor.getFloat(UNIT_SETTINGS_UNIFY_DATA);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        
        Log.d(TAG, " queryUnitUnifyData  data = " + data + " unifyUnit = " + unifyUnit);
        
        return data * unifyUnit;
    }

    public String getWeightUnit() {
        return mWeightUnit.getText().toString();
    }

    private static final String DEFAULT_PASSWORD = "888888";

}
