package com.breezing.health.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.breezing.health.R;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.ui.fragment.BaseDialogFragment;
import com.breezing.health.ui.fragment.DialogFragmentInterface;
import com.breezing.health.ui.fragment.WeightPickerDialogFragment;

public class WeightRecordActivity extends ActionBarActivity implements OnClickListener {
    
    private TextView mNotice;
    private TextView mHopeWeight;
    private TextView mDifferentValue;
    private EditText mWeight;
    private TextView mWeightUnit;
    private Button mRecord;
    private Button mConnect;
    
    private String mErrorInfo;

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
        
    }

    private void initViews() {
        setActionBarTitle(R.string.update_weight);
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));
        
        mNotice = (TextView) findViewById(R.id.notice);
        mHopeWeight = (TextView) findViewById(R.id.hopeWeight);
        mDifferentValue = (TextView) findViewById(R.id.different_value);
        
        mWeight = (EditText) findViewById(R.id.weight);
        mWeightUnit = (TextView) findViewById(R.id.weight_unit);
        mWeight.setFocusable(false);
        
        mRecord = (Button) findViewById(R.id.record);
        mConnect = (Button) findViewById(R.id.connect);
    }

    private void valueToView() {
        final String notice = getString(R.string.weight_update_notice, 2013, 2, 1, 100);
        mNotice.setText(notice);
        
        final String hopeWeight = getString(R.string.hope_weight_notice, 120);
        mHopeWeight.setText(hopeWeight);
        
        final String differentValue = getString(R.string.different_weight_value_notice, 20);
        mDifferentValue.setText(differentValue);
        
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

        weightPicker = WeightPickerDialogFragment.newInstance();
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
    
}
