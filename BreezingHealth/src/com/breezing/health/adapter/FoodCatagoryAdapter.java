package com.breezing.health.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.breezing.health.R;
import com.breezing.health.entity.CatagoryEntity;
import com.breezing.health.providers.Breezing.EnergyCost;
import com.breezing.health.providers.Breezing.FoodClassify;
import com.breezing.health.util.BLog;
import com.breezing.health.util.BreezingQueryViews;

public class FoodCatagoryAdapter extends BaseAdapter {
    public static final String TAG = "FoodCatagoryAdapter";
    public Context mContext;
    public ArrayList<CatagoryEntity> mCatagories;
    
    public FoodCatagoryAdapter(Context context) {
        this.mContext = context;
        mCatagories = new ArrayList<CatagoryEntity>();
        initFoodCatagories();
    }
    
    private void initFoodCatagories() {    
        queryFoodTypes();
    }
    
    @Override
    public int getCount() {
        return mCatagories.size();
    }

    @Override
    public CatagoryEntity getItem(int position) {
        return mCatagories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    
    public void addItem( CatagoryEntity catagory ) {
        if (catagory != null) {
            this.mCatagories.add(catagory);
        }
    }
    
    public void reset() {
        this.mCatagories.clear();
    }
    
    public CatagoryEntity getCheckedCatagories() {
        CatagoryEntity catagory = null;
        for (CatagoryEntity catagoryEntity : mCatagories) {
            if ( catagoryEntity.isChecked() ) {
                catagory = catagoryEntity;
                break;
            }
        }
        
        return catagory;
    }
    
    public int getCheckedCatagoryCount() {
        int size = 0;
        
        for ( CatagoryEntity catagory : mCatagories ) {
            if ( catagory.isChecked() ) {
                size++;
            }
        }
        
        return size;
    }
    
    public void toggle(int position) {
        
        if ( mCatagories != null && position < mCatagories.size() ) {
            
            boolean checked = mCatagories.get(position).isChecked();
            
            if ( checked == false ) {
                
                for (CatagoryEntity catagory : mCatagories) {
                    
                    if ( catagory.isChecked() ) {
                        catagory.setChecked(false);
                        break;
                    }
                    
                }
                
                mCatagories.get(position).setChecked(true);
            } 
           
        }
    }
    
    public ArrayList<CatagoryEntity> getAllCatagories() {
    	return mCatagories;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder holder;
        if (contentView == null) {
            contentView = LayoutInflater.from(mContext).inflate(R.layout.gridview_food_catagory_item, null);
            
            holder = new ViewHolder();
            holder.icon = (ImageView) contentView.findViewById(R.id.icon);
            holder.name = (TextView) contentView.findViewById(R.id.name);
            
            contentView.setTag(holder);
            
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        
        final CatagoryEntity catagory = getItem(position);
        
        int iconRes = mContext.getResources().getIdentifier(catagory.getIconRes(), "drawable", mContext.getPackageName() );
        holder.icon.setImageResource(iconRes);
        holder.name.setText( catagory.getName() );
        holder.icon.setSelected( catagory.isChecked() );
        holder.name.setSelected( catagory.isChecked() );
        
        return contentView;
    }
    
    class ViewHolder {
        TextView name;
        ImageView icon;
    }
    
    
    private static final String[] PROJECTION_FOOD_CLASSIFY = new String[] {
        FoodClassify.FOOD_CLASSIFY_ID,               // 0
        FoodClassify.FOOD_TYPE,
        FoodClassify.SELECT_PICTURE
    };

    private static final int FOOD_CLASSIFY_ID_INDEX = 0;
    private static final int FOOD_TYPE_INDEX = 1;
    private static final int FOOD_PICTURE_INDEX = 2;
    
    private void queryFoodTypes() {
        String sortOrder = FoodClassify.FOOD_CLASSIFY_ID + " ASC";
        
        Cursor cursor  = mContext.getContentResolver().query(
                FoodClassify.CONTENT_URI,
                PROJECTION_FOOD_CLASSIFY, null, null, sortOrder);

        if (cursor == null) {
            BLog.d(TAG, " queryFoodTypes cursor = " + cursor);
        }

        try {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext() ) {
                CatagoryEntity catagory = new CatagoryEntity(
                        cursor.getInt(FOOD_CLASSIFY_ID_INDEX), 
                        cursor.getString(FOOD_TYPE_INDEX), 
                        cursor.getString(FOOD_PICTURE_INDEX) );
                mCatagories.add(catagory);
            }
        } finally {
            cursor.close();
        }
    }
}
