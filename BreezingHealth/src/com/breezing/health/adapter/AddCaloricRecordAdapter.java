package com.breezing.health.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.breezing.health.R;
import com.breezing.health.entity.RecordFunctionEntity;
import com.breezing.health.widget.HoloCircleSeekBar;

public class AddCaloricRecordAdapter extends BaseAdapter {
    
    private Context context;
    private ArrayList<RecordFunctionEntity> functions;
    
    public AddCaloricRecordAdapter(Context context, ArrayList<RecordFunctionEntity> functions) {
        this.context = context;
        this.functions = functions;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return functions.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return functions.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder = null;
        if (arg1 == null) {
            arg1 = LayoutInflater.from(context).inflate(R.layout.gridview_caloric_item, null);
            viewHolder = new ViewHolder();
            viewHolder.seekBar = (HoloCircleSeekBar) arg1.findViewById(R.id.seekBar);
            viewHolder.title = (Button) arg1.findViewById(R.id.title);
            arg1.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) arg1.getTag();
        }
        
        final RecordFunctionEntity fun = (RecordFunctionEntity) getItem(arg0);
        viewHolder.title.setText(fun.getTitleRes());
        
        return arg1;
    }
    
    class ViewHolder {
        HoloCircleSeekBar seekBar;
        Button title;
    }

    
    
}
