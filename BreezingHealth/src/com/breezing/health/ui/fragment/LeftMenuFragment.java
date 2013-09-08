package com.breezing.health.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.breezing.health.R;
import com.breezing.health.adapter.LeftMenuAdapter;
import com.breezing.health.ui.activity.BaseActivity;

public class LeftMenuFragment extends BaseFragment {
    
    private View mFragmentView;
    private ListView mListView;
    private LeftMenuAdapter mAdapter;

    public static LeftMenuFragment newInstance() {
        LeftMenuFragment fragment = new LeftMenuFragment();
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.left_menu, null);
        
        mListView = (ListView) mFragmentView.findViewById(R.id.list);
        
        View headerView = inflater.inflate(R.layout.left_menu_header, null);
        mListView.addHeaderView(headerView);
        
        mAdapter = new LeftMenuAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                    long arg3) {
                //check click position is headerView
                if (position < mListView.getHeaderViewsCount()) {
                    return ;
                }
                final int menuPosition = position - mListView.getHeaderViewsCount();
                if (mAdapter.getItem(menuPosition).intent != null) {
                    ((BaseActivity) getActivity()).toggle();
                    getActivity().startActivity(new Intent(mAdapter.getItem(menuPosition).intent));
                }
            }
        });
        
        return mFragmentView;
    }
    
}
