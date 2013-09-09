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
    private NumberPicker mYear;
    private NumberPicker mMonth;
    private NumberPicker mDay;
    private TextView mTitle;
    private Button mCancel;
    private Button mConfirm;
    private DialogFragmentInterface.OnClickListener mPositiveClickListener;
    private DialogFragmentInterface.OnClickListener mNegativeClickListener;
    private String mTitleString;
    
    public static DatePickerDialogFragment newInstance() {
        DatePickerDialogFragment fragment = new DatePickerDialogFragment();
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
        mFragmentView = inflater.inflate(R.layout.fragment_dialog_date_picker, null);
        mYear = (NumberPicker) mFragmentView.findViewById(R.id.year);
        mMonth = (NumberPicker) mFragmentView.findViewById(R.id.month);
        mDay = (NumberPicker) mFragmentView.findViewById(R.id.day);
        mTitle = (TextView) mFragmentView.findViewById(R.id.title);
        mCancel = (Button) mFragmentView.findViewById(R.id.cancel);
        mConfirm = (Button) mFragmentView.findViewById(R.id.confirm);
        
        mYear.setMaxValue(SHOW_YEAR_MAX);
        mYear.setMinValue(SHOW_YEAR_MIN);
        mYear.setFocusable(false);
        mYear.setFocusableInTouchMode(false);
        
        mMonth.setMaxValue(SHOW_MONTH_MAX);
        mMonth.setMinValue(SHOW_MONTH_MIN);
        mMonth.setFocusable(false);
        mMonth.setFocusableInTouchMode(false);
        
        mDay.setMaxValue(SHOW_DAY_MAX);
        mDay.setMinValue(SHOW_DAT_MIN);
        mDay.setFocusable(true);
        mDay.setFocusableInTouchMode(true);
        
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
    
    public int getDay() {
        return mDay.getValue();
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
                mPositiveClickListener.onClick( this, mYear.getValue(), mMonth.getValue(), mDay.getValue() );
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
    private static final int SHOW_MONTH_MAX = 12;
    private static final int SHOW_MONTH_MIN = 1;
    private static final int SHOW_DAY_MAX = 31;
    private static final int SHOW_DAT_MIN = 1;
    
}
