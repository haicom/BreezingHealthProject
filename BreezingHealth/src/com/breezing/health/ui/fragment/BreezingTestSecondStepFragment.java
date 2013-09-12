package com.breezing.health.ui.fragment;

import android.content.ContentProviderOperation;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import com.breezing.health.R;
import com.breezing.health.providers.Breezing;
import com.breezing.health.providers.Breezing.EnergyCost;
import com.breezing.health.ui.activity.BreezingTestActivity;
import com.breezing.health.util.DateFormatUtil;
import com.breezing.health.util.LocalSharedPrefsUtil;

public class BreezingTestSecondStepFragment extends BaseFragment implements OnClickListener {
    private static final String TAG = "BreezingTestSecondStepFragment";
    private View mFragmentView;
    private Button mBegin;
    private String mTestInfo;

    public static BreezingTestSecondStepFragment newInstance() {
        BreezingTestSecondStepFragment fragment = new BreezingTestSecondStepFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_breezing_test_second_step, null);
        mBegin = (Button) mFragmentView.findViewById(R.id.begin);
        mBegin.setOnClickListener(this);

        return mFragmentView;
    }

    @Override
    public void onClick(View v) {
        if (v == mBegin) {
//            showBluetoothSettingDialog();
            mTestInfo = appendEnergyCost();
            ((BreezingTestActivity)getActivity()).setTestResult();
            Toast.makeText(getActivity(), mTestInfo, Toast.LENGTH_SHORT).show();
            return ;
        }
    }

    private void showBluetoothSettingDialog() {
        BreezingTestBTSettingFragment setting = (BreezingTestBTSettingFragment) getActivity().getSupportFragmentManager().findFragmentByTag("bluetoothSetting");
        if (setting != null) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(setting);
        }
        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null);

        setting = BreezingTestBTSettingFragment.newInstance();
        setting.show(getActivity().getSupportFragmentManager(), "bluetoothSetting");
    }

    private String appendEnergyCost() {
        String result;

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int accountId = LocalSharedPrefsUtil.
                        getSharedPrefsValueInt(this.getActivity(), LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        ops.add(ContentProviderOperation.newInsert(EnergyCost.CONTENT_URI)
                .withValue(EnergyCost.ACCOUNT_ID, accountId)
                .withValue(EnergyCost.METABOLISM, ENERGY_COST_METABOLISM)
                .withValue(EnergyCost.SPORT, ENERGY_COST_SPORT)
                .withValue(EnergyCost.DIGEST, ENERGY_COST_DIGEST)
                .withValue(EnergyCost.TRAIN, 0)
                .withValue(EnergyCost.ENERGY_COST_DATE, DateFormatUtil.simpleDateFormat("yyyyMMdd"))
                .build());
        try {
            getActivity().getContentResolver().applyBatch(Breezing.AUTHORITY, ops);
            result =  getActivity().getString(R.string.input_infomation, 
                    ENERGY_COST_METABOLISM, ENERGY_COST_METABOLISM, ENERGY_COST_DIGEST);                   
        } catch (Exception e) {
            result = getResources().getString(R.string.data_error);
            // Log exception
            Log.e(TAG, "Exceptoin encoutered while inserting contact: " + e);
        }

        return result;
    }

    private static final int ENERGY_COST_METABOLISM = 3500;
    private static final int ENERGY_COST_SPORT = 160;
    private static final int ENERGY_COST_DIGEST = ENERGY_COST_METABOLISM / 20;
}
