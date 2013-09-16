package com.breezing.health.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.breezing.health.R;
import com.breezing.health.entity.ExerciseRecordEntity;

public class ExerciseRecordAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ExerciseRecordEntity> records;
    private int titleColor;
    private int contentColor;
    
    public ExerciseRecordAdapter(Context context) {
        this.context = context;
        this.titleColor = context.getResources().getColor(R.color.black);
        this.contentColor = context.getResources().getColor(R.color.gray);
        initRecords();
    }
    
    private void initRecords() {
        records = new ArrayList<ExerciseRecordEntity>();
        records.add(new ExerciseRecordEntity());
        records.add(new ExerciseRecordEntity());
        records.add(new ExerciseRecordEntity());
        records.add(new ExerciseRecordEntity());
        records.add(new ExerciseRecordEntity());
        records.add(new ExerciseRecordEntity());
        records.add(new ExerciseRecordEntity());
        records.add(new ExerciseRecordEntity());
        records.add(new ExerciseRecordEntity());
    }

    @Override
    public int getCount() {
        return records.size() + 1;
    }

    @Override
    public ExerciseRecordEntity getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_exercise_record_item, null);
            
            holder = new ViewHolder();
            holder.type = (TextView) convertView.findViewById(R.id.type);
            holder.des = (TextView) convertView.findViewById(R.id.des);
            holder.caloric = (TextView) convertView.findViewById(R.id.caloric);
            
            convertView.setTag(holder);
            
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        if (position == 0) {
            holder.type.setTextColor(titleColor);
            holder.des.setTextColor(titleColor);
            holder.caloric.setTextColor(titleColor);
            
            holder.type.setText(R.string.type);
            holder.des.setText(R.string.detail);
            holder.caloric.setText(R.string.consume);
            
        } else {
            
            final ExerciseRecordEntity record = getItem(position - 1);
            
            holder.type.setTextColor(contentColor);
            holder.des.setTextColor(contentColor);
            holder.caloric.setTextColor(contentColor);
            
            holder.type.setText("");
            holder.des.setText("");
            holder.caloric.setText("");
        }
        
        return convertView;
    }
    
    class ViewHolder {
        TextView type;
        TextView des;
        TextView caloric;
    }

}
