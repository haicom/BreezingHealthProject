package com.breezing.health.ui.activity;


import java.text.DecimalFormat;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.breezing.health.R;
import com.breezing.health.adapter.CaloricPagerAdapter;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.providers.Breezing.Account;
import com.breezing.health.providers.Breezing.Information;
import com.breezing.health.providers.Breezing.WeightChange;
import com.breezing.health.tools.IntentAction;
import com.breezing.health.ui.fragment.BaseDialogFragment;
import com.breezing.health.ui.fragment.CalendarDialogFragment;
import com.breezing.health.ui.fragment.CaloricBurnFragment;
import com.breezing.health.ui.fragment.DialogFragmentInterface;
import com.breezing.health.util.DateFormatUtil;
import com.breezing.health.util.LocalSharedPrefsUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;

public class MainActivity extends ActionBarActivity implements OnClickListener {
    private final static String TAG = "MainActivity";

    private ViewPager mViewPager;
    private Button mWeight;
    private Button mCalendar;
    private ContentResolver mContentResolver;
    private CaloricPagerAdapter mCaloricPagerAdapter;

    private int mDate;
    private int mAccountId;
    private int mPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setSlidingMenuEnable(true);

        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_main);
        initViews();
        valueToView();
        initListeners();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initValues();
    }

    private void initValues() {

        String weightString;
        mAccountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this,
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        mDate = DateFormatUtil.simpleDateFormat("yyyyMMdd");
        String dateString = DateFormatUtil.getCurrentDateString(this, mDate);
        mCalendar.setText(dateString);
        mContentResolver = getContentResolver();
        weightString = getBaseInfoViews(mAccountId);
        mWeight.setText( weightString );

    }

    private void initViews() {
        addRightActionItem(new ActionItem(ActionItem.ACTION_DONE));
        mWeight = (Button) findViewById(R.id.weight);
        mCalendar = (Button) findViewById(R.id.calendar);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
    }

    private void valueToView() {
        setActionBarTitle(R.string.app_name);
        mCaloricPagerAdapter = new CaloricPagerAdapter( getSupportFragmentManager() );
        mViewPager.setAdapter(mCaloricPagerAdapter);
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

        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
    }

    private void drawPiePicture() {
        CaloricBurnFragment caloricBurnFragment  = (CaloricBurnFragment) mCaloricPagerAdapter.
                getItem(mCaloricPagerAdapter.MAIN_INTERFACE_CALORIC_BURIN);
        caloricBurnFragment.drawPieChar( mAccountId, mDate );
    }

    public int getAccountId() {
        return mAccountId;
    }

    public int getDate() {
        return mDate;
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
//        if (calendar != null) {
//            getSupportFragmentManager().beginTransaction().remove(calendar);
//        }
//        getSupportFragmentManager().beginTransaction().addToBackStack(null);
        calendar = CalendarDialogFragment.getInstance();
        calendar.setTitle(getString(R.string.title_select_born_date));
        calendar.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {
            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                Log.d(TAG, "showCalendar params[0] = " + params[0] + " params[1] = " + params[1] + " params[2] =  " + params[2] );
                int date = DateFormatUtil.getCompleteDate(
                        Integer.parseInt( String.valueOf( params[0] ) ),
                        Integer.parseInt( String.valueOf( params[1] ) ),
                        Integer.parseInt( String.valueOf( params[2] ) ) );

                mDate = date;
                String dateString = DateFormatUtil.getCurrentDateString(dialog.getActivity(), date);
                Log.d(TAG, "showCalendar dateString = " + dateString + " mDate = " + mDate);
                mCalendar.setText(dateString);
                drawPiePicture();
            }

        });

        calendar.show(getSupportFragmentManager(), "calendar");
    }




    /**
     * 查询基本信息视图列表
     */
    private static final String[] PROJECTION_BASE_INFO = new String[] {
        Information.WEIGHT_UNIT ,     // 1
        WeightChange.WEIGHT           // 2

    };

    private static final int INFO_WEIGHT_UNIT_INDEX = 0;
    private static final int INFO_WEIGHT_INDEX = 1;


    public String getBaseInfoViews(int accountId) {
        Log.d(TAG, "queryBaseInfoView");
        String weightUnit = null;
        float  weight = 0;

        String accountClause =  Account.ACCOUNT_ID + " = ?";
        String sortOrder = WeightChange.DATE + " DESC";
        String[] args = new String[] { String.valueOf(accountId) };

        Cursor cursor  = mContentResolver.query(Information.CONTENT_BASE_INFO_URI,
                PROJECTION_BASE_INFO, accountClause, args, sortOrder);

        if (cursor == null) {
            Log.d(TAG, " testBaseInfoView cursor = " + cursor);
        }


        try {
            if (cursor != null) {
                if ( cursor.getCount() > 0 ) {
                    cursor.moveToPosition(0);
                    weightUnit = cursor.getString(INFO_WEIGHT_UNIT_INDEX);
                    weight = cursor.getFloat(INFO_WEIGHT_INDEX);
                }
            }
        } finally {
            cursor.close();
        }
        DecimalFormat df = new DecimalFormat("#.0");
        String str = df.format(weight);
        Log.d(TAG, "getBaseInfoViews str = " + str);
        return getResources().getString(R.string.breezing_weight, str, weightUnit);
    }

    @Override
    public void onClickActionBarItems(ActionItem item, View v) {

        switch( item.getActionId() ) {
            case ActionItem.ACTION_DONE:
                if (mPosition ==
                         CaloricPagerAdapter.MAIN_INTERFACE_CALORIC_BURIN ) {
                    Intent intent = new Intent(IntentAction.ACTIVITY_CALORIC_HISTORY);
                    startActivity(intent);
                } else if ( mPosition ==
                        CaloricPagerAdapter.MAIN_INTERFACE_CALORIC_BURIN ) {

                }
        }
        super.onClickActionBarItems(item, v);
    }
}
