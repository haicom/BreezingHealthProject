package com.breezing.health.ui.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.breezing.health.R;
import com.breezing.health.adapter.BreezingPagerAdapter;
import com.breezing.health.ui.activity.TestingActivity;
import com.breezing.health.widget.CustomViewPager;
import com.breezing.health.widget.flake.FlakeView;
import com.breezing.health.widget.linechart.FancyChartNoLine;
import com.breezing.health.widget.linechart.data.ChartData;

public class TestingBreezingFragment extends BaseFragment implements OnClickListener {

    private final int INIT_SUCCESS = 101;
    private final int READY_SUCCESS = 102;
    private final int BLOW_SUCCESS = 103;
    
    private View mFragmentView;
    private Button mStop;
    private CustomViewPager mViewPager;
    private View mInitializePage;
    private View mReadyForBlowPage;
    private View mBlowPage;
    private View mResultPage;
    private FlakeView mFlakeView;
    private BreezingPagerAdapter mPagerAdapter;
    private Handler mHandler;
    private boolean mIsTestOver;
    
    public static TestingBreezingFragment newInstance() {
        TestingBreezingFragment fragment = new TestingBreezingFragment();
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_testing_breezing, null);
        mStop = (Button) mFragmentView.findViewById(R.id.stop);
        mStop.setOnClickListener(this);
        mViewPager = (CustomViewPager) mFragmentView.findViewById(R.id.view_pager);
//        mViewPager.setPagingEnabled(false);
        mInitializePage = inflater.inflate(R.layout.page_of_initialize, null);
        mReadyForBlowPage = inflater.inflate(R.layout.page_of_ready_blow, null);
        mBlowPage = inflater.inflate(R.layout.page_of_blow, null);
        mFlakeView = new FlakeView(getActivity());
        LinearLayout flakeLayout = (LinearLayout) mBlowPage.findViewById(R.id.flake_layout);
        flakeLayout.addView(mFlakeView,
                new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mResultPage = inflater.inflate(R.layout.page_of_result, null);
        ArrayList<View> views = new ArrayList<View>();
        views.add(mInitializePage);
        views.add(mReadyForBlowPage);
        views.add(mBlowPage);
        views.add(mResultPage);
        mPagerAdapter = new BreezingPagerAdapter(views);
        mViewPager.setAdapter(mPagerAdapter);
        mHandler = new Handler(getActivity().getMainLooper()) {
            @Override
            public void dispatchMessage(Message msg) {
                
                if (isRemoving()) {
                    return ;
                }
                
                final int what = msg.what;
                switch (what) {
                case INIT_SUCCESS:
                    changeToReady();
                    return ;
                
                case READY_SUCCESS:
                    changeToBlow();
                    return ;
                    
                case BLOW_SUCCESS:
                    changeToResult();
                    return ;

                default:
                    break;
                }
                super.dispatchMessage(msg);
            }
        };
        mHandler.sendEmptyMessageDelayed(INIT_SUCCESS, 1 * 1000);
        
        return mFragmentView;
    }
    
    private void changeToReady() {
        mViewPager.setCurrentItem(1);
        ((TestingActivity) getActivity()).setActionBarTitle(R.string.begin_to_blow);
        mHandler.sendEmptyMessageDelayed(READY_SUCCESS, 1 * 1000);
    }
    
    private void changeToBlow() {
        mViewPager.setCurrentItem(2);
        ((TestingActivity) getActivity()).setActionBarTitle(R.string.blow);
        mHandler.sendEmptyMessageDelayed(BLOW_SUCCESS, 1 * 1000);
        updateBlowData();
    }
    
    private void updateBlowData() {
        final String times = "11.4";
        final String notice = getString(R.string.blow_per_minite, times);
        SpannableString span = new SpannableString(notice);
        span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)),
                0,
                times.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView) mBlowPage.findViewById(R.id.count_notice)).setText(span);
        
        ChartData chartData = new ChartData(ChartData.LINE_COLOR_RED);
        chartData.addXValue(1, "1");
        chartData.addXValue(2, "2");
        chartData.addXValue(3, "3");
        chartData.addXValue(4, "4");
        chartData.addXValue(5, "5");
        chartData.addPoint(1, 70, "", "");
        chartData.addPoint(2, 110, "", "");
        chartData.addPoint(3, 114, "", "");
        chartData.addPoint(4, 86, "", "");
        chartData.addPoint(5, 108, "", "");
        FancyChartNoLine chart = ((FancyChartNoLine) mBlowPage.findViewById(R.id.chart));
        chart.clearValues();
        chart.addData(chartData);
        chart.invalidate();
    }
    
    private void changeToResult() {
        mViewPager.setCurrentItem(3);
        ((TestingActivity) getActivity()).setActionBarTitle(R.string.test_over);
        final String name = "张三";
        final String notice = getString(R.string.test_result_notice, name);
        SpannableString span = new SpannableString(notice);
        span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)),
                0,
                2 + name.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new RelativeSizeSpan(1.3f),
                0,
                2 + name.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView) mResultPage.findViewById(R.id.notice)).setText(span);
        mStop.setText(R.string.confirm);
        mIsTestOver = true;
    }

    @Override
    public void onClick(View v) {
        if (v == mStop) {
            if (mIsTestOver) {
                ((TestingActivity) getActivity()).createResultFragment();
            } else {
                ((TestingActivity) getActivity()).createBTScanFragment();
            }
            return ;
        }
    }
    
    @Override
    public void onDestroy() {
        mHandler.removeMessages(INIT_SUCCESS);
        mHandler.removeMessages(READY_SUCCESS);
        mHandler.removeMessages(BLOW_SUCCESS);
        super.onDestroy();
    }
    
    @Override
    public void onPause() {
        super.onPause();
        mFlakeView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFlakeView.resume();
    }
    
}
