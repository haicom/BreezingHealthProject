package com.breezing.health.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.breezing.health.R;
import com.breezing.health.adapter.HelperPagerAdapter;
import com.breezing.health.entity.enums.HelperStep;
import com.breezing.health.tools.IntentAction;
import com.breezing.health.util.LocalSharedPrefsUtil;
import com.breezing.health.widget.CustomViewPager;
import com.viewpagerindicator.CirclePageIndicator;

public class HelperActivity extends ActionBarActivity implements OnClickListener {

	private CustomViewPager mViewPager;
	private CirclePageIndicator mIndicator;
	private HelperPagerAdapter mPagerAdapter;
	private Button mBegin;
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
	    // TODO Auto-generated method stub
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        startToFillInActivity();
	        return true;
	    }
	    return super.onKeyUp(keyCode, event);
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_helper);
        initValues();
        initViews();
        valueToView();
        initListeners();
    }

    private void initValues() {
        LocalSharedPrefsUtil.saveFirstTime(this);
    }

    private void initViews() {
        setActionBarTitle(R.string.helper);
        hideActionBar();
        
        mViewPager = (CustomViewPager) findViewById(R.id.view_pager);
    	mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
    	mBegin = (Button) findViewById(R.id.begin);
    	mBegin.setVisibility(View.INVISIBLE);
    }

    private void valueToView() {
    	mPagerAdapter = new HelperPagerAdapter(getSupportFragmentManager());
    	mViewPager.setAdapter(mPagerAdapter);
    	
    	mIndicator.setViewPager(mViewPager);
    }

    private void initListeners() {
        mBegin.setOnClickListener(this);
        mIndicator.setOnPageChangeListener(new OnPageChangeListener() {
            
            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                if (arg0 < HelperStep.values().length - 1) {
                    mBegin.setVisibility(View.INVISIBLE);
                } else {
                    mBegin.setVisibility(View.VISIBLE);
                }
            }
            
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
                
            }
        });
    }
    
    private void startToFillInActivity() {
        Intent intent = new Intent(IntentAction.ACTIVITY_FILLIN_INFORMATION);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == mBegin) {
            startToFillInActivity();
            return ;
        }
    }
	
}
