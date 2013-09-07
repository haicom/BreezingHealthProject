package com.breezing.health.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.breezing.health.R;
import com.breezing.health.tools.IntentAction;
import com.breezing.health.ui.fragment.BaseDialogFragment;
import com.breezing.health.ui.fragment.DatePickerDialogFragment;
import com.breezing.health.ui.fragment.DialogFragmentInterface;
import com.breezing.health.ui.fragment.HeightPickerDialogFragment;
import com.breezing.health.ui.fragment.JobTypePickerDialogFragment;
import com.breezing.health.ui.fragment.WeightPickerDialogFragment;

public class FillInInformationActivity extends ActionBarActivity implements OnClickListener {

    private Button mNext;
    
    private EditText mBornDate;
    private EditText mJobType;
    private EditText mHeight;
    private EditText mWeight;
    private EditText mHopeWeight;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_fillin_information);
        initValues();
        initViews();
        valueToView();
        initListeners();
    }

    private void initValues() {
        // TODO Auto-generated method stub
        
    }

    private void initViews() {
        // TODO Auto-generated method stub
        setActionBarTitle(R.string.title_fillin_personal_information);
        
        mBornDate = (EditText) findViewById(R.id.date);
        mBornDate.setFocusable(false);
        mJobType = (EditText) findViewById(R.id.jobType);
        mJobType.setFocusable(false);
        mHeight = (EditText) findViewById(R.id.height);
        mHeight.setFocusable(false);
        mWeight = (EditText) findViewById(R.id.weight);
        mWeight.setFocusable(false);
        mHopeWeight = (EditText) findViewById(R.id.hopeWeight);
        mHopeWeight.setFocusable(false);
        mNext = (Button) findViewById(R.id.next);
    }

    private void valueToView() {
        // TODO Auto-generated method stub
        
    }

    private void initListeners() {
        // TODO Auto-generated method stub
        mNext.setOnClickListener(this);
        mBornDate.setOnClickListener(this);
        mJobType.setOnClickListener(this);
        mWeight.setOnClickListener(this);
        mHopeWeight.setOnClickListener(this);
        mHeight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == mNext) {
            Intent intent = new Intent(IntentAction.ACTIVITY_BREEZING_TEST);
            startActivity(intent);
            finish();
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
                // TODO Auto-generated method stub
                
            }
            
        });
        datePicker.setNegativeClickListener(new DialogFragmentInterface.OnClickListener() {

            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                // TODO Auto-generated method stub
                
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
                // TODO Auto-generated method stub
                
            }
            
        });
        datePicker.setNegativeClickListener(new DialogFragmentInterface.OnClickListener() {

            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                // TODO Auto-generated method stub
                
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
                // TODO Auto-generated method stub
                
            }
            
        });
        weightPicker.setNegativeClickListener(new DialogFragmentInterface.OnClickListener() {

            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                // TODO Auto-generated method stub
                
            }
            
        });
        weightPicker.show(getSupportFragmentManager(), "weightPicker");
    }
    
    private void showHopeWeightPicker() {
        WeightPickerDialogFragment weightPicker = (WeightPickerDialogFragment) getSupportFragmentManager().findFragmentByTag("hopeWeightPicker");
        if (weightPicker != null) {
            getSupportFragmentManager().beginTransaction().remove(weightPicker);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(null);
        
        weightPicker = WeightPickerDialogFragment.newInstance();
        weightPicker.setTitle(getString(R.string.title_select_hope_weight));
        weightPicker.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {

            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                // TODO Auto-generated method stub
                
            }
            
        });
        weightPicker.setNegativeClickListener(new DialogFragmentInterface.OnClickListener() {

            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                // TODO Auto-generated method stub
                
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
        heightPicker.setTitle(getString(R.string.title_select_height));
        heightPicker.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {

            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                // TODO Auto-generated method stub
                
            }
            
        });
        heightPicker.setNegativeClickListener(new DialogFragmentInterface.OnClickListener() {

            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                // TODO Auto-generated method stub
                
            }
            
        });
        heightPicker.show(getSupportFragmentManager(), "heightPicker");
    }
    
}
