package com.breezing.health.ui.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.breezing.health.R;
import com.breezing.health.adapter.LeftMenuAdapter;
import com.breezing.health.entity.MenuItem;

public class LeftMenuFragment extends BaseFragment {
    
    private View mFragmentView;
    private ListView mListView;

    public static LeftMenuFragment newInstance() {
        LeftMenuFragment fragment = new LeftMenuFragment();
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
        mFragmentView = inflater.inflate(R.layout.left_menu, null);
        
        mListView = (ListView) mFragmentView.findViewById(R.id.list);
        
        View headerView = inflater.inflate(R.layout.left_menu_header, null);
        mListView.addHeaderView(headerView);
        
        ArrayList<MenuItem> items = new ArrayList<MenuItem>();
        final String[] names = getActivity().getResources().getStringArray(R.array.lefMenuNames);
        final int count = names.length;
        for (int i = 0; i < count; i++) {
            MenuItem item = new MenuItem(names[i], R.drawable.ic_launcher, null);
            items.add(item);
        }
        
        mListView.setAdapter(new LeftMenuAdapter(getActivity(), items));
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                // TODO Auto-generated method stub
                
            }
        });
        
        return mFragmentView;
    }
    
}
