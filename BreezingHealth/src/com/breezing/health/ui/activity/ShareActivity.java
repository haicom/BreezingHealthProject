package com.breezing.health.ui.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import br.com.dina.ui.model.GroupIndex;
import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.OnItemClickListener;

import com.breezing.health.R;
import com.breezing.health.entity.AccountEntity;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.providers.Breezing.EnergyCost;
import com.breezing.health.providers.Breezing.Ingestion;
import com.breezing.health.tools.Tools;
import com.breezing.health.util.BLog;
import com.breezing.health.util.BreezingQueryViews;
import com.breezing.health.util.ExtraName;
import com.breezing.health.util.LocalSharedPrefsUtil;

public class ShareActivity extends ActionBarActivity {

    private final String TAG = "ShareActivity";
    
    private final String ACTION_MESSAGE = "message";
    private final String ACTION_EMAIL = "email";
    private final String ACTION_OTHER = "other";

    private UITableView mTableView;
    
    private float mUnifyUnit;
    
    private float mTotalEnergy;
    private float mTotalIngetion;
    
    private String mMessageSubject;
    private String mMessageContent;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_share);
        initValues();
        initViews();
        valueToView();
        initListeners();
    }

    private void initValues() {
        final int date = getIntent().getIntExtra(ExtraName.EXTRA_DATE, 0);
        int accountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(this,
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        BreezingQueryViews query = new BreezingQueryViews(this);
        final AccountEntity account = query.queryBaseInfoViews(accountId);
        mUnifyUnit = query.queryUnitObtainData( this.getString(R.string.caloric_type), account.getCaloricUnit() );
        queryTotalCostEnergy(accountId, date);
        queryTotalIngestionEnergy(accountId, date);
        
        final String dateString = String.valueOf(date);
        mMessageSubject = getString(R.string.share_message_subject, dateString.substring(0, 4)
                , dateString.substring(4, 6)
                , dateString.substring(6, 8));
        mMessageContent = getString(R.string.share_message_content, dateString.substring(0, 4)
                , dateString.substring(4, 6)
                , dateString.substring(6, 8)
                , account.getAccountName()
                , mTotalIngetion
                , account.getCaloricUnit()
                , mTotalEnergy
                , account.getCaloricUnit());
    }

    private void initViews() {
        setActionBarTitle(R.string.share);
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));
        
        mTableView = (UITableView) findViewById(R.id.tableView);
        createList();
        mTableView.commit();
    }

    private void valueToView() {
        
    }

    private void initListeners() {
        mTableView.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onClick(View view, ViewGroup contentView, String action,
                    GroupIndex index) {
                // TODO Auto-generated method stub
                if (action.equals(ACTION_MESSAGE)) {
                    Tools.sendSMS(ShareActivity.this, mMessageContent);
                    return ;
                } else if (action.equals(ACTION_EMAIL)) {
                    Tools.sendEMAIL(ShareActivity.this, mMessageSubject, mMessageContent);
                    return ;
                } else if (action.equals(ACTION_OTHER)) {
                    Tools.sendMessage(ShareActivity.this, mMessageSubject, mMessageContent);
                    return ;
                }
            }
        });
    }
    
    /**
     * create UITableView items
     */
    private void createList() {
        mTableView.addBasicItem(getString(R.string.share_by_message), getString(R.string.share_by_message_summary), ACTION_MESSAGE);
        mTableView.addBasicItem(getString(R.string.share_by_email), getString(R.string.share_by_email_summary), ACTION_EMAIL);
        mTableView.addBasicItem(getString(R.string.share_by_other), getString(R.string.share_by_other_summary), ACTION_OTHER);
    }
    
    private static final String[] PROJECTION_ENERGY_COST = new String[] {
        EnergyCost.TOTAL_ENERGY,   //0
    };

    private final static int ENERGY_COST_TOTAL_ENERGY_INDEX = 0;
    
    public void queryTotalCostEnergy(int accountId, int date) {
        
        String sortOrder = EnergyCost.ENERGY_COST_DATE + " DESC";

        BLog.d(TAG, " drawPieChar  accountId = " + accountId + " date = " + date);
        
        if ( date == 0 ) {
            return;
        }
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(EnergyCost.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(EnergyCost.DATE + " = ?  ");
        
        Cursor cursor = null;
        
        try {
            cursor = getContentResolver().query(EnergyCost.CONTENT_URI,
                    PROJECTION_ENERGY_COST,
                    stringBuilder.toString(),
                    new String[] { String.valueOf(accountId),  String.valueOf(date) },
                    sortOrder);

            if (cursor != null) {
                if ( cursor.getCount() > 0 ) {
                    cursor.moveToPosition(0);
                    mTotalEnergy =  cursor.getFloat(ENERGY_COST_TOTAL_ENERGY_INDEX) * mUnifyUnit;
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }
    
    private static final String[] PROJECTION_ENERGY_INGESTION = new String[] {
        Ingestion.TOTAL_INGESTION // 1
    };

    private final static int ENERGY_INGESTION_TOTAL_ENERGY_INDEX = 0;
    
    public void queryTotalIngestionEnergy(int accountId, int date) {
        
        String sortOrder = Ingestion.DATE + " DESC";

        BLog.d(TAG, " drawPieChar  accountId = " + accountId + " date = " + date);
        
        if ( date == 0 ) {
            return;
        }
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(Ingestion.ACCOUNT_ID + " = ? AND ");
        stringBuilder.append(Ingestion.DATE + " = ?  ");
        
        Cursor cursor = null;
        
        try {
            cursor = getContentResolver().query(Ingestion.CONTENT_URI,
                    PROJECTION_ENERGY_INGESTION,
                    stringBuilder.toString(),
                    new String[] { String.valueOf(accountId),  String.valueOf(date) },
                    sortOrder);

            if (cursor != null) {
                if ( cursor.getCount() > 0 ) {
                    cursor.moveToPosition(0);
                    mTotalIngetion =  cursor.getFloat(ENERGY_INGESTION_TOTAL_ENERGY_INDEX) * mUnifyUnit;
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }
    
}
