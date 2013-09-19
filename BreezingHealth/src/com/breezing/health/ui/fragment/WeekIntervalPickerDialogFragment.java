package com.breezing.health.ui.fragment;

import java.util.Date;

import net.simonvt.numberpicker.NumberPicker;
import net.simonvt.numberpicker.NumberPicker.OnValueChangeListener;
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

import com.breezing.health.R;
import com.breezing.health.util.CalendarUtil;

public class WeekIntervalPickerDialogFragment extends BaseDialogFragment implements OnClickListener {
    private static final String TAG = "WeekIntervalPickerDialogFragment";

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
    
    private int mYearWeek;

    public static WeekIntervalPickerDialogFragment newInstance() {
        WeekIntervalPickerDialogFragment fragment = new WeekIntervalPickerDialogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {       
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_NoTitleBar);
        
        mYearWeek = getArguments().getInt(WEEK_PICKER_DIALOG_WEEK, 0);
        
        if (mYearWeek == 0) {
            
            mYearWeek = CalendarUtil.getWeekOfYear( new Date() );
            
        }
        
        Log.d(TAG, " onCreate mYearWeek = " + mYearWeek);
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


        final int year = Integer.parseInt(mYearValues[mYear.getValue()]);

        final int totalWeeks = CalendarUtil.getTotalWeeksInYear(year);
        Log.d(TAG, " initWeeks year = " + year);
        mWeekValues = new String[totalWeeks];

        for (int week = 1; week <= totalWeeks; week++) {
            try {
                mWeekValues[ week - 1 ] = CalendarUtil.getFirstDayAndLastDayOfWeek( getActivity(), year, week );
                
                Log.d(TAG, "initWeeks mWeekValues[week] =  " + mWeekValues[week] + " week = " + week);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        initWeeks(year, totalWeeks);
        mWeek.setFocusable(false);
        mWeek.setFocusableInTouchMode(false);

        mYear.setOnValueChangedListener(new OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                final int year = Integer.parseInt(mYearValues[newVal]);
                initWeeks(year, totalWeeks);
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

    private void initWeeks(int year , int totalWeeks) {

        if ( mWeek.getMaxValue() > (totalWeeks - 1) ) {
            mWeek.setMaxValue(totalWeeks - 1);
            mWeek.setMinValue(0);
            mWeek.setDisplayedValues(mWeekValues);
        } else {
            mWeek.setDisplayedValues(mWeekValues);
            mWeek.setMaxValue(totalWeeks - 1);
            mWeek.setMinValue(0);
            mWeek.setValue( mYearWeek - 1 );
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
                String[] years = mYear.getDisplayedValues();
                String year = years[mYear.getValue()];

                String[] weeks = mWeek.getDisplayedValues();
                String firstLastDay = weeks[mWeek.getValue() + 1];

                mPositiveClickListener.onClick(this, year, mWeek.getValue() +  1 , firstLastDay);
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
    
    public final static String  WEEK_PICKER_DIALOG_WEEK = "week";

}
