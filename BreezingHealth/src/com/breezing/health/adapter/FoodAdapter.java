package com.breezing.health.adapter;

import java.util.ArrayList;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.breezing.health.R;
import com.breezing.health.entity.CatagoryEntity;
import com.breezing.health.entity.FoodEntity;
import com.breezing.health.providers.Breezing.HeatConsumptionRecord;
import com.breezing.health.providers.Breezing.HeatIngestion;
import com.breezing.health.providers.Breezing.IngestiveRecord;
import com.breezing.health.ui.activity.CaloricIntakeActivity;
import com.breezing.health.ui.activity.CaloricIntakeActivity.CaloricIntakeType;
import com.breezing.health.util.OnDataSetChangedListener;

public class FoodAdapter extends CursorAdapter implements OnClickListener {
	
    private static final String TAG = "FoodAdapter";
    private CaloricIntakeActivity mContext;
    private ArrayList<FoodEntity> mSelectedFoods;
    private FoodCatagoryAdapter mCatagoryAdapter;
    private int mAccountId;
    private int mDate;
    private CaloricIntakeType mCaloricIntakeType;
    
    private BackgroundQueryHandler mBackgroundQueryHandler;
    private String checkedCatagoryName;
    
    public FoodAdapter(CaloricIntakeActivity context, FoodCatagoryAdapter catagoryAdapter, 
            int accountId, int date, CaloricIntakeType caloricIntakeType, float unifyUnit) {
    	super(context, null, FLAG_REGISTER_CONTENT_OBSERVER); 
        this.mContext = context;
        mBackgroundQueryHandler = new BackgroundQueryHandler(this.mContext.getContentResolver());
        this.mCatagoryAdapter = catagoryAdapter;
        mSelectedFoods = new ArrayList<FoodEntity>();
        mAccountId = accountId;
        mDate = date;
        mCaloricIntakeType = caloricIntakeType;
        queryIngestiveRecord();
        refreshCatagoryItems(null);
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
    
    public void refreshCatagoryItems(String name) {
    	checkedCatagoryName = name;
    	getFoodSortFromFoodTypes(mCatagoryAdapter.getCheckedCatagories(), checkedCatagoryName);
    }
    
    public void refreshCatagoryItems() {
    	getFoodSortFromFoodTypes(mCatagoryAdapter.getCheckedCatagories(), checkedCatagoryName);
    }
    
    public void increaseFoodNumber(FoodEntity food) {
        final int index = getSelectedFoodIndex( food.getFoodId() );
        if (index == -1) {
            food.increaseSelectedNumber();
            mSelectedFoods.add(food);
        } else {
            mSelectedFoods.get(index).increaseSelectedNumber();
        }
    }
    
    public int getSelectedFoodIndex( int foodId ) {
        for (FoodEntity food : mSelectedFoods) {
            if ( food.getFoodId() == foodId ) {
                return mSelectedFoods.indexOf(food);
            }
        }
    
        return -1;
    }
    
    public float getTotalCaloric() {
        int total = 0;
        
        for ( FoodEntity food : mSelectedFoods ) {
            final int intake = food.getSelectedNumber() * food.getCalorie();
            total = total + intake;
        }
        
       
        return total;
    }
    
    public ArrayList<FoodEntity> getSelectedFoods() {
    	return mSelectedFoods;
    }
    
    public void decreaseFoodNumber(FoodEntity food) {
    	final int index = getSelectedFoodIndex( food.getFoodId() );
    	if (index != -1) {
    		mSelectedFoods.get(index).decreaseSelectedNumber();
    		if (mSelectedFoods.get(index).getSelectedNumber() <= 0) {
    		    removeSelectedFood(index);
    		}
    	}
    }
    
    public void removeSelectedFood(int position) {
        mContext.deleteIngestiveRecord(mSelectedFoods.get(position));
    	mSelectedFoods.remove(position);
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
			final int index = Integer.parseInt(String.valueOf(v.getTag()));
			increaseFoodNumber(getFoodItem(index));
			notifyDataSetChanged();
			return ;
		} else if (v.getId() == R.id.decrease) {
			final int index = Integer.parseInt(String.valueOf(v.getTag()));
			decreaseFoodNumber(getFoodItem(index));
			notifyDataSetChanged();
			return ;
		}
	}
	
	private FoodEntity getFoodItem(int position) {
		Cursor cursor = (Cursor) getItem(position);
		int foodType = cursor.getInt(FOOD_CLASSIFY_ID_INGESTION_INDEX);
        String foodName = cursor.getString(FOOD_NAME_INGESTION_INDEX);
        int foodId = cursor.getInt(FOOD_ID_INGESTION_INDEX);;
        String nameExpress = cursor.getString(NAME_EXPRESS_INGESTION_INDEX);                
        int  priority = cursor.getInt(PRIORITY_INGESTION_INDEX);
        int  foodQuantity = cursor.getInt(FOOD_QUANTITY_INGESTION_INDEX);
        int  calorie = cursor.getInt(CALORIE_INGESTION_INDEX);
        String imageRes = cursor.getString(FOOD_PICTURE_INGESTION_INDEX);
       
        FoodEntity food = new FoodEntity();
        food.setFoodClassifyId(foodType);
        food.setFoodId(foodId);
        food.setFoodName(foodName);
        food.setNameExpress(nameExpress);
        food.setPriority(priority);
        food.setFoodQuantity(foodQuantity);
        food.setCalorie(calorie);
        food.setImageRes(imageRes);
        
        return food;
	}

