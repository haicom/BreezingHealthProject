package com.breezing.health.ui.activity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.breezing.health.R;
import com.breezing.health.adapter.BalanceHistoryPagerAdapter;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.entity.AccountEntity;
import com.breezing.health.entity.enums.ChartModel;
import com.breezing.health.providers.Breezing.EnergyCost;
import com.breezing.health.providers.Breezing.Ingestion;
import com.breezing.health.providers.Breezing.WeightChange;
import com.breezing.health.ui.activity.CaloricHistoryActivity.CaloricHistoryType;
import com.breezing.health.ui.fragment.BaseDialogFragment;
import com.breezing.health.ui.fragment.CaloricWeeklyHistoryFragment;
import com.breezing.health.ui.fragment.ChartModelPickerDialogFragment;
import com.breezing.health.ui.fragment.DialogFragmentInterface;
import com.breezing.health.ui.fragment.MonthIntervalPickerDialogFragment;
import com.breezing.health.ui.fragment.WeekIntervalPickerDialogFragment;
import com.breezing.health.ui.fragment.YearIntervalPickerDialogFragment;
import com.breezing.health.util.BLog;
import com.breezing.health.util.CalendarUtil;
import com.breezing.health.util.DateFormatUtil;
import com.breezing.health.util.LocalSharedPrefsUtil;
import com.breezing.health.util.BreezingQueryViews;
import com.breezing.health.widget.BalanceBarChartView;
import com.breezing.health.widget.linechart.FancyChart;
import com.breezing.health.widget.linechart.FancyChartPointListener;
import com.breezing.health.widget.linechart.data.ChartData;
import com.breezing.health.widget.linechart.data.Point;
import com.breezing.health.widget.BarChart;
import com.viewpagerindicator.LinePageIndicator;

public class BalanceHistoryActivity extends ActionBarActivity implements OnClickListener {
    
    private static final String TAG = "BalanceHistoryActivity";
    
    public enum BalanceHistoryType { 
        CALOIRC_INTAKE(R.string.caloric_intake_history),
        CALOIRC_BURN(R.string.caloric_burn_history),
        CALOIRC_BALANCE(R.string.caloric_balance_history),
        CALOIRC_WEIGHT(R.string.caloric_weight_history),
        ENERGY_COST(R.string.energy_cost_history);
        
        private BalanceHistoryType(int nameRes) {
            this.nameRes = nameRes;
        }
        
        public int nameRes;
    }
    
    private Button mSelectModelButton;
    private Button mSelectIntervalButton;
    
    private ViewPager mBalancePager;
    private BalanceHistoryPagerAdapter mPagerAdapter;
    
    private View mIntakeHistoryPage;
    private View mBurnHistoryPage;
    private View mEnergyCostPage;
    private View mWeightHistoryPage;
    private View mBalanceHistoryPage;
    
    private FancyChart mIntakeHistoryChart;
    private FancyChart mBurnHistoryChart;
    private FancyChart mEnergyCostChart;
    private FancyChart mWeightHistoryChart;
    private BarChart   mBalanceHistoryChart;

   // private ViewGroup mBalanceHistoryPage;
    private BalanceBarChartView mBalanceBarChartView;

    private LinePageIndicator mLinePageIndicator;

    private ChartModel mBalanceChartModel = ChartModel.WEEK;
    
    private ContentResolver mContentResolver;
    private LayoutInflater  mLayoutInflater;
    
    private int mYear;
    private int mMonth;
    private int mWeek;
    
    private int mAccountId;
    
 
    
    private BalanceHistoryType mBalanceHistoryType;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_balance_history);
        initValues();
        initViews();
        valueToView();
        initListeners();
    }

    private void initValues() {        
        mBalanceHistoryType = BalanceHistoryType.values()[0];
        mContentResolver = getContentResolver();
        mLayoutInflater = getLayoutInflater();
        mYear = CalendarUtil.getCurrentYear();     
        mWeek = CalendarUtil.getWeekOfYear(new Date() );
        mMonth = CalendarUtil.getCurrentMonth();
       
    }

    private void initViews() {
        setActionBarTitle(R.string.balance_history);
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));
        setActionBarTitle(mBalanceHistoryType.nameRes);
        mSelectModelButton = (Button) findViewById(R.id.model);
        mSelectIntervalButton = (Button) findViewById(R.id.during);
        mBalancePager = (ViewPager) findViewById(R.id.view_pager);
        mLinePageIndicator = (LinePageIndicator) findViewById(R.id.indicator);
        
        mIntakeHistoryPage = mLayoutInflater.inflate(R.layout.page_of_caloric_intake_history, null);
        mIntakeHistoryChart = (FancyChart) mIntakeHistoryPage.findViewById(R.id.chart);
        
        mBurnHistoryPage = mLayoutInflater.inflate(R.layout.page_of_caloric_burn_history, null);
        mBurnHistoryChart = (FancyChart) mBurnHistoryPage.findViewById(R.id.chart);
        
        mEnergyCostPage = mLayoutInflater.inflate(R.layout.page_of_energy_cost_history, null);
        mEnergyCostChart = (FancyChart) mEnergyCostPage.findViewById(R.id.chart);       
        
     
        mWeightHistoryPage = mLayoutInflater.inflate(R.layout.page_of_weight_history, null);
        mWeightHistoryChart = (FancyChart) mWeightHistoryPage.findViewById(R.id.chart);

        mBalanceHistoryPage =  mLayoutInflater.inflate(R.layout.page_of_caloric_balance_history, null);
        mBalanceHistoryChart = (BarChart)mBalanceHistoryPage.findViewById(R.id.bar_chart);
