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
import com.breezing.health.entity.MenuItem;

public class LeftMenuAdapter extends BaseAdapter {
    
    private ArrayList<MenuItem> menuItems;
    private Context context;
    
    public LeftMenuAdapter(Context context, ArrayList<MenuItem> menuItems) {
        this.context = context;
        this.menuItems = menuItems;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return menuItems.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return menuItems.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (arg1 == null) {
            arg1 = LayoutInflater.from(context).inflate(R.layout.left_menu_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) arg1.findViewById(R.id.name);
            holder.icon = (ImageView) arg1.findViewById(R.id.icon);
            arg1.setTag(holder);
        } else {
            holder = (ViewHolder) arg1.getTag();
        }
        
        MenuItem item = (MenuItem) getItem(arg0);
        holder.name.setText(item.getName());
        holder.icon.setImageResource(item.getIconRes());
        
        return arg1;
    }
    
    class ViewHolder {
        TextView name;
        ImageView icon;
    }

}
