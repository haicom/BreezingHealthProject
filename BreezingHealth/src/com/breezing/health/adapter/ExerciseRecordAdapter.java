package com.breezing.health.adapter;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.breezing.health.R;

public class ExerciseRecordAdapter extends CursorAdapter {

    private Context context;
   // private ArrayList<ExerciseRecordEntity> records;
    private int titleColor;
    private int contentColor;
    
    private static final String TAG = "ExerciseRecordAdapter";
    
    
    private OnDataSetChangedListener mOnDataSetChangedListener;
    protected LayoutInflater mInflater;
    
    public ExerciseRecordAdapter(Context context, Cursor c) {
        super(context, c, FLAG_REGISTER_CONTENT_OBSERVER);      
        this.context = context;
        this.titleColor = context.getResources().getColor(R.color.black);
        this.contentColor = context.getResources().getColor(R.color.gray);
        
        mInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
       // initRecords();
    }
    
//    private void initRecords() {
//        records = new ArrayList<ExerciseRecordEntity>();
//        records.add(new ExerciseRecordEntity());
//        records.add(new ExerciseRecordEntity());
//        records.add(new ExerciseRecordEntity());
//        records.add(new ExerciseRecordEntity());
//        records.add(new ExerciseRecordEntity());
//        records.add(new ExerciseRecordEntity());
//        records.add(new ExerciseRecordEntity());
//        records.add(new ExerciseRecordEntity());
//        records.add(new ExerciseRecordEntity());
//    }

//    @Override
//    public int getCount() {
//        return super.getCount() + 1;
//    }
//
//    @Override
//    public ExerciseRecordEntity getItem(int position) {
//        return records.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder = null;
//        
//        if (convertView == null) {
//            return super.getView(position, convertView, parent);            
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        
//        if (position == 0) {
//            if (holder != null) {
//                holder.type.setTextColor(titleColor);
//                holder.des.setTextColor(titleColor);
//                holder.calorie.setTextColor(titleColor);
//                
//                holder.type.setText(R.string.type);
//                holder.des.setText(R.string.detail);
//                holder.calorie.setText(R.string.consume);
//            }            
//        } else {            
//            return super.getView(position, convertView, parent);
//        }
//        
//        return convertView;
//    }
//    
    class ViewHolder {
        TextView type;
        TextView des;
        TextView calorie;
    }

    public interface OnDataSetChangedListener {
        void onDataSetChanged(ExerciseRecordAdapter adapter);
        void onContentChanged(ExerciseRecordAdapter adapter);
    }
    
    public void setOnDataSetChangedListener(OnDataSetChangedListener l) {
        mOnDataSetChangedListener = l;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder;
        holder = (ViewHolder) view.getTag();
        holder.type.setTextColor(contentColor);
        holder.des.setTextColor(contentColor);
        holder.calorie.setTextColor(contentColor);
        
        String  sportType = cursor.getString(SPORT_TYPE_INDEX);
        String  sportIntensity = cursor.getString(SPORT_INTENSITY_INDEX);
        int     sportQuantity = cursor.getInt(SPORT_QUANTITY_INDEX);
        String  sportUnit = cursor.getString(SPORT_UNIT_INDEX);
        float   calorie = cursor.getFloat(CALORIE_INDEX);
        
        holder.type.setText(sportType);
        holder.des.setText( context.getString(R.string.exercise_description, 
                sportQuantity, sportUnit, sportIntensity) );
        DecimalFormat fnum = new DecimalFormat("##0.0"); 
        holder.calorie.setText( context.getString(R.string.exercise_calorie, 
                fnum.format(calorie)));
        
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) { 
        ViewHolder holder = new ViewHolder();
        
        View view =  mInflater.inflate(R.layout.list_exercise_record_item,
                parent, false);
        
        holder.type = (TextView) view.findViewById(R.id.type);
        holder.des = (TextView) view.findViewById(R.id.des);
        holder.calorie = (TextView) view.findViewById(R.id.caloric);
        
        view.setTag(holder);
        
        return view;
    }
    
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        
        Log.v(TAG, "notifyDataSetChanged MessageListAdapter.notifyDataSetChanged().");
      

        if (mOnDataSetChangedListener != null) {
            mOnDataSetChangedListener.onDataSetChanged(this);
        }
    }

    @Override
    protected void onContentChanged() {
        Log.d(TAG, "onContentChanged");
        if (getCursor() != null && !getCursor().isClosed()) {
            if (mOnDataSetChangedListener != null) {
                mOnDataSetChangedListener.onContentChanged(this);
            }
        }
    }
    
    private static final int SPORT_ID_INDEX = 0;
    private static final int SPORT_TYPE_INDEX = 1;
    private static final int SPORT_INTENSITY_INDEX = 2;
    private static final int SPORT_QUANTITY_INDEX = 3;
    private static final int SPORT_UNIT_INDEX = 4;
    private static final int CALORIE_INDEX = 5;

}
