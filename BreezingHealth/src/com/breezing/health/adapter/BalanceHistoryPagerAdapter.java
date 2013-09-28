package com.breezing.health.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class BalanceHistoryPagerAdapter extends PagerAdapter {

    private List<View> mViews;
    
    public BalanceHistoryPagerAdapter() {
        mViews = new ArrayList<View>();
    }
    
    @Override
    public int getCount() {
        return mViews.size();
    }
    
    public void addViewPage(View page) {        
        mViews.add( page );
    }

    @Override
    public boolean isViewFromObject (View view, Object object) {
        return view == object;
    }
    
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {      
        container.removeView( mViews.get(position) );
    }
    
    @Override
    public Object instantiateItem(ViewGroup container, int position) {       
        container.addView( mViews.get(position) );
        
        return mViews.get(position);
    }
    
    public List<View> getViews() {
        return mViews;
    }

}
