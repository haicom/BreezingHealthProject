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
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        switch (arg0) {
        case 0:
            return BreezingTestFirstStepFragment.newInstance();
            
        case 1:
            
            return BreezingTestSecondStepFragment.newInstance();
            
        case 2:
            
            return BreezingTestResultFragment.newInstance();

        default:
            return null;
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 3;
    }

}
