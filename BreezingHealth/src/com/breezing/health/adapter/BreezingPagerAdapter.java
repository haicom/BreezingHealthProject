package com.breezing.health.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class BreezingPagerAdapter extends PagerAdapter {

    private ArrayList<View> views;
    
    public BreezingPagerAdapter(ArrayList<View> views) {
        this.views = views;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)   {   
        container.removeView(views.get(position));//删除页卡
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡       
         container.addView(views.get(position), 0);//添加页卡
         return views.get(position);
    }

    @Override
    public int getCount() {         
        return  views.size();//返回页卡的数量
    }
    
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {           
        return arg0 == arg1;
    }


}
