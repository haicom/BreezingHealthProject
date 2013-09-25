package com.breezing.health.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.breezing.health.R;
import com.breezing.health.entity.RecordFunctionEntity;
import com.breezing.health.util.BLog;
import com.breezing.health.widget.HoloCircleSeekBar;

public class AddCaloricRecordAdapter extends BaseAdapter {
    private static final String TAG = "AddCaloricRecordAdapter";

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

        if ( convertView == null ) {
            convertView = LayoutInflater.from(context).inflate(R.layout.gridview_caloric_item, null);
            viewHolder = new ViewHolder();
            BLog.d(TAG, " getView convertView.findViewById(R.id.seekBar) = "
                    + convertView.findViewById(R.id.seek_bar) );
            viewHolder.seekBar = (HoloCircleSeekBar) convertView.findViewById(R.id.seek_bar);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.divider = (ImageView) convertView.findViewById(R.id.divider);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final RecordFunctionEntity fun = (RecordFunctionEntity) getItem(position);
        viewHolder.title.setText(fun.getTitleRes());
        viewHolder.icon.setImageResource(fun.getIconRes());
        viewHolder.seekBar.setCircleSeekBar(fun.getMax(), fun.getValue(), fun.getColorRes());

        if (position == getCount() - 1) {
            viewHolder.divider.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.divider.setVisibility(View.VISIBLE);
        }

       // viewHolder.seekBar.setTextAlignment(fun.getPercentage());
        return convertView;
    }

    class ViewHolder {
        HoloCircleSeekBar seekBar;
        TextView title;
        ImageView divider;
        ImageView icon;
    }



}
