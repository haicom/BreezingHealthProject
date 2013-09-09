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

    private final Context context;
    private final ArrayList<RecordFunctionEntity> functions;

    public AddCaloricRecordAdapter(Context context, ArrayList<RecordFunctionEntity> functions) {
        this.context = context;
        this.functions = functions;
    }

    @Override
    public int getCount() {
        return functions.size();
    }

    @Override
    public Object getItem(int position) {
        return functions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View  convertView, ViewGroup  parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.gridview_caloric_item, null);
            viewHolder = new ViewHolder();
            viewHolder.seekBar = (HoloCircleSeekBar) convertView.findViewById(R.id.seekBar);
            viewHolder.title = (Button) convertView.findViewById(R.id.title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final RecordFunctionEntity fun = (RecordFunctionEntity) getItem(position);
        viewHolder.title.setText(fun.getTitleRes());

        return convertView;
    }

    class ViewHolder {
        HoloCircleSeekBar seekBar;
        Button title;
    }



}
