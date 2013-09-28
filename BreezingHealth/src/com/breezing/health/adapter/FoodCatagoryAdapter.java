package com.breezing.health.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.breezing.health.R;
import com.breezing.health.entity.CatagoryEntity;
import com.breezing.health.util.BreezingQueryViews;

public class FoodCatagoryAdapter extends BaseAdapter {

    public Context context;
    public ArrayList<CatagoryEntity> catagories;
    
    public FoodCatagoryAdapter(Context context) {
        this.context = context;
        initFoodCatagories();
    }
    
    private void initFoodCatagories() {
    	BreezingQueryViews query = new BreezingQueryViews(context);
        this.catagories = query.queryFoodTypes();
    }
    
    @Override
    public int getCount() {
        return catagories.size();
    }

    @Override
    public CatagoryEntity getItem(int position) {
        return catagories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    
    public void addItem(CatagoryEntity catagory) {
        if (catagory != null) {
            this.catagories.add(catagory);
        }
    }
    
    public void reset() {
        this.catagories.clear();
    }
    
    public ArrayList<CatagoryEntity> getCheckedCatagories() {
    	ArrayList<CatagoryEntity> checkedTypes = new ArrayList<CatagoryEntity>();
    	for (CatagoryEntity catagory : catagories) {
    		if (catagory.isChecked()) {
    			checkedTypes.add(catagory);
    		}
    	}
    	return checkedTypes;
    }
    
    public int getCheckedCatagoryCount() {
    	int size = 0;
    	for (CatagoryEntity catagory : catagories) {
    		if (catagory.isChecked()) {
    			size++;
    		}
    	}
    	return size;
    }
    
    public void toggle(int position) {
    	if (catagories != null && position < catagories.size()) {
    		catagories.get(position).setChecked(!catagories.get(position).isChecked());
    	}
    }
    
    public ArrayList<CatagoryEntity> getAllCatagories() {
    	return catagories;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder holder;
        if (contentView == null) {
            contentView = LayoutInflater.from(context).inflate(R.layout.gridview_food_catagory_item, null);
            
            holder = new ViewHolder();
            holder.icon = (ImageView) contentView.findViewById(R.id.icon);
            holder.name = (TextView) contentView.findViewById(R.id.name);
            
            contentView.setTag(holder);
            
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        
        final CatagoryEntity catagory = getItem(position);
        
        holder.icon.setImageResource(catagory.getIconRes());
        holder.name.setText(catagory.getName());
        holder.icon.setSelected(catagory.isChecked());
        holder.name.setSelected(catagory.isChecked());
        
        return contentView;
    }
    
    class ViewHolder {
        TextView name;
        ImageView icon;
    }
    

}
