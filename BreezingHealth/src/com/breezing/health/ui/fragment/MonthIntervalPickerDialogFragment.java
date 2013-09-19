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

public class MonthIntervalPickerDialogFragment extends BaseDialogFragment implements OnClickListener {

    private View mFragmentView;
    private NumberPicker mYear;
    private NumberPicker mMonth;
    private TextView mTitle;
    private Button mCancel;
    private Button mConfirm;
    private DialogFragmentInterface.OnClickListener mPositiveClickListener;
    private DialogFragmentInterface.OnClickListener mNegativeClickListener;
    private String mTitleString;
    
    private int mYearMonth;

    public static MonthIntervalPickerDialogFragment newInstance() {
    	MonthIntervalPickerDialogFragment fragment = new MonthIntervalPickerDialogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_NoTitleBar);
        
        mYearMonth = getArguments().getInt(MONTH_PICKER_DIALOG_MONTH, 0);
        if (mYearMonth == 0) {
            mYearMonth = CalendarUtil.getCurrentMonth();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_dialog_month_interval_picker, null);
        mYear = (NumberPicker) mFragmentView.findViewById(R.id.year);
        mMonth = (NumberPicker) mFragmentView.findViewById(R.id.month);
        mTitle = (TextView) mFragmentView.findViewById(R.id.title);
        mCancel = (Button) mFragmentView.findViewById(R.id.cancel);
        mConfirm = (Button) mFragmentView.findViewById(R.id.confirm);

        final String[] years = CalendarUtil.getYearsFrom2013();
        mYear.setDisplayedValues(years);
        mYear.setMaxValue(years.length - 1);
        mYear.setMinValue(0);
        mYear.setFocusable(false);
        mYear.setFocusableInTouchMode(false);
       

        mMonth.setMaxValue(SHOW_MONTH_MAX);
        mMonth.setMinValue(SHOW_MONTH_MIN);
        mMonth.setValue(mYearMonth);
        mMonth.setFocusable(false);
        mMonth.setFocusableInTouchMode(false);
      
        
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

    public int getMonth() {
        return mMonth.getValue();
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
                String[] displayValues = mYear.getDisplayedValues();
                String displayValue = displayValues[mYear.getValue()];
                
                mPositiveClickListener.onClick( this, displayValue, mMonth.getValue());
            }

            dismiss();
            return;
        } else if (v == mCancel) {

            if (mNegativeClickListener != null) {

                mNegativeClickListener.onClick(this);

            }

            dismiss();
            return ;
        }
    }
    
    public static final String MONTH_PICKER_DIALOG_MONTH = "month";
    
    private static final int SHOW_MONTH_MAX = 12;
    private static final int SHOW_MONTH_MIN = 1;

}