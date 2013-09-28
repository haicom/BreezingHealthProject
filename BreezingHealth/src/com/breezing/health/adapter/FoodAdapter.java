package com.breezing.health.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
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
import com.breezing.health.entity.FoodEntity;
import com.breezing.health.ui.activity.CaloricIntakeActivity;
import com.breezing.health.util.BreezingQueryViews;

public class FoodAdapter extends BaseAdapter implements OnClickListener {

    private Context context;
    private ArrayList<FoodEntity> foods;
    private ArrayList<FoodEntity> selectedFoods;
    private FoodCatagoryAdapter catagoryAdapter;
    
    public FoodAdapter(Context context, FoodCatagoryAdapter catagoryAdapter) {
        this.context = context;
        this.catagoryAdapter = catagoryAdapter;
        refreshCatagoryItems();
        initSelectedFood();
    }

    private void initSelectedFood() {
    	selectedFoods = new ArrayList<FoodEntity>();
	}

	@Override
    public int getCount() {
    	if (foods == null) {
    		return 0;
    	}
        return foods.size();
    }

    @Override
    public FoodEntity getItem(int position) {
    	if (foods == null) {
    		return null;
    	}
        return foods.get(position);
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
    	if (foods != null) {
    		foods.clear();
    	}
    }
    
    public void refreshCatagoryItems() {
    	clearItems();
    	BreezingQueryViews query = new BreezingQueryViews(context);
    	final int size = catagoryAdapter.getCheckedCatagoryCount();
    	if (size == 0) {
    		foods = query.getFoodSortFromFoodTypes(catagoryAdapter.getAllCatagories());
    	} else {
    		foods = query.getFoodSortFromFoodTypes(catagoryAdapter.getCheckedCatagories());
    	}
    }
    
    public void increaseFoodNumber(FoodEntity food) {
    	final int index = getSelectedFoodIndex(food.getFoodName());
    	if (index == -1) {
    		food.increaseSelectedNumber();
    		selectedFoods.add(food);
    	} else {
    		selectedFoods.get(index).increaseSelectedNumber();
    	}
    }
    
    public int getSelectedFoodIndex(String foodName) {
    	for (FoodEntity food : selectedFoods) {
    		if (food.getFoodName().equals(foodName)) {
    			return selectedFoods.indexOf(food);
    		}
    	}
    	
    	return -1;
    }
    
    public String getTotalCaloric() {
        float total = 0;
        for (FoodEntity food : selectedFoods) {
            final int intake = food.getSelectedNumber() * food.getFoodQuantity();
            total =+ intake;
        }
        
        DecimalFormat fnum = new DecimalFormat("##0.0"); 
        return fnum.format(total);
    }
    
    public ArrayList<FoodEntity> getSelectedFoods() {
    	return selectedFoods;
    }
    
    public void decreaseFoodNumber(FoodEntity food) {
    	final int index = getSelectedFoodIndex(food.getFoodName());
    	if (index != -1) {
    		selectedFoods.get(index).decreaseSelectedNumber();
    		if (selectedFoods.get(index).getSelectedNumber() <= 0) {
    			selectedFoods.remove(index);
    		}
    	}
    }
    
    public void removeSelectedFood(int position) {
    	selectedFoods.remove(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_food_item, null);
            
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
        holder.name.setText(food.getFoodName());
        holder.unit.setText(food.getCalorie() + context.getString(R.string.kilojoule) 
        		+ "/" + food.getFoodQuantity() + food.getNameExpress());
        
        holder.increase.setTag(food);
        holder.increase.setOnClickListener(this);
        
        holder.decrease.setTag(food);
        holder.decrease.setOnClickListener(this);
        
        final int selectedIndex = getSelectedFoodIndex(food.getFoodName());
        if (selectedIndex == -1) {
        	holder.number.setText("");
        } else {
        	final int total = selectedFoods.get(selectedIndex).getSelectedNumber() * selectedFoods.get(selectedIndex).getFoodQuantity();
        	holder.number.setText(String.valueOf(total));
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
	    if (context instanceof CaloricIntakeActivity) {
	        ((CaloricIntakeActivity) context).updateTotalCaloric();
	    }
	    
	    super.notifyDataSetChanged();
	}

}
