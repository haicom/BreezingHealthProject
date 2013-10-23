package com.breezing.health.ui.activity;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.breezing.health.R;
import com.breezing.health.entity.AccountEntity;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.entity.enums.ChartModel;
import com.breezing.health.providers.Breezing.WeightChange;
import com.breezing.health.ui.fragment.BaseDialogFragment;
import com.breezing.health.ui.fragment.CaloricWeeklyHistoryFragment;
import com.breezing.health.ui.fragment.ChartModelPickerDialogFragment;
import com.breezing.health.ui.fragment.DialogFragmentInterface;
import com.breezing.health.ui.fragment.MonthIntervalPickerDialogFragment;
import com.breezing.health.ui.fragment.WeekIntervalPickerDialogFragment;
import com.breezing.health.ui.fragment.YearIntervalPickerDialogFragment;
import com.breezing.health.util.*;
import com.breezing.health.widget.linechart.data.ChartData;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WeightHistoryActivity  extends ActionBarActivity implements OnClickListener {
    
    private static final String TAG = "WeightHistoryActivity";
    
    private ChartModel mCaloricHistoryChartModel = ChartModel.WEEK;

    private Button mSelectModelButton;
    private Button mSelectIntervalButton;

    private ContentResolver mContentResolver;

    private int mYear;
    private int mMonth;
    private int mWeek;
    private int mAccountId;
    private ChartModel mBalanceChartModel = ChartModel.WEEK;
    
 
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_weight_history);
        mContentResolver = getContentResolver();
        setActionBarTitle(R.string.caloric_weight_history);
        mAccountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this,
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));

        mSelectModelButton = (Button) findViewById(R.id.model);
        mSelectIntervalButton = (Button) findViewById(R.id.during);
        mYear = CalendarUtil.getCurrentYear();
        mWeek = CalendarUtil.getWeekOfYear(new Date() );
        mMonth = CalendarUtil.getCurrentMonth();
        mSelectModelButton.setOnClickListener(this);
        mSelectIntervalButton.setOnClickListener(this);
        refreshFragment();
    }
    
    @Override
    public void onClick(View v) {
        if ( v == mSelectModelButton ) {
            showModelPickerDialog();
            return ;
        } else if ( v == mSelectIntervalButton ) {

                switch ( mCaloricHistoryChartModel ) {
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
                mCaloricHistoryChartModel = (ChartModel) params[0];
                refreshFragment();
            }

        });

        chartModelPicker.show(getSupportFragmentManager(), "chartModelPicker");
    }
    
    private void refreshFragment() {
        AccountEntity account;
        float weightUnify;
        int accountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this, LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        BreezingQueryViews query = new BreezingQueryViews( this );
        account = query.queryBaseInfoViews(accountId);
        weightUnify = query.queryUnitObtainData( getString(R.string.weight_type), account.getWeightUnit() );

        mSelectModelButton.setText(mCaloricHistoryChartModel.nameRes);
        ChartData data = new ChartData(ChartData.LINE_COLOR_RED);

        switch ( mCaloricHistoryChartModel ) {
            case WEEK: {
              //  Log.d(TAG, " refreshFragment  year = " + year);
                mSelectIntervalButton.setText(
                        getString(R.string.year_first_day_last_day,
                        mYear,
                        CalendarUtil.getFirstDayAndLastDayOfWeek( this,
                        mYear,
                        mWeek ) ) );
                drawWeightChartDataWeekly(data, weightUnify);
                break;
            }

            case MONTH: {
                mSelectIntervalButton.setText(
                        getString(R.string.year_and_month, mYear, mMonth) );
                drawWeightChartDataMonthly(data, weightUnify);
                break;
            }

            case YEAR: {
                mSelectIntervalButton.setText(mYear + getString(R.string.year));
                drawWeightChartDataYearly(data, weightUnify);
                break;
            }

        }

        CaloricWeeklyHistoryFragment caloricWeeklyHistoryFragment =
                new CaloricWeeklyHistoryFragment();      
        caloricWeeklyHistoryFragment.setChartModel(mCaloricHistoryChartModel);
        caloricWeeklyHistoryFragment.setChartData(data);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.chart_content, caloricWeeklyHistoryFragment);
        ft.commit();
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
                refreshFragment();
                Log.d(TAG, " showWeekIntervalPickerDialog mYear =  " + mYear + " mWeek = " + mWeek);
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
                refreshFragment();
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

                refreshFragment();
            }

        });

        yearIntervalPicker.show(getSupportFragmentManager(), "yearIntervalPicker");
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
    private ChartData drawWeightChartDataYearly(ChartData  chartData, float weightUnify ) {

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
    private ChartData  fillInWeightChangeInWeek(int yearWeek,
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
    private static final int YEAR_WEEK_COLUMN_WEEKLY_INDEX = 1;
    private static final int YEAR_MONTH_COLUMN_INDEX = 1;
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
                    chartData.addPoint( index,
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

    private final static int CALORIC_MONTH_NUMBER = 12;
}
