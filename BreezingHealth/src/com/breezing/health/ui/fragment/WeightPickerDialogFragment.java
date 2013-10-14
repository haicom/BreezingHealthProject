package com.breezing.health.ui.fragment;

import net.simonvt.numberpicker.NumberPicker;
import net.simonvt.numberpicker.NumberPicker.OnValueChangeListener;

import com.breezing.health.R;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class WeightPickerDialogFragment extends BaseDialogFragment implements OnClickListener {
    private final String TAG = "WeightPickerDialogFragment";
    
    private View mFragmentView;
    
    private NumberPicker mInteger;
    private NumberPicker mDecimals;
    private NumberPicker mUnit;
    
    private TextView mTitle;
    
    private Button mCancel;
    private Button mConfirm;
    
    private DialogFragmentInterface.OnClickListener mPositiveClickListener;
    private DialogFragmentInterface.OnClickListener mNegativeClickListener;
    
    private String mTitleString;
    
    private float mWeight;
    private String mUnitString;

    public static WeightPickerDialogFragment newInstance(float weight, String unit) {
        WeightPickerDialogFragment fragment = new WeightPickerDialogFragment(weight, unit);
        return fragment;
    }
    
    private WeightPickerDialogFragment(float weight, String unit) {
        mWeight = weight;
        mUnitString = unit;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {       
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_NoTitleBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    
        mFragmentView = inflater.inflate(R.layout.fragment_dialog_weight_picker, null);
        mInteger = (NumberPicker) mFragmentView.findViewById(R.id.integer);
        mDecimals = (NumberPicker) mFragmentView.findViewById(R.id.decimals);
        mUnit = (NumberPicker) mFragmentView.findViewById(R.id.unit);
        mTitle = (TextView) mFragmentView.findViewById(R.id.title);
        mCancel = (Button) mFragmentView.findViewById(R.id.cancel);
        mConfirm = (Button) mFragmentView.findViewById(R.id.confirm);

        mInteger.setMaxValue(SHOW_INTEGER_JIN_MAX);
        mInteger.setMinValue(SHOW_INTEGER_JIN_MIN);
       
        if (mWeight == 0 ) {
            mInteger.setValue(SHOW_INTEGER_CURRENT_JIN);
        } else {
            mInteger.setValue( (int) Math.floor(mWeight) );
        }
        
        mInteger.setFocusable(false);
        mInteger.setFocusableInTouchMode(false);

        mDecimals.setMaxValue(SHOW_DECIMALS_MAX);
        mDecimals.setMinValue(SHOW_DECIMALS_MIN);
        
        if (mWeight != 0) {
            mDecimals.setValue( ( (int)( mWeight * 10 ) ) % 10);
        }
       
        mDecimals.setFocusable(false);
        mDecimals.setFocusableInTouchMode(false);

        final String[] units = getActivity().getResources().getStringArray(R.array.weightUnits);
        mUnit.setDisplayedValues(units);
        mUnit.setMaxValue(units.length - 1);
        mUnit.setMinValue(0);
        
        int value = 0;
        
        if ( !mUnitString.isEmpty() ) {            
            for (value = 0; value < units.length; value++) {
                if ( mUnitString.equals(units[value]) ) {
                    break;
                }
            }
            mUnit.setValue(value);
        }
        
       
        
        mUnit.setFocusable(false);
        mUnit.setFocusableInTouchMode(false);
        mUnit.setOnValueChangedListener(new OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                switch(newVal) {
                case WEIGHT_JIN:
                    mInteger.setMaxValue(SHOW_INTEGER_JIN_MAX);
                    mInteger.setMinValue(SHOW_INTEGER_JIN_MIN);
                    mInteger.setValue(SHOW_INTEGER_CURRENT_JIN);
                    mDecimals.setMaxValue(SHOW_DECIMALS_MAX);
                    mDecimals.setMinValue(SHOW_DECIMALS_MIN);
                    return ;
                case WEIGHT_KILO:
                    mInteger.setMaxValue(SHOW_INTEGER_KILO_MAX);
                    mInteger.setMinValue(SHOW_INTEGER_KILO_MIN);
                    mInteger.setValue(SHOW_INTEGER_CURRENT_KILO);
                    mDecimals.setMaxValue(SHOW_DECIMALS_MAX);
                    mDecimals.setMinValue(SHOW_DECIMALS_MIN);
                    return ;
                case WEIGHT_POUND:
                    mInteger.setMaxValue(SHOW_INTEGER_POUND_MAX);
                    mInteger.setMinValue(SHOW_INTEGER_POUND_MIN);
                    mInteger.setValue(SHOW_INTEGER_CURRENT_POUND);
                    mDecimals.setMaxValue(SHOW_DECIMALS_MAX);
                    mDecimals.setMinValue(SHOW_DECIMALS_MIN);
                    return ;
                }
            }
        });

        if (mTitleString != null) {
            mTitle.setText(mTitleString);
        }

        mCancel.setOnClickListener(this);
        mConfirm.setOnClickListener(this);

        getDialog().getWindow().setBackgroundDrawable(new
                ColorDrawable(Color.TRANSPARENT));

        return mFragmentView;
    }

    public void setTitle(String titleString) {
        mTitleString = titleString;
    }

    public void setPositiveClickListener(DialogFragmentInterface.OnClickListener listener) {
        mPositiveClickListener = listener;
    }

    public void setNegativeClickListener(DialogFragmentInterface.OnClickListener listener) {
        mNegativeClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v == mConfirm) {
            if (mPositiveClickListener != null) {
                float weight = mInteger.getValue() +  (float) mDecimals.getValue()/(mDecimals.getMaxValue() + 1);
                Log.d(TAG, " onClick weight = " + weight + " mInteger.getValue() = " + mInteger.getValue()
                        + " mDecimals.getValue() = " + mDecimals.getValue() + " mDecimals.getMaxValue() = " + mDecimals.getMaxValue());
                String[] unitValues = mUnit.getDisplayedValues();
                String unitValue = unitValues[mUnit.getValue()];
                mPositiveClickListener.onClick(this, weight, unitValue);
            }
            dismiss();
            return ;
        } else if (v == mCancel) {
            if (mNegativeClickListener != null) {
                mNegativeClickListener.onClick(this);
            }
            dismiss();
            return ;
        }
    }

    private static final int WEIGHT_JIN = 0;
    private static final int WEIGHT_KILO = 1;
    private static final int WEIGHT_POUND = 2;

    private static final int SHOW_INTEGER_JIN_MAX = 800;
    private static final int SHOW_INTEGER_JIN_MIN = 50;
    private static final int SHOW_INTEGER_CURRENT_JIN = 120;

    private static final int SHOW_INTEGER_KILO_MAX = 400;
    private static final int SHOW_INTEGER_KILO_MIN = 25;
    private static final int SHOW_INTEGER_CURRENT_KILO = 60;

    private static final int SHOW_INTEGER_POUND_MAX = 1000;
    private static final int SHOW_INTEGER_POUND_MIN = 50;
    private static final int SHOW_INTEGER_CURRENT_POUND = 200;

    private static final int SHOW_DECIMALS_MAX = 9;
    private static final int SHOW_DECIMALS_MIN = 0;
}