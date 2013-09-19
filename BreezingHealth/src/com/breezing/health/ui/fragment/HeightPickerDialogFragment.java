package com.breezing.health.ui.fragment;

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
    private NumberPicker mInteger;
    private NumberPicker mDecimals;
    private NumberPicker mUnit;
    private TextView mTitle;
    private Button mCancel;
    private Button mConfirm;
    private DialogFragmentInterface.OnClickListener mPositiveClickListener;
    private DialogFragmentInterface.OnClickListener mNegativeClickListener;
    private String mTitleString;

    private final Set<String> mUnits = new HashSet();
    private final StringBuilder  mStringBuilder = new StringBuilder();

    public static HeightPickerDialogFragment newInstance() {
        HeightPickerDialogFragment fragment = new HeightPickerDialogFragment();
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
        mFragmentView = inflater.inflate(R.layout.fragment_dialog_height_picker, null);
        mInteger = (NumberPicker) mFragmentView.findViewById(R.id.integer);
        mDecimals = (NumberPicker) mFragmentView.findViewById(R.id.decimals);
        mUnit = (NumberPicker) mFragmentView.findViewById(R.id.unit);
        mTitle = (TextView) mFragmentView.findViewById(R.id.title);
        mCancel = (Button) mFragmentView.findViewById(R.id.cancel);
        mConfirm = (Button) mFragmentView.findViewById(R.id.confirm);

        mInteger.setMaxValue(SHOW_INTEGER_CENTIMETRE_MAX);
        mInteger.setMinValue(SHOW_INTEGER_CENTIMETRE_MIN);
        mInteger.setValue(SHOW_INTEGER_CURRENT_CENTIMETRE);
        mInteger.setFocusable(false);
        mInteger.setFocusableInTouchMode(false);

        mDecimals.setMaxValue(SHOW_DECIMALS_CENTIMETRE_MAX);
        mDecimals.setMinValue(SHOW_DECIMALS_CENTIMETRE_MIN);
        mDecimals.setFocusable(false);
        mDecimals.setFocusableInTouchMode(false);

        final String[] units = getActivity().getResources().getStringArray(R.array.heightUnits);
        mUnit.setDisplayedValues(units);
        mUnit.setMaxValue(units.length - 1);
        mUnit.setMinValue(0);
        mUnit.setFocusable(false);
        mUnit.setFocusableInTouchMode(false);
        mUnit.setOnValueChangedListener(new OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                switch(newVal) {
                case HEIGHT_CENTIMETRE:
                    mInteger.setMaxValue(SHOW_INTEGER_CENTIMETRE_MAX);
                    mInteger.setMinValue(SHOW_INTEGER_CENTIMETRE_MIN);
                    mInteger.setValue(SHOW_INTEGER_CURRENT_CENTIMETRE);
                    mDecimals.setMaxValue(SHOW_INTEGER_CENTIMETRE_MAX);
                    mDecimals.setMinValue(SHOW_DECIMALS_CENTIMETRE_MIN);
                    return ;
                case HEIGHT_METRE:
                    mInteger.setMaxValue(SHOW_INTEGER_METRE_MAX);
                    mInteger.setMinValue(SHOW_INTEGER_METRE_MIN);
                    mDecimals.setMaxValue(SHOW_DECIMALS_METRE_MAX);
                    mDecimals.setMinValue(SHOW_DECIMALS_METRE_MIN);
                    return ;
                case HEIGHT_FEET:
                    mInteger.setMaxValue(SHOW_INTEGER_FEET_MAX);
                    mInteger.setMinValue(SHOW_INTEGER_FEET_MIN);
                    mDecimals.setMaxValue(SHOW_DECIMALS_FEET_MAX);
                    mDecimals.setMinValue(SHOW_DECIMALS_FEET_MIN);
                    return ;
                case HEIGHT_INCH:
                    mInteger.setMaxValue(SHOW_INTEGER_INCH_MAX);
                    mInteger.setMinValue(SHOW_INTEGER_INCH_MIN);
                    mInteger.setValue(SHOW_INTEGER_CURRENT_INCHE);
                    mDecimals.setMaxValue(SHOW_DECIMALS_INCH_MAX);
                    mDecimals.setMinValue(SHOW_DECIMALS_INCH_MIN);
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
                String[] units = mUnit.getDisplayedValues();
                String unit = units[mUnit.getValue()];
                float height = mInteger.getValue() + (float) mDecimals.getValue()/(mDecimals.getMaxValue() + 1);
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
    private static final int SHOW_DECIMALS_CENTIMETRE_MIN = 1;

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
