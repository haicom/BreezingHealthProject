package com.breezing.health.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.breezing.health.R;
import com.breezing.health.tools.IntentAction;

public class BreezingTestResultFragment extends BaseFragment implements OnClickListener {

    private View mFragmentView;
    private Button mNext;
    
    public static BreezingTestResultFragment newInstance() {
        BreezingTestResultFragment fragment = new BreezingTestResultFragment();
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mFragmentView = inflater.inflate(R.layout.fragment_breezing_test_result, null);
        mNext = (Button) mFragmentView.findViewById(R.id.next);
        
        mNext.setOnClickListener(this);
    
        return mFragmentView;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == mNext) {
            Intent intent = new Intent(IntentAction.ACTIVITY_MAIN);
            startActivity(intent);
            getActivity().finish();
            return ;
        }
    }
}
