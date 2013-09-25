package com.breezing.health.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.breezing.health.R;
import com.breezing.health.entity.LeftMenuFunction;

public class LeftMenuAdapter extends BaseAdapter {
    
    private LeftMenuFunction[] menuItems;
    private Context context;
    
    public LeftMenuAdapter(Context context) {
        this.context = context;
        this.menuItems = LeftMenuFunction.values();
    }

    @Override
    public int getCount() {
        return menuItems.length;
    }

    @Override
    public LeftMenuFunction getItem(int position) {
        return menuItems[position];
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup arg2) {
        ViewHolder holder;
        if (contentView == null) {
            contentView = LayoutInflater.from(context).inflate(R.layout.left_menu_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) contentView.findViewById(R.id.name);
            holder.icon = (ImageView) contentView.findViewById(R.id.icon);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        
        LeftMenuFunction item = (LeftMenuFunction) getItem(position);
        holder.name.setText(item.titleRes);
        holder.icon.setImageResource(item.iconRes);
        
        return contentView;
    }
    
    class ViewHolder {
        TextView name;
        ImageView icon;
    }

}
