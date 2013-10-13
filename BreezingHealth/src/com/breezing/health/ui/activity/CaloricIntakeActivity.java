package com.breezing.health.ui.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.content.ContentProviderOperation;
import android.database.Cursor;

import com.breezing.health.R;
import com.breezing.health.providers.Breezing;
import com.breezing.health.providers.Breezing.Ingestion;
import com.breezing.health.providers.Breezing.IngestiveRecord;
import com.breezing.health.adapter.FoodAdapter;
import com.breezing.health.adapter.FoodCatagoryAdapter;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.entity.FoodEntity;
import com.breezing.health.ui.fragment.FoodIntakeDialogFragment;
import com.breezing.health.util.ExtraName;
import com.breezing.health.widget.MultiDirectionSlidingDrawer;
import com.breezing.health.widget.MultiDirectionSlidingDrawer.OnDrawerCloseListener;
import com.haarman.listviewanimations.itemmanipulation.OnDismissCallback;

public class CaloricIntakeActivity extends ActionBarActivity implements OnClickListener {
    private static final String TAG = "CaloricIntakeActivity";
    
    public enum CaloricIntakeType {
        BREAKFAST, LUNCH, DINNER, OTHER
    }

    private CaloricIntakeType mCaloricIntakeType;

    private MultiDirectionSlidingDrawer mDrawer;
    private GridView mCatagoryGridView;
    private FoodCatagoryAdapter mCatagoryAdapter;
    private ListView mFoodList;
    private FoodAdapter mFoodAdapter;

    private TextView mTotal;
    private Button mDetail;
    
