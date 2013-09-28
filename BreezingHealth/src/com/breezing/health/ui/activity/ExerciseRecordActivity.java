package com.breezing.health.ui.activity;



import java.util.ArrayList;
import java.util.HashSet;

import android.content.AsyncQueryHandler;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.breezing.health.R;
import com.breezing.health.adapter.ExerciseRecordAdapter;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.providers.Breezing;
import com.breezing.health.providers.Breezing.EnergyCost;
import com.breezing.health.providers.Breezing.HeatConsumption;
import com.breezing.health.providers.Breezing.HeatConsumptionRecord;
import com.breezing.health.ui.fragment.BaseDialogFragment;
import com.breezing.health.ui.fragment.DialogFragmentInterface;
import com.breezing.health.ui.fragment.SportIntensityPickerDialogFragment;
import com.breezing.health.ui.fragment.SportTypePickerDialogFragment;
import com.breezing.health.util.BLog;
import com.breezing.health.util.DateFormatUtil;
import com.breezing.health.util.ExtraName;
import com.breezing.health.util.LocalSharedPrefsUtil;

public class ExerciseRecordActivity extends ActionBarActivity
              implements View.OnClickListener {

    private static final String TAG = "ExerciseRecordActivity";

    private ListView mRecordList;

    private ExerciseRecordAdapter mRecordAdapter;
    private BackgroundQueryHandler mBackgroundQueryHandler;
    private ContentResolver mContentResolver;

    private TextView mTotalCaloric;

    private EditText mEditAmount;
    private Button mButtonType;
    private Button mButtonIntensity;
    private Button mButtonEquipment;
    private Button mButtonRecord;

    private String mSportType;
    private String mSportIntensity;
    private String mSportUnit;

    private String mErrorInfo;
    private String[] mIntensitys;

    private HashSet<String> mHashSet;

    private int mAccountId;
    private int mDate;

    private float mTotalCalorie;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onRestart");
        setContentFrame(R.layout.activity_exercise_record);
        initValues();
        initViews();
        valueToView();
        initListeners();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    private void initValues() {
        mAccountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this,
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);

        Bundle bundle = getIntent().getExtras();

        mDate = bundle.getInt(ExtraName.EXTRA_DATE, 0);

        if (mDate == 0) {
            mDate = DateFormatUtil.simpleDateFormat("yyyyMMdd");
        }

        mHashSet = new HashSet<String>();

        mIntensitys = getResources().getStringArray(R.array.sport_intensity);

        mContentResolver = getContentResolver();
        mBackgroundQueryHandler = new BackgroundQueryHandler(mContentResolver);



    }

    private void initViews() {
        setActionBarTitle(R.string.manual_input_exercise_record);
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));

        mRecordList = (ListView) findViewById(R.id.list);
      //
        View recordHeader = getLayoutInflater().inflate(R.layout.exercise_record_header, null);
        mButtonType = (Button) recordHeader.findViewById(R.id.type);
        mButtonIntensity = (Button) recordHeader.findViewById(R.id.intensity);
        mButtonEquipment = (Button) recordHeader.findViewById(R.id.equipment);
        mButtonRecord = (Button) recordHeader.findViewById(R.id.record);

        mEditAmount =  (EditText) recordHeader.findViewById(R.id.amount);
        mTotalCaloric = (TextView) recordHeader.findViewById(R.id.totalCaloric);
        mRecordList.addHeaderView(recordHeader);

        View recordHeaderTitle = getLayoutInflater().inflate(R.layout.list_exercise_record_header_title, null);
        mRecordList.addHeaderView(recordHeaderTitle);
        
        View recordFooter = getLayoutInflater().inflate(R.layout.list_exercise_record_footer, null);
        mRecordList.addFooterView(recordFooter);

        mButtonType.setOnClickListener(this);



    }

    private void valueToView() {

        querySportType();
        final String[] types = (String[]) mHashSet.toArray(new String[0]);

        mSportType = types[0];
        mButtonType.setText(mSportType);

        mSportIntensity = mIntensitys[0];
        mSportUnit = getResources().
                getString(R.string.sport_type_gengeral);
        mButtonIntensity.setText( getString(R.string.intensity_unit,
                                  mSportIntensity,
                                  mSportUnit ) );
        mRecordAdapter = new ExerciseRecordAdapter(this, null);
        mRecordAdapter.setOnDataSetChangedListener(mDataSetChangedListener);
        mRecordList.setAdapter(mRecordAdapter);
        mEditAmount.setHint(mSportUnit);
        startMsgListQuery();

    }

    private void initListeners() {
        mButtonIntensity.setOnClickListener(this);
        mButtonEquipment.setVisibility(View.GONE);
        mButtonEquipment.setOnClickListener(this);
        mButtonRecord.setOnClickListener(this);

    }

    private void updateTotalCaloric(float count) {
        final String title = getString(R.string.title_total_exercise_caloric);
        final String unit = getString(R.string.kilojoule);

        SpannableString span = new SpannableString(title + String.valueOf(count) + unit);
        span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)),
                title.length(),
                title.length() + String.valueOf(count).length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new RelativeSizeSpan(1.5f),
                title.length(), title.length() + String.valueOf(count).length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTotalCaloric.setText(span);
    }

    private void showSportTypePicker() {

        SportTypePickerDialogFragment sportType = (SportTypePickerDialogFragment)
                getSupportFragmentManager().findFragmentByTag(EXERCISE_SPORT_TYPE);

        if (sportType != null) {
            getSupportFragmentManager().beginTransaction().remove(sportType);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(null);

        sportType = SportTypePickerDialogFragment.newInstance(mHashSet);
        sportType.setTitle( getString(R.string.exercise_intensity) );

        sportType.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {
            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {

                mButtonType.setText( params[0].toString() );
                mSportType = params[0].toString();
                if (mButtonType.equals(
                        dialog.getString(R.string.sport_type_runing) ) ) {
                    mButtonEquipment.setVisibility(View.VISIBLE);
                }

            }
        });

        sportType.show(getSupportFragmentManager(), EXERCISE_SPORT_TYPE);
    }

    private void showSportIntensityPicker() {

        SportIntensityPickerDialogFragment sportIntensity = (SportIntensityPickerDialogFragment)
                getSupportFragmentManager().findFragmentByTag(EXERCISE_SPORT_INTENSITY);

        if (sportIntensity != null) {
            getSupportFragmentManager().beginTransaction().remove(sportIntensity);
        }

        getSupportFragmentManager().beginTransaction().addToBackStack(null);

        sportIntensity = SportIntensityPickerDialogFragment.newInstance(mSportType);
        sportIntensity.setTitle( getString(R.string.exercise_type) );

        sportIntensity.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {
            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {

                mButtonIntensity.setText(dialog.getString(R.string.intensity_unit,
                                                 params[0].toString(),
                                                 params[1].toString() ) );

                mSportIntensity = params[0].toString();
                mSportUnit = params[1].toString();
                mEditAmount.setHint(mSportUnit);
            }
        });

        sportIntensity.show(getSupportFragmentManager(), EXERCISE_SPORT_INTENSITY);
    }

    private boolean checkFillInputRecord() {

        boolean bResult = true;
        mErrorInfo = null;

        if (mButtonType.getText().length() == 0) {
            mErrorInfo = getResources().getString(R.string.info_prompt)
                       + getResources().getString(R.string.exercise_type) ;
            bResult = false;
        } else if (mButtonIntensity.getText().length() == 0) {
            mErrorInfo = getResources().getString(R.string.info_prompt)
                    + getResources().getString(R.string.exercise_intensity) ;
            bResult = false;
        } else if (mEditAmount.getText().length() == 0) {
            mErrorInfo = getResources().getString(R.string.info_prompt)
                    + getResources().getString(R.string.edittext_hint_exercise_amount) ;
            bResult = false;
        }

        return bResult;
    }

    private boolean appendIngestiveRecord() {
        boolean result = false;

        if ( !checkFillInputRecord() ) {
            return false;
        }

        float calorie = obtainSportCalorie();
        float totalCalorie = calorie * Integer.valueOf( mEditAmount.getText().toString() );

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ops.add(ContentProviderOperation.newInsert(HeatConsumptionRecord.CONTENT_URI)
                .withValue(HeatConsumptionRecord.ACCOUNT_ID, mAccountId)
                .withValue(HeatConsumptionRecord.SPORT_TYPE, mSportType)
                .withValue(HeatConsumptionRecord.SPORT_INTENSITY, mSportIntensity)
                .withValue(HeatConsumptionRecord.SPORT_QUANTITY, Integer.valueOf( mEditAmount.getText().toString() ) )
                .withValue(HeatConsumptionRecord.SPORT_UNIT, mSportUnit)
                .withValue(HeatConsumptionRecord.CALORIE, totalCalorie)
                .build());

        try {
            getContentResolver().applyBatch(Breezing.AUTHORITY, ops);
            result = true;
        } catch (Exception e) {
            mErrorInfo = getResources().getString(R.string.data_error);
            // Log exception
            Log.e(TAG, "Exceptoin encoutered while inserting contact: " + e);
        }

        return result;
    }

    private float obtainSportCalorie() {
        float  calorie = 0;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(HeatConsumption.SPORT_TYPE + " = ? AND ");
        stringBuilder.append(HeatConsumption.SPORT_INTENSITY + " = ? AND ");
        stringBuilder.append(HeatConsumption.SPORT_UNIT + " = ? ");

        Cursor cursor = null;

        try {
            cursor = getContentResolver().query(HeatConsumption.CONTENT_URI,
                    new String[] { HeatConsumption.CALORIE },
                    stringBuilder.toString(),
                    new String[] {mSportType, mSportIntensity, mSportUnit},
                    null);

            if (cursor != null) {
                if ( cursor.getCount() > 0 ) {
                    cursor.moveToPosition(0);
                    calorie = cursor.getFloat(0);
                }

            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.d(TAG, " obtainSportCalorie  calorie = " + calorie);

        return calorie;
    }

    private void querySportType(){
        Cursor cursor  = getContentResolver().query(
                HeatConsumption.CONTENT_SPORT_TYPE,
                new String[] { HeatConsumption.SPORT_TYPE }, null, null, null);

        if (cursor == null) {
            BLog.d(TAG, " querySportType cursor = " + cursor);
        }

        try {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext() ) {
                String sportType = cursor.getString(0);
                mHashSet.add(sportType);
            }
        } finally {
            cursor.close();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRecordAdapter != null) {
            mRecordAdapter.changeCursor(null);
        }
    }


    private final ExerciseRecordAdapter.OnDataSetChangedListener
        mDataSetChangedListener = new ExerciseRecordAdapter.OnDataSetChangedListener() {
            public void onDataSetChanged(ExerciseRecordAdapter adapter) {

            }

            public void onContentChanged(ExerciseRecordAdapter adapter) {
                Log.d(TAG, "MessageListAdapter.OnDataSetChangedListener.onContentChanged");
                startMsgListQuery();
           }
   };

   private final class BackgroundQueryHandler extends AsyncQueryHandler {
       public BackgroundQueryHandler(ContentResolver contentResolver) {
           super(contentResolver);
       }

       @Override
       protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
           switch(token) {
               case MESSAGE_LIST_QUERY_TOKEN:
                   // check consistency between the query result and 'mConversation'
                   long tid = (Long) cookie;
                   mTotalCalorie = 0;

                   if (cursor != null) {
                       cursor.moveToPosition(-1);
                       while (cursor.moveToNext() ) {
                           float  calorie = cursor.getFloat(CALORIE_INDEX);
                           mTotalCalorie += calorie;

                       }
                   }

                   updateTotalCaloric(mTotalCalorie);

                   mRecordAdapter.changeCursor(cursor);
                   return;
           }
       }
   }

   private void startMsgListQuery() {

       StringBuilder stringBuilder = new StringBuilder();
       stringBuilder.setLength(0);
       stringBuilder.append(HeatConsumptionRecord.ACCOUNT_ID + " = ? AND ");
       stringBuilder.append(HeatConsumptionRecord.DATE + " = ? ");

       long threadId = 0;


       // Cancel any pending queries
       mBackgroundQueryHandler.cancelOperation(MESSAGE_LIST_QUERY_TOKEN);
       try {
           // Kick off the new query
           mBackgroundQueryHandler.startQuery(
                   MESSAGE_LIST_QUERY_TOKEN,
                   threadId /* cookie */,
                   HeatConsumptionRecord.CONTENT_URI,
                   PROJECTION_CONSUMPTION_RECORD,
                   stringBuilder.toString(),
                   new String[] {String.valueOf(mAccountId), String.valueOf(mDate) },
                   null);
       } catch (SQLiteException e) {

       }
   }




    /**
     * 查询基本信息视图列表
     */
    private static final String[] PROJECTION_CONSUMPTION_RECORD = new String[] {
        HeatConsumptionRecord._ID,
        HeatConsumptionRecord.SPORT_TYPE,          // 0
        HeatConsumptionRecord.SPORT_INTENSITY,      // 1
        HeatConsumptionRecord.SPORT_QUANTITY,    // 2
        HeatConsumptionRecord.SPORT_UNIT ,     //3
        HeatConsumptionRecord.CALORIE   //4

    };

    private static final int SPORT_ID_INDEX = 0;
    private static final int SPORT_TYPE_INDEX = 1;
    private static final int SPORT_INTENSITY_INDEX = 2;
    private static final int SPORT_QUANTITY_INDEX = 3;
    private static final int SPORT_UNIT_INDEX = 4;
    private static final int CALORIE_INDEX = 5;
