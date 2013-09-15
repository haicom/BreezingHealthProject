package com.breezing.health.ui.fragment;

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
import com.breezing.health.widget.calendar.CalendarView;

public class CalendarDialogFragment extends BaseDialogFragment implements OnClickListener {
    private final static String TAG = "CalendarDialogFragment";
    private View mFragmentView;
    private CalendarView mCalendarView;
    private TextView mTitle;
    private Button mCancel;
    private Button mConfirm;
    private DialogFragmentInterface.OnClickListener mPositiveClickListener;
    private DialogFragmentInterface.OnClickListener mNegativeClickListener;
    private static CalendarDialogFragment mCalendarDialogFragment;
    private String mTitleString;
    private static long mTimeInMillis;
    private static int mYear;
    private static int mMonth;
    private static int mDay;

    public static CalendarDialogFragment newInstance() {
        CalendarDialogFragment fragment = new CalendarDialogFragment();
        return fragment;
    }
    
    public static CalendarDialogFragment getInstance() {
        if ( mCalendarDialogFragment == null ) {
            mCalendarDialogFragment  = new CalendarDialogFragment();
        }        
        return mCalendarDialogFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        Log.d(TAG, "onCreateView");
        
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_NoTitleBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
        Log.d(TAG, "onCreateView mTimeInMillis = " + mTimeInMillis);
        
        mFragmentView = inflater.inflate(R.layout.fragment_dialog_calendar, null);
        
        
        
        mTitle = (TextView) mFragmentView.findViewById(R.id.title);
        mCancel = (Button) mFragmentView.findViewById(R.id.cancel);
        mConfirm = (Button) mFragmentView.findViewById(R.id.confirm);
        
        mCalendarView = (CalendarView) mFragmentView.findViewById(R.id.calendarView);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                 Log.d(TAG, "onCreateView  onSelectedDayChange year = " + year + " month = " + month + " dayOfMonth = " + dayOfMonth);
                 mYear = year;
                 mMonth = month + 1;
                 mDay = dayOfMonth;
                 mTimeInMillis = mCalendarView.getDate();
            }
        });
        
        if (mTitleString != null) {
            mTitle.setText(mTitleString);
        }
        
        if (mTimeInMillis != 0) {
            mCalendarView.setDate(mTimeInMillis); 
        }
        mCancel.setOnClickListener(this);
        mConfirm.setOnClickListener(this);

        getDialog().getWindow().setBackgroundDrawable(new
                ColorDrawable(Color.TRANSPARENT) );

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
            Log.d(TAG, "onClick   mYear = " + mYear + " mMonth = " + mMonth + " mDay = " + mDay);
            if (mPositiveClickListener != null) {
                mPositiveClickListener.onClick( this, mYear, mMonth, mDay );
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
