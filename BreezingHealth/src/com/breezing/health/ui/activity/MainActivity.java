package com.breezing.health.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.breezing.health.R;
import com.breezing.health.adapter.CaloricPagerAdapter;
import com.breezing.health.ui.fragment.BaseDialogFragment;
import com.breezing.health.ui.fragment.CalendarDialogFragment;
import com.breezing.health.ui.fragment.DialogFragmentInterface;

public class MainActivity extends ActionBarActivity implements OnClickListener {

    private ViewPager mViewPager;
    private Button mWeight;
    private Button mCalendar;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        setSlidingMenuEnable(true);
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_main);
        initValues();
        initViews();
        valueToView();
        initListeners();
    }

    private void initValues() {
        // TODO Auto-generated method stub
        
    }

    private void initViews() {
        // TODO Auto-generated method stub
        mWeight = (Button) findViewById(R.id.weight);
        mCalendar = (Button) findViewById(R.id.calendar);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    private void valueToView() {
        // TODO Auto-generated method stub
        setActionBarTitle(R.string.app_name);
        mViewPager.setAdapter(new CaloricPagerAdapter(getSupportFragmentManager()));
    }

    private void initListeners() {
        // TODO Auto-generated method stub
        mWeight.setOnClickListener(this);
        mCalendar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == mCalendar) {
            showCalendar();
            return ;
        }
    }
    
    private void showCalendar() {
        CalendarDialogFragment calendar = (CalendarDialogFragment) getSupportFragmentManager().findFragmentByTag("calendar");
        if (calendar != null) {
            getSupportFragmentManager().beginTransaction().remove(calendar);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(null);
        
        calendar = CalendarDialogFragment.newInstance();
        calendar.setTitle(getString(R.string.title_select_born_date));
        calendar.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {

            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                // TODO Auto-generated method stub
                
            }
            
        });
        calendar.setNegativeClickListener(new DialogFragmentInterface.OnClickListener() {

            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                // TODO Auto-generated method stub
                
            }
            
        });
        calendar.show(getSupportFragmentManager(), "calendar");
    }
    
}
