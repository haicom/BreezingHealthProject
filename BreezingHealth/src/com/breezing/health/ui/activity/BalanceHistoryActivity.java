package com.breezing.health.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Date;

import com.breezing.health.R;
import com.breezing.health.adapter.BalanceHistoryPagerAdapter;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.entity.enums.ChartModel;
import com.breezing.health.ui.fragment.BaseDialogFragment;
import com.breezing.health.ui.fragment.ChartModelPickerDialogFragment;
import com.breezing.health.ui.fragment.DialogFragmentInterface;
import com.breezing.health.ui.fragment.MonthIntervalPickerDialogFragment;
import com.breezing.health.ui.fragment.WeekIntervalPickerDialogFragment;
import com.breezing.health.ui.fragment.YearIntervalPickerDialogFragment;
import com.breezing.health.util.CalendarUtil;
import com.breezing.health.widget.BalanceBarChartView;
import com.breezing.health.widget.linechart.FancyChart;
import com.breezing.health.widget.linechart.FancyChartPointListener;
import com.breezing.health.widget.linechart.data.Point;

public class BalanceHistoryActivity extends ActionBarActivity implements OnClickListener {

    private Button mSelectModelButton;
    private Button mSelectIntervalButton;
    private ViewPager mBalancePager;
    private BalanceHistoryPagerAdapter mPagerAdapter;

    private View mWeightHistoryPage;
    private FancyChart mWeightHistoryChart;

    private ViewGroup mBalanceHistoryPage;
    private BalanceBarChartView mBalanceBarChartView;


    private ChartModel mBalanceChartModel = ChartModel.WEEK;

    private int mYear;
    private int mMonth;
    private int mWeek;

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
        mYear = CalendarUtil.getCurrentYear();
        mWeek = CalendarUtil.getWeekOfYear( new Date() );
        mMonth = CalendarUtil.getCurrentMonth();
    }

    private void initViews() {
        setActionBarTitle(R.string.balance_history);
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));

        mSelectModelButton = (Button) findViewById(R.id.model);
        mSelectIntervalButton = (Button) findViewById(R.id.during);
        mBalancePager = (ViewPager) findViewById(R.id.viewPager);

        mWeightHistoryPage = getLayoutInflater().inflate(R.layout.page_of_weight_history, null);
        mWeightHistoryChart = (FancyChart) mWeightHistoryPage.findViewById(R.id.chart);

        mBalanceHistoryPage = (ViewGroup) getLayoutInflater().inflate(R.layout.page_of_caloric_balance_history, null);
        mBalanceBarChartView = new BalanceBarChartView(this);
        mBalanceBarChartView.setZoomable(false);
        mBalanceHistoryPage.addView(mBalanceBarChartView);
    }

    private void valueToView() {
        refreshChartData();

        mPagerAdapter = new BalanceHistoryPagerAdapter();
        mPagerAdapter.addViewPage(mWeightHistoryPage);
        mPagerAdapter.addViewPage(mBalanceHistoryPage);
        mBalancePager.setAdapter(mPagerAdapter);
    }

    private void initListeners() {
        mSelectModelButton.setOnClickListener(this);
        mSelectIntervalButton.setOnClickListener(this);
        mWeightHistoryChart.setOnPointClickListener(new FancyChartPointListener() {

            @Override
            public void onClick(Point point) {

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

    private void refreshWeightView() {

    }

    private void refreshBalanceView() {

    }
}