    /**
     * 获取食物种类
     */
    private static final String[] PROJECTION_FOOD_SORT = new String[] {
    		HeatIngestion._ID,
            HeatIngestion.FOOD_CLASSIFY_ID,
            HeatIngestion.FOOD_ID,
            HeatIngestion.FOOD_NAME,          // 0
            HeatIngestion.NAME_EXPRESS,      // 1
            HeatIngestion.PRIORITY,    // 2
            HeatIngestion.FOOD_QUANTITY ,     //3
            HeatIngestion.CALORIE ,  //4
            HeatIngestion.FOOD_PICTURE
    };


    private static final int FOOD_CLASSIFY_ID_INGESTION_INDEX = 1;
    private static final int FOOD_ID_INGESTION_INDEX = 2;
    private static final int FOOD_NAME_INGESTION_INDEX = 3;
    private static final int NAME_EXPRESS_INGESTION_INDEX = 4;
    private static final int PRIORITY_INGESTION_INDEX = 5;
    private static final int FOOD_QUANTITY_INGESTION_INDEX = 6;
    private static final int CALORIE_INGESTION_INDEX = 7;
    private static final int FOOD_PICTURE_INGESTION_INDEX = 8;
    /**
     * 获得食物种类通过食物的类型
     *
     *
     */
    private void getFoodSortFromFoodTypes(CatagoryEntity catagory, String name) {
        
        Log.d(TAG, " getFoodSortFromFoodTypes foodTypes.size() " + catagory + " name = " + name);
        
        if ( catagory == null ) {
            return ;
        }
        
        String sortOrder = HeatIngestion.FOOD_ID + " ASC";
        
        
       
        StringBuilder foodBuilder = new StringBuilder();
        foodBuilder.setLength(0);
        foodBuilder.append( catagory.getId() );
        
        StringBuilder nameBuilder = null;
        if (name != null && name.length() > 0) {
            nameBuilder = new StringBuilder();
            nameBuilder.setLength(0);
            nameBuilder.append(HeatIngestion.FOOD_NAME );
            nameBuilder.append(" GLOB '" + name + "*' ");            
        }
        
        Log.d(TAG, " getFoodSortFromFoodTypes foodBuilder ＝ " + foodBuilder.toString() );
        
        if ( foodBuilder.length() > 0 ) {
            
            String whereClause = null;
            if ( !foodBuilder.toString().equals( String.valueOf(0) ) ) {
                whereClause = HeatIngestion.FOOD_CLASSIFY_ID + " = " + foodBuilder.toString() ;
            }
            
            if ( nameBuilder != null && nameBuilder.length() > 0 ) {
                if (whereClause == null) {
                    whereClause = nameBuilder.toString();
                } else {
                    whereClause = whereClause + " AND " + nameBuilder.toString();
                }
                
            }
            
            Log.d(TAG, "getFoodSortFromFoodTypes whereClause = " + whereClause);
            long threadId = 0;


            // Cancel any pending queries
            mBackgroundQueryHandler.cancelOperation(MESSAGE_LIST_QUERY_TOKEN);
            try {
                // Kick off the new query
                mBackgroundQueryHandler.startQuery(
                        MESSAGE_LIST_QUERY_TOKEN,
                        threadId /* cookie */,
                        HeatIngestion.CONTENT_URI,
                        PROJECTION_FOOD_SORT,
                        whereClause,
                        null,
                        sortOrder);
            } catch (SQLiteException e) {

            }
        }
    }
    
    private final OnDataSetChangedListener
    	mOnDataSetChangedListener = new OnDataSetChangedListener() {
        public void onDataSetChanged(CursorAdapter adapter) {

        }

        public void onContentChanged(CursorAdapter adapter) {
            Log.d(TAG, "MessageListAdapter.OnDataSetChangedListener.onContentChanged");
            getFoodSortFromFoodTypes( mCatagoryAdapter.getCheckedCatagories(), checkedCatagoryName);
       }
    };
    
    private final class BackgroundQueryHandler extends AsyncQueryHandler {
        public BackgroundQueryHandler(ContentResolver contentResolver) {
            super(contentResolver);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            switch(token) {
                case MESSAGE_LIST_QUERY_TOKEN:
                    // check consistency between the query result and 'mConversation'
                    long tid = (Long) cookie;
                    changeCursor(cursor);
                    return;
            }
        }
    }
    
    private static final int MESSAGE_LIST_QUERY_TOKEN = 9527;
    
    private static final String[] PROJECTION_INGESTIVE_RECORD_SORT = new String[] {
        IngestiveRecord.FOOD_ID,
        IngestiveRecord.FOOD_QUANTITY
    };


