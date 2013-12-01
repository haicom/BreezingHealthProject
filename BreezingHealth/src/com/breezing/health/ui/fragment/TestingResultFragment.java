package com.breezing.health.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.breezing.health.R;
import com.breezing.health.entity.BreezingTestReport;
import com.breezing.health.util.ExtraName;

public class TestingResultFragment extends BaseFragment implements OnClickListener {

    private View mFragmentView;
    private Button mConfirm;
    private TextView mREE;
    private TextView mRQ;
    
    public static TestingResultFragment newInstance() {
        TestingResultFragment fragment = new TestingResultFragment();
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_testing_result, null);
        mREE = (TextView) mFragmentView.findViewById(R.id.result_of_ree);
        mRQ = (TextView) mFragmentView.findViewById(R.id.result_of_rq);
        mConfirm = (Button) mFragmentView.findViewById(R.id.confirm);
        mConfirm.setOnClickListener(this);
        updateREE();
        updateRQ();
        return mFragmentView;
    }
    
    private void updateREE() {
        final String result = "2000千卡/天";
        final String state = "正常";
        SpannableString span = new SpannableString(result + "    " + state);
        span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.green)),
                span.length() - state.length(),
                span.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mREE.setText(span);
    }
    
    private void updateRQ() {
        mRQ.setText("0.89碳水化合物");
    }
    
    @Override
    public void onClick(View v) {
        if (v == mConfirm) {
            BreezingTestReport report = new BreezingTestReport();
            report.setMetabolism(2345);
            report.setSport(434);
            report.setDigest(234);
            
            getActivity().getIntent().putExtra(ExtraName.EXTRA_DATA, report);
            getActivity().setResult(Activity.RESULT_OK, getActivity().getIntent());
            getActivity().finish();
            return ;
        }
    }
    
}
