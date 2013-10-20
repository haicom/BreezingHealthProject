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

public class TimerPickerDialogFragment extends BaseDialogFragment implements OnClickListener {

    private View mFragmentView;
    
    private NumberPicker mHourPicker;
    private NumberPicker mMinitePicker;
    
    private TextView mTitle;
    
    private Button mCancel;
    private Button mConfirm;
    
    private DialogFragmentInterface.OnClickListener mPositiveClickListener;
    private DialogFragmentInterface.OnClickListener mNegativeClickListener;
    
    private String mTitleString;
    
    private int mHour;
    private int mMinite;
    
    public static TimerPickerDialogFragment newInstance() {
        TimerPickerDialogFragment fragment = new TimerPickerDialogFragment();
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {       
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_NoTitleBar);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {        
        mFragmentView = inflater.inflate(R.layout.fragment_dialog_timer_picker, null);
        
        mHourPicker = (NumberPicker) mFragmentView.findViewById(R.id.hour);
        mMinitePicker = (NumberPicker) mFragmentView.findViewById(R.id.minite);
        
        mTitle = (TextView) mFragmentView.findViewById(R.id.title);
        
        mCancel = (Button) mFragmentView.findViewById(R.id.cancel);        
        mConfirm = (Button) mFragmentView.findViewById(R.id.confirm);
        
        mHourPicker.setMaxValue(SHOW_HOUR_MAX);
        mHourPicker.setMinValue(SHOW_HOUR_MIN);
        mHourPicker.setValue(mHour);
        mHourPicker.setFocusable(false);
        mHourPicker.setFocusableInTouchMode(false);
        
        mMinitePicker.setMaxValue(SHOW_MINITE_MAX);
        mMinitePicker.setMinValue(SHOW_MINITE_MIN);
        mMinitePicker.setValue(mMinite);
        mMinitePicker.setFocusable(false);
        mMinitePicker.setFocusableInTouchMode(false);
        
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
                mPositiveClickListener.onClick( this, 
                        mHourPicker.getValue(), 
                        mMinitePicker.getValue());
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
    
    private static final int SHOW_HOUR_MAX = 24;
    private static final int SHOW_HOUR_MIN = 0;
    private static final int SHOW_MINITE_MAX = 60;
    private static final int SHOW_MINITE_MIN = 0;
    
}

