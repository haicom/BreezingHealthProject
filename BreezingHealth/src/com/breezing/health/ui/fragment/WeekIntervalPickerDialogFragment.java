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
import com.breezing.health.util.CalendarUtil;

public class WeekIntervalPickerDialogFragment extends BaseDialogFragment implements OnClickListener {

    private View mFragmentView;
    private NumberPicker mYear;
    private NumberPicker mWeek;
    private TextView mTitle;
    private Button mCancel;
    private Button mConfirm;
    private DialogFragmentInterface.OnClickListener mPositiveClickListener;
    private DialogFragmentInterface.OnClickListener mNegativeClickListener;
    private String mTitleString;
    private String[] mYearValues;
    private String[] mWeekValues;
    
    public static WeekIntervalPickerDialogFragment newInstance() {
        WeekIntervalPickerDialogFragment fragment = new WeekIntervalPickerDialogFragment();
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
        mFragmentView = inflater.inflate(R.layout.fragment_dialog_week_interval_picker, null);
        mYear = (NumberPicker) mFragmentView.findViewById(R.id.year);
        mWeek = (NumberPicker) mFragmentView.findViewById(R.id.week);
        mTitle = (TextView) mFragmentView.findViewById(R.id.title);
        mCancel = (Button) mFragmentView.findViewById(R.id.cancel);
        mConfirm = (Button) mFragmentView.findViewById(R.id.confirm);
        
        mYearValues = CalendarUtil.getYearsFrom2013();
        mYear.setDisplayedValues(mYearValues);
        mYear.setMaxValue(mYearValues.length - 1);
        mYear.setMinValue(0);
        mYear.setFocusable(false);
        mYear.setFocusableInTouchMode(false);
        
        final int year = Integer.parseInt(mYearValues[0]);
        initWeeks(year);
        mWeek.setFocusable(false);
        mWeek.setFocusableInTouchMode(false);
        
        mYear.setOnValueChangedListener(new OnValueChangeListener() {
            
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // TODO Auto-generated method stub
                final int year = Integer.parseInt(mYearValues[newVal]);
                initWeeks(year);
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
    
    private void initWeeks(int year) {
        final int totalWeeks = CalendarUtil.getTotalWeeksInYear(year);
        mWeekValues = new String[totalWeeks];
        for (int i = 0; i < totalWeeks; i ++) {
            try {
                final String[] startEnd = CalendarUtil.getStartEndOFWeek(year, i + 1);
                mWeekValues[i] = getActivity().getString(R.string.week_des_first) + (i + 1) + getString(R.string.week);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        if (mWeek.getMaxValue() > (totalWeeks - 1)) {
        	mWeek.setMaxValue(totalWeeks - 1);
            mWeek.setMinValue(0);
            mWeek.setDisplayedValues(mWeekValues);
        } else {
        	mWeek.setDisplayedValues(mWeekValues);
        	mWeek.setMaxValue(totalWeeks - 1);
            mWeek.setMinValue(0);
        }
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
