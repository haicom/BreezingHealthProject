package com.breezing.health.adapter;

import com.breezing.health.ui.fragment.CaloricBurnFragment;
import com.breezing.health.ui.fragment.CaloricIntakeFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CaloricPagerAdapter extends FragmentPagerAdapter {

    public CaloricPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch( position ) {
            case MAIN_INTERFACE_CALORIC_BURIN:
                return CaloricBurnFragment.getInstance(MAIN_INTERFACE_CALORIC_BURIN);
            case MAIN_INTERFACE_CALORIC_INTAKE:
                return CaloricIntakeFragment.getInstance(MAIN_INTERFACE_CALORIC_INTAKE);
        }

        return null;
    }

    @Override
    public int getCount() {
        return MAIN_INTERFACE_CALORIC_NUM;
    }

    public static final int MAIN_INTERFACE_CALORIC_BURIN = 0;
    public static final int MAIN_INTERFACE_CALORIC_INTAKE = 1;
    public static final String MAIN_INTERFACE_SAVE_NUM = "num";

    private static final int MAIN_INTERFACE_CALORIC_NUM = 2;

}
