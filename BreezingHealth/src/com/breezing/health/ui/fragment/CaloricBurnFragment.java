package com.breezing.health.ui.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.breezing.health.R;
import com.breezing.health.adapter.AddCaloricRecordAdapter;
import com.breezing.health.entity.PiePartEntity;
import com.breezing.health.entity.RecordFunctionEntity;
import com.breezing.health.widget.PieChart;
import com.breezing.health.widget.PieChart.OnSelectedLisenter;

public class CaloricBurnFragment extends BaseFragment {

    private View mFragmentView;
    private PieChart mPieChart;
    private GridView mGridView;
    private AddCaloricRecordAdapter mAdapter;

    public static CaloricBurnFragment newInstance() {
        CaloricBurnFragment fragment = new CaloricBurnFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState ) {
        mFragmentView = inflater.inflate(R.layout.fragment_caloric_burn, null);
        mPieChart = (PieChart) mFragmentView.findViewById(R.id.pieChart);
        mGridView = (GridView) mFragmentView.findViewById(R.id.gridView);

        ArrayList<PiePartEntity> pieParts = new ArrayList<PiePartEntity>();
        pieParts.add(new PiePartEntity(70.0f, R.color.orange));
        pieParts.add(new PiePartEntity(30.0f, R.color.red));

        try {
            mPieChart.setAdapter(pieParts);
            mPieChart.setClickable(false);
            mPieChart.setOnSelectedListener(new OnSelectedLisenter() {
                @Override
                public void onSelected(int iSelectedIndex) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<RecordFunctionEntity> funs = new ArrayList<RecordFunctionEntity>();

        funs.add(new RecordFunctionEntity(R.string.energy_metabolism, 30f, R.color.orange));
        funs.add(new RecordFunctionEntity(R.string.exercise, 55f, R.color.orange));
        funs.add(new RecordFunctionEntity(R.string.other, 15f, R.color.orange));

        mAdapter = new AddCaloricRecordAdapter(getActivity(), funs);
        mGridView.setAdapter(mAdapter);

        return mFragmentView;
    }

}
