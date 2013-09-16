package com.breezing.health.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.breezing.health.R;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.ui.fragment.BaseDialogFragment;
import com.breezing.health.ui.fragment.CaloricWeeklyHistoryFragment;
import com.breezing.health.ui.fragment.ChartModelPickerDialogFragment;
import com.breezing.health.ui.fragment.ChartModelPickerDialogFragment.CaloricHistoryChartModel;
import com.breezing.health.ui.fragment.DialogFragmentInterface;
import com.breezing.health.ui.fragment.MonthIntervalPickerDialogFragment;
import com.breezing.health.ui.fragment.WeekIntervalPickerDialogFragment;
import com.breezing.health.ui.fragment.YearIntervalPickerDialogFragment;
import com.breezing.health.util.CalendarUtil;
import com.breezing.health.util.ExtraName;
import com.breezing.health.widget.linechart.data.ChartData;

public class CaloricHistoryActivity extends ActionBarActivity implements OnClickListener {

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
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_caloric_history);
        initValues();
        initViews();
        valueToView();
        initListeners();
        refreshFragment();
    }

    private void initValues() {
        mCaloricHistoryType = CaloricHistoryType.values()[getIntent().getIntExtra(ExtraName.EXTRA_TYPE, CaloricHistoryType.BURN.ordinal())];
    }

    private void initViews() {
        setActionBarTitle(mCaloricHistoryType.nameRes);
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));
        
        mSelectModelButton = (Button) findViewById(R.id.model);
        mSelectIntervalButton = (Button) findViewById(R.id.during);
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
        
        switch(mCaloricHistoryChartModel) {
        case WEEK: {
        	
        	final int year = CalendarUtil.getCurrentYear();
        	final int week = CalendarUtil.getCurrentWeek();
        	mSelectIntervalButton.setText(year + getString(R.string.year) 
        			+ getString(R.string.week_des_first) 
        			+ week + getString(R.string.week));
        	
            data = new ChartData(ChartData.LINE_COLOR_BLUE);
            int[] yValues = new int[]{1000, 1308, 1529, 1800, 1335, 1330, 1533};
            for(int i = 0; i < yValues.length; i++) {
                data.addPoint(i, yValues[i], "title", "subtitle");
                data.addXValue(i, String.valueOf(i));
            }
            break;
        }
        
        case MONTH: {
        	
        	final int year = CalendarUtil.getCurrentYear();
        	final int month = CalendarUtil.getCurrentMonth();
        	mSelectIntervalButton.setText(year + getString(R.string.year) 
        			+ getString(R.string.week_des_first) 
        			+ month + getString(R.string.month));
        	
            data = new ChartData(ChartData.LINE_COLOR_BLUE);
            int[] yValues = new int[]{1000, 1308, 1529, 1800};
            for(int i = 0; i < yValues.length; i++) {
                data.addPoint(i, yValues[i], "title", "subtitle");
                data.addXValue(i, String.valueOf(i));
            }
            break;
        }
        
        case YEAR: {
        	
        	final int year = CalendarUtil.getCurrentYear();
        	mSelectIntervalButton.setText(year + getString(R.string.year));
        	
            data = new ChartData(ChartData.LINE_COLOR_BLUE);
            int[] yValues = new int[]{1000, 1308, 1529, 1800, 1335, 1330, 1533, 1529, 1800, 1335, 1330, 1533};
            for(int i = 0; i < yValues.length; i++) {
                data.addPoint(i, yValues[i], "title", "subtitle");
                data.addXValue(i, String.valueOf(i));
            }
            break;
        }
        
        }
        
        CaloricWeeklyHistoryFragment caloricWeeklyHistoryFragment = new CaloricWeeklyHistoryFragment();
        caloricWeeklyHistoryFragment.setChartType(mCaloricHistoryType);
        caloricWeeklyHistoryFragment.setChartModel(mCaloricHistoryChartModel);
        caloricWeeklyHistoryFragment.setChartData(data);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.chart_content, caloricWeeklyHistoryFragment);
        ft.commit();
    }
    
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if(v == mSelectModelButton) {
            showModelPickerDialog();
            return ;
        } else if (v == mSelectIntervalButton) {
        	switch(mCaloricHistoryChartModel) {
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
            return ;
        }
    }

    private void showModelPickerDialog() {
        ChartModelPickerDialogFragment weightPicker = (ChartModelPickerDialogFragment) getSupportFragmentManager().findFragmentByTag("chartModelPicker");
        if (weightPicker != null) {
            getSupportFragmentManager().beginTransaction().remove(weightPicker);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(null);

        weightPicker = ChartModelPickerDialogFragment.newInstance();
        weightPicker.setTitle(getString(R.string.title_select_chart_model));
        weightPicker.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {

            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                mCaloricHistoryChartModel = (CaloricHistoryChartModel) params[0];
                refreshFragment();
            }

        });

        weightPicker.show(getSupportFragmentManager(), "chartModelPicker");
    }
    
    private void showWeekIntervalPickerDialog() {
        WeekIntervalPickerDialogFragment weightPicker = (WeekIntervalPickerDialogFragment) getSupportFragmentManager().findFragmentByTag("weekIntervalPicker");
        if (weightPicker != null) {
            getSupportFragmentManager().beginTransaction().remove(weightPicker);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(null);

        weightPicker = WeekIntervalPickerDialogFragment.newInstance();
        weightPicker.setTitle(getString(R.string.title_select_interval));
        weightPicker.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {

            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                
            }

        });

        weightPicker.show(getSupportFragmentManager(), "weekIntervalPicker");
    }
    
    private void showMonthIntervalPickerDialog() {
    	MonthIntervalPickerDialogFragment weightPicker = (MonthIntervalPickerDialogFragment) getSupportFragmentManager().findFragmentByTag("monthIntervalPicker");
        if (weightPicker != null) {
            getSupportFragmentManager().beginTransaction().remove(weightPicker);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(null);

        weightPicker = MonthIntervalPickerDialogFragment.newInstance();
        weightPicker.setTitle(getString(R.string.title_select_interval));
        weightPicker.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {

            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                
            }

        });

        weightPicker.show(getSupportFragmentManager(), "monthIntervalPicker");
    }
    
    private void showYearIntervalPickerDialog() {
    	YearIntervalPickerDialogFragment weightPicker = (YearIntervalPickerDialogFragment) getSupportFragmentManager().findFragmentByTag("yearIntervalPicker");
        if (weightPicker != null) {
            getSupportFragmentManager().beginTransaction().remove(weightPicker);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(null);

        weightPicker = YearIntervalPickerDialogFragment.newInstance();
        weightPicker.setTitle(getString(R.string.title_select_interval));
        weightPicker.setPositiveClickListener(new DialogFragmentInterface.OnClickListener() {

            @Override
            public void onClick(BaseDialogFragment dialog,
                    Object... params) {
                
            }

        });

        weightPicker.show(getSupportFragmentManager(), "yearIntervalPicker");
    }
    
}