    private static final int INGESTIVE_RECORD_FOOD_ID_INDEX = 0;
    private static final int INGESTIVE_RECORD_FOOD_QUANTITY_INDEX = 1;
   
    
    public void queryIngestiveRecord( ) {
        Cursor cursor = null;        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(IngestiveRecord.ACCOUNT_ID + " = ? "  + " AND ");     
        stringBuilder.append(IngestiveRecord.DINING + "= ? " + " AND ");
        stringBuilder.append(IngestiveRecord.DATE + "= ? " );
        
       
        cursor = mContext.getContentResolver().query(IngestiveRecord.CONTENT_URI,
                 PROJECTION_INGESTIVE_RECORD_SORT,
                 stringBuilder.toString(),
                 new String[] { String.valueOf(mAccountId),                                
                                   String.valueOf( mCaloricIntakeType.ordinal() ),
                                   String.valueOf(mDate) },
                 null);

          
        try {
             cursor.moveToPosition(-1);
             while (cursor.moveToNext() ) {
                 final int foodId = cursor.getInt(INGESTIVE_RECORD_FOOD_ID_INDEX);
                 final int selectedNumber = cursor.getInt(INGESTIVE_RECORD_FOOD_QUANTITY_INDEX);  
                 
                 FoodEntity foodEntity = queryFoodById(foodId);
                 if (foodEntity != null) {
                	 foodEntity.setSelectedNumber(selectedNumber);
                     mSelectedFoods.add(foodEntity);
                 }
             }            
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }       
        
        return;
    }

	@Override
	public void bindView(View convertView, Context context, Cursor cursor) {
      ViewHolder holder = (ViewHolder) convertView.getTag();
      
      final String foodName = cursor.getString(FOOD_NAME_INGESTION_INDEX);
      final int foodId = cursor.getInt(FOOD_ID_INGESTION_INDEX);;
      final String nameExpress = cursor.getString(NAME_EXPRESS_INGESTION_INDEX);                
      final String imageRes = cursor.getString(FOOD_PICTURE_INGESTION_INDEX);
      
      int iconRes = mContext.getResources().getIdentifier( imageRes, "drawable", mContext.getPackageName() );
      holder.image.setImageResource(iconRes);
      holder.name.setText( foodName );
      holder.unit.setText( nameExpress );
      
      holder.increase.setTag(String.valueOf(cursor.getPosition()));
      holder.increase.setOnClickListener(this);
      
      holder.decrease.setTag(String.valueOf(cursor.getPosition()));
      holder.decrease.setOnClickListener(this);
      
      int total = 0;
      for (FoodEntity selectFoodEntity: mSelectedFoods) {
           if (selectFoodEntity.getFoodId() ==  foodId ) {
               total = selectFoodEntity.getSelectedNumber() * selectFoodEntity.getFoodQuantity();
               break;
           }
      }
      
      if ( total == 0 ) {
          holder.number.setText("");
      } else {           
          holder.number.setText( String.valueOf(total) );
      }
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		View convertView = LayoutInflater.from(context).inflate(R.layout.list_food_item, null);
        
        holder.image = (ImageView) convertView.findViewById(R.id.image);
        holder.name = (TextView) convertView.findViewById(R.id.name);
        holder.unit = (TextView) convertView.findViewById(R.id.unit);
        holder.increase = (Button) convertView.findViewById(R.id.increase);
        holder.number = (EditText) convertView.findViewById(R.id.number);
        holder.decrease = (Button) convertView.findViewById(R.id.decrease);
        
        convertView.setTag(holder);
		return convertView;
	}
    
	@Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        Log.v(TAG, "notifyDataSetChanged MessageListAdapter.notifyDataSetChanged().");
        if (mContext instanceof CaloricIntakeActivity) {
	        ((CaloricIntakeActivity) mContext).updateTotalCaloric();
	    }
        if (mOnDataSetChangedListener != null) {
            mOnDataSetChangedListener.onDataSetChanged(this);
        }
    }

    @Override
    protected void onContentChanged() {
        Log.d(TAG, "onContentChanged");
        if (getCursor() != null && !getCursor().isClosed()) {
            if (mOnDataSetChangedListener != null) {
                mOnDataSetChangedListener.onContentChanged(this);
            }
        }
    }
    
    private FoodEntity queryFoodById(int id) {
    	Cursor cursor = null;        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        stringBuilder.append(HeatIngestion.FOOD_ID + "= ? " );
        
       
        cursor = mContext.getContentResolver().query(HeatIngestion.CONTENT_URI,
        		PROJECTION_FOOD_SORT,
                 stringBuilder.toString(),
                 new String[] { String.valueOf(id)},
                 null);

        FoodEntity food = null;
          
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
                
                 food = new FoodEntity();
                 food.setFoodClassifyId(foodType);
                 food.setFoodId(foodId);
                 food.setFoodName(foodName);
                 food.setNameExpress(nameExpress);
                 food.setPriority(priority);
                 food.setFoodQuantity(foodQuantity);
                 food.setCalorie(calorie);
                 food.setImageRes(imageRes);
             }            
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }       
        
        return food;
    }

}
