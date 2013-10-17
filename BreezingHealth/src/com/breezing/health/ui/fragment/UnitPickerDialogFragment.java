package com.breezing.health.ui.fragment;

import java.util.ArrayList;

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
import com.breezing.health.entity.UnitEntity;
import com.breezing.health.util.BreezingQueryViews;

public class UnitPickerDialogFragment extends BaseDialogFragment implements OnClickListener {

    public enum UnitType {
        CALORIC(R.string.caloric_type), HEIGHT(R.string.height_type), WEIGHT(R.string.weight_type), DISTANCE(R.string.distance_type);

        private UnitType(int nameRes) {
            this.nameRes = nameRes;
        }

        public int nameRes;
    }
    
    private View mFragmentView;
    private NumberPicker mNumberPicker;
    private TextView mTitle;
    private Button mCancel;
    private Button mConfirm;
    private DialogFragmentInterface.OnClickListener mPositiveClickListener;
    private DialogFragmentInterface.OnClickListener mNegativeClickListener;
    private String mTitleString;
    private ArrayList<UnitEntity> mUnits;
    private UnitType mUnitType;

    public static UnitPickerDialogFragment newInstance(UnitType unitType) {
        UnitPickerDialogFragment fragment = new UnitPickerDialogFragment();
        fragment.mUnitType = unitType;
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
        mFragmentView = inflater.inflate(R.layout.fragment_dialog_single_picker, null);
        mNumberPicker = (NumberPicker) mFragmentView.findViewById(R.id.type);
        mTitle = (TextView) mFragmentView.findViewById(R.id.title);
        mCancel = (Button) mFragmentView.findViewById(R.id.cancel);
        mConfirm = (Button) mFragmentView.findViewById(R.id.confirm);

        BreezingQueryViews query = new BreezingQueryViews(getActivity());
        mUnits = query.queryUnitsByType(getActivity().getString(mUnitType.nameRes));

        final int length = mUnits.size();
        String[] values = new String[length];
        for (int i = 0; i < length; i++) {
            values[i] = mUnits.get(i).getUnitName();
        }

        mNumberPicker.setDisplayedValues(values);
        mNumberPicker.setMaxValue(length - 1);
        mNumberPicker.setMinValue(0);
        mNumberPicker.setFocusable(false);
        mNumberPicker.setFocusableInTouchMode(false);

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
                mPositiveClickListener.onClick(this, mUnitType, mUnits.get(mNumberPicker.getValue()));
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
