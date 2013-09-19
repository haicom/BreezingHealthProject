package com.breezing.health.ui.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.breezing.health.R;
import com.breezing.health.adapter.AddCaloricRecordAdapter;
import com.breezing.health.adapter.CaloricPagerAdapter;
import com.breezing.health.entity.PiePartEntity;
import com.breezing.health.entity.RecordFunctionEntity;
import com.breezing.health.widget.PieChart;
import com.breezing.health.widget.PieChart.OnSelectedLisenter;

public class CaloricIntakeFragment extends BaseFragment {

    private View mFragmentView;
    private PieChart mPieChart;
    private GridView mGridView;
    private AddCaloricRecordAdapter mAdapter;
    private static CaloricIntakeFragment mCaloricIntakeFragment;

    public static CaloricIntakeFragment newInstance() {
        CaloricIntakeFragment fragment = new CaloricIntakeFragment();
        return fragment;
    }

    public static CaloricIntakeFragment getInstance(int num) {
        if (mCaloricIntakeFragment == null) {
            mCaloricIntakeFragment = new CaloricIntakeFragment();
            Bundle args = new Bundle();
            args.putInt(CaloricPagerAdapter.MAIN_INTERFACE_SAVE_NUM, num);
            mCaloricIntakeFragment.setArguments(args);
        }
        return mCaloricIntakeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mFragmentView = inflater.inflate(R.layout.fragment_caloric_intake, null);
        mPieChart = (PieChart) mFragmentView.findViewById(R.id.pieChart);
        mGridView = (GridView) mFragmentView.findViewById(R.id.gridView);

        ArrayList<PiePartEntity> pieParts = new ArrayList<PiePartEntity>();
        pieParts.add(new PiePartEntity(10.0f, R.color.black));
        pieParts.add(new PiePartEntity(20.0f, R.color.orange));
        pieParts.add(new PiePartEntity(30.0f, R.color.red));
        pieParts.add(new PiePartEntity(40.0f, R.color.gray));

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
        funs.add(new RecordFunctionEntity(R.string.breakfast, 30, 100, R.color.orange));
        funs.add(new RecordFunctionEntity(R.string.lunch, 55, 100, R.color.orange));
        funs.add(new RecordFunctionEntity(R.string.dinner, 15, 100, R.color.orange));
        funs.add(new RecordFunctionEntity(R.string.other, 15, 100, R.color.orange));
        mAdapter = new AddCaloricRecordAdapter(getActivity(), funs);
        mGridView.setAdapter(mAdapter);

        return mFragmentView;
    }

}
