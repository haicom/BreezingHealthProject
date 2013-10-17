package com.breezing.health.ui.fragment;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.breezing.health.R;
import com.breezing.health.adapter.LeftMenuAdapter;
import com.breezing.health.entity.AccountEntity;
import com.breezing.health.providers.Breezing.EnergyCost;
import com.breezing.health.ui.activity.BaseActivity;
import com.breezing.health.ui.activity.MainActivity;
import com.breezing.health.util.BreezingQueryViews;
import com.breezing.health.util.ChangeUnitUtil;
import com.breezing.health.util.InternalStorageContentProvider;
import com.breezing.health.util.LocalSharedPrefsUtil;

public class LeftMenuFragment extends BaseFragment implements android.view.View.OnClickListener {
    
    private View mFragmentView;
    private TextView mName;
    private TextView mSex;
    private ListView mListView;
    private LeftMenuAdapter mAdapter;
    private View mHeaderView;
    private ImageView mAvatar;
    
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
        
        mHeaderView = inflater.inflate(R.layout.left_menu_header, null);
        mName = (TextView) mHeaderView.findViewById(R.id.name);
        mSex = (TextView) mHeaderView.findViewById(R.id.sex);
        mAvatar = (ImageView) mHeaderView.findViewById(R.id.icon);
        mAvatar.setOnClickListener(this);
        mListView.addHeaderView(mHeaderView);
        
        int accountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(getActivity(),
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        BreezingQueryViews query = new BreezingQueryViews(getActivity());
        final AccountEntity account = query.queryBaseInfoViews(accountId);
        if (account != null) {
            mName.setText(account.getAccountName());
            mSex.setText(ChangeUnitUtil.changeGenderUtil(getActivity(), account.getGender()));
        }
        
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

    @Override
    public void onClick(View v) {
        
        if (v == mAvatar) {
            ((BaseActivity) getActivity()).toggle();
            ((MainActivity) getActivity()).showImagePickerDialog();
            return ;
        }
        
    }
    
    @Override
    public void onResume() {
        super.onResume();
        queryEnergyCost();
        setAvatar();
    }
    
    private void setAvatar() {
        final int accountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(getActivity(),
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        try {
            File f = new File(getActivity().getFilesDir(), String.valueOf(accountId) + InternalStorageContentProvider.PHOTO_FILE_NAME);
            if (f.exists()) {
                ParcelFileDescriptor pfd =  (ParcelFileDescriptor.open(f, ParcelFileDescriptor.MODE_READ_WRITE));
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    Bitmap bm = BitmapFactory.decodeFileDescriptor(fd);
                    mAvatar.setImageBitmap(bm);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private final static int ENERGY_COST_TOTAL_ENERGY = 0;
    /*** 
     * 查询能量消耗值
     */
    private void queryEnergyCost() {

        int accountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(getActivity(),
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        String sortOrder = EnergyCost.ENERGY_COST_DATE + " DESC";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(EnergyCost.ACCOUNT_ID + " = ? ");
        Cursor cursor = null;
        try {
            cursor = getActivity().getContentResolver().query(EnergyCost.CONTENT_URI,
                    new String[] {EnergyCost.TOTAL_ENERGY},
                    stringBuilder.toString(),
                    new String[] { String.valueOf(accountId) },
                    sortOrder);

            if (cursor != null) {
                if ( cursor.getCount() > 0 ) {
                    cursor.moveToPosition(0);
                    final String unit = getString(R.string.kilojoule);
                    mSex.setText(String.valueOf(cursor.getInt(ENERGY_COST_TOTAL_ENERGY)) + unit);
                }


            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    
}