    private int mAccountId;
    private int mDate;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                if (mDrawer.isOpened()) {
                    mDrawer.animateToggle();
                    return true;
                }
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_caloric_intake);
        initValues();
        initViews();
        valueToView();
        initListeners();
    }

    private void initValues() {
        Intent intent = this.getIntent();
        mCaloricIntakeType = CaloricIntakeType.values()[intent.getIntExtra(ExtraName.EXTRA_TYPE, CaloricIntakeType.BREAKFAST.ordinal())];
        mAccountId = intent.getIntExtra(ExtraName.EXTRA_ACCOUNT_ID, 0);
        mDate = intent.getIntExtra(ExtraName.EXTRA_DATE, 0);
        Log.d(TAG," initValues mCaloricIntakeType = " + mCaloricIntakeType + " mAccountId = " + mAccountId + " mDate = " + mDate );
    }

    private void initViews() {
        switch(mCaloricIntakeType) {
            case BREAKFAST:
                setActionBarTitle(R.string.breakfast);
                break;
            case LUNCH:
                setActionBarTitle(R.string.lunch);
                break;
            case DINNER:
                setActionBarTitle(R.string.dinner);
                break;
            case OTHER:
                setActionBarTitle(R.string.other);
                break;
        }

        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));
        addRightActionItem(new ActionItem(ActionItem.ACTION_MORE));

        mDrawer = (MultiDirectionSlidingDrawer) findViewById(R.id.drawer);
        mCatagoryGridView = (GridView) findViewById(R.id.gridView);
        mFoodList = (ListView) findViewById(R.id.list);
        mDetail = (Button) findViewById(R.id.detail);
        mTotal = (TextView) findViewById(R.id.total);
        mDrawer.animateToggle();
    }

    private void valueToView() {
        mCatagoryAdapter = new FoodCatagoryAdapter(this);
        mCatagoryAdapter.toggle(0);
        mCatagoryGridView.setAdapter(mCatagoryAdapter);
        mFoodAdapter = new FoodAdapter(this, mCatagoryAdapter, mAccountId, mDate, mCaloricIntakeType);
        mFoodList.setAdapter(mFoodAdapter);

        updateTotalCaloric();
    }

    private void initListeners() {
    mDetail.setOnClickListener(this);
    mCatagoryGridView.setOnItemClickListener(new OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, 
            int position, long id) {
            mCatagoryAdapter.toggle(position);
            mCatagoryAdapter.notifyDataSetChanged();
            mDrawer.animateToggle();
        }
    });

    mDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
        @Override
        public void onDrawerClosed() {
            mFoodAdapter.refreshCatagoryItems();
            mFoodAdapter.notifyDataSetChanged();
        } });
    }

    @Override
    public void onClickActionBarItems(ActionItem item, View v) {
        switch( item.getActionId() ) {
            case ActionItem.ACTION_MORE: 
                mDrawer.animateToggle();
                return ;
            case ActionItem.ACTION_BACK:
                appendFoodInfo();
                break;
              
        }
        super.onClickActionBarItems(item, v);
    }

    @Override
    public void onClick(View v) {
        if (v == mDetail) {
            showDetailDialog();
            return ;
        }
    }

    private void showDetailDialog() {
        FoodIntakeDialogFragment detailDialog = (FoodIntakeDialogFragment) getSupportFragmentManager().findFragmentByTag("detailPicker");
        if (detailDialog != null) {
            getSupportFragmentManager().beginTransaction().remove(detailDialog);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(null);
        detailDialog = FoodIntakeDialogFragment.newInstance();
        detailDialog.setSelectedFoods(mFoodAdapter.getSelectedFoods());
        detailDialog.setOnDeleteListener(new OnDismissCallback() {

            @Override
            public void onDismiss(AbsListView listView, int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {
                    mFoodAdapter.removeSelectedFood(position);
                }
                mFoodAdapter.notifyDataSetChanged();
            }
        });
        detailDialog.show(getSupportFragmentManager(), "detailPicker");
    }
    
    private void appendFoodInfo() {
        int count = 0;
        ArrayList<ContentProviderOperation>  ops = new ArrayList<ContentProviderOperation>();
        
        ArrayList<FoodEntity> foods = mFoodAdapter.getSelectedFoods();
        for (FoodEntity food: foods ) {
            count = queryIngestiveRecord( food.getFoodId() );
            Log.d(TAG, "appendFoodInfo food.getFoodName() =" + food.getFoodName() + " food.getFoodQuantity() = " + food.getFoodQuantity() 
                    + " food.getSelectedNumber() =    " + food.getSelectedNumber() );
            if (count == 0) {
                appendIngestiveRecord(ops, food);
            } else {
                updateIngestiveRecord(ops, food);
            }
        }
        
        count = queryIngestive();
        
        if (count == 0) {
            if ( mFoodAdapter.getTotalCaloric() > 0) {
                appendTotalIngestive(ops); 
            }
            
        } else {
            updateTotalIngestive(ops);
        }
        
        try {
            getContentResolver().applyBatch(Breezing.AUTHORITY, ops);           
        } catch (Exception e) {           
            Log.e(TAG, "Exceptoin encoutered while inserting contact: " + e);
        }
        
    }
    
    private void appendIngestiveRecord( ArrayList<ContentProviderOperation> ops, FoodEntity foodEntity) {
        ops.add(ContentProviderOperation.newInsert(IngestiveRecord.CONTENT_URI)
                .withValue(IngestiveRecord.ACCOUNT_ID, mAccountId)
                .withValue(IngestiveRecord.FOOD_ID, foodEntity.getFoodId() )                
                .withValue(IngestiveRecord.FOOD_QUANTITY, foodEntity.getSelectedNumber() )
                .withValue(IngestiveRecord.DINING, String.valueOf(mCaloricIntakeType) )
                .withValue(IngestiveRecord.DATE, mDate)              
                .build());
    }
    
    private void updateIngestiveRecord( ArrayList<ContentProviderOperation> ops, FoodEntity foodEntity) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(IngestiveRecord.ACCOUNT_ID + " = ? "  + " AND ");
        stringBuilder.append(IngestiveRecord.FOOD_ID + "= ? " + " AND ");
        stringBuilder.append(IngestiveRecord.DINING + "= ? " + " AND ");
        stringBuilder.append(IngestiveRecord.DATE + "= ? " );
        
        ops.add(ContentProviderOperation.newUpdate(IngestiveRecord.CONTENT_URI)
                .withSelection(stringBuilder.toString(),  
                        new String[] { String.valueOf(mAccountId), 
                                       String.valueOf(foodEntity.getFoodId()), 
                                       String.valueOf(String.valueOf( mCaloricIntakeType) ),
                                       String.valueOf(String.valueOf(mDate)        ) } ) 
                .withValue(IngestiveRecord.ACCOUNT_ID, mAccountId)
                .withValue(IngestiveRecord.FOOD_ID, foodEntity.getFoodId() )                
                .withValue(IngestiveRecord.FOOD_QUANTITY, foodEntity.getSelectedNumber() )
                .withValue(IngestiveRecord.DINING, String.valueOf(mCaloricIntakeType) )
                .withValue(IngestiveRecord.DATE, mDate)              
                .build());
    }
    
    private void appendTotalIngestive( ArrayList<ContentProviderOperation> ops) {
        int breakfast = 0;
        int lunch = 0;
        int dinner = 0;
        int etc = 0;
        
        switch ( mCaloricIntakeType ) {
            case BREAKFAST:
                breakfast =  mFoodAdapter.getTotalCaloric() ;
                break;
            case LUNCH:
                lunch = mFoodAdapter.getTotalCaloric();
                break;
            case DINNER:
                dinner = mFoodAdapter.getTotalCaloric();
                break;
            case OTHER:
                etc = mFoodAdapter.getTotalCaloric();
                break;
        }
        
        ops.add(ContentProviderOperation.newInsert(Ingestion.CONTENT_URI)
                .withValue(Ingestion.ACCOUNT_ID, mAccountId)
                .withValue(Ingestion.BREAKFAST, breakfast)
                .withValue(Ingestion.LUNCH, lunch)
                .withValue(Ingestion.DINNER, dinner)
                .withValue(Ingestion.ETC, etc)  
                .withValue(Ingestion.DATE, mDate)  
                .build() );
    }
    
    private void updateTotalIngestive(ArrayList<ContentProviderOperation> ops) {
        
        
        ContentProviderOperation.Builder builder = ContentProviderOperation     
                .newUpdate(Ingestion.CONTENT_URI); 
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(Ingestion.ACCOUNT_ID + " = ? "  + " AND ");    
        stringBuilder.append(Ingestion.DATE + "= ? " );
        
        builder.withSelection( stringBuilder.toString(),  
                new String[] { String.valueOf(mAccountId),                              
                               String.valueOf( String.valueOf(mDate) ) } );
        
        switch ( mCaloricIntakeType ) {
            case BREAKFAST:
                builder.withValue(Ingestion.BREAKFAST, mFoodAdapter.getTotalCaloric() );
                break;
            case LUNCH:
                builder.withValue(Ingestion.LUNCH, mFoodAdapter.getTotalCaloric() );
                break;
            case DINNER:
                builder.withValue(Ingestion.DINNER, mFoodAdapter.getTotalCaloric() );
                break;
            case OTHER:
                builder.withValue(Ingestion.ETC, mFoodAdapter.getTotalCaloric() );
                break;
        }
        
        ops.add(builder.build() );
    }
    
    private int queryIngestiveRecord(int foodId) {
        Cursor cursor = null;
        int count = 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(IngestiveRecord.ACCOUNT_ID + " = ? "  + " AND ");
        stringBuilder.append(IngestiveRecord.FOOD_ID + "= ? " + " AND ");
        stringBuilder.append(IngestiveRecord.DINING + "= ? " + " AND ");
        stringBuilder.append(IngestiveRecord.DATE + "= ? " );
        
        try {
            cursor = getContentResolver().query(IngestiveRecord.CONTENT_URI,
                    new String[] { IngestiveRecord.FOOD_ID },
                    stringBuilder.toString(),
                    new String[] { String.valueOf(mAccountId), 
                                   String.valueOf(foodId), 
                                   String.valueOf(mCaloricIntakeType),
                                   String.valueOf(mDate) },
                    null);

            if (cursor != null) {
                count =  cursor.getCount();              
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        
        return count;
    }
    
    private int queryIngestive( ) {
        Cursor cursor = null;
        int count = 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(Ingestion.ACCOUNT_ID + " = ? "  + " AND ");     
        stringBuilder.append(Ingestion.DATE + "= ? " );
        
        try {
            cursor = getContentResolver().query(Ingestion.CONTENT_URI,
                    new String[] { Ingestion.ACCOUNT_ID },
                    stringBuilder.toString(),
                    new String[] { String.valueOf(mAccountId),                                
                                   String.valueOf(mDate) },
                    null);

            if (cursor != null) {
                count =  cursor.getCount();              
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        
        return count;
    }


    
    public void updateTotalCaloric() {
        final String total = String.valueOf( mFoodAdapter.getTotalCaloric() );
        final String title = getString(R.string.total_food_intake, total);

        SpannableString span = new SpannableString(title);
        span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.gray)),
                0,
                5,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new RelativeSizeSpan(1.2f),
                5, span.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTotal.setText(span);
    }
    
    

}
