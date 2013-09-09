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
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;

public class MainActivity extends ActionBarActivity implements OnClickListener {

    private ViewPager mViewPager;
    private Button mWeight;
    private Button mCalendar;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setSlidingMenuEnable(true);
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_main);
        initValues();
        initViews();
        valueToView();
        initListeners();
    }

    private void initValues() {


    }

    private void initViews() {
        mWeight = (Button) findViewById(R.id.weight);
        mCalendar = (Button) findViewById(R.id.calendar);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
    }

    private void valueToView() {
        setActionBarTitle(R.string.app_name);
        mViewPager.setAdapter(new CaloricPagerAdapter(getSupportFragmentManager()));
    }

    private void initListeners() {
        
        getSlidingMenu().setOnClosedListener(new OnClosedListener() {
            
            @Override
            public void onClosed() {                
                
            }
        });
        
        getSlidingMenu().setOnOpenedListener(new OnOpenedListener() {
            
            @Override
            public void onOpened() {
                
                
            }
        });
        
        mWeight.setOnClickListener(this);
        mCalendar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mCalendar) {
            showCalendar();
            return ;
        }
    }

    private void showCalendar() {
        CalendarDialogFragment calendar = (CalendarDialogFragment) getSupportFragmentManager().
                                           findFragmentByTag("calendar");

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
            }
            
        });
        calendar.setNegativeClickListener(new DialogFragmentInterface.OnClickListener() {
            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {

            }

        });

        calendar.show(getSupportFragmentManager(), "calendar");
    }


}
