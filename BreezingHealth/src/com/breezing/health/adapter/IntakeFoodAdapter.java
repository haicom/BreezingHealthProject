package com.breezing.health.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.breezing.health.R;
import com.breezing.health.entity.AccountEntity;
import com.breezing.health.entity.FoodEntity;
import com.breezing.health.util.BreezingQueryViews;
import com.breezing.health.util.LocalSharedPrefsUtil;

public class IntakeFoodAdapter extends BaseAdapter {

	private Context context;
    private ArrayList<FoodEntity> foods;
    private AccountEntity mAccount;
    private float mUnifyUnit;
    private int mAccountId;
    
    public IntakeFoodAdapter(Context context) {
        this.context = context;
        foods = new ArrayList<FoodEntity>();
        mAccountId = LocalSharedPrefsUtil.getSharedPrefsValueInt(context,
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
        BreezingQueryViews query = new BreezingQueryViews(context);
        mAccount = query.queryBaseInfoViews(mAccountId);
        mUnifyUnit = query.queryUnitObtainData( context.getString(R.string.caloric_type), mAccount.getCaloricUnit() );
    }

    @Override
    public int getCount() {
        return foods.size();
    }

    @Override
    public FoodEntity getItem(int position) {
        return foods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    
    public void addItems(ArrayList<FoodEntity> foods) {
    	if (foods != null) {
    		for (FoodEntity food : foods) {
    			this.foods.add(food);
    		}
    	}
    }
    
    public void removeItem(int position) {
    	if (position < foods.size()) {
    		foods.remove(position);
    	}
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_intake_food_item, null);
            
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.unit = (TextView) convertView.findViewById(R.id.unit);
            holder.total = (TextView) convertView.findViewById(R.id.total);
            
            convertView.setTag(holder);
            
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DecimalFormat df = new DecimalFormat("#.0");
        final FoodEntity food = getItem(position);
        holder.name.setText(food.getFoodName());
        holder.unit.setText( df.format(food.getCalorie() * mUnifyUnit) + mAccount.getCaloricUnit()
            + "/" + food.getFoodQuantity() + food.getNameExpress() );
        final float total = food.getSelectedNumber() * food.getCalorie() * mUnifyUnit;
        holder.total.setText( df.format(total) + mAccount.getCaloricUnit() );        
        return convertView;
    }
    
    class ViewHolder {
        TextView name;
        TextView unit;
        TextView total;
    }
	
}
