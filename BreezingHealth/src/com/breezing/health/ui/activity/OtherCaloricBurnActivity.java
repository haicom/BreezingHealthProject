package com.breezing.health.ui.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import com.breezing.health.R;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.providers.Breezing.EnergyCost;
import com.breezing.health.tools.Tools;
import com.breezing.health.util.ExtraName;
import com.breezing.health.util.LocalSharedPrefsUtil;

public class OtherCaloricBurnActivity extends ActionBarActivity {

    private int mAccountId;
    private int mDate;
    private int mSport;
    private int mDigest;
    private View mSportView;
    private View mDigestView;
    
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
        queryUserOtherEnergyCost();
    }

    private void initViews() {
        setActionBarTitle(R.string.other_caloric_burn);
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));
        
        mSportView = findViewById(R.id.base_vane);
        mDigestView = findViewById(R.id.digest_vane);
    }

    private void valueToView() {
        Tools.refreshVane(mSport, mSportView);
        Tools.refreshVane(mDigest, mDigestView);
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
    }
    
}