//
//
//    private void queryHeatConsumptionRecord(int accountId, int date) {
//
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.setLength(0);
//        stringBuilder.append(HeatConsumptionRecord.ACCOUNT_ID + " = ? AND ");
//        stringBuilder.append(HeatConsumptionRecord.DATE + " = ? ");
//
//
//        Cursor cursor = null;
//
//
//        cursor = getContentResolver().query(HeatConsumptionRecord.CONTENT_URI,
//                    PROJECTION_CONSUMPTION_RECORD,
//                    stringBuilder.toString(),
//                    new String[] {String.valueOf(accountId), String.valueOf(date) },
//                    null);
//
//        if (cursor == null) {
//            Log.d(TAG, " testBaseInfoView cursor = " + cursor);
//        }
//
//
//        try {
//
//            cursor.moveToPosition(-1);
//            while (cursor.moveToNext() ) {
//                ExerciseRecordEntity exerciseRecord = new ExerciseRecordEntity();
//                String  sportType = cursor.getString(SPORT_TYPE_INDEX);
//                String  sportIntensity = cursor.getString(SPORT_INTENSITY_INDEX);
//                int     sportQuantity = cursor.getInt(SPORT_QUANTITY_INDEX);
//                String  sportUnit = cursor.getString(SPORT_UNIT_INDEX);
//                float    calorie = cursor.getFloat(CALORIE_INDEX);
//                exerciseRecord.setName(sportType);
//                exerciseRecord.setDes(sportIntensity);
//                exerciseRecord.setCaloric(calorie);
//            }
//
//        } finally {
//            cursor.close();
//        }
//    }


    @Override
    public void onClick(View v) {

        if ( mButtonType == v ) {
            showSportTypePicker();
        } else if ( mButtonIntensity == v) {
            showSportIntensityPicker();
        } else if (mButtonRecord == v) {
           if ( !appendIngestiveRecord() ) {
               Toast.makeText(this, mErrorInfo, Toast.LENGTH_SHORT).show();
           }
        }

    }

    @Override
    public void onClickActionBarItems(ActionItem item, View v) {

        switch( item.getActionId() ) {

            case ActionItem.ACTION_BACK: {
                updateEnergyCost();
            }
        }

        super.onClickActionBarItems(item, v);
    }

    private void updateEnergyCost() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(HeatConsumptionRecord.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(HeatConsumptionRecord.DATE + " = ? ");

        ContentValues values = new ContentValues();
        values.put(EnergyCost.TRAIN, mTotalCalorie);

        mContentResolver.update(EnergyCost.CONTENT_URI,
                values, stringBuilder.toString(),
                new String[] {
                String.valueOf(mAccountId),
                String.valueOf(mDate) });
    }

    private static final int MESSAGE_LIST_QUERY_TOKEN = 9527;

    private static final String EXERCISE_SPORT_TYPE = "sport_type_picker";
    private static final String EXERCISE_SPORT_INTENSITY = "sport_type_intensity";
}
