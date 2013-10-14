package com.breezing.health.ui.fragment;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

import net.simonvt.numberpicker.NumberPicker;
import net.simonvt.numberpicker.NumberPicker.OnValueChangeListener;
import android.database.Cursor;
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
import com.breezing.health.providers.Breezing.Account;


public class HeightPickerDialogFragment extends BaseDialogFragment implements OnClickListener {

    private View mFragmentView;
    
    private NumberPicker mIntegerPicker;
    private NumberPicker mDecimalsPicker;
    private NumberPicker mUnitPicker;
    
    private TextView mTitle;
    
    private Button mCancel;
    private Button mConfirm;
    
    private DialogFragmentInterface.OnClickListener mPositiveClickListener;
    private DialogFragmentInterface.OnClickListener mNegativeClickListener;
    
    private String mTitleString;
    
    private float mHeight;
    private String mUnit;

    private final Set<String> mUnits = new HashSet();
    private final StringBuilder  mStringBuilder = new StringBuilder();

    public static HeightPickerDialogFragment newInstance(float height, String unit) {
        
        HeightPickerDialogFragment fragment = new HeightPickerDialogFragment(height, unit);
        return fragment;
    }
    
    public HeightPickerDialogFragment(float height, String unit) {
        mHeight = height;
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
        
        mFragmentView = inflater.inflate(R.layout.fragment_dialog_height_picker, null);
        mIntegerPicker = (NumberPicker) mFragmentView.findViewById(R.id.integer);
        mDecimalsPicker = (NumberPicker) mFragmentView.findViewById(R.id.decimals);
        
        mUnitPicker = (NumberPicker) mFragmentView.findViewById(R.id.unit);
        mTitle = (TextView) mFragmentView.findViewById(R.id.title);
        mCancel = (Button) mFragmentView.findViewById(R.id.cancel);
        mConfirm = (Button) mFragmentView.findViewById(R.id.confirm);

        mIntegerPicker.setMaxValue(SHOW_INTEGER_CENTIMETRE_MAX);
        mIntegerPicker.setMinValue(SHOW_INTEGER_CENTIMETRE_MIN);
        
        if (mHeight == 0 ) {
            mIntegerPicker.setValue(SHOW_INTEGER_CURRENT_CENTIMETRE);
        } else {
            mIntegerPicker.setValue( (int) Math.floor(mHeight) );
        }
        
        mIntegerPicker.setFocusable(false);
        mIntegerPicker.setFocusableInTouchMode(false);

        mDecimalsPicker.setMaxValue(SHOW_DECIMALS_CENTIMETRE_MAX);
        mDecimalsPicker.setMinValue(SHOW_DECIMALS_CENTIMETRE_MIN);
        
        if (mHeight != 0) {
            if (mUnit.equals(getString(R.string.height_meter) ) ) {
                mDecimalsPicker.setValue( ( (int) ( mHeight*100 ) ) % 100);
            } else {
                mDecimalsPicker.setValue( ( (int) ( mHeight*10 ) ) % 10);
            }           
        }
        
        
        mDecimalsPicker.setFocusable(false);
        mDecimalsPicker.setFocusableInTouchMode(false);

        final String[] units = getActivity().getResources().getStringArray(R.array.heightUnits);
        mUnitPicker.setDisplayedValues(units);
        mUnitPicker.setMaxValue(units.length - 1);
        mUnitPicker.setMinValue(0);
        
        int value = 0;
        
        if ( !mUnit.isEmpty() ) {            
            for (value = 0; value < units.length; value++) {
                if ( mUnit.equals(units[value]) ) {
                    break;
                }
            }            
            mUnitPicker.setValue(value);
        }
        
        
       
        mUnitPicker.setFocusable(false);
        mUnitPicker.setFocusableInTouchMode(false);
        mUnitPicker.setOnValueChangedListener(new OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                switch(newVal) {
                case HEIGHT_CENTIMETRE:
                    mIntegerPicker.setMaxValue(SHOW_INTEGER_CENTIMETRE_MAX);
                    mIntegerPicker.setMinValue(SHOW_INTEGER_CENTIMETRE_MIN);
                    mIntegerPicker.setValue(SHOW_INTEGER_CURRENT_CENTIMETRE);
                    mIntegerPicker.setMaxValue(SHOW_INTEGER_CENTIMETRE_MAX);
                    mIntegerPicker.setMinValue(SHOW_DECIMALS_CENTIMETRE_MIN);
                    return ;
                case HEIGHT_METRE:
                    mIntegerPicker.setMaxValue(SHOW_INTEGER_METRE_MAX);
                    mIntegerPicker.setMinValue(SHOW_INTEGER_METRE_MIN);
                    mIntegerPicker.setMaxValue(SHOW_DECIMALS_METRE_MAX);
                    mIntegerPicker.setMinValue(SHOW_DECIMALS_METRE_MIN);
                    return ;
                case HEIGHT_FEET:
                    mIntegerPicker.setMaxValue(SHOW_INTEGER_FEET_MAX);
                    mIntegerPicker.setMinValue(SHOW_INTEGER_FEET_MIN);
                    mIntegerPicker.setMaxValue(SHOW_DECIMALS_FEET_MAX);
                    mIntegerPicker.setMinValue(SHOW_DECIMALS_FEET_MIN);
                    return ;
                case HEIGHT_INCH:
                    mIntegerPicker.setMaxValue(SHOW_INTEGER_INCH_MAX);
                    mIntegerPicker.setMinValue(SHOW_INTEGER_INCH_MIN);
                    mIntegerPicker.setValue(SHOW_INTEGER_CURRENT_INCHE);
                    mIntegerPicker.setMaxValue(SHOW_DECIMALS_INCH_MAX);
                    mIntegerPicker.setMinValue(SHOW_DECIMALS_INCH_MIN);
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
        if (v == mConfirm) {
            if (mPositiveClickListener != null) {
                String[] units = mUnitPicker.getDisplayedValues();
                String unit = units[mUnitPicker.getValue()];
                float height = mIntegerPicker.getValue() + (float) mDecimalsPicker.getValue()/(mDecimalsPicker.getMaxValue() + 1);
                mPositiveClickListener.onClick(this, height, unit);
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

    private static final int HEIGHT_CENTIMETRE = 0;
    private static final int HEIGHT_METRE = 1;
    private static final int HEIGHT_FEET = 2;
    private static final int HEIGHT_INCH = 3;

    private static final int SHOW_INTEGER_CENTIMETRE_MAX = 200;
    private static final int SHOW_INTEGER_CENTIMETRE_MIN = 50;
    private static final int SHOW_INTEGER_CURRENT_CENTIMETRE = 170;
    private static final int SHOW_DECIMALS_CENTIMETRE_MAX = 9;
    private static final int SHOW_DECIMALS_CENTIMETRE_MIN = 0;

    private static final int SHOW_INTEGER_METRE_MAX = SHOW_INTEGER_CENTIMETRE_MAX / 100;
    private static final int SHOW_INTEGER_METRE_MIN = 0;
    private static final int SHOW_DECIMALS_METRE_MAX = 99;
    private static final int SHOW_DECIMALS_METRE_MIN = 1;

    private static final int SHOW_INTEGER_FEET_MAX = 9;
    private static final int SHOW_INTEGER_FEET_MIN = 0;
    private static final int SHOW_DECIMALS_FEET_MAX = 9;
    private static final int SHOW_DECIMALS_FEET_MIN = 1;

    private static final int SHOW_INTEGER_INCH_MAX = SHOW_INTEGER_FEET_MAX * 12;
    private static final int SHOW_INTEGER_CURRENT_INCHE = 60;
    private static final int SHOW_INTEGER_INCH_MIN = 0;
    private static final int SHOW_DECIMALS_INCH_MAX = 99;
    private static final int SHOW_DECIMALS_INCH_MIN = 1;
}
