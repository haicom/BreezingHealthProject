package com.breezing.health.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.breezing.health.R;

public class BreezingTestFirstStepFragment extends BaseFragment {

    private View mFragmentView;

    public static BreezingTestFirstStepFragment newInstance() {
        BreezingTestFirstStepFragment fragment = new BreezingTestFirstStepFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_breezing_test_first_step, null);

        return mFragmentView;
    }
}
