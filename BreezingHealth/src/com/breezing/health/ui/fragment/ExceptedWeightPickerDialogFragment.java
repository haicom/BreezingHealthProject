package com.breezing.health.ui.fragment;

import net.simonvt.numberpicker.NumberPicker;
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
import com.breezing.health.ui.activity.FillInInformationActivity;

public class ExceptedWeightPickerDialogFragment extends BaseDialogFragment implements OnClickListener {
    
    private final String TAG = "ExceptedWeightPickerDialogFragment";
    
    private View mFragmentView;
    
    private NumberPicker mInteger;
    private NumberPicker mDecimals;
    
    private TextView mTitle;
    
    private Button mCancel;
    private Button mConfirm;
    
    private DialogFragmentInterface.OnClickListener mPositiveClickListener;
    private DialogFragmentInterface.OnClickListener mNegativeClickListener;
    
    private String mTitleString;
    
    private float mHopeWeight;
    private String mUnit;

    public static ExceptedWeightPickerDialogFragment newInstance(float hopeWeight, String unit) {
        ExceptedWeightPickerDialogFragment fragment = new ExceptedWeightPickerDialogFragment(hopeWeight, unit);
        return fragment;
    }
    
    private ExceptedWeightPickerDialogFragment(float hopeWeight, String unit) {
        mHopeWeight = hopeWeight;
        mUnit = unit;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_NoTitleBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
        Log.d(TAG, "onCreateView");
        
        mFragmentView = inflater.inflate(R.layout.fragment_dialog_excepted_weight_picker, null);
        
        mInteger = (NumberPicker) mFragmentView.findViewById(R.id.integer);
        mDecimals = (NumberPicker) mFragmentView.findViewById(R.id.decimals);
        mTitle = (TextView) mFragmentView.findViewById(R.id.title);
        mCancel = (Button) mFragmentView.findViewById(R.id.cancel);
        mConfirm = (Button) mFragmentView.findViewById(R.id.confirm);

       
        if ( mUnit.isEmpty() ) {
            
            mInteger.setMaxValue(SHOW_INTEGER_JIN_MAX);
            mInteger.setMinValue(SHOW_INTEGER_JIN_MIN);
            mInteger.setValue(SHOW_INTEGER_CURRENT_JIN);           
            mDecimals.setMaxValue(SHOW_DECIMALS_MAX);
            mDecimals.setMinValue(SHOW_DECIMALS_MIN);
        }
        else if ( mUnit.equals( getString(R.string.weight_jin) ) ) {
            
            mInteger.setMaxValue(SHOW_INTEGER_JIN_MAX);
            mInteger.setMinValue(SHOW_INTEGER_JIN_MIN);

            if (mHopeWeight == 0 ) {
                mInteger.setValue(SHOW_INTEGER_CURRENT_JIN);
            } else {
                mInteger.setValue( (int) Math.floor(mHopeWeight) );
            }
            
           
            mDecimals.setMaxValue(SHOW_DECIMALS_MAX);
            mDecimals.setMinValue(SHOW_DECIMALS_MIN);
            
            
        } else if ( mUnit.equals( getString(R.string.weight_kilo) ) ) {
            
            mInteger.setMaxValue(SHOW_INTEGER_KILO_MAX);
            mInteger.setMinValue(SHOW_INTEGER_KILO_MIN);
            
            if (mHopeWeight == 0 ) {
                mInteger.setValue(SHOW_INTEGER_CURRENT_KILO);
            } else {
                mInteger.setValue( (int) Math.floor(mHopeWeight) );
            }
          
            mDecimals.setMaxValue(SHOW_DECIMALS_MAX);
            mDecimals.setMinValue(SHOW_DECIMALS_MIN);
            
           
            
        } else if ( mUnit.equals( getString(R.string.weight_pound) ) ) {
            
            mInteger.setMaxValue(SHOW_INTEGER_POUND_MAX);
            mInteger.setMinValue(SHOW_INTEGER_POUND_MIN);
           
            if (mHopeWeight == 0 ) {
                mInteger.setValue(SHOW_INTEGER_CURRENT_POUND);
            } else {
                mInteger.setValue( (int) Math.floor(mHopeWeight) );
            }
            mDecimals.setMaxValue(SHOW_DECIMALS_MAX);
            mDecimals.setMinValue(SHOW_DECIMALS_MIN);
            
        }
        
        if (mHopeWeight != 0) {
            mDecimals.setValue( ( (int)( mHopeWeight * 10 ) ) % 10);
        }
        
        mInteger.setFocusable(false);
        mInteger.setFocusableInTouchMode(false);
        mDecimals.setFocusable(false);
        mDecimals.setFocusableInTouchMode(false);

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
                mPositiveClickListener.onClick(this, weight);
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

    private static final int SHOW_INTEGER_JIN_MAX = 800;
    private static final int SHOW_INTEGER_JIN_MIN = 0;
    private static final int SHOW_INTEGER_CURRENT_JIN = 120;

    private static final int SHOW_INTEGER_KILO_MAX = 400;
    private static final int SHOW_INTEGER_KILO_MIN = 0;
    private static final int SHOW_INTEGER_CURRENT_KILO = 60;

    private static final int SHOW_INTEGER_POUND_MAX = 1000;
    private static final int SHOW_INTEGER_POUND_MIN = 0;
    private static final int SHOW_INTEGER_CURRENT_POUND = 200;

    private static final int SHOW_DECIMALS_MAX = 9;
    private static final int SHOW_DECIMALS_MIN = 0;
}