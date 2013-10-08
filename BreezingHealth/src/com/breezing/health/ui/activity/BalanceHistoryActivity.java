package com.breezing.health.ui.activity;

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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.breezing.health.R;
import com.breezing.health.adapter.BalanceHistoryPagerAdapter;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.entity.enums.ChartModel;
import com.breezing.health.providers.Breezing.EnergyCost;
import com.breezing.health.providers.Breezing.Ingestion;
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
import com.breezing.health.widget.BalanceBarChartView;
import com.breezing.health.widget.linechart.FancyChart;
import com.breezing.health.widget.linechart.FancyChartPointListener;
import com.breezing.health.widget.linechart.data.ChartData;
import com.breezing.health.widget.linechart.data.Point;
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
    
    private FancyChart mIntakeHistoryChart;
    private FancyChart mBurnHistoryChart;
    private FancyChart mEnergyCostChart;
    private FancyChart mWeightHistoryChart;

    private ViewGroup mBalanceHistoryPage;
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

        mBalanceHistoryPage = (ViewGroup) mLayoutInflater.inflate(R.layout.page_of_caloric_balance_history, null);
        
        mBalanceBarChartView = new BalanceBarChartView(this);
        mBalanceBarChartView.setZoomable(false);        
        mBalanceHistoryPage.addView(mBalanceBarChartView);
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
        mSelectModelButton.setText(mBalanceChartModel.nameRes);
        refreshIntakeView();
        refreshBurnView();
        refreshEnergyCostView();
        refreshWeightView();
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
    
    private void refreshIntakeView() {       
        ChartData data = new ChartData(ChartData.LINE_COLOR_RED);

        switch ( mBalanceChartModel ) {
            case WEEK: {
                drawIntakeChartDataWeekly(data);
                break;
            }

            case MONTH: {
                drawIntakeChartDataMonthly(data);
                break;
            }

            case YEAR: {
                drawIntakeChartDataYearly(data);

                break;
            }
        }

        if (data != null) {
            mIntakeHistoryChart.clearValues();
            mIntakeHistoryChart.addData(data);
        }
    }
    
    private void refreshBurnView() {       
        ChartData data = new ChartData(ChartData.LINE_COLOR_RED);

        switch ( mBalanceChartModel ) {
            case WEEK: {
                drawBurnChartDataWeekly(data);
                break;
            }

            case MONTH: {
                drawBurnChartDataMonthly(data);
                break;
            }

            case YEAR: {
                drawBurnChartDataYearly(data);
                break;
            }

        }

        if (data != null) {
            mBurnHistoryChart.clearValues();
            mBurnHistoryChart.addData(data);
        }
    }

    /**
     * 画我的我的能量摄入周视图
     * @param chartData
     * @return
     */
    private ChartData drawIntakeChartDataWeekly( ChartData chartData ) {

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
        
        chartData = fillInTotalIngestionInWeek( yearWeek, chartData, hashMap );
        return chartData;
    }
    
    /**
     * 画我的我的能量代谢周视图
     * @param chartData
     * @return
     */
    private ChartData drawBurnChartDataWeekly( ChartData chartData ) {

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
        
        chartData =  fillInTotalBurnInWeek( yearWeek, chartData, hashMap);
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
                               HashMap<Integer, Integer> hashMap ) {

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
                long allTotalEnergy = cursor.getLong(ALL_TOTAL_ENERGY_COLUMN_DAYLY_INDEX);
                int  day = cursor.getInt(DATE_COLUMN_DAYLY_INDEX);
                if ( hashMap.containsKey(day) ) {
                    int index = hashMap.get(day);
                    chartData.addPoint(index,
                            (int)allTotalEnergy,
                            getString(mBalanceChartModel.nameRes),                            
                            String.valueOf(allTotalEnergy) );
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
                               HashMap<Integer, Integer> hashMap ) {

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
                long allTotalEnergy = cursor.getLong(TOTAL_INGESTION_COLUMN_DAYLY_INDEX);
                int  day = cursor.getInt(INGESTION_DATE_COLUMN_DAYLY_INDEX);
                if ( hashMap.containsKey(day) ) {
                    int index = hashMap.get(day);
                    chartData.addPoint(index,
                            (int)allTotalEnergy,
                            getString(mBalanceChartModel.nameRes),
                            String.valueOf(allTotalEnergy) );
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
    private ChartData drawIntakeChartDataMonthly(ChartData  chartData) {

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
        
        chartData = fillInTotalIngestionInMonth(yearMonth, chartData, hashMap);
        return chartData;
    }
    
    private ChartData drawBurnChartDataMonthly(ChartData  chartData) {

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
        
        chartData = fillInTotalBurnInMonth(yearMonth, chartData, hashMap);
        
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
                               HashMap<Integer, Integer> hashMap ) {

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
                long avgTotalEnergy = cursor.getLong(AVG_TOTAL_ENERGY_COLUMN_WEEKLY_INDEX);
                int  yearWeek = cursor.getInt(YEAR_WEEK_COLUMN_WEEKLY_INDEX);
                Log.d(TAG, " fillInTotalEnergyInMonth avgTotalEnergy = " + avgTotalEnergy + " yearWeek = " + yearWeek);
                if ( hashMap.containsKey(yearWeek) ) {
                    int index = hashMap.get(yearWeek);
                    Log.d(TAG, " fillInTotalEnergyInMonth index = " + index);
                    chartData.addPoint(index,
                            (int)avgTotalEnergy,
                            getString(mBalanceChartModel.nameRes),
                            String.valueOf(avgTotalEnergy) );
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
                               HashMap<Integer, Integer> hashMap ) {

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

                long avgTotalEnergy = cursor.getLong(AVG_TOTAL_INGESTION_COLUMN_WEEKLY_INDEX);
                int  yearWeek = cursor.getInt(INGESTION_YEAR_WEEK_COLUMN_WEEKLY_INDEX);

                Log.d(TAG, " fillInTotalEnergyInMonth avgTotalEnergy = " + avgTotalEnergy + " yearWeek = " + yearWeek);

                if ( hashMap.containsKey(yearWeek) ) {

                    int index = hashMap.get(yearWeek);

                    Log.d(TAG, " fillInTotalEnergyInMonth index = " + index);

                    chartData.addPoint(index,
                            (int)avgTotalEnergy,
                            getString(mBalanceChartModel.nameRes),
                            String.valueOf(avgTotalEnergy) );
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
    private ChartData drawIntakeChartDataYearly(ChartData  chartData) {

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
        
        chartData = fillInIngestionInYear(chartData, hashMap);
        return chartData;
    }
    
    /**
     * 画年视图
     * @param chartData
     * @return
     */
    private ChartData drawBurnChartDataYearly(ChartData  chartData) {

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
        
        chartData = fillInTotalBurnInYear(chartData, hashMap);
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
                               HashMap<Integer, Integer> hashMap ) {

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
                long avgTotalEnergy = cursor.getLong(AVG_TOTAL_ENERGY_COLUMN_MONTHLY_INDEX);
                int  yearMonth = cursor.getInt(YEAR_MONTH_COLUMN_INDEX);

                Log.d(TAG, " fillInTotalEnergyInYear avgTotalEnergy = " + avgTotalEnergy + " yearMonth = " + yearMonth);

                if ( hashMap.containsKey(yearMonth) ) {
                    int index = hashMap.get(yearMonth);
                    Log.d(TAG, " fillInTotalEnergyInMonth index = " + index);
                    chartData.addPoint(index,
                            (int)avgTotalEnergy,
                            getString(mBalanceChartModel.nameRes),
                            String.valueOf(avgTotalEnergy) );
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
                               HashMap<Integer, Integer> hashMap ) {

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
                long avgTotalEnergy = cursor.getLong(AVG_INGESTION_COLUMN_MONTHLY_INDEX);
                int  yearMonth = cursor.getInt(INGESTION_YEAR_MONTH_COLUMN_INDEX);

                Log.d(TAG, " fillInTotalEnergyInYear avgTotalEnergy = " + avgTotalEnergy + " yearMonth = " + yearMonth);

                if ( hashMap.containsKey(yearMonth) ) {
                    int index = hashMap.get(yearMonth);
                    Log.d(TAG, " fillInTotalEnergyInMonth index = " + index);
                    chartData.addPoint(index,
                            (int)avgTotalEnergy,
                            getString(mBalanceChartModel.nameRes),
                            String.valueOf(avgTotalEnergy) );
                }

            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return chartData;
    }
    
    
    
    private void refreshEnergyCostView() {       
        ChartData data = new ChartData(ChartData.LINE_COLOR_RED);

        switch ( mBalanceChartModel ) {
            case WEEK: {
                drawEnergyCostChartDataWeekly(data);
                break;
            }

            case MONTH: {
                drawEnergyCostChartDataMonthly(data);
                break;
            }

            case YEAR: {
                drawEnergyCostChartDataYearly(data);
                break;
            }

        }

        if (data != null) {
            mEnergyCostChart.clearValues();
            mEnergyCostChart.addData(data);
        }
    }
    
    /**
     * 画我的我的能量代谢周视图
     * @param chartData
     * @return
     */
    private ChartData drawEnergyCostChartDataWeekly( ChartData chartData ) {

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
        
        chartData =  fillInTotalEnergyCostInWeek( yearWeek, chartData, hashMap);
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
                               HashMap<Integer, Integer> hashMap ) {

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
                long metabolism = cursor.getLong(ALL_METABOLISM_COLUMN_DAYLY_INDEX);
                int  day = cursor.getInt(DATE_COLUMN_DAYLY_INDEX);
                if ( hashMap.containsKey(day) ) {
                    int index = hashMap.get(day);
                    chartData.addPoint(index,
                            (int)metabolism,
                            getString(mBalanceChartModel.nameRes),                            
                            String.valueOf(metabolism) );
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return chartData;
    }
    
    private ChartData drawEnergyCostChartDataMonthly(ChartData  chartData) {

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
        
        chartData = fillInTotalEnergyCostInMonth(yearMonth, chartData, hashMap);
        
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
                               HashMap<Integer, Integer> hashMap ) {

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
                long metabolism = cursor.getLong(METABOLISM_COLUMN_WEEKLY_INDEX);
                int  yearWeek = cursor.getInt(YEAR_WEEK_COLUMN_WEEKLY_INDEX);
                Log.d(TAG, " fillInTotalEnergyInMonth avgTotalEnergy = " + metabolism + " yearWeek = " + yearWeek);
                if ( hashMap.containsKey(yearWeek) ) {
                    int index = hashMap.get(yearWeek);
                    Log.d(TAG, " fillInTotalEnergyInMonth index = " + index);
                    chartData.addPoint(index,
                            (int)metabolism,
                            getString(mBalanceChartModel.nameRes),
                            String.valueOf(metabolism) );
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
    private ChartData drawEnergyCostChartDataYearly(ChartData  chartData) {

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
        
        chartData = fillInTotalEnergyCostInYear(chartData, hashMap);
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
                               HashMap<Integer, Integer> hashMap ) {

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
                long avgTotalEnergy = cursor.getLong(AVG_METABOLISM_MONTHLY_INDEX);
                int  yearMonth = cursor.getInt(YEAR_MONTH_COLUMN_INDEX);

                Log.d(TAG, " fillInTotalEnergyInYear avgTotalEnergy = " + avgTotalEnergy + " yearMonth = " + yearMonth);

                if ( hashMap.containsKey(yearMonth) ) {  
                    int index = hashMap.get(yearMonth);
                    Log.d(TAG, " fillInTotalEnergyInMonth index = " + index);   
                    chartData.addPoint(index,
                            (int)avgTotalEnergy,
                            getString(mBalanceChartModel.nameRes),
                            String.valueOf(avgTotalEnergy) );
                }

            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return chartData;
    }
    
    private void refreshWeightView() {
        ChartData data = new ChartData(ChartData.LINE_COLOR_RED);

        switch ( mBalanceChartModel ) {
            case WEEK: {
                drawWeightChartDataWeekly(data);
                break;
            }

            case MONTH: {
                drawWeightChartDataMonthly(data);
                break;
            }

            case YEAR: {
                drawWeightChartDataYearly(data);
                break;
            }

        }

        if (data != null) {
            mWeightHistoryChart.clearValues();
            mWeightHistoryChart.addData(data);
        }
    }
    
    /**
     * 画我的我的能量代谢周视图
     * @param chartData
     * @return
     */
    private ChartData drawWeightChartDataWeekly( ChartData chartData ) {

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
        
       
        return chartData;
    }
    
    private ChartData drawWeightChartDataMonthly(ChartData  chartData) {

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
        
              
        return chartData;
    }
    
    /**
     * 画年视图
     * @param chartData
     * @return
     */
    private ChartData drawWeightChartDataYearly(ChartData  chartData) {

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
        
     
        return chartData;
    }

    private void refreshBalanceView() {

    }
    
    private final static int CALORIC_MONTH_NUMBER = 12;
}
