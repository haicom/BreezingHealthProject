package com.breezing.health.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.breezing.health.R;
import com.breezing.health.entity.CatagoryEntity;
import com.breezing.health.entity.FoodEntity;
import com.breezing.health.providers.Breezing.FoodClassify;
import com.breezing.health.providers.Breezing.HeatIngestion;
import com.breezing.health.providers.Breezing.IngestiveRecord;
import com.breezing.health.ui.activity.CaloricIntakeActivity;
import com.breezing.health.ui.activity.CaloricIntakeActivity.CaloricIntakeType;
import com.breezing.health.util.BreezingQueryViews;

public class FoodAdapter extends BaseAdapter implements OnClickListener {
    private static final String TAG = "FoodAdapter";
    private Context mContext;
    private ArrayList<FoodEntity> mFoods;
    private ArrayList<FoodEntity> mSelectedFoods;
    private FoodCatagoryAdapter mCatagoryAdapter;
    private int mAccountId;
    private int mDate;
    private CaloricIntakeType mCaloricIntakeType;
    
    public FoodAdapter(Context context, FoodCatagoryAdapter catagoryAdapter, 
            int accountId, int date, CaloricIntakeType caloricIntakeType) {
        this.mContext = context;
        this.mCatagoryAdapter = catagoryAdapter;
        mFoods = new ArrayList<FoodEntity>();
        mAccountId = accountId;
        mDate = date;
        mCaloricIntakeType = caloricIntakeType;
        refreshCatagoryItems();
        initSelectedFood();
    }

    private void initSelectedFood() {
        mSelectedFoods = new ArrayList<FoodEntity>();
	}

	@Override
    public int getCount() {
	    
        if (mFoods == null) {
            return 0;
        }
        
        return mFoods.size();
    }

