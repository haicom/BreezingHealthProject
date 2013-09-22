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
import com.breezing.health.entity.enums.ChartModel;

public class ChartModelPickerDialogFragment extends BaseDialogFragment implements OnClickListener {

    private View mFragmentView;
    private NumberPicker mNumberPicker;
    private TextView mTitle;
    private Button mCancel;
    private Button mConfirm;
    private DialogFragmentInterface.OnClickListener mPositiveClickListener;
    private DialogFragmentInterface.OnClickListener mNegativeClickListener;
    private String mTitleString;
    private ChartModel[] mModels;

    public static ChartModelPickerDialogFragment newInstance() {
        ChartModelPickerDialogFragment fragment = new ChartModelPickerDialogFragment();
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
        mFragmentView = inflater.inflate(R.layout.fragment_dialog_single_picker, null);
        mNumberPicker = (NumberPicker) mFragmentView.findViewById(R.id.type);
        mTitle = (TextView) mFragmentView.findViewById(R.id.title);
        mCancel = (Button) mFragmentView.findViewById(R.id.cancel);
        mConfirm = (Button) mFragmentView.findViewById(R.id.confirm);

        mModels = ChartModel.values();

        final int length = mModels.length;
        String[] values = new String[length];
        for (int i = 0; i < length; i++) {
            values[i] = getActivity().getString(mModels[i].nameRes);
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
                ChartModel displayValue = mModels[mNumberPicker.getValue()];
                mPositiveClickListener.onClick(this, displayValue);
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
