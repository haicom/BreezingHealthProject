package com.breezing.health.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.breezing.health.R;
import com.breezing.health.entity.FoodEntity;

public class FoodAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<FoodEntity> foods;
    
    public FoodAdapter(Context context) {
        this.context = context;
        initFoods();
    }
    
    private void initFoods() {
        foods = new ArrayList<FoodEntity>();
        foods.add(new FoodEntity());
        foods.add(new FoodEntity());
        foods.add(new FoodEntity());
        foods.add(new FoodEntity());
        foods.add(new FoodEntity());
        foods.add(new FoodEntity());
        foods.add(new FoodEntity());
        foods.add(new FoodEntity());
        foods.add(new FoodEntity());
        foods.add(new FoodEntity());
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

}