    @Override
    public FoodEntity getItem(int position) {
        if (mFoods == null) {
            return null;
    	}
        return mFoods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    
    public void addItems(ArrayList<FoodEntity> foods) {
    	if (foods != null) {
    		for (FoodEntity food : foods) {
    			foods.add(food);
    		}
    	}
    }
    
    public void clearItems() {
    	if (mFoods != null) {
            mFoods.clear();
    	}
    }
    
    public void refreshCatagoryItems() {
        clearItems();
        getFoodSortFromFoodTypes( mCatagoryAdapter.getCheckedCatagories() );
    }
    
    public void increaseFoodNumber(FoodEntity food) {
    	final int index = getSelectedFoodIndex(food.getFoodName());
    	if (index == -1) {
    		food.increaseSelectedNumber();
            mSelectedFoods.add(food);
    	} else {
            mSelectedFoods.get(index).increaseSelectedNumber();
    	}
    }
    
    public int getSelectedFoodIndex(String foodName) {
        for (FoodEntity food : mSelectedFoods) {
            if (food.getFoodName().equals(foodName)) {
                return mSelectedFoods.indexOf(food);
            }
        }
    
        return -1;
    }
    
    public String getTotalCaloric() {
        int total = 0;
        
        for ( FoodEntity food : mSelectedFoods ) {
            final int intake = food.getSelectedNumber() * food.getCalorie();
            total = total + intake;
        }
        
       
        return String.valueOf(total);
    }
    
    public ArrayList<FoodEntity> getSelectedFoods() {
    	return mSelectedFoods;
    }
    
    public void decreaseFoodNumber(FoodEntity food) {
    	final int index = getSelectedFoodIndex(food.getFoodName());
    	if (index != -1) {
    		mSelectedFoods.get(index).decreaseSelectedNumber();
    		if (mSelectedFoods.get(index).getSelectedNumber() <= 0) {
    			mSelectedFoods.remove(index);
    		}
    	}
    }
    
    public void removeSelectedFood(int position) {
    	mSelectedFoods.remove(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_food_item, null);
            
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.unit = (TextView) convertView.findViewById(R.id.unit);
            holder.increase = (Button) convertView.findViewById(R.id.increase);
            holder.number = (EditText) convertView.findViewById(R.id.number);
            holder.decrease = (Button) convertView.findViewById(R.id.decrease);
            
            convertView.setTag(holder);
            
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        final FoodEntity food = getItem(position);
        Log.d(TAG, "getView food.getImageRes() = " + food.getImageRes() + " position = " + position );
        int iconRes = mContext.getResources().getIdentifier( food.getImageRes(), "drawable", mContext.getPackageName() );
        holder.image.setImageResource(iconRes);
        holder.name.setText( food.getFoodName() );
        holder.unit.setText( food.getNameExpress() );
        
        holder.increase.setTag(food);
        holder.increase.setOnClickListener(this);
        
        holder.decrease.setTag(food);
        holder.decrease.setOnClickListener(this);
        
        final int selectedIndex = getSelectedFoodIndex( food.getFoodName() );
        if (selectedIndex == -1) {
           	holder.number.setText("");
        } else {
        	final int total = mSelectedFoods.get(selectedIndex).getSelectedNumber() * mSelectedFoods.get(selectedIndex).getFoodQuantity();
        	holder.number.setText( String.valueOf(total) );
        }
        
        return convertView;
    }
    
    class ViewHolder {
        TextView name;
        ImageView image;
        TextView unit;
        Button increase;
        EditText number;
        Button decrease;
    }

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.increase) {
			final FoodEntity food = (FoodEntity) v.getTag();
			increaseFoodNumber(food);
			notifyDataSetChanged();
			return ;
		} else if (v.getId() == R.id.decrease) {
			final FoodEntity food = (FoodEntity) v.getTag();
			decreaseFoodNumber(food);
			notifyDataSetChanged();
			return ;
		}
	}
	
	@Override
	public void notifyDataSetChanged() {
	    if (mContext instanceof CaloricIntakeActivity) {
	        ((CaloricIntakeActivity) mContext).updateTotalCaloric();
	    }
	    
	    super.notifyDataSetChanged();
	}

    /**
     * 获取食物种类
     */
    private static final String[] PROJECTION_FOOD_SORT = new String[] {
            HeatIngestion.FOOD_CLASSIFY_ID,
            HeatIngestion.FOOD_ID,
            HeatIngestion.FOOD_NAME,          // 0
            HeatIngestion.NAME_EXPRESS,      // 1
            HeatIngestion.PRIORITY,    // 2
            HeatIngestion.FOOD_QUANTITY ,     //3
            HeatIngestion.CALORIE ,  //4
            HeatIngestion.FOOD_PICTURE
    };


    private static final int FOOD_CLASSIFY_ID_INGESTION_INDEX = 0;
    private static final int FOOD_ID_INGESTION_INDEX = 1;
    private static final int FOOD_NAME_INGESTION_INDEX = 2;
    private static final int NAME_EXPRESS_INGESTION_INDEX = 3;
    private static final int PRIORITY_INGESTION_INDEX = 4;
    private static final int FOOD_QUANTITY_INGESTION_INDEX = 5;
    private static final int CALORIE_INGESTION_INDEX = 6;
    private static final int FOOD_PICTURE_INGESTION_INDEX = 7;
    /**
     * 获得食物种类通过食物的类型
     *
     *
     */
    public void getFoodSortFromFoodTypes(CatagoryEntity catagory) {
        
        Log.d(TAG, " getFoodSortFromFoodTypes foodTypes.size() " + catagory );
        
        if ( catagory == null ) {
            return;
        }
        
        String sortOrder = HeatIngestion.FOOD_ID + " ASC";
        
        
       
        Cursor cursor = null;

//        for (CatagoryEntity catagory: foodTypes) {
//            if (first) {
//                first = false;
//                foodBuilder.append( catagory.getId() );
//
//            } else {
//                foodBuilder.append(',');
//                foodBuilder.append( catagory.getId() );
//            }
//        }
        StringBuilder foodBuilder = new StringBuilder();
        foodBuilder.setLength(0);
        foodBuilder.append( catagory.getId() );
       
        Log.d(TAG, " getFoodSortFromFoodTypes foodBuilder ＝ " + foodBuilder.toString() );
        
        if ( foodBuilder.length() > 0 ) {
            
            String whereClause = null;
            if ( !foodBuilder.toString().equals( String.valueOf(0) ) ) {
                whereClause = HeatIngestion.FOOD_CLASSIFY_ID + " = (" + foodBuilder.toString() + ")";
            }
            
            Log.d(TAG, "getFoodSortFromFoodTypes whereClause = " + whereClause);
            cursor = mContext.getContentResolver().query(
                    HeatIngestion.CONTENT_URI, PROJECTION_FOOD_SORT, whereClause, null, sortOrder);
        }

        if ( cursor == null ) {
            return;
        }

        try {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext() ) {
                int foodType = cursor.getInt(FOOD_CLASSIFY_ID_INGESTION_INDEX);
                String foodName = cursor.getString(FOOD_NAME_INGESTION_INDEX);
                int foodId = cursor.getInt(FOOD_ID_INGESTION_INDEX);;
                String nameExpress = cursor.getString(NAME_EXPRESS_INGESTION_INDEX);                
                int  priority = cursor.getInt(PRIORITY_INGESTION_INDEX);
                int  foodQuantity = cursor.getInt(FOOD_QUANTITY_INGESTION_INDEX);
                int  calorie = cursor.getInt(CALORIE_INGESTION_INDEX);
                String imageRes = cursor.getString(FOOD_PICTURE_INGESTION_INDEX);
                int selectedNumber = queryIngestiveRecord(foodId);
                FoodEntity food = new FoodEntity();
                food.setFoodClassifyId(foodType);
                food.setFoodId(foodId);
                food.setFoodName(foodName);
                food.setNameExpress(nameExpress);
                food.setPriority(priority);
                food.setFoodQuantity(foodQuantity);
                food.setCalorie(calorie);
                food.setImageRes(imageRes);
                food.setSelectedNumber(selectedNumber);
                mFoods.add(food);
            }
        } finally {
            cursor.close();
        }

        return;
    }
    
    private int queryIngestiveRecord(int foodId) {
        Cursor cursor = null;
        int selectedNumber = 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(IngestiveRecord.ACCOUNT_ID + " = ? "  + " AND ");
        stringBuilder.append(IngestiveRecord.FOOD_ID + "= ? " + " AND ");
        stringBuilder.append(IngestiveRecord.DINING + "= ? " + " AND ");
        stringBuilder.append(IngestiveRecord.DATE + "= ? " );
        
        try {
            cursor = mContext.getContentResolver().query(IngestiveRecord.CONTENT_URI,
                    new String[] { IngestiveRecord.FOOD_QUANTITY },
                    stringBuilder.toString(),
                    new String[] { String.valueOf(mAccountId), 
                                   String.valueOf(foodId), 
                                   String.valueOf(mCaloricIntakeType),
                                   String.valueOf(mDate) },
                    null);

            if (cursor != null) {
                if ( cursor.getCount() > 0 ) {
                    selectedNumber = cursor.getInt(0);
                }
              
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        
        return selectedNumber;
    }

}
