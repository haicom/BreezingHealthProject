package com.breezing.health.adapter;

import com.breezing.health.ui.fragment.BreezingTestFirstStepFragment;
import com.breezing.health.ui.fragment.BreezingTestResultFragment;
import com.breezing.health.ui.fragment.BreezingTestSecondStepFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class BreezingTestPagerAdapter extends FragmentPagerAdapter {

    public BreezingTestPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
        case BREEZING_TEST_FIRST_STEP:
            return BreezingTestFirstStepFragment.newInstance();

        case BREEZING_TEST_SECOND_STEP:
            return BreezingTestSecondStepFragment.newInstance();

        case BREEZING_TEST_RESULT:
            return BreezingTestResultFragment.newInstance();

        default:
            return null;
        }
    }

    @Override
    public int getCount() {
        return BREEZING_TEST_SETP_NUM;
    }

    public  static final int BREEZING_TEST_FIRST_STEP = 0;
    public  static final int BREEZING_TEST_SECOND_STEP = 1;
    public  static final int BREEZING_TEST_RESULT = 2;

    private static final int BREEZING_TEST_SETP_NUM = 3;

}
