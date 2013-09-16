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

public class FoodCatagoryAdapter extends BaseAdapter {

    public Context context;
    public ArrayList<CatagoryEntity> catagories;
    
    public FoodCatagoryAdapter(Context context) {
        this.context = context;
        initFoodCatagories();
    }
    
    private void initFoodCatagories() {
        this.catagories = new ArrayList<CatagoryEntity>();
        this.catagories.add(new CatagoryEntity("all catagory", R.drawable.ic_launcher));
        this.catagories.add(new CatagoryEntity("catagory1", R.drawable.ic_launcher));
        this.catagories.add(new CatagoryEntity("catagory2", R.drawable.ic_launcher));
        this.catagories.add(new CatagoryEntity("catagory3", R.drawable.ic_launcher));
        this.catagories.add(new CatagoryEntity("catagory4", R.drawable.ic_launcher));
        this.catagories.add(new CatagoryEntity("catagory5", R.drawable.ic_launcher));
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
        
        return contentView;
    }
    
    class ViewHolder {
        TextView name;
        ImageView icon;
    }
    

}
