package com.breezing.health.ui.activity;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.breezing.health.R;
import com.breezing.health.adapter.CaloricPagerAdapter;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.providers.Breezing.EnergyCost;
import com.breezing.health.ui.fragment.BaseDialogFragment;
import com.breezing.health.ui.fragment.CaloricWeeklyHistoryFragment;
import com.breezing.health.ui.fragment.ChartModelPickerDialogFragment;
import com.breezing.health.ui.fragment.ChartModelPickerDialogFragment.CaloricHistoryChartModel;
import com.breezing.health.ui.fragment.DialogFragmentInterface;
import com.breezing.health.ui.fragment.MonthIntervalPickerDialogFragment;
import com.breezing.health.ui.fragment.WeekIntervalPickerDialogFragment;
import com.breezing.health.ui.fragment.YearIntervalPickerDialogFragment;
import com.breezing.health.util.BLog;
import com.breezing.health.util.CalendarUtil;
import com.breezing.health.util.DateFormatUtil;
import com.breezing.health.util.ExtraName;
import com.breezing.health.util.LocalSharedPrefsUtil;
import com.breezing.health.widget.linechart.data.ChartData;

public class CaloricHistoryActivity extends ActionBarActivity implements OnClickListener {
    private final static String TAG = "CaloricHistoryActivity";

    public enum CaloricHistoryType {
        BURN(R.string.caloric_intake_history), INTAKE(R.string.caloric_burn_history);

        private CaloricHistoryType(int nameRes) {
            this.nameRes = nameRes;
        }

        public int nameRes;
    }

    private CaloricHistoryType mCaloricHistoryType;
    private CaloricHistoryChartModel mCaloricHistoryChartModel = CaloricHistoryChartModel.WEEK;

    private Button mSelectModelButton;
    private Button mSelectIntervalButton;

    private ContentResolver mContentResolver;

