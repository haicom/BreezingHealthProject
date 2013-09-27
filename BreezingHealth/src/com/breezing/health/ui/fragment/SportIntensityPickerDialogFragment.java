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

public class SportIntensityPickerDialogFragment extends BaseDialogFragment implements OnClickListener {

    private View mFragmentView;
    private NumberPicker mIntensity;
    private NumberPicker mUnit;
    private TextView mTitle;
    private Button mCancel;
    private Button mConfirm;
    private DialogFragmentInterface.OnClickListener mPositiveClickListener;
    private DialogFragmentInterface.OnClickListener mNegativeClickListener;

    private String mTitleString;
    private final String mType;

    public static SportIntensityPickerDialogFragment newInstance(String sportType) {
        SportIntensityPickerDialogFragment fragment =
                new SportIntensityPickerDialogFragment(sportType);
        return fragment;
    }

    public SportIntensityPickerDialogFragment(String type) {
        mType = type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_NoTitleBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mFragmentView = inflater.inflate(R.layout.fragment_dialog_intensity_unit,
                    null);
        mIntensity = (NumberPicker) mFragmentView.findViewById(R.id.intensity);
        mUnit = (NumberPicker) mFragmentView.findViewById(R.id.distance_unit);

        mTitle = (TextView) mFragmentView.findViewById(R.id.title);
        mCancel = (Button) mFragmentView.findViewById(R.id.cancel);
        mConfirm = (Button) mFragmentView.findViewById(R.id.confirm);

        final String[] types = getActivity().getResources().
                getStringArray(R.array.sport_intensity);

        mIntensity.setDisplayedValues(types);
        mIntensity.setMaxValue(types.length - 1);
        mIntensity.setMinValue(0);
        mIntensity.setFocusable(false);
        mIntensity.setFocusableInTouchMode(false);

        String[] units;
        if (mType == null) {
            units = new String[] { getActivity().getResources().
                    getString(R.string.sport_type_gengeral) };
        } else {
            if (mType.equals(getActivity().
                    getResources().getString(R.string.sport_type_runing))) {
                units = getActivity().getResources().
                        getStringArray(R.array.distance_units);
            } else {
                units = new String[] { getActivity().getResources().
                        getString(R.string.sport_type_gengeral) };
            }
        }

        mUnit.setDisplayedValues(units);
        mUnit.setMaxValue(units.length - 1);
        mUnit.setMinValue(0);
        mUnit.setFocusable(false);
        mUnit.setFocusableInTouchMode(false);

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

                String[] intensitys = mIntensity.getDisplayedValues();
                String intensity = intensitys[ mIntensity.getValue() ];

                String[] units = mUnit.getDisplayedValues();
                String unit = units[ mUnit.getValue() ];

                mPositiveClickListener.onClick(this, intensity, unit);
            }

            dismiss();

        } else if (v == mCancel) {

            if (mNegativeClickListener != null) {
                mNegativeClickListener.onClick(this);
            }

            dismiss();
        }
    }

}
