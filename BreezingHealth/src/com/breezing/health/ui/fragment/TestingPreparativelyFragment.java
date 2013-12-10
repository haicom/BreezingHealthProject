package com.breezing.health.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.breezing.health.R;
import com.breezing.health.entity.enums.TestStep;
import com.breezing.health.ui.activity.TestingActivity;
import com.breezing.health.widget.NoticeDialog;

public class TestingPreparativelyFragment extends BaseFragment implements OnClickListener {

    private View mFragmentView;
    private LinearLayout mStepContainer;
    private View mCurrentStepView;
    private Button mBeginConnect;
    private TestStep mCurrentStep;
    private int mStepCount = 0;
    
    public static TestingPreparativelyFragment newInstance() {
        TestingPreparativelyFragment fragment = new TestingPreparativelyFragment();
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_testing_preparatively, null);
        mStepContainer = (LinearLayout) mFragmentView.findViewById(R.id.step_content);
        mBeginConnect = (Button) mFragmentView.findViewById(R.id.next);
        mBeginConnect.setOnClickListener(this);
        
        createNewStep();
        showFirstTestNotice();
        
        return mFragmentView;
    }
    
    private void showFirstTestNotice() {
        new NoticeDialog.Builder(getActivity())
        .setTitle(R.string.notice)
        .setCancelable(false)
        .setMessage(getString(R.string.first_breezing_test_notice, "张三"))
        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
                showScanQRDialog();
            }
        })
        .create().show();
        
    }
    
    private void showScanQRDialog() {
        new NoticeDialog.Builder(getActivity())
        .setTitle(R.string.scan_qr_number)
        .setMessage(getString(R.string.do_you_scan_qr))
        .setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        })
        .setNegativeButton(R.string.btn_no, new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showScanQRNowDialog();
            }
        })
        .create().show();
        
    }
    
    private void showScanQRNowDialog() {
        new NoticeDialog.Builder(getActivity())
        .setTitle(R.string.scan_qr_number)
        .setMessage(getString(R.string.do_you_scan_qr_right_now))
        .setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        })
        .setNegativeButton(R.string.btn_no, new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
        .create().show();
        
    }
    
    private boolean createNewStep() {
        if (mCurrentStepView != null) {
            mCurrentStepView.setSelected(true);
            ((ImageView) mCurrentStepView.findViewById(R.id.checkbox)).setVisibility(View.VISIBLE);
        }
        
        int stepOrdinal = -1;
        if (mCurrentStep != null) {
            stepOrdinal = mCurrentStep.ordinal();
        }
        stepOrdinal++;
        if (stepOrdinal < TestStep.values().length) {
            mStepCount ++;
            mCurrentStep = TestStep.values()[stepOrdinal];
            mCurrentStepView = getActivity().getLayoutInflater().inflate(R.layout.breezing_step_item, null);
            ((TextView) mCurrentStepView.findViewById(R.id.name)).setText(mCurrentStep.nameRes);
            ((ImageView) mCurrentStepView.findViewById(R.id.number)).setImageResource(getStepNumberImage(mStepCount));
            mCurrentStepView.setOnClickListener(this);
            
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, getResources().getDimensionPixelSize(R.dimen.activity_vertical_small_margin), 0, 0);
            mStepContainer.addView(mCurrentStepView, params);
            return true;
        } else {
            return false;
        }
        
//        if (stepOrdinal >= TestStep.values().length - 1) {
//            return false;
//        } else {
//            return true;
//        }
        
    }
    
    private int getStepNumberImage(int step) {
        switch (step) {
        case 1:
            return R.drawable.breezing_step_number1;
        case 2:
            return R.drawable.breezing_step_number2;
        case 3:
            return R.drawable.breezing_step_number3;
        case 4:
            return R.drawable.breezing_step_number4;

        default:
            return 0;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mCurrentStepView) {
            boolean hadNextStep = createNewStep();
            if (!hadNextStep) {
                mBeginConnect.setVisibility(View.VISIBLE);
            }
            return ;
        } else if (v == mBeginConnect) {
            ((TestingActivity) getActivity()).createBTScanFragment();
            return ;
        }
    }
    
}