//        mBalanceBarChartView = new BalanceBarChartView(this);
//        mBalanceBarChartView.setZoomable(false);
//        mBalanceHistoryPage.addView(mBalanceBarChartView);
    }

    private void valueToView() {
        mPagerAdapter = new BalanceHistoryPagerAdapter();
        mPagerAdapter.addViewPage(mIntakeHistoryPage);
        mPagerAdapter.addViewPage(mBurnHistoryPage);
        mPagerAdapter.addViewPage(mBalanceHistoryPage);
        mPagerAdapter.addViewPage(mWeightHistoryPage);
        mPagerAdapter.addViewPage(mEnergyCostPage);
        
        mBalancePager.setAdapter(mPagerAdapter);
        mLinePageIndicator.setViewPager(mBalancePager);
        mAccountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this,
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        
        refreshChartData();
    }

    private void initListeners() {
        mSelectModelButton.setOnClickListener(this);
        mSelectIntervalButton.setOnClickListener(this);
        mWeightHistoryChart.setOnPointClickListener(new FancyChartPointListener() {
            @Override
            public void onClick(Point point) {

            }
        });
        
        
        mLinePageIndicator.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                BLog.v(TAG, "ViewPager position =" + position);
                mBalanceHistoryType = BalanceHistoryType.values()[position];
                mLinePageIndicator.setCurrentItem(position);
                setActionBarTitle(mBalanceHistoryType.nameRes);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
        
        
    }

    @Override
    public void onClick(View v) {
        if ( v == mSelectModelButton ) {
            showModelPickerDialog();
            return ;
        } else if ( v == mSelectIntervalButton ) {

                switch ( mBalanceChartModel ) {
                    case WEEK: {
                        showWeekIntervalPickerDialog();
                        return ;
                    }

                    case MONTH: {
                        showMonthIntervalPickerDialog();
                        return ;
                    }

                    case YEAR: {
                        showYearIntervalPickerDialog();
                        return ;
                 }
            }

            return;
        }
    }

    private void showModelPickerDialog() {

        ChartModelPickerDialogFragment chartModelPicker =
                (ChartModelPickerDialogFragment) getSupportFragmentManager().findFragmentByTag("chartModelPicker");

        if (chartModelPicker != null) {
            getSupportFragmentManager().beginTransaction().remove(chartModelPicker);
        }

        getSupportFragmentManager().beginTransaction().addToBackStack(null);

        chartModelPicker = ChartModelPickerDialogFragment.newInstance();
        chartModelPicker.setTitle(getString(R.string.title_select_chart_model));
        chartModelPicker.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {

            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                mBalanceChartModel = (ChartModel) params[0];
                refreshChartData();
            }

        });

        chartModelPicker.show(getSupportFragmentManager(), "chartModelPicker");
    }

    private void showWeekIntervalPickerDialog() {

        WeekIntervalPickerDialogFragment weekIntervalPicker =
                (WeekIntervalPickerDialogFragment) getSupportFragmentManager().findFragmentByTag("weekIntervalPicker");

        if (weekIntervalPicker != null) {
            getSupportFragmentManager().beginTransaction().remove(weekIntervalPicker);
        }

        getSupportFragmentManager().beginTransaction().addToBackStack(null);

        weekIntervalPicker = WeekIntervalPickerDialogFragment.newInstance();
        weekIntervalPicker.setTitle(getString(R.string.title_select_interval));
        weekIntervalPicker.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {

            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                mYear = Integer.valueOf( String.valueOf(params[0]) );
                mWeek = Integer.valueOf( String.valueOf(params[1]) );

                mSelectIntervalButton.setText(dialog.getString(R.string.year_first_day_last_day,
                        mYear, String.valueOf( params[2] ) ) );
                refreshChartData();
            }

        });

        Bundle args = new Bundle();
        args.putInt(WeekIntervalPickerDialogFragment.WEEK_PICKER_DIALOG_WEEK, mWeek);
        weekIntervalPicker.setArguments(args);
        weekIntervalPicker.show(getSupportFragmentManager(), "weekIntervalPicker");
    }

    private void showMonthIntervalPickerDialog() {

        MonthIntervalPickerDialogFragment monthIntervalPicker =
                (MonthIntervalPickerDialogFragment) getSupportFragmentManager().findFragmentByTag("monthIntervalPicker");

        if ( monthIntervalPicker != null ) {
            getSupportFragmentManager().beginTransaction().remove(monthIntervalPicker);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(null);

        monthIntervalPicker = MonthIntervalPickerDialogFragment.newInstance();
        monthIntervalPicker.setTitle(getString(R.string.title_select_interval));
        monthIntervalPicker.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {

            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {

                mYear = Integer.valueOf( String.valueOf(params[0]) );
                mMonth = Integer.valueOf( String.valueOf(params[1]) );

                mSelectIntervalButton.setText(
                        getString(R.string.year_and_month, mYear, mMonth) );

                refreshChartData();
            }

        });

        Bundle args = new Bundle();
        args.putInt(MonthIntervalPickerDialogFragment.MONTH_PICKER_DIALOG_MONTH, mMonth);
        monthIntervalPicker.setArguments(args);
        monthIntervalPicker.show(getSupportFragmentManager(), "monthIntervalPicker");
    }

    private void showYearIntervalPickerDialog() {

       YearIntervalPickerDialogFragment yearIntervalPicker =
               (YearIntervalPickerDialogFragment)getSupportFragmentManager().findFragmentByTag("yearIntervalPicker");

        if (yearIntervalPicker != null) {
            getSupportFragmentManager().beginTransaction().remove(yearIntervalPicker);
        }

        getSupportFragmentManager().beginTransaction().addToBackStack(null);

        yearIntervalPicker = YearIntervalPickerDialogFragment.newInstance();
        yearIntervalPicker.setTitle(getString(R.string.title_select_interval));
        yearIntervalPicker.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {

            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {

                mYear = Integer.valueOf( String.valueOf(params[0]) );
                mSelectIntervalButton.setText(mYear + getString(R.string.year));
                refreshChartData();
            }

        });

        yearIntervalPicker.show(getSupportFragmentManager(), "yearIntervalPicker");
    }

    private void refreshChartData() {
        AccountEntity account;
        float caloricUnify;
        float weightUnify;
        int accountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this, LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        BreezingQueryViews query = new BreezingQueryViews( this );
        account = query.queryBaseInfoViews(accountId);
        caloricUnify = query.queryUnitObtainData( getString(R.string.caloric_type), account.getCaloricUnit() );
        weightUnify = query.queryUnitObtainData( getString(R.string.weight_type), account.getWeightUnit() );

        mSelectModelButton.setText(mBalanceChartModel.nameRes);
        refreshIntakeView(caloricUnify);
        refreshBurnView(caloricUnify);
        refreshEnergyCostView(caloricUnify);
        refreshWeightView(weightUnify);
        refreshBalanceBarView();
        switch ( mBalanceChartModel ) {
            case WEEK: {    
                mSelectIntervalButton.setText(
                        getString(R.string.year_first_day_last_day,
                        mYear,
                        CalendarUtil.getFirstDayAndLastDayOfWeek( this,
                        mYear,
                        mWeek ) ) );
                break;
            }
    
            case MONTH: {
                mSelectIntervalButton.setText(
                        getString(R.string.year_and_month, mYear, mMonth) );
                break;
            }
    
            case YEAR: {    
                mSelectIntervalButton.setText(mYear + getString(R.string.year));    
                break;
            }    
        }
    }
    
    private void refreshIntakeView(float caloricUnify) {
        ChartData data = new ChartData(ChartData.LINE_COLOR_RED);

        switch ( mBalanceChartModel ) {
            case WEEK: {
                drawIntakeChartDataWeekly(data, caloricUnify);
                break;
            }

            case MONTH: {
                drawIntakeChartDataMonthly(data, caloricUnify);
                break;
            }

            case YEAR: {
                drawIntakeChartDataYearly(data, caloricUnify);

                break;
            }
        }

        if (data != null) {
            mIntakeHistoryChart.clearValues();
            mIntakeHistoryChart.addData(data);
            mIntakeHistoryChart.invalidate();
        }
    }
    
    private void refreshBurnView(float caloricUnify) {
        ChartData data = new ChartData(ChartData.LINE_COLOR_RED);

        switch ( mBalanceChartModel ) {
            case WEEK: {
                drawBurnChartDataWeekly(data, caloricUnify);
                break;
            }

            case MONTH: {
                drawBurnChartDataMonthly(data, caloricUnify);
                break;
            }

            case YEAR: {
                drawBurnChartDataYearly(data, caloricUnify);
                break;
            }

        }

        if (data != null) {
            mBurnHistoryChart.clearValues();
            mBurnHistoryChart.addData(data);
            mBurnHistoryChart.invalidate();
        }
    }

    /**
     * 画我的我的能量摄入周视图
     * @param chartData
     * @return
     */
    private ChartData drawIntakeChartDataWeekly( ChartData chartData , float caloricUnify) {

        int  yearWeek =  DateFormatUtil.getCompleteWeek(mYear, mWeek);


        Calendar fistCalendar = CalendarUtil.getFirstDayOfWeek(mYear, mWeek) ;
        Calendar lastCalendar = CalendarUtil.getLastDayOfWeek(mYear, mWeek);

        HashMap<Integer, Integer> hashMap =
                new HashMap<Integer, Integer>();

        Log.d(TAG, " drawChartDataWeekly yearWeek = " + yearWeek
                + " mYear = " + mYear + " mWeek = " + mWeek);

        int index = 0;

        do {

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.setLength(0);

            int month = fistCalendar.get(Calendar.MONTH) + 1;
            int day = fistCalendar.get(Calendar.DAY_OF_MONTH);
            int date = Integer.valueOf(
                    CalendarUtil.getDayFromCalendar( fistCalendar ) );

            stringBuilder.append( String.valueOf(month) );
            stringBuilder.append(".");
            stringBuilder.append( String.valueOf(day) );
            chartData.addXValue( index, stringBuilder.toString() );
            hashMap.put(date, index);

            fistCalendar.add(GregorianCalendar.DATE, 1);
            Log.d(TAG, " drawChartDataWeekly index = " + index);
            index++;
        }  while ( fistCalendar.compareTo(lastCalendar) <= 0 );
        
        chartData = fillInTotalIngestionInWeek( yearWeek, chartData, hashMap, caloricUnify );
        return chartData;
    }
    
    /**
     * 画我的我的能量代谢周视图
     * @param chartData
     * @return
     */
    private ChartData drawBurnChartDataWeekly( ChartData chartData, float caloricUnify ) {

        int  yearWeek =  DateFormatUtil.getCompleteWeek(mYear, mWeek);


        Calendar fistCalendar = CalendarUtil.getFirstDayOfWeek(mYear, mWeek) ;
        Calendar lastCalendar = CalendarUtil.getLastDayOfWeek(mYear, mWeek);

        HashMap<Integer, Integer> hashMap =
                new HashMap<Integer, Integer>();

        Log.d(TAG, " drawChartDataWeekly yearWeek = " + yearWeek
                + " mYear = " + mYear + " mWeek = " + mWeek);

        int index = 0;

        do {

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.setLength(0);

            int month = fistCalendar.get(Calendar.MONTH) + 1;
            int day = fistCalendar.get(Calendar.DAY_OF_MONTH);
            int date = Integer.valueOf(
                    CalendarUtil.getDayFromCalendar( fistCalendar ) );

            stringBuilder.append( String.valueOf(month) );
            stringBuilder.append(".");
            stringBuilder.append( String.valueOf(day) );
            chartData.addXValue( index, stringBuilder.toString() );
            hashMap.put(date, index);

            fistCalendar.add(GregorianCalendar.DATE, 1);
            Log.d(TAG, " drawChartDataWeekly index = " + index);
            index++;
        }  while ( fistCalendar.compareTo(lastCalendar) <= 0 );
        
        chartData =  fillInTotalBurnInWeek( yearWeek, chartData, hashMap, caloricUnify);
        return chartData;
    }

    private static final String[] PROJECTION_ENERGY_COST_DAYLY = new String[] {
        EnergyCost.TOTAL_ENERGY ,   // 0
        EnergyCost.DATE       // 1
    };


    private static final int ALL_TOTAL_ENERGY_COLUMN_DAYLY_INDEX = 0;
    private static final int DATE_COLUMN_DAYLY_INDEX = 1;


    /***
     * 周视图,我的能量代谢到ChartData队列中
     * @param yearWeek
     * @param chartData
     * @param hashMap
     * @return
     */
    private ChartData  fillInTotalBurnInWeek(int yearWeek ,
                               ChartData chartData,
                               HashMap<Integer, Integer> hashMap,
                               float caloricUnify) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(EnergyCost.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(EnergyCost.YEAR_WEEK + " = ? ");

        String sortOrder = EnergyCost.DATE + " ASC ";

        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(EnergyCost.CONTENT_URI,
                    PROJECTION_ENERGY_COST_DAYLY,
                    stringBuilder.toString(),
                    new String[] { String.valueOf(mAccountId),  String.valueOf(yearWeek) },
                    sortOrder );

            if (cursor == null) {
                Log.d(TAG, " fillInTotalEnergyInWeek cursor = " + cursor);
            }

            cursor.moveToPosition(-1);

            while (cursor.moveToNext() ) {
                float allTotalEnergy = cursor.getFloat(ALL_TOTAL_ENERGY_COLUMN_DAYLY_INDEX);
                int  day = cursor.getInt(DATE_COLUMN_DAYLY_INDEX);
                if ( hashMap.containsKey(day) ) {
                    int index = hashMap.get(day);
                    chartData.addPoint(index,
                            (int) (allTotalEnergy * caloricUnify),
                            getString(mBalanceChartModel.nameRes),                            
                            String.valueOf( (int) (allTotalEnergy * caloricUnify ) ) );
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return chartData;
    }

    private static final String[] PROJECTION_INGESTION_COST_DAYLY = new String[] {
        Ingestion.TOTAL_INGESTION ,   // 1
        Ingestion.DATE       // 2
    };

    private static final int TOTAL_INGESTION_COLUMN_DAYLY_INDEX = 0;
    private static final int INGESTION_DATE_COLUMN_DAYLY_INDEX = 1;


    /***
     * 周视图,我的能量消耗到ChartData队列中
     * @param yearWeek
     * @param chartData
     * @param hashMap
     * @return
     */
    private ChartData  fillInTotalIngestionInWeek(int yearWeek ,
                               ChartData chartData,
                               HashMap<Integer, Integer> hashMap, float caloricUnify ) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(Ingestion.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(Ingestion.YEAR_WEEK + " = ? ");

        String sortOrder = Ingestion.DATE + " ASC ";

        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(Ingestion.CONTENT_URI,
                    PROJECTION_INGESTION_COST_DAYLY,
                    stringBuilder.toString(),
                    new String[] { String.valueOf(mAccountId),  String.valueOf(yearWeek) },
                    sortOrder );

            if (cursor == null) {
                Log.d(TAG, " fillInTotalIngestionInWeek cursor = " + cursor);
            }

            cursor.moveToPosition(-1);

            while (cursor.moveToNext() ) {
                float allTotalEnergy = cursor.getFloat(TOTAL_INGESTION_COLUMN_DAYLY_INDEX);
                int  day = cursor.getInt(INGESTION_DATE_COLUMN_DAYLY_INDEX);
                if ( hashMap.containsKey(day) ) {
                    int index = hashMap.get(day);
                    chartData.addPoint(index,
                            (int) (allTotalEnergy * caloricUnify) ,
                            getString(mBalanceChartModel.nameRes),
                            String.valueOf( (int) (allTotalEnergy * caloricUnify) ) );
                }

            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return chartData;
    }


    /**
     * 画我的消耗，我的摄入月视图
     * @param chartData
     * @return
     */
    private ChartData drawIntakeChartDataMonthly(ChartData  chartData, float caloricUnify) {

        int  yearMonth =  DateFormatUtil.getCompleteWeek(mYear, mMonth);
        int  weekNum = CalendarUtil.getTotalWeeksInMonth(mYear, mMonth);

        HashMap<Integer, Integer> hashMap =
                new HashMap<Integer, Integer>();

        Calendar fistCalendar = CalendarUtil.getFirstCalendarOfMonth(mYear, mMonth);


        BLog.d(TAG, " drawChartDataMonthly yearMonth = " + yearMonth
                + " mYear = " + mYear + " mMonth = " + mMonth + " weekNum = " + weekNum);

        for ( int index = 0; index < weekNum; index++ ) {

             int weeks = fistCalendar.get(Calendar.WEEK_OF_YEAR);
             int yearWeek = DateFormatUtil.getCompleteWeek(mYear, weeks);

             hashMap.put(yearWeek, index);

             BLog.d(TAG, " drawChartDataMonthly weeks = " + weeks
                     + " yearWeek = " + yearWeek);

             chartData.addXValue( index, getString(R.string.month_in_week, index + 1) );
             fistCalendar.add(Calendar.WEEK_OF_YEAR, 1);
        }
        
        chartData = fillInTotalIngestionInMonth(yearMonth, chartData, hashMap, caloricUnify);
        return chartData;
    }
    
    private ChartData drawBurnChartDataMonthly(ChartData  chartData, float caloricUnify) {

        int  yearMonth =  DateFormatUtil.getCompleteWeek(mYear, mMonth);
        int  weekNum = CalendarUtil.getTotalWeeksInMonth(mYear, mMonth);

        HashMap<Integer, Integer> hashMap =
                new HashMap<Integer, Integer>();

        Calendar fistCalendar = CalendarUtil.getFirstCalendarOfMonth(mYear, mMonth);


        BLog.d(TAG, " drawChartDataMonthly yearMonth = " + yearMonth
                + " mYear = " + mYear + " mMonth = " + mMonth + " weekNum = " + weekNum);

        for ( int index = 0; index < weekNum; index++ ) {

             int weeks = fistCalendar.get(Calendar.WEEK_OF_YEAR);
             int yearWeek = DateFormatUtil.getCompleteWeek(mYear, weeks);

             hashMap.put(yearWeek, index);

             BLog.d(TAG, " drawChartDataMonthly weeks = " + weeks
                     + " yearWeek = " + yearWeek);

             chartData.addXValue( index, getString(R.string.month_in_week, index + 1) );
             fistCalendar.add(Calendar.WEEK_OF_YEAR, 1);
        }
        
        chartData = fillInTotalBurnInMonth(yearMonth, chartData, hashMap, caloricUnify);
        
        return chartData;
    }
    
    /**
     * 我的总能量消耗查看每一周，某一个帐户的周信息列表
     */
    private static final String[] PROJECTION_ENERGY_COST_WEEKLY = new String[] {
        EnergyCost.AVG_TOTAL_ENERGY,    // 2
        EnergyCost.YEAR_WEEK            //5
    };


    private static final int AVG_TOTAL_ENERGY_COLUMN_WEEKLY_INDEX = 0;
    private static final int YEAR_WEEK_COLUMN_WEEKLY_INDEX = 1;


    /***
     * 填写我的能量消耗月视图总能量到ChartData队列中
     * @param yearWeek
     * @param chartData
     * @param hashMap
     * @return
     */
    private ChartData  fillInTotalBurnInMonth(int  yearMonth ,
                               ChartData chartData,
                               HashMap<Integer, Integer> hashMap,
                               float caloricUnify) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(EnergyCost.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(EnergyCost.YEAR_MONTH + " = ? ");

        String sortOrder = EnergyCost.YEAR_WEEK + " ASC ";

        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(EnergyCost.CONTENT_WEEKLY_URI,
                    PROJECTION_ENERGY_COST_WEEKLY,
                    stringBuilder.toString(),
                    new String[] { String.valueOf(mAccountId),  String.valueOf(yearMonth) },
                    sortOrder );

            if (cursor == null) {
                Log.d(TAG, " fillInTotalEnergyInMonth cursor = " + cursor);
            }

            cursor.moveToPosition(-1);

            while (cursor.moveToNext() ) {
                float avgTotalEnergy = cursor.getFloat(AVG_TOTAL_ENERGY_COLUMN_WEEKLY_INDEX);
                int  yearWeek = cursor.getInt(YEAR_WEEK_COLUMN_WEEKLY_INDEX);
                Log.d(TAG, " fillInTotalEnergyInMonth avgTotalEnergy = " + avgTotalEnergy + " yearWeek = " + yearWeek);
                if ( hashMap.containsKey(yearWeek) ) {
                    int index = hashMap.get(yearWeek);
                    Log.d(TAG, " fillInTotalEnergyInMonth index = " + index);
                    chartData.addPoint(index,
                            (int) (avgTotalEnergy * caloricUnify),
                            getString(mBalanceChartModel.nameRes),
                            String.valueOf( (int) (avgTotalEnergy * caloricUnify ) ) );
                }

            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return chartData;
    }

    /**
     * 我的能量摄入查看每一周，某一个帐户的周信息列表
     */
    private static final String[] PROJECTION_INGESTION_WEEKLY = new String[] {
        Ingestion.AVG_TOTAL_INGESTION,    // 1
        Ingestion.YEAR_WEEK            //2
    };


    private static final int AVG_TOTAL_INGESTION_COLUMN_WEEKLY_INDEX = 0;
    private static final int INGESTION_YEAR_WEEK_COLUMN_WEEKLY_INDEX = 1;


    /***
     * 填写我的摄入月视图总能量到ChartData队列中
     * @param yearWeek
     * @param chartData
     * @param hashMap
     * @return
     */
    private ChartData  fillInTotalIngestionInMonth(int  yearMonth ,
                               ChartData chartData,
                               HashMap<Integer, Integer> hashMap,
                               float caloricUnify) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(Ingestion.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(Ingestion.YEAR_MONTH + " = ? ");

        String sortOrder = Ingestion.YEAR_WEEK + " ASC ";

        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(Ingestion.CONTENT_WEEKLY_URI,
                    PROJECTION_INGESTION_WEEKLY,
                    stringBuilder.toString(),
                    new String[] { String.valueOf(mAccountId),  String.valueOf(yearMonth) },
                    sortOrder );

            if (cursor == null) {
                Log.d(TAG, " fillInTotalIngestionInMonth cursor = " + cursor);
            }

            cursor.moveToPosition(-1);

            while (cursor.moveToNext() ) {

                float  avgTotalEnergy = cursor.getFloat(AVG_TOTAL_INGESTION_COLUMN_WEEKLY_INDEX);
                int  yearWeek = cursor.getInt(INGESTION_YEAR_WEEK_COLUMN_WEEKLY_INDEX);

                Log.d(TAG, " fillInTotalEnergyInMonth avgTotalEnergy = " + avgTotalEnergy + " yearWeek = " + yearWeek);

                if ( hashMap.containsKey(yearWeek) ) {

                    int index = hashMap.get(yearWeek);

                    Log.d(TAG, " fillInTotalEnergyInMonth index = " + index);

                    chartData.addPoint(index,
                            (int) (avgTotalEnergy * caloricUnify),
                            getString(mBalanceChartModel.nameRes),
                            String.valueOf( (int) (avgTotalEnergy * caloricUnify) ) );
                }

            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return chartData;
    }


    /**
     * 画年视图
     * @param chartData
     * @return
     */
    private ChartData drawIntakeChartDataYearly(ChartData  chartData, float caloricUnify) {

        HashMap<Integer, Integer> hashMap =
                new HashMap<Integer, Integer>();

        Calendar fistCalendar = CalendarUtil.getFirstCalendarOfMonth(mYear, mMonth);


        BLog.d(TAG, " drawChartDataMonthly  mYear = " + mYear + " mMonth = " + mMonth );

        for ( int index = 0; index < CALORIC_MONTH_NUMBER; index++ ) {

             int weeks = fistCalendar.get(Calendar.WEEK_OF_YEAR);
             int yearMonth = DateFormatUtil.getCompleteYearMonth(mYear, index + 1);
             hashMap.put(yearMonth, index);

             BLog.d(TAG, " drawChartDataMonthly weeks = " + weeks
                     + " yearMonth = " + yearMonth);

             chartData.addXValue( index, getString(R.string.year_in_month, index + 1) );
             fistCalendar.add(Calendar.WEEK_OF_YEAR, 1);
        }
        
        chartData = fillInIngestionInYear(chartData, hashMap, caloricUnify);
        return chartData;
    }
    
    /**
     * 画年视图
     * @param chartData
     * @return
     */
    private ChartData drawBurnChartDataYearly(ChartData  chartData, float caloricUnify) {

        HashMap<Integer, Integer> hashMap =
                new HashMap<Integer, Integer>();

        Calendar fistCalendar = CalendarUtil.getFirstCalendarOfMonth(mYear, mMonth);


        BLog.d(TAG, " drawChartDataMonthly  mYear = " + mYear + " mMonth = " + mMonth );

        for ( int index = 0; index < CALORIC_MONTH_NUMBER; index++ ) {

             int weeks = fistCalendar.get(Calendar.WEEK_OF_YEAR);
             int yearMonth = DateFormatUtil.getCompleteYearMonth(mYear, index + 1);
             hashMap.put(yearMonth, index);

             BLog.d(TAG, " drawChartDataMonthly weeks = " + weeks
                     + " yearMonth = " + yearMonth);

             chartData.addXValue( index, getString(R.string.year_in_month, index + 1) );
             fistCalendar.add(Calendar.WEEK_OF_YEAR, 1);
        }
        
        chartData = fillInTotalBurnInYear(chartData, hashMap, caloricUnify);
        return chartData;
    }

    /**
     * 我的总能量消耗查看每一月，某一个帐户的周信息列表
     */
    private static final String[] PROJECTION_ENERGY_COST_MONTHLY = new String[] {
        EnergyCost.AVG_TOTAL_ENERGY,    // 1
        EnergyCost.YEAR_MONTH            //2
    };


    private static final int AVG_TOTAL_ENERGY_COLUMN_MONTHLY_INDEX = 0;
    private static final int YEAR_MONTH_COLUMN_INDEX = 1;

    /***
     * 填写年视图总能量到ChartData队列中
     * @param yearWeek
     * @param chartData
     * @param hashMap
     * @return
     */
    private ChartData  fillInTotalBurnInYear(ChartData chartData,
                               HashMap<Integer, Integer> hashMap,
                               float caloricUnify) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(EnergyCost.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(EnergyCost.YEAR + " = ? ");

        String sortOrder = EnergyCost.YEAR_MONTH + " ASC ";

        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(EnergyCost.CONTENT_MONTHLY_URI,
                    PROJECTION_ENERGY_COST_MONTHLY,
                    stringBuilder.toString(),
                    new String[] { String.valueOf(mAccountId),  String.valueOf(mYear) },
                    sortOrder );

            if (cursor == null) {
                Log.d(TAG, " testCostWeekly cursor = " + cursor);
            }

            cursor.moveToPosition(-1);

            while (cursor.moveToNext() ) {
                float avgTotalEnergy = cursor.getFloat(AVG_TOTAL_ENERGY_COLUMN_MONTHLY_INDEX);
                int  yearMonth = cursor.getInt(YEAR_MONTH_COLUMN_INDEX);

                Log.d(TAG, " fillInTotalEnergyInYear avgTotalEnergy = " + avgTotalEnergy + " yearMonth = " + yearMonth);

                if ( hashMap.containsKey(yearMonth) ) {
                    int index = hashMap.get(yearMonth);
                    Log.d(TAG, " fillInTotalEnergyInMonth index = " + index);
                    chartData.addPoint(index,
                            (int) (avgTotalEnergy * caloricUnify),
                            getString(mBalanceChartModel.nameRes),
                            String.valueOf( (int) (avgTotalEnergy * caloricUnify) ) );
                }

            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return chartData;
    }

    /**
     * 我的能量摄入查看每一月，某一个帐户的月信息列表
     */
    private static final String[] PROJECTION_INGESTION_MONTHLY = new String[] {
        Ingestion.AVG_TOTAL_INGESTION,    // 1
        Ingestion.YEAR_MONTH            //2
    };


    private static final int AVG_INGESTION_COLUMN_MONTHLY_INDEX = 0;
    private static final int INGESTION_YEAR_MONTH_COLUMN_INDEX = 1;

    /***
     * 填写年视图能量摄入到ChartData队列中
     * @param yearWeek
     * @param chartData
     * @param hashMap
     * @return
     */
    private ChartData  fillInIngestionInYear(ChartData chartData,
                               HashMap<Integer, Integer> hashMap,
                               float caloricUnify) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(EnergyCost.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(EnergyCost.YEAR + " = ? ");

        String sortOrder = EnergyCost.YEAR_MONTH + " ASC ";

        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(Ingestion.CONTENT_MONTHLY_URI,
                    PROJECTION_INGESTION_MONTHLY,
                    stringBuilder.toString(),
                    new String[] { String.valueOf(mAccountId),  String.valueOf(mYear) },
                    sortOrder );

            if (cursor == null) {
                Log.d(TAG, " testCostWeekly cursor = " + cursor);
            }

            cursor.moveToPosition(-1);

            while (cursor.moveToNext() ) {
                float avgTotalEnergy = cursor.getFloat(AVG_INGESTION_COLUMN_MONTHLY_INDEX);
                int  yearMonth = cursor.getInt(INGESTION_YEAR_MONTH_COLUMN_INDEX);

                Log.d(TAG, " fillInTotalEnergyInYear avgTotalEnergy = " + avgTotalEnergy + " yearMonth = " + yearMonth);

                if ( hashMap.containsKey(yearMonth) ) {
                    int index = hashMap.get(yearMonth);
                    Log.d(TAG, " fillInTotalEnergyInMonth index = " + index);
                    chartData.addPoint(index,
                            (int) (avgTotalEnergy * caloricUnify),
                            getString(mBalanceChartModel.nameRes),
                            String.valueOf( ( int ) (avgTotalEnergy * caloricUnify) ) );
                }

            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return chartData;
    }
    
    
    
    private void refreshEnergyCostView(float caloricUnify) {
        ChartData data = new ChartData(ChartData.LINE_COLOR_RED);

        switch ( mBalanceChartModel ) {
            case WEEK: {
                drawEnergyCostChartDataWeekly(data, caloricUnify);
                break;
            }

            case MONTH: {
                drawEnergyCostChartDataMonthly(data, caloricUnify);
                break;
            }

            case YEAR: {
                drawEnergyCostChartDataYearly(data, caloricUnify);
                break;
            }

        }

        if (data != null) {
            mEnergyCostChart.clearValues();
            mEnergyCostChart.addData(data);
            mEnergyCostChart.invalidate();
        }
    }
    
    /**
     * 画我的我的能量代谢周视图
     * @param chartData
     * @return
     */
    private ChartData drawEnergyCostChartDataWeekly( ChartData chartData,
                                                     float caloricUnify ) {

        int  yearWeek =  DateFormatUtil.getCompleteWeek(mYear, mWeek);


        Calendar fistCalendar = CalendarUtil.getFirstDayOfWeek(mYear, mWeek) ;
        Calendar lastCalendar = CalendarUtil.getLastDayOfWeek(mYear, mWeek);

        HashMap<Integer, Integer> hashMap =
                new HashMap<Integer, Integer>();

        Log.d(TAG, " drawChartDataWeekly yearWeek = " + yearWeek
                + " mYear = " + mYear + " mWeek = " + mWeek);

        int index = 0;

        do {

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.setLength(0);

            int month = fistCalendar.get(Calendar.MONTH) + 1;
            int day = fistCalendar.get(Calendar.DAY_OF_MONTH);
            int date = Integer.valueOf(
                    CalendarUtil.getDayFromCalendar( fistCalendar ) );

            stringBuilder.append( String.valueOf(month) );
            stringBuilder.append(".");
            stringBuilder.append( String.valueOf(day) );
            chartData.addXValue( index, stringBuilder.toString() );
            hashMap.put(date, index);

            fistCalendar.add(GregorianCalendar.DATE, 1);
            Log.d(TAG, " drawChartDataWeekly index = " + index);
            index++;
        }  while ( fistCalendar.compareTo(lastCalendar) <= 0 );
        
        chartData =  fillInTotalEnergyCostInWeek( yearWeek, chartData, hashMap, caloricUnify);
        return chartData;
    }
    
    private static final String[] PROJECTION_METABOLISM_DAYLY = new String[] {
        EnergyCost.METABOLISM ,   // 0
        EnergyCost.DATE       // 1
    };


    private static final int ALL_METABOLISM_COLUMN_DAYLY_INDEX = 0;
  
    
    /***
     * 周视图,我的能量代谢到ChartData队列中
     * @param yearWeek
     * @param chartData
     * @param hashMap
     * @return
     */
    private ChartData  fillInTotalEnergyCostInWeek(int yearWeek ,
                               ChartData chartData,
                               HashMap<Integer, Integer> hashMap,
                               float caloricUnify) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(EnergyCost.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(EnergyCost.YEAR_WEEK + " = ? ");

        String sortOrder = EnergyCost.DATE + " ASC ";

        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(EnergyCost.CONTENT_URI,
                    PROJECTION_METABOLISM_DAYLY,
                    stringBuilder.toString(),
                    new String[] { String.valueOf(mAccountId),  String.valueOf(yearWeek) },
                    sortOrder );

            if (cursor == null) {
                Log.d(TAG, " fillInTotalEnergyInWeek cursor = " + cursor);
            }

            cursor.moveToPosition(-1);

            while (cursor.moveToNext() ) {
                float metabolism = cursor.getFloat(ALL_METABOLISM_COLUMN_DAYLY_INDEX);
                int  day = cursor.getInt(DATE_COLUMN_DAYLY_INDEX);
                if ( hashMap.containsKey(day) ) {
                    int index = hashMap.get(day);
                    chartData.addPoint(index,
                            (int)(metabolism * caloricUnify),
                            getString(mBalanceChartModel.nameRes),                            
                            String.valueOf( (int) (metabolism * caloricUnify)  ) );
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return chartData;
    }
    
    private ChartData drawEnergyCostChartDataMonthly(ChartData  chartData, float caloricUnify) {

        int  yearMonth =  DateFormatUtil.getCompleteWeek(mYear, mMonth);
        int  weekNum = CalendarUtil.getTotalWeeksInMonth(mYear, mMonth);

        HashMap<Integer, Integer> hashMap =
                new HashMap<Integer, Integer>();

        Calendar fistCalendar = CalendarUtil.getFirstCalendarOfMonth(mYear, mMonth);


        BLog.d(TAG, " drawChartDataMonthly yearMonth = " + yearMonth
                + " mYear = " + mYear + " mMonth = " + mMonth + " weekNum = " + weekNum);

        for ( int index = 0; index < weekNum; index++ ) {

             int weeks = fistCalendar.get(Calendar.WEEK_OF_YEAR);
             int yearWeek = DateFormatUtil.getCompleteWeek(mYear, weeks);

             hashMap.put(yearWeek, index);

             BLog.d(TAG, " drawChartDataMonthly weeks = " + weeks
                     + " yearWeek = " + yearWeek);

             chartData.addXValue( index, getString(R.string.month_in_week, index + 1) );
             fistCalendar.add(Calendar.WEEK_OF_YEAR, 1);
        }
        
        chartData = fillInTotalEnergyCostInMonth(yearMonth, chartData, hashMap, caloricUnify);
        
        return chartData;
    }
    
    /**
     * 我的总能量消耗查看每一周，某一个帐户的周信息列表
     */
    private static final String[] PROJECTION_METABOLISM_WEEKLY = new String[] {
        EnergyCost.AVG_METABOLISM,    // 2
        EnergyCost.YEAR_WEEK            //5
    };


    private static final int METABOLISM_COLUMN_WEEKLY_INDEX = 0;
 
    
    /***
     * 填写我的能量消耗月视图总能量到ChartData队列中
     * @param yearWeek
     * @param chartData
     * @param hashMap
     * @return
     */
    private ChartData  fillInTotalEnergyCostInMonth(int  yearMonth ,
                               ChartData chartData,
                               HashMap<Integer, Integer> hashMap,
                               float caloricUnify) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(EnergyCost.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(EnergyCost.YEAR_MONTH + " = ? ");

        String sortOrder = EnergyCost.YEAR_WEEK + " ASC ";

        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(EnergyCost.CONTENT_WEEKLY_URI,
                    PROJECTION_METABOLISM_WEEKLY,
                    stringBuilder.toString(),
                    new String[] { String.valueOf(mAccountId),  String.valueOf(yearMonth) },
                    sortOrder );

            if (cursor == null) {
                Log.d(TAG, " fillInTotalEnergyInMonth cursor = " + cursor);
            }

            cursor.moveToPosition(-1);

            while (cursor.moveToNext() ) {
                float metabolism = cursor.getFloat(METABOLISM_COLUMN_WEEKLY_INDEX);
                int  yearWeek = cursor.getInt(YEAR_WEEK_COLUMN_WEEKLY_INDEX);
                Log.d(TAG, " fillInTotalEnergyInMonth avgTotalEnergy = " + metabolism + " yearWeek = " + yearWeek);
                if ( hashMap.containsKey(yearWeek) ) {
                    int index = hashMap.get(yearWeek);
                    Log.d(TAG, " fillInTotalEnergyInMonth index = " + index);
                    chartData.addPoint(index,
                            (int)(metabolism * caloricUnify),
                            getString(mBalanceChartModel.nameRes),
                            String.valueOf( (int) (metabolism * caloricUnify) ) );
                }

            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return chartData;
    }
    
    /**
     * 画年视图
     * @param chartData
     * @return
     */
    private ChartData drawEnergyCostChartDataYearly(ChartData  chartData, float caloricUnify) {

        HashMap<Integer, Integer> hashMap =
                new HashMap<Integer, Integer>();

        Calendar fistCalendar = CalendarUtil.getFirstCalendarOfMonth(mYear, mMonth);


        BLog.d(TAG, " drawChartDataMonthly  mYear = " + mYear + " mMonth = " + mMonth );

        for ( int index = 0; index < CALORIC_MONTH_NUMBER; index++ ) {

             int weeks = fistCalendar.get(Calendar.WEEK_OF_YEAR);
             int yearMonth = DateFormatUtil.getCompleteYearMonth(mYear, index + 1);
             hashMap.put(yearMonth, index);

             BLog.d(TAG, " drawChartDataMonthly weeks = " + weeks
                     + " yearMonth = " + yearMonth);

             chartData.addXValue( index, getString(R.string.year_in_month, index + 1) );
             fistCalendar.add(Calendar.WEEK_OF_YEAR, 1);
        }
        
        chartData = fillInTotalEnergyCostInYear(chartData, hashMap, caloricUnify);
        return chartData;
    }
    
    /**
     * 我的总能量消耗查看每一月，某一个帐户的周信息列表
     */
    private static final String[] PROJECTION_METABOLISM_MONTHLY = new String[] {
        EnergyCost.AVG_METABOLISM,    // 1
        EnergyCost.YEAR_MONTH            //2
    };


    private static final int AVG_METABOLISM_MONTHLY_INDEX = 0;
   

    /***
     * 填写年视图总能量到ChartData队列中
     * @param yearWeek
     * @param chartData
     * @param hashMap
     * @return
     */
    private ChartData  fillInTotalEnergyCostInYear(ChartData chartData,
                               HashMap<Integer, Integer> hashMap,
                               float caloricUnify) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(EnergyCost.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(EnergyCost.YEAR + " = ? ");

        String sortOrder = EnergyCost.YEAR_MONTH + " ASC ";

        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(EnergyCost.CONTENT_MONTHLY_URI,
                    PROJECTION_METABOLISM_MONTHLY,
                    stringBuilder.toString(),
                    new String[] { String.valueOf(mAccountId),  String.valueOf(mYear) },
                    sortOrder );

            if (cursor == null) {
                Log.d(TAG, " testCostWeekly cursor = " + cursor);
            }

            cursor.moveToPosition(-1);

            while (cursor.moveToNext() ) {
                float avgTotalEnergy = cursor.getFloat(AVG_METABOLISM_MONTHLY_INDEX);
                int  yearMonth = cursor.getInt(YEAR_MONTH_COLUMN_INDEX);

                Log.d(TAG, " fillInTotalEnergyInYear avgTotalEnergy = " + avgTotalEnergy + " yearMonth = " + yearMonth);

                if ( hashMap.containsKey(yearMonth) ) {  
                    int index = hashMap.get(yearMonth);
                    Log.d(TAG, " fillInTotalEnergyInMonth index = " + index);   
                    chartData.addPoint(index,
                            (int) (avgTotalEnergy * caloricUnify),
                            getString(mBalanceChartModel.nameRes),
                            String.valueOf( (int) (avgTotalEnergy * caloricUnify ) ) );
                }

            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return chartData;
    }
    
    private void refreshWeightView(float weightUnify) {
        ChartData data = new ChartData(ChartData.LINE_COLOR_RED);

        switch ( mBalanceChartModel ) {
            case WEEK: {
                drawWeightChartDataWeekly(data, weightUnify);
                break;
            }

            case MONTH: {
                drawWeightChartDataMonthly(data, weightUnify);
                break;
            }

            case YEAR: {
                drawWeightChartDataYearly(data, weightUnify);
                break;
            }

        }

        if (data != null) {
            mWeightHistoryChart.clearValues();
            mWeightHistoryChart.addData(data);
            mWeightHistoryChart.invalidate();
        }
    }

    private void refreshBalanceBarView( ) {       

        switch ( mBalanceChartModel ) {
            case WEEK: {
                drawBalanceChartDataWeekly();
                break;
            }

            case MONTH: {
                drawBalanceChartDataMonthly();
                break;
            }

            case YEAR: {
                drawBalanceChartDataYearly();
                break;
            }

        }

        mBalanceHistoryChart.invalidate();
    }
    
    /**
     * 画我的我的能量代谢周视图
     * @param chartData
     * @return
     */
    private ChartData drawWeightChartDataWeekly( ChartData chartData, float weightUnify ) {

        int  yearWeek =  DateFormatUtil.getCompleteWeek(mYear, mWeek);


        Calendar fistCalendar = CalendarUtil.getFirstDayOfWeek(mYear, mWeek) ;
        Calendar lastCalendar = CalendarUtil.getLastDayOfWeek(mYear, mWeek);

        HashMap<Integer, Integer> hashMap =
                new HashMap<Integer, Integer>();

        Log.d(TAG, " drawChartDataWeekly yearWeek = " + yearWeek
                + " mYear = " + mYear + " mWeek = " + mWeek);

        int index = 0;

        do {

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.setLength(0);

            int month = fistCalendar.get(Calendar.MONTH) + 1;
            int day = fistCalendar.get(Calendar.DAY_OF_MONTH);
            int date = Integer.valueOf(
                    CalendarUtil.getDayFromCalendar( fistCalendar ) );

            stringBuilder.append( String.valueOf(month) );
            stringBuilder.append(".");
            stringBuilder.append( String.valueOf(day) );
            chartData.addXValue( index, stringBuilder.toString() );
            hashMap.put(date, index);

            fistCalendar.add(GregorianCalendar.DATE, 1);
            Log.d(TAG, " drawChartDataWeekly index = " + index);
            index++;
        }  while ( fistCalendar.compareTo(lastCalendar) <= 0 );
        
        chartData =  fillInWeightChangeInWeek( yearWeek, chartData, hashMap, weightUnify);
        return chartData;
    }
    
    private ChartData drawWeightChartDataMonthly(ChartData  chartData, float weightUnify) {

        int  yearMonth =  DateFormatUtil.getCompleteWeek(mYear, mMonth);
        int  weekNum = CalendarUtil.getTotalWeeksInMonth(mYear, mMonth);

        HashMap<Integer, Integer> hashMap =
                new HashMap<Integer, Integer>();

        Calendar fistCalendar = CalendarUtil.getFirstCalendarOfMonth(mYear, mMonth);


        BLog.d(TAG, " drawChartDataMonthly yearMonth = " + yearMonth
                + " mYear = " + mYear + " mMonth = " + mMonth + " weekNum = " + weekNum);

        for ( int index = 0; index < weekNum; index++ ) {

             int weeks = fistCalendar.get(Calendar.WEEK_OF_YEAR);
             int yearWeek = DateFormatUtil.getCompleteWeek(mYear, weeks);

             hashMap.put(yearWeek, index);

             BLog.d(TAG, " drawChartDataMonthly weeks = " + weeks
                     + " yearWeek = " + yearWeek);

             chartData.addXValue( index, getString(R.string.month_in_week, index + 1) );
             fistCalendar.add(Calendar.WEEK_OF_YEAR, 1);
        }
        
        chartData = fillInWeightChangeInMonth(yearMonth, chartData, hashMap, weightUnify);
              
        return chartData;
    }
    
    /**
     * 画年视图
     * @param chartData
     * @return
     */
    private ChartData drawWeightChartDataYearly(ChartData  chartData, float weightUnify) {

        HashMap<Integer, Integer> hashMap =
                new HashMap<Integer, Integer>();

        Calendar fistCalendar = CalendarUtil.getFirstCalendarOfMonth(mYear, mMonth);


        BLog.d(TAG, " drawChartDataMonthly  mYear = " + mYear + " mMonth = " + mMonth );

        for ( int index = 0; index < CALORIC_MONTH_NUMBER; index++ ) {

             int weeks = fistCalendar.get(Calendar.WEEK_OF_YEAR);
             int yearMonth = DateFormatUtil.getCompleteYearMonth(mYear, index + 1);
             hashMap.put(yearMonth, index);

             BLog.d(TAG, " drawChartDataMonthly weeks = " + weeks
                     + " yearMonth = " + yearMonth);

             chartData.addXValue( index, getString(R.string.year_in_month, index + 1) );
             fistCalendar.add(Calendar.WEEK_OF_YEAR, 1);
        }
        
        chartData = fillInWeightChangeInYear(chartData, hashMap, weightUnify);
        return chartData;
    }

    private static final String[] PROJECTION_WEIGHT_CHANGE_DAYLY = new String[] {
            WeightChange.EVERY_WEIGHT ,   // 0
            WeightChange.DATE       // 1
    };


    private static final int WEIGHT_COLUMN_DAYLY_INDEX = 0;
    private static final int WEIGHT_DATE_COLUMN_DAYLY_INDEX = 1;

    /***
     * 周视图,我的能量代谢到ChartData队列中
     * @param yearWeek
     * @param chartData
     * @param hashMap
     * @return
     */
    private ChartData  fillInWeightChangeInWeek(int yearWeek ,
                                                ChartData chartData,
                                                HashMap<Integer, Integer> hashMap,
                                                float weightUnify) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(WeightChange.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(WeightChange.YEAR_WEEK + " = ? ");

        String sortOrder = WeightChange.DATE + " ASC ";

        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(WeightChange.CONTENT_URI,
                    PROJECTION_WEIGHT_CHANGE_DAYLY,
                    stringBuilder.toString(),
                    new String[] { String.valueOf(mAccountId),  String.valueOf(yearWeek) },
                    sortOrder );

            if (cursor == null) {
                Log.d(TAG, " fillInTotalEnergyInWeek cursor = " + cursor);
            }

            cursor.moveToPosition(-1);

            while (cursor.moveToNext() ) {
                float weight = cursor.getFloat(WEIGHT_COLUMN_DAYLY_INDEX);
                int  day = cursor.getInt(WEIGHT_DATE_COLUMN_DAYLY_INDEX);
                if ( hashMap.containsKey(day) ) {
                    int index = hashMap.get(day);
                    chartData.addPoint(index,
                            (int) (weight * weightUnify),
                            getString(mBalanceChartModel.nameRes),
                            String.valueOf( (int) (weight * weightUnify) ) );
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return chartData;
    }



    /**
     * 我的总能量消耗查看每一周，某一个帐户的周信息列表
     */
    private static final String[] PROJECTION_WEIGHT_CHANGE_WEEKLY = new String[] {
            WeightChange.EVERY_AVG_WEIGHT,    // 2
            WeightChange.YEAR_WEEK            //5
    };


    private static final int WEIGHT_COLUMN_WEEKLY_INDEX = 0;


    /***
     * 填写我的能量消耗月视图总能量到ChartData队列中
     * @param yearWeek
     * @param chartData
     * @param hashMap
     * @return
     */
    private ChartData  fillInWeightChangeInMonth(int  yearMonth ,
                                                 ChartData chartData,
                                                 HashMap<Integer, Integer> hashMap,
                                                 float weightUnify) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(WeightChange.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(WeightChange.YEAR_MONTH + " = ? ");

        String sortOrder = WeightChange.YEAR_WEEK + " ASC ";

        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(WeightChange.CONTENT_WEEKLY_URI,
                    PROJECTION_WEIGHT_CHANGE_WEEKLY,
                    stringBuilder.toString(),
                    new String[] { String.valueOf(mAccountId),  String.valueOf(yearMonth) },
                    sortOrder );

            if (cursor == null) {
                Log.d(TAG, " fillInTotalEnergyInMonth cursor = " + cursor);
            }

            cursor.moveToPosition(-1);

            while (cursor.moveToNext() ) {
                float weight = cursor.getFloat(WEIGHT_COLUMN_WEEKLY_INDEX);
                int  yearWeek = cursor.getInt(YEAR_WEEK_COLUMN_WEEKLY_INDEX);
                Log.d(TAG, " fillInWeightChangeInMonth weight = " + weight + " yearWeek = " + yearWeek);
                if ( hashMap.containsKey(yearWeek) ) {
                    int index = hashMap.get(yearWeek);
                    Log.d(TAG, " fillInWeightChangeInMonth index = " + index);
                    chartData.addPoint(index,
                            (int)(weight * weightUnify),
                            getString(mBalanceChartModel.nameRes),
                            String.valueOf( (int) (weight * weightUnify) ) );
                }

            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return chartData;
    }

    /**
     * 我的总能量消耗查看每一月，某一个帐户的周信息列表
     */
    private static final String[] PROJECTION_WEIGHT_CHANGE_MONTHLY = new String[] {
            WeightChange.EVERY_AVG_WEIGHT,    // 1
            WeightChange.YEAR_MONTH            //2
    };


    private static final int WEIGHT_COLUMN_MONTHLY_INDEX = 0;


    /***
     * 填写年视图总能量到ChartData队列中
     * @param yearWeek
     * @param chartData
     * @param hashMap
     * @return
     */
    private ChartData  fillInWeightChangeInYear( ChartData chartData,
                                                 HashMap<Integer, Integer> hashMap,
                                                 float weightUnify ) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(WeightChange.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(WeightChange.YEAR + " = ? ");

        String sortOrder = WeightChange.YEAR_MONTH + " ASC ";

        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(WeightChange.CONTENT_MONTHLY_URI,
                    PROJECTION_WEIGHT_CHANGE_MONTHLY,
                    stringBuilder.toString(),
                    new String[] { String.valueOf(mAccountId),  String.valueOf(mYear) },
                    sortOrder );

            if (cursor == null) {
                Log.d(TAG, " testCostWeekly cursor = " + cursor);
            }

            cursor.moveToPosition(-1);

            while (cursor.moveToNext() ) {
                float weight = cursor.getFloat(WEIGHT_COLUMN_MONTHLY_INDEX);
                int  yearMonth = cursor.getInt(YEAR_MONTH_COLUMN_INDEX);

                Log.d(TAG, " fillInWeightChangeInYear avgTotalEnergy = " + weight + " yearMonth = " + yearMonth);

                if ( hashMap.containsKey(yearMonth) ) {
                    int index = hashMap.get(yearMonth);
                    Log.d(TAG, " fillInWeightChangeInYear index = " + index);
                    chartData.addPoint(index,
                            (int) (weight * weightUnify),
                            getString(mBalanceChartModel.nameRes),
                            String.valueOf( (int) (weight * weightUnify) ) );
                }

            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return chartData;
    }

    /**
     * 画我的平衡代谢周视图
     * @param chartData
     * @return
     */
    private void drawBalanceChartDataWeekly( ) {

        int  yearWeek =  DateFormatUtil.getCompleteWeek(mYear, mWeek);


        Calendar fistCalendar = CalendarUtil.getFirstDayOfWeek(mYear, mWeek) ;
        Calendar lastCalendar = CalendarUtil.getLastDayOfWeek(mYear, mWeek);

        TreeMap<Integer, String> hashMap =
                new TreeMap<Integer, String>();

        Log.d(TAG, " drawBalanceChartDataWeekly yearWeek = " + yearWeek
                + " mYear = " + mYear + " mWeek = " + mWeek);

        do {

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.setLength(0);

            int month = fistCalendar.get(Calendar.MONTH) + 1;
            int day = fistCalendar.get(Calendar.DAY_OF_MONTH);
            int date = Integer.valueOf(
                    CalendarUtil.getDayFromCalendar( fistCalendar ) );

            stringBuilder.append( String.valueOf(month) );
            stringBuilder.append(".");
            stringBuilder.append( String.valueOf(day) );
            hashMap.put( date, stringBuilder.toString() );
            fistCalendar.add(GregorianCalendar.DATE, 1);
           
           
        }  while ( fistCalendar.compareTo(lastCalendar) <= 0 );
        
//        // 对HashMap中的key 进行排序 
//        
//        ArrayList<Entry<Integer, String>> list = new ArrayList< Entry<Integer, String> >( hashMap.entrySet() );
//        
//        Collections.sort(list, new Comparator< Map.Entry<Integer, String> >() {  
//            public int compare(Map.Entry<Integer, String> o1,  
//                    Map.Entry<Integer, String> o2) {  
////              System.out.println(o1.getKey()+"   ===  "+o2.getKey());  
//                return (o1.getKey()).toString().compareTo(o2.getKey().toString());  
//            }  
//        });  
        fillInBalanceChangeInWeek(yearWeek, hashMap);

    }

    private static final String[] PROJECTION_BALANCE_CHANGE_DAYLY = new String[] {
            Ingestion.TOTAL_INGESTION ,   // 0
            EnergyCost.TOTAL_ENERGY ,      // 1
            Ingestion.DATE
    };

    private static final int BALANCE_TOTAL_INGESTION_COLUMN_INDEX = 0;
    private static final int BALANCE_TOTAL_ENERGY_COLUMN_INDEX = 1;
    private static final int BALANCE_DATE_COLUMN_INDEX = 2;

    @SuppressLint("UseSparseArrays")
    private void  fillInBalanceChangeInWeek(int yearWeek,  TreeMap<Integer, String> hashMap) {
        HashMap<Integer, Float> balanceHashMap = new HashMap<Integer, Float>();
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append( Ingestion.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append( Ingestion.YEAR_WEEK + " = ? ");

        String sortOrder = Ingestion.DATE + " ASC ";
        
        mBalanceHistoryChart.clearDate();
        
        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(Ingestion.CONTENT_BALANCE_WEEKLY_URI,
                    PROJECTION_BALANCE_CHANGE_DAYLY,
                    stringBuilder.toString(),
                    new String[] { String.valueOf(mAccountId),  String.valueOf(yearWeek) },
                    sortOrder );

            if (cursor == null) {
                Log.d(TAG, "fillInBalanceChangeInWeek cursor = " + cursor);
            }

            cursor.moveToPosition(-1);

            while (cursor.moveToNext() ) {
                float ingestion = cursor.getFloat(BALANCE_TOTAL_INGESTION_COLUMN_INDEX);
                float energy = cursor.getFloat(BALANCE_TOTAL_ENERGY_COLUMN_INDEX);
                int  day = cursor.getInt(BALANCE_DATE_COLUMN_INDEX);
                balanceHashMap.put(day, ingestion - energy);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        
        Iterator< Entry<Integer, String> > dateIter = hashMap.entrySet().iterator(); 
        
        while ( dateIter.hasNext() ) { 
            Entry<Integer,String> dateEntry = (Entry<Integer, String>) dateIter.next(); 
            int    dateKey = Integer.parseInt( dateEntry.getKey().toString() ); 
            String dateVal = dateEntry.getValue().toString();
            
            double value = findBalanceMatch(dateKey, balanceHashMap);
            Log.d(TAG, "fillInBalanceChangeInWeek dateVal = " + dateVal + " value = " + value);
            mBalanceHistoryChart.addData(dateVal, value);            
        } 
        
    }
    
    private double findBalanceMatch(int date, HashMap<Integer, Float> hashMap) {
        double result = 0;
        
        if ( hashMap.containsKey(date) ) {
            result = hashMap.get(date);
        }    
        
        return result;
    }

    @SuppressLint("UseSparseArrays")
    private void drawBalanceChartDataMonthly( ) {
        int  yearMonth =  DateFormatUtil.getCompleteWeek(mYear, mMonth);
        int  weekNum = CalendarUtil.getTotalWeeksInMonth(mYear, mMonth);

        TreeMap<Integer, String> hashMap =
                new TreeMap<Integer, String>();

        Calendar fistCalendar = CalendarUtil.getFirstCalendarOfMonth(mYear, mMonth);


        BLog.d(TAG, " drawChartDataMonthly yearMonth = " + yearMonth
                + " mYear = " + mYear + " mMonth = " + mMonth + " weekNum = " + weekNum);

        for ( int index = 0; index < weekNum; index++ ) {
            int weeks = fistCalendar.get(Calendar.WEEK_OF_YEAR);
            int yearWeek = DateFormatUtil.getCompleteWeek(mYear, weeks);

            hashMap.put(yearWeek, getString(R.string.month_in_week, index + 1) );
            BLog.d(TAG, " drawChartDataMonthly weeks = " + weeks
                    + " yearWeek = " + yearWeek);

            fistCalendar.add(Calendar.WEEK_OF_YEAR, 1);
        }

        fillInBalanceChangeInMonth(yearMonth, hashMap);        
    }



    private static final String[] PROJECTION_BALANCE_CHANGE_WEEKLY = new String[] {
            Ingestion.AVG_TOTAL_INGESTION ,   // 0
            EnergyCost.AVG_TOTAL_ENERGY ,      // 1
            Ingestion.YEAR_WEEK
    };

    private static final int BALANCE_AVG_TOTAL_INGESTION_COLUMN_INDEX = 0;
    private static final int BALANCE_AVG_TOTAL_ENERGY_COLUMN_INDEX = 1;
    private static final int BALANCE_YEAR_WEEK_COLUMN_INDEX = 2;

    @SuppressLint("UseSparseArrays")
    private void  fillInBalanceChangeInMonth(int yearMonth, TreeMap<Integer, String> hashMap) {
        HashMap<Integer, Float> balanceHashMap = new HashMap<Integer, Float>();
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append( Ingestion.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append( Ingestion.YEAR_MONTH + " = ? ");

        String sortOrder = Ingestion.YEAR_WEEK + " ASC ";
        
        mBalanceHistoryChart.clearDate();
        
        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(Ingestion.CONTENT_BALANCE_MONTHLY_URI,
                    PROJECTION_BALANCE_CHANGE_WEEKLY,
                    stringBuilder.toString(),
                    new String[] { String.valueOf(mAccountId),  String.valueOf(yearMonth) },
                    sortOrder );

            if (cursor == null) {
                Log.d(TAG, " fillInBalanceChangeInMonth cursor = " + cursor);
            }

            cursor.moveToPosition(-1);

            while (cursor.moveToNext() ) {
                float ingestion = cursor.getFloat(BALANCE_AVG_TOTAL_INGESTION_COLUMN_INDEX);
                float energy = cursor.getFloat(BALANCE_AVG_TOTAL_ENERGY_COLUMN_INDEX);
                int  yearWeek = cursor.getInt(BALANCE_YEAR_WEEK_COLUMN_INDEX);
                
                balanceHashMap.put(yearWeek, ingestion - energy);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        
        Iterator< Entry<Integer, String> > dateIter = hashMap.entrySet().iterator(); 
        
        while ( dateIter.hasNext() ) { 
            Entry<Integer,String> dateEntry = (Entry<Integer, String>) dateIter.next(); 
            int    dateKey = Integer.parseInt( dateEntry.getKey().toString() ); 
            String dateVal = dateEntry.getValue().toString();
            
            double value = findBalanceMatch(dateKey, balanceHashMap);
            mBalanceHistoryChart.addData(dateVal, value);            
        } 


    }
    
    
    /**
     * 画年视图
     * @param chartData
     * @return
     */
    private void drawBalanceChartDataYearly( ) {

        TreeMap<Integer, String> hashMap =
                new TreeMap<Integer, String>();

        Calendar fistCalendar = CalendarUtil.getFirstCalendarOfMonth(mYear, mMonth);


        BLog.d(TAG, " drawChartDataMonthly  mYear = " + mYear + " mMonth = " + mMonth );

        for ( int index = 0; index < CALORIC_MONTH_NUMBER; index++ ) {

             int weeks = fistCalendar.get(Calendar.WEEK_OF_YEAR);
             int yearMonth = DateFormatUtil.getCompleteYearMonth(mYear, index + 1);
             
             hashMap.put(yearMonth, getString(R.string.year_in_month, index + 1) );
            
             BLog.d(TAG, " drawChartDataMonthly weeks = " + weeks
                     + " yearMonth = " + yearMonth);

            
             fistCalendar.add(Calendar.WEEK_OF_YEAR, 1);
        }
        
        fillInBalanceChangeInYear(hashMap);
        
    }

    private static final String[] PROJECTION_BALANCE_CHANGE_MONTHLY = new String[] {
            Ingestion.AVG_TOTAL_INGESTION ,   // 0
            EnergyCost.AVG_TOTAL_ENERGY ,      // 1
            Ingestion.YEAR_MONTH
    };


    private static final int BALANCE_YEAR_MONTH_COLUMN_INDEX = 2;

    private void  fillInBalanceChangeInYear(TreeMap<Integer, String> hashMap ) {
        HashMap<Integer, Float> balanceHashMap = new HashMap<Integer, Float>();
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append( Ingestion.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append( Ingestion.YEAR + " = ? ");

        String sortOrder =  Ingestion.YEAR_MONTH + " ASC ";
        
        mBalanceHistoryChart.clearDate();

        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(Ingestion.CONTENT_BALANCE_YEARLY_URI,
                    PROJECTION_BALANCE_CHANGE_MONTHLY,
                    stringBuilder.toString(),
                    new String[] { String.valueOf(mAccountId),  String.valueOf(mYear) },
                    sortOrder );

            if (cursor == null) {
                Log.d(TAG, " fillInBalanceChangeInMonth cursor = " + cursor);
            }

            cursor.moveToPosition(-1);

            while (cursor.moveToNext() ) {
                float ingestion = cursor.getFloat(BALANCE_AVG_TOTAL_INGESTION_COLUMN_INDEX);
                float energy = cursor.getFloat(BALANCE_AVG_TOTAL_ENERGY_COLUMN_INDEX);
                int  yearMonth = cursor.getInt(BALANCE_YEAR_MONTH_COLUMN_INDEX);
                
                balanceHashMap.put(yearMonth, ingestion - energy);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        
        Iterator< Entry<Integer, String> > dateIter = hashMap.entrySet().iterator(); 
        
        while ( dateIter.hasNext() ) { 
            Entry<Integer,String> dateEntry = (Entry<Integer, String>) dateIter.next(); 
            int    dateKey = Integer.parseInt( dateEntry.getKey().toString() ); 
            String dateVal = dateEntry.getValue().toString();
            
            double value = findBalanceMatch(dateKey, balanceHashMap);
            mBalanceHistoryChart.addData(dateVal, value);            
        } 
    }
    
   

    private void refreshBalanceView() {

    }
    
    private final static int CALORIC_MONTH_NUMBER = 12;
}
