package com.breezing.health.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.breezing.health.R;
import com.breezing.health.entity.AccountEntity;
import com.breezing.health.providers.Breezing.EnergyCost;
import com.breezing.health.tools.IntentAction;
import com.breezing.health.tools.Tools;
import com.breezing.health.util.BreezingQueryViews;
import com.breezing.health.util.ExtraName;
import com.breezing.health.util.LocalSharedPrefsUtil;

public class BreezingTestResultFragment extends BaseFragment implements OnClickListener {
    private final static String TAG = "BreezingTestResultFragment";
    private View mFragmentView;
    private Button mNext;
    private TextView   mTextView;
    private float mTotalEnergy = 0;
    private int mEnergyCostDate = 0;
    private static BreezingTestResultFragment mFragment;
    private View mTotalVane;
    
    private float mUnifyUnit;
    private String mCaloricUnit;
    
    private AccountEntity mAccount;
    
    private boolean mIsUpdate;

    public static BreezingTestResultFragment newInstance() {
        
        if (mFragment == null) {
            mFragment = new BreezingTestResultFragment();
        }
        
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {        
        mFragmentView = inflater.inflate(R.layout.fragment_breezing_test_result, null);
        mTotalVane = mFragmentView.findViewById(R.id.total_vane);
        mTextView = (TextView) mFragmentView.findViewById(R.id.result);
        mNext = (Button) mFragmentView.findViewById(R.id.next);
        mNext.setOnClickListener(this);
        
        mIsUpdate = getActivity().getIntent().getBooleanExtra(ExtraName.EXTRA_DATE, false);
        if (mIsUpdate) {
            mNext.setText(R.string.confirm);
        }
       
        return mFragmentView;
    }
    
    @Override
    public void onResume() {
        Log.d(TAG, " onResume ");
        super.onResume();
        int accountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this.getActivity(),
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        BreezingQueryViews query = new BreezingQueryViews(this.getActivity());
        mAccount = query.queryBaseInfoViews(accountId);
        mUnifyUnit = query.queryUnitObtainData( this.getString(R.string.caloric_type), mAccount.getCaloricUnit() );
    }
    
    @Override
    public void onClick(View v) {
        if ( v == mNext ) {
            if (mIsUpdate) {
                getActivity().finish();
                return ;
            }
            
            Intent intent = new Intent(IntentAction.ACTIVITY_MAIN);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            getActivity().finish();
            return ;
        }
    }

    public void showBreezingResult() {
        queryEnergyCost();
        Log.d(TAG, "showBreezingResult mTotalEnergy = " + mTotalEnergy + " mEnergyCostDate = " + mEnergyCostDate  );
        if ( (mEnergyCostDate != 0) && (mTotalEnergy != 0) ) {
            String year = String.valueOf(mEnergyCostDate).subSequence(0, ENERGY_COST_YEAR).toString();
            String month =  String.valueOf(mEnergyCostDate).subSequence(ENERGY_COST_YEAR ,
                    ENERGY_COST_YEAR + ENERGY_COST_MONTH ).toString();
            String day = String.valueOf(mEnergyCostDate).subSequence( ENERGY_COST_YEAR + ENERGY_COST_MONTH  ,
                    String.valueOf(mEnergyCostDate).length() ).toString();
            Log.d(TAG, "onCreateView year = " + year + " month = " + month + " day =" +  day  );
            final String result = getActivity().getString(R.string.breezing_result,
                    year, month , day, mTotalEnergy);
            SpannableString spannable = new SpannableString(result);
            spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), result.length() - 7, result.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 2, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mTextView.setText(spannable);
        }
        
        Tools.refreshVane( (int) mTotalEnergy, mTotalVane);
    }

    private final static int ENERGY_COST_METABOLISML_ENERGY = 0;
    private final static int ENERGY_COST_ENERGY_COST_DATE = 1;
    /*** 
     * 查询能量消耗值
     */
    private void queryEnergyCost() {

        int accountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(getActivity(),
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        String sortOrder = EnergyCost.ENERGY_COST_DATE + " DESC";

        Log.d(TAG, " queryEnergyCost accountId = " + accountId);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(EnergyCost.ACCOUNT_ID + " = ? ");
        Cursor cursor = null;
        try {
            cursor = getActivity().getContentResolver().query(EnergyCost.CONTENT_URI,
                    new String[] { EnergyCost.METABOLISM, EnergyCost.ENERGY_COST_DATE },
                    stringBuilder.toString(),
                    new String[] { String.valueOf(accountId) },
                    sortOrder);

            if (cursor != null) {
                if ( cursor.getCount() > 0 ) {
                    cursor.moveToPosition(0);
                    mTotalEnergy =  cursor.getFloat(ENERGY_COST_METABOLISML_ENERGY) * mUnifyUnit;
                    mEnergyCostDate = cursor.getInt(ENERGY_COST_ENERGY_COST_DATE);
                }


            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.d(TAG, " queryEnergyCost  mTotalEnergy = " + mTotalEnergy + " mEnergyCostDate = " + mEnergyCostDate);
    }

    private static final int ENERGY_COST_YEAR = 4;
    private static final int ENERGY_COST_MONTH = 2;
    private static final int ENERGY_COST_DAY = 2;
    
}
