package com.breezing.health.adapter;

import com.breezing.health.ui.activity.CaloricHistoryActivity.CaloricHistoryType;
import com.breezing.health.ui.fragment.CaloricBurnFragment;
import com.breezing.health.ui.fragment.CaloricIntakeFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CaloricPagerAdapter extends FragmentPagerAdapter {
    private int mAccountId;
    private int mDate;
    
    public CaloricPagerAdapter(FragmentManager fm, int accountId, int date) {
        super(fm);
        mAccountId = accountId;
        mDate = date;
        
    }

    @Override
    public Fragment getItem(int position) {
        switch( CaloricHistoryType.values()[position] ) {
            case BURN:
                return CaloricBurnFragment.getInstance();
            case INTAKE:
                return CaloricIntakeFragment.getInstance(mAccountId, mDate);
        }

        return null;
    }
    
    

    @Override
    public int getCount() {
        return CaloricHistoryType.values().length;
    }

}
