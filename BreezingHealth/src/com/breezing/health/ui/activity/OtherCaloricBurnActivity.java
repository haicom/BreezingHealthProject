package com.breezing.health.ui.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.breezing.health.R;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.providers.Breezing.Account;
import com.breezing.health.providers.Breezing.EnergyCost;
import com.breezing.health.providers.Breezing.Information;
import com.breezing.health.providers.Breezing.WeightChange;
import com.breezing.health.tools.Tools;
import com.breezing.health.util.BreezingQueryViews;
import com.breezing.health.util.ExtraName;
import com.breezing.health.util.LocalSharedPrefsUtil;

public class OtherCaloricBurnActivity extends ActionBarActivity {
	
	private final String TAG = "OtherCaloricBurnActivity";

    private int mAccountId;
    private int mDate;
    private int mSport;
    private int mDigest;
    private View mSportView;
    private View mDigestView;
    private String mCaloricUnit;
    private TextView mBaseNotice;
    private TextView mDigestNotice;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_other_caloric_burn);
        initValues();
        initViews();
        valueToView();
        initListeners();
    }

    private void initValues() {
        LocalSharedPrefsUtil.saveFirstTime(this);
        mAccountId = getIntent().getIntExtra(ExtraName.EXTRA_ACCOUNT_ID, 0);
        mDate = getIntent().getIntExtra(ExtraName.EXTRA_DATE, 0);
        queryBaseInfoViews(mAccountId);
        queryUserOtherEnergyCost();
    }

    private void initViews() {
        setActionBarTitle(R.string.other_caloric_burn);
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));
        
        mSportView = findViewById(R.id.base_vane);
        mDigestView = findViewById(R.id.digest_vane);
        
        mBaseNotice = (TextView) findViewById(R.id.baseResult);
        mDigestNotice = (TextView) findViewById(R.id.digestResult);
    }

    private void valueToView() {
        Tools.refreshVane(mSport, mSportView);
        Tools.refreshVane(mDigest, mDigestView);
        mBaseNotice.setText(getString(R.string.base_exercise_caloric_burn, mCaloricUnit));
        mDigestNotice.setText(getString(R.string.digest_exercise_caloric_burn, mCaloricUnit));
    }

    private void initListeners() {
        
    }
    
    private static final String[] PROJECTION_ENERGY_COST = new String[] {
        EnergyCost.SPORT,
        EnergyCost.DIGEST
    };

    private final static int ENERGY_COST_SPORT_INDEX = 0;
    private final static int ENERGY_COST_DIGEST_INDEX = 1;
    
    private void queryUserOtherEnergyCost() {
        String sortOrder = EnergyCost.ENERGY_COST_DATE + " DESC";
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(EnergyCost.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(EnergyCost.DATE + " = ?  ");
        
        Cursor cursor = null;
        
        try {
            cursor = getContentResolver().query(EnergyCost.CONTENT_URI,
                    PROJECTION_ENERGY_COST,
                    stringBuilder.toString(),
                    new String[] { String.valueOf(mAccountId),  String.valueOf(mDate) },
                    sortOrder);

            if (cursor != null) {
                if ( cursor.getCount() > 0 ) {
                    cursor.moveToPosition(0);
                    mSport = cursor.getInt(ENERGY_COST_SPORT_INDEX);
                    mDigest = cursor.getInt(ENERGY_COST_DIGEST_INDEX);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        
        BreezingQueryViews query = new BreezingQueryViews(this);
        mSport = (int) query.queryUnitUnifyData(
        		mSport ,
                getResources().getString(R.string.caloric_type),
                mCaloricUnit);
        mDigest = (int) query.queryUnitUnifyData(
        		mDigest ,
                getResources().getString(R.string.caloric_type),
                mCaloricUnit);
    }
    
    private static final String[] PROJECTION_BASE_INFO = new String[] {
        Information.CALORIC_UNIT
    };
    
    private static final int INFO_CALORIC_UNIT_INDEX = 0;

    public void queryBaseInfoViews(int accountId) {
        Log.d(TAG, "queryBaseInfoView");
        
        String accountClause =  Account.ACCOUNT_ID + " = ?";
        String sortOrder = WeightChange.DATE + " DESC";
        String[] args = new String[] {String.valueOf(accountId)};
        
        Cursor cursor  = getContentResolver().query(Information.CONTENT_BASE_INFO_URI,
                PROJECTION_BASE_INFO, accountClause, args, sortOrder);
        if (cursor == null) {
            Log.d(TAG, " testBaseInfoView cursor = " + cursor);
        }
  
        try {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext() ) {
                mCaloricUnit = cursor.getString(INFO_CALORIC_UNIT_INDEX);
            }
        } finally {
            cursor.close();            
        }
        
    }
    
}
