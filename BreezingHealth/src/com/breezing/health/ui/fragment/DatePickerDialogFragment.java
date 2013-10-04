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
import com.breezing.health.util.DateFormatUtil;

public class DatePickerDialogFragment extends BaseDialogFragment implements OnClickListener {

    private View mFragmentView;
    
    private NumberPicker mYearPicker;
    private NumberPicker mMonthPicker;
    private NumberPicker mDayPicker;
    
    private TextView mTitle;
    
    private Button mCancel;
    private Button mConfirm;
    
    private DialogFragmentInterface.OnClickListener mPositiveClickListener;
    private DialogFragmentInterface.OnClickListener mNegativeClickListener;
    
    private String mTitleString;
    
    private int mYear;
    private int mMonth;
    private int mDay;
    
    public static DatePickerDialogFragment newInstance(int year, int month, int day) {
        DatePickerDialogFragment fragment = new DatePickerDialogFragment(year, month, day);
        return fragment;
    }
    
    private DatePickerDialogFragment(int year, int month, int day) {
        mYear = year;
        mMonth = month;
        mDay = day;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {       
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_NoTitleBar);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {        
        mFragmentView = inflater.inflate(R.layout.fragment_dialog_date_picker, null);
        
        mYearPicker = (NumberPicker) mFragmentView.findViewById(R.id.year);
        mMonthPicker = (NumberPicker) mFragmentView.findViewById(R.id.month);
        mDayPicker = (NumberPicker) mFragmentView.findViewById(R.id.day);
        
        mTitle = (TextView) mFragmentView.findViewById(R.id.title);
        
        mCancel = (Button) mFragmentView.findViewById(R.id.cancel);        
        mConfirm = (Button) mFragmentView.findViewById(R.id.confirm);
        
        mYearPicker.setMaxValue(SHOW_YEAR_MAX);
        mYearPicker.setMinValue(SHOW_YEAR_MIN);
        if ( mYear == 0 ) {
            mYearPicker.setValue(SHOW_CURRENT_YEAR);
        } else {
            mYearPicker.setValue(mYear);
        }
       
        mYearPicker.setFocusable(false);
        mYearPicker.setFocusableInTouchMode(false);
        
        mMonthPicker.setMaxValue(SHOW_MONTH_MAX);
        mMonthPicker.setMinValue(SHOW_MONTH_MIN);
        mMonthPicker.setValue(mMonth);
        mMonthPicker.setFocusable(false);
        mMonthPicker.setFocusableInTouchMode(false);
        
        mDayPicker.setMaxValue(SHOW_DAY_MAX);
        mDayPicker.setMinValue(SHOW_DAT_MIN);
        mDayPicker.setValue(mDay);
        mDayPicker.setFocusable(true);
        mDayPicker.setFocusableInTouchMode(true);
        
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
        return  mYearPicker.getValue();   
    }
    
    public int getMonth() {
        return mMonthPicker.getValue();
    }
    
    public int getDay() {
        return mDayPicker.getValue();
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
                mPositiveClickListener.onClick( this, 
                        mYearPicker.getValue(), 
                        mMonthPicker.getValue(), 
                        mDayPicker.getValue() );
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
    
    private static final int SHOW_YEAR_MAX = DateFormatUtil.simpleDateFormat("yyyy") -10;
    private static final int SHOW_YEAR_MIN = SHOW_YEAR_MAX - 80;
    private static final int SHOW_CURRENT_YEAR = SHOW_YEAR_MAX - 30;
    private static final int SHOW_MONTH_MAX = 12;
    private static final int SHOW_MONTH_MIN = 1;
    private static final int SHOW_DAY_MAX = 31;
    private static final int SHOW_DAT_MIN = 1;
    
}
