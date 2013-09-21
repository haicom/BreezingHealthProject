package com.breezing.health.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.breezing.health.R;
import com.breezing.health.entity.enums.ChartModel;
import com.breezing.health.ui.activity.CaloricHistoryActivity.CaloricHistoryType;
import com.breezing.health.widget.linechart.FancyChart;
import com.breezing.health.widget.linechart.FancyChartPointListener;
import com.breezing.health.widget.linechart.data.ChartData;
import com.breezing.health.widget.linechart.data.Point;

public class CaloricWeeklyHistoryFragment extends BaseFragment {

    private View mFragmentView;
    private FancyChart mChart;
    
    private CaloricHistoryType mCaloricHistoryType;
    private ChartModel mCaloricHistoryChartModel;
    
    private ChartData mChartData;
    
    public static CaloricWeeklyHistoryFragment newInstance() {
        CaloricWeeklyHistoryFragment fragment = new CaloricWeeklyHistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_caloric_weekly_history, null);
        mChart = (FancyChart) mFragmentView.findViewById(R.id.chart);
        
        mChart.setOnPointClickListener(new FancyChartPointListener() {
            
            @Override
            public void onClick(Point point) {
                
            }
        });
        
        refreshChart();
        
        return mFragmentView;
    }
    
    public void setChartType(CaloricHistoryType type) {
        mCaloricHistoryType = type;
    }
    
    public void setChartModel(ChartModel model) {
        mCaloricHistoryChartModel = model;
    }

    public void setChartData(ChartData data) {
        mChartData = data;
    }
    
    public void refreshChart() {
        if (mChartData != null) {
            mChart.clearValues();
            mChart.addData(mChartData);
        }
    }
    
}
