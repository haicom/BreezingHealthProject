package com.breezing.health.ui.fragment;

import net.simonvt.numberpicker.NumberPicker;
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
import com.breezing.health.util.CalendarUtil;

public class YearIntervalPickerDialogFragment extends BaseDialogFragment implements OnClickListener {

    private View mFragmentView;
    private NumberPicker mYear;
    private TextView mTitle;
    private Button mCancel;
    private Button mConfirm;
    private DialogFragmentInterface.OnClickListener mPositiveClickListener;
    private DialogFragmentInterface.OnClickListener mNegativeClickListener;
    private String mTitleString;
    
    public static YearIntervalPickerDialogFragment newInstance() {
    	YearIntervalPickerDialogFragment fragment = new YearIntervalPickerDialogFragment();
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
        mFragmentView = inflater.inflate(R.layout.fragment_dialog_year_interval_picker, null);
        mYear = (NumberPicker) mFragmentView.findViewById(R.id.year);
        mTitle = (TextView) mFragmentView.findViewById(R.id.title);
        mCancel = (Button) mFragmentView.findViewById(R.id.cancel);
        mConfirm = (Button) mFragmentView.findViewById(R.id.confirm);
        
        final String[] years = CalendarUtil.getYearsFrom2013();
        mYear.setDisplayedValues(years);
        mYear.setMaxValue(years.length - 1);
        mYear.setMinValue(0);
        mYear.setFocusable(false);
        mYear.setFocusableInTouchMode(false);
        
        if (mTitleString != null) {
            mTitle.setText(mTitleString);
        }
        
        mCancel.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        
        getDialog().getWindow().setBackgroundDrawable(new
                ColorDrawable(Color.TRANSPARENT));
        
        return mFragmentView;
    }
    
    
    public int getYear() {
        return  mYear.getValue();   
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
                mPositiveClickListener.onClick( this, mYear.getValue());
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