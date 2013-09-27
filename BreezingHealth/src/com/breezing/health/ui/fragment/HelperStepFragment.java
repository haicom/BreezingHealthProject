package com.breezing.health.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.breezing.health.R;
import com.breezing.health.entity.enums.HelperStep;

public class HelperStepFragment extends BaseFragment {

	private View mFragmentView;
	private HelperStep mStep;

    public static HelperStepFragment newInstance(HelperStep step) {
    	HelperStepFragment fragment = new HelperStepFragment();
    	fragment.mStep = step;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.helper_pager_item, null);
        ImageView bg = (ImageView) mFragmentView.findViewById(R.id.background);
        ImageView step = (ImageView) mFragmentView.findViewById(R.id.step);
        TextView intro = (TextView) mFragmentView.findViewById(R.id.introduce);
        
        bg.setImageResource(mStep.bgRes);
        step.setImageResource(mStep.stepRes);
        intro.setText(mStep.nameRes);
        
        return mFragmentView;
    }
	
}