    private int mYear;
    private int mMonth;
    private int mWeek;
    private int mAccountId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_caloric_history);
        initValues();
        initViews();
        valueToView();
        initListeners();       
    }
    
    @Override
    protected void onResume() {      
        super.onResume();
        
        refreshFragment();
    }

    private void initValues() {
        mCaloricHistoryType = CaloricHistoryType.values()
                [getIntent().getIntExtra( ExtraName.EXTRA_TYPE,
                        CaloricHistoryType.BURN.ordinal() ) ];

        mContentResolver = getContentResolver();
        
        mAccountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this,
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);

    }

    private void initViews() {
        setActionBarTitle(mCaloricHistoryType.nameRes);
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));

        mSelectModelButton = (Button) findViewById(R.id.model);
        mSelectIntervalButton = (Button) findViewById(R.id.during);
        mYear = CalendarUtil.getCurrentYear();
        mWeek = CalendarUtil.getCurrentWeek();     
        mMonth = CalendarUtil.getCurrentMonth();
        
    }

    private void valueToView() {

    }

    private void initListeners() {
        mSelectModelButton.setOnClickListener(this);
        mSelectIntervalButton.setOnClickListener(this);
    }

    private void refreshFragment() {
        mSelectModelButton.setText(mCaloricHistoryChartModel.nameRes);

        ChartData data = null;
        data = new ChartData(ChartData.LINE_COLOR_BLUE);
        switch ( mCaloricHistoryChartModel ) {

            case WEEK: {
                
              //  Log.d(TAG, " refreshFragment  year = " + year);
                mSelectIntervalButton.setText(
                        getString(R.string.year_first_day_last_day,
                        mYear,
                        CalendarUtil.getFirstDayAndLastDayOfWeek( this,
                        mYear,
                        mWeek ) ) );
                drawChartDataWeekly(data);
                break;
            }

            case MONTH: {              
                mSelectIntervalButton.setText(
                        getString(R.string.year_and_month, mYear, mMonth) );                  
                drawChartDataMonthly(data);                
                break;
            }

            case YEAR: {
               
                mSelectIntervalButton.setText(mYear + getString(R.string.year));
                drawChartDataYearly(data);
                
                break;
            }

        }

        CaloricWeeklyHistoryFragment caloricWeeklyHistoryFragment =
                new CaloricWeeklyHistoryFragment();
        caloricWeeklyHistoryFragment.setChartType(mCaloricHistoryType);
        caloricWeeklyHistoryFragment.setChartModel(mCaloricHistoryChartModel);
        caloricWeeklyHistoryFragment.setChartData(data);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.chart_content, caloricWeeklyHistoryFragment);
        ft.commit();
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
                mCaloricHistoryChartModel = (CaloricHistoryChartModel) params[0];
                refreshFragment();
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
     * 画周视图
     * @param chartData
     * @return
     */
    private ChartData drawChartDataWeekly(ChartData  chartData) {
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

        return fillInTotalEnergyInWeek(yearWeek, chartData, hashMap);
    }

    private static final String[] PROJECTION_ENERGY_COST_DAYLY = new String[] {
        EnergyCost.ACCOUNT_ID,          // 0
        EnergyCost.TOTAL_ENERGY ,   // 1
        EnergyCost.DATE       // 2
    };

    private static final int ACCOUNT_ID_COLUMN_DAYLY_INDEX = 0;
    private static final int ALL_TOTAL_ENERGY_COLUMN_DAYLY_INDEX = 1;
    private static final int DATE_COLUMN_DAYLY_INDEX = 2;
    

    /***
     * 周视图填写总能量到ChartData队列中
     * @param yearWeek
     * @param chartData
     * @param hashMap
     * @return
     */
    private ChartData  fillInTotalEnergyInWeek(int yearWeek ,
                               ChartData chartData,
                               HashMap<Integer, Integer> hashMap ) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(EnergyCost.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(EnergyCost.YEAR_WEEK + " = ? ");
       

        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(EnergyCost.CONTENT_URI,
                    PROJECTION_ENERGY_COST_DAYLY,
                    stringBuilder.toString(),
                    new String[] { String.valueOf(mAccountId),  String.valueOf(yearWeek) },
                    null );

            if (cursor == null) {
                Log.d(TAG, " testCostWeekly cursor = " + cursor);
            }

            cursor.moveToPosition(-1);

            while (cursor.moveToNext() ) {
                long allTotalEnergy = cursor.getLong(ALL_TOTAL_ENERGY_COLUMN_DAYLY_INDEX);
                int  day = cursor.getInt(DATE_COLUMN_DAYLY_INDEX);
                if ( hashMap.containsKey(day) ) {
                    int index = hashMap.get(day);
                    chartData.addPoint(index,
                            (int)allTotalEnergy,
                            getString(mCaloricHistoryChartModel.nameRes),
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
     * 画月视图
     * @param chartData
     * @return
     */
    private ChartData drawChartDataMonthly(ChartData  chartData) {

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
        
        return this.fillInTotalEnergyInMonth(yearMonth, chartData, hashMap);
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
     * 填写月视图总能量到ChartData队列中
     * @param yearWeek
     * @param chartData
     * @param hashMap
     * @return
     */
    private ChartData  fillInTotalEnergyInMonth(int  yearMonth ,
                               ChartData chartData,
                               HashMap<Integer, Integer> hashMap ) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(EnergyCost.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(EnergyCost.YEAR_MONTH + " = ? ");

      
        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(EnergyCost.CONTENT_WEEKLY_URI,
                    PROJECTION_ENERGY_COST_WEEKLY,
                    stringBuilder.toString(),
                    new String[] { String.valueOf(mAccountId),  String.valueOf(yearMonth) },
                    null );

            if (cursor == null) {
                Log.d(TAG, " testCostWeekly cursor = " + cursor);
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
                            getString(mCaloricHistoryChartModel.nameRes),
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
    private ChartData drawChartDataYearly(ChartData  chartData) {
      
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
        
        return fillInTotalEnergyInYear(chartData, hashMap);
    }
    
    /**
     * 我的总能量消耗查看每一月，某一个帐户的周信息列表
     */
    private static final String[] PROJECTION_ENERGY_COST_MONTHLY = new String[] {
        EnergyCost.AVG_TOTAL_ENERGY,    // 1
        EnergyCost.YEAR_MONTH            //2
    };

 
    private static final int AVG_TOTAL_ENERGY_COLUMN_MONTHLY_INDEX = 0;  
    private static final int YEAR_MONTH_COLUMN_WEEKLY_INDEX = 1;
    
    /***
     * 填写年视图总能量到ChartData队列中
     * @param yearWeek
     * @param chartData
     * @param hashMap
     * @return
     */
    private ChartData  fillInTotalEnergyInYear(ChartData chartData,
                               HashMap<Integer, Integer> hashMap ) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(EnergyCost.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(EnergyCost.YEAR + " = ? ");

      
        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(EnergyCost.CONTENT_MONTHLY_URI,
                    PROJECTION_ENERGY_COST_MONTHLY,
                    stringBuilder.toString(),
                    new String[] { String.valueOf(mAccountId),  String.valueOf(mYear) },
                    null );

            if (cursor == null) {
                Log.d(TAG, " testCostWeekly cursor = " + cursor);
            }

            cursor.moveToPosition(-1);

            while (cursor.moveToNext() ) {
                long avgTotalEnergy = cursor.getLong(AVG_TOTAL_ENERGY_COLUMN_MONTHLY_INDEX);
                int  yearMonth = cursor.getInt(YEAR_MONTH_COLUMN_WEEKLY_INDEX);
                
                Log.d(TAG, " fillInTotalEnergyInYear avgTotalEnergy = " + avgTotalEnergy + " yearMonth = " + yearMonth); 
                
                if ( hashMap.containsKey(yearMonth) ) {
                    int index = hashMap.get(yearMonth);
                    Log.d(TAG, " fillInTotalEnergyInMonth index = " + index);
                    chartData.addPoint(index,
                            (int)avgTotalEnergy,
                            getString(mCaloricHistoryChartModel.nameRes),
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
    
    private final static int CALORIC_MONTH_NUMBER = 12;


}
