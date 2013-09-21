package com.breezing.health.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class BalanceHistoryPagerAdapter extends PagerAdapter {

    private List<View> views;
    
    public BalanceHistoryPagerAdapter() {
        views = new ArrayList<View>();
    }
    
    @Override
    public int getCount() {
        return views.size();
    }
    
    public void addViewPage(View page) {
        views.add(page);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
    
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        container.removeView(views.get(position));
    }
    
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        container.addView(views.get(position));
        return views.get(position);
    }
    
    public List<View> getViews() {
        return views;
    }

}
