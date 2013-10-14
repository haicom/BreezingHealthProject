package com.breezing.health.adapter;

import com.breezing.health.ui.activity.CaloricHistoryActivity.CaloricHistoryType;
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
        switch( CaloricHistoryType.values()[position] ) {
            case BURN:
                return CaloricBurnFragment.getInstance();
            case INTAKE:
                return CaloricIntakeFragment.getInstance( );
        }

        return null;
    }
    
    

    @Override
    public int getCount() {
        return CaloricHistoryType.values().length;
    }

}
