package com.breezing.health.ui.fragment;

import net.simonvt.numberpicker.NumberPicker;
import net.simonvt.numberpicker.NumberPicker.OnValueChangeListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.breezing.health.R;

public class HeightPickerDialogFragment extends BaseDialogFragment implements OnClickListener {

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
    
    public static HeightPickerDialogFragment newInstance() {
        HeightPickerDialogFragment fragment = new HeightPickerDialogFragment();
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_NoTitleBar);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        
        mFragmentView = inflater.inflate(R.layout.fragment_dialog_height_picker, null);
        mInteger = (NumberPicker) mFragmentView.findViewById(R.id.integer);
        mDecimals = (NumberPicker) mFragmentView.findViewById(R.id.decimals);
        mUnit = (NumberPicker) mFragmentView.findViewById(R.id.unit);
        mTitle = (TextView) mFragmentView.findViewById(R.id.title);
        mCancel = (Button) mFragmentView.findViewById(R.id.cancel);
        mConfirm = (Button) mFragmentView.findViewById(R.id.confirm);
        
        mInteger.setMaxValue(300);
        mInteger.setMinValue(1);
        mInteger.setFocusable(true);
        mInteger.setFocusableInTouchMode(true);
        
        mDecimals.setMaxValue(9);
        mDecimals.setMinValue(1);
        mDecimals.setFocusable(true);
        mDecimals.setFocusableInTouchMode(true);
        
        final String[] units = getActivity().getResources().getStringArray(R.array.heightUnits);
        mUnit.setDisplayedValues(units);
        mUnit.setMaxValue(units.length - 1);
        mUnit.setMinValue(0);
        mUnit.setFocusable(true);
        mUnit.setFocusableInTouchMode(true);
        mUnit.setOnValueChangedListener(new OnValueChangeListener() {
            
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // TODO Auto-generated method stub
                switch(newVal) {
                case 0:
                    mInteger.setMaxValue(300);
                    mInteger.setMinValue(1);
                    mDecimals.setMaxValue(9);
                    mDecimals.setMinValue(1);
                    return ;
                case 1:
                    mInteger.setMaxValue(2);
                    mInteger.setMinValue(0);
                    mDecimals.setMaxValue(99);
                    mDecimals.setMinValue(1);
                    return ;
                case 2:
                    mInteger.setMaxValue(1);
                    mInteger.setMinValue(0);
                    mDecimals.setMaxValue(99);
                    mDecimals.setMinValue(1);
                    return ;
                case 3:
                    mInteger.setMaxValue(1);
                    mInteger.setMinValue(0);
                    mDecimals.setMaxValue(99);
                    mDecimals.setMinValue(1);
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
        // TODO Auto-generated method stub
        if (v == mConfirm) {
            if (mPositiveClickListener != null) {
                mPositiveClickListener.onClick(this);
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
    
}
