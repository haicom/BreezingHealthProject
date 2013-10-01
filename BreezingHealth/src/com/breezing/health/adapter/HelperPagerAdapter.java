package com.breezing.health.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.breezing.health.entity.enums.HelperStep;
import com.breezing.health.ui.fragment.HelperStepFragment;

public class HelperPagerAdapter extends FragmentPagerAdapter {

	public HelperPagerAdapter(FragmentManager fm) {
		super(fm);		
	}

	@Override
	public Fragment getItem(int arg0) {		
		return HelperStepFragment.newInstance(HelperStep.values()[arg0]);
	}

	@Override
	public int getCount() {		
		return HelperStep.values().length;
	}


}
