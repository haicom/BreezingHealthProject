package com.breezing.health.ui.activity;

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

import com.breezing.health.R;
import com.breezing.health.adapter.ExerciseRecordAdapter;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.ui.fragment.BaseDialogFragment;
import com.breezing.health.ui.fragment.DialogFragmentInterface;
import com.breezing.health.ui.fragment.SportIntensityPickerDialogFragment;
import com.breezing.health.ui.fragment.SportTypePickerDialogFragment;

public class ExerciseRecordActivity extends ActionBarActivity
              implements View.OnClickListener {

    private static final String TAG = "ExerciseRecordActivity";

    private ListView mRecordList;

    private View mRecordHeader;

    private ExerciseRecordAdapter mRecordAdapter;

    private TextView mTotalCaloric;

    private EditText mEditAmount;
    private Button mButtonType;
    private Button mButtonIntensity;
    private Button mButtonEquipment;
    private Button mButtonRecord;

    private String mSportType;
    private String mSportIntensity;
    private String mSportUnit;

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

    }

    private void initViews() {
        setActionBarTitle(R.string.manual_input_exercise_record);
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));

        mRecordList = (ListView) findViewById(R.id.list);
        mRecordHeader = getLayoutInflater().inflate(R.layout.list_exercise_record_header, null);
        mButtonType = (Button) mRecordHeader.findViewById(R.id.type);
        mButtonIntensity = (Button) mRecordHeader.findViewById(R.id.intensity);
        mButtonEquipment = (Button) mRecordHeader.findViewById(R.id.equipment);
        mButtonRecord = (Button) mRecordHeader.findViewById(R.id.record);

        mEditAmount =  (EditText) mRecordHeader.findViewById(R.id.amount);
        mTotalCaloric = (TextView) mRecordHeader.findViewById(R.id.totalCaloric);

        mRecordList.addHeaderView(mRecordHeader);

        mButtonType.setOnClickListener(this);
        mButtonIntensity.setOnClickListener(this);
        mButtonEquipment.setVisibility(View.GONE);
        mButtonEquipment.setOnClickListener(this);
        mButtonRecord.setOnClickListener(this);
    }

    private void valueToView() {
        updateTotalCaloric(1000);

        mRecordAdapter = new ExerciseRecordAdapter(this);
        mRecordList.setAdapter(mRecordAdapter);

    }

    private void initListeners() {

    }

    private void updateTotalCaloric(long count) {
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
        SportTypePickerDialogFragment sportType = (SportTypePickerDialogFragment) getSupportFragmentManager().findFragmentByTag("jobTypePicker");
        if (sportType != null) {
            getSupportFragmentManager().beginTransaction().remove(sportType);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(null);

        sportType = SportTypePickerDialogFragment.newInstance();
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
        SportIntensityPickerDialogFragment sportIntensity = (SportIntensityPickerDialogFragment) getSupportFragmentManager().findFragmentByTag("jobTypePicker");
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

    private void insertIngestiveRecord() {

    }



    @Override
    public void onClick(View v) {

        if ( mButtonType == v ) {
            showSportTypePicker();
        } else if ( mButtonIntensity == v) {
            showSportIntensityPicker();
        } else if (mButtonRecord == v) {

        }

    }

    private static final String EXERCISE_SPORT_TYPE = "sport_type_picker";
    private static final String EXERCISE_SPORT_INTENSITY = "sport_type_intensity";
}
