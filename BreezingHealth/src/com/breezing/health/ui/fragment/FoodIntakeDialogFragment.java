package com.breezing.health.ui.fragment;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;

import com.breezing.health.R;
import com.breezing.health.adapter.IntakeFoodAdapter;
import com.breezing.health.entity.FoodEntity;
import com.haarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.haarman.listviewanimations.itemmanipulation.SwipeDismissAdapter;

public class FoodIntakeDialogFragment extends BaseDialogFragment implements OnClickListener, OnDismissCallback {

	private View mFragmentView;
	private ListView mListView;
	private IntakeFoodAdapter mAdapter;
	private Button mConfirm;
	private OnDismissCallback mOnDismissCallback;
	private ArrayList<FoodEntity> mFoods;
    
    public static FoodIntakeDialogFragment newInstance() {
    	FoodIntakeDialogFragment fragment = new FoodIntakeDialogFragment();
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {        
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_NoTitleBar);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_dialog_food_intake, null);
        mConfirm = (Button) mFragmentView.findViewById(R.id.confirm);
        mListView = (ListView) mFragmentView.findViewById(R.id.list);
        
        mConfirm.setOnClickListener(this);
        
        mAdapter = new IntakeFoodAdapter(getActivity());
		mAdapter.addItems(mFoods);
        SwipeDismissAdapter adapter = new SwipeDismissAdapter(mAdapter, this);
		adapter.setAbsListView(mListView);
        mListView.setAdapter(adapter);
        
        getDialog().getWindow().setBackgroundDrawable(new
                ColorDrawable(Color.TRANSPARENT));
        
        return mFragmentView;
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mConfirm) {
			dismiss();
			return ;
		}
	}
	
	public void setSelectedFoods(ArrayList<FoodEntity> foods) {
		mFoods = foods;
	}
	
	public void setOnDeleteListener(OnDismissCallback listener) {
		mOnDismissCallback = listener;
	}

	@Override
	public void onDismiss(AbsListView listView, int[] reverseSortedPositions) {
		// TODO Auto-generated method stub
		for (int position : reverseSortedPositions) {
			mAdapter.removeItem(position);
		}
		mAdapter.notifyDataSetChanged();
		mOnDismissCallback.onDismiss(listView, reverseSortedPositions);
	}

}
