package com.breezing.health.adapter;

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
import com.breezing.health.ui.activity.ExerciseRecordActivity;
import com.breezing.health.util.OnDataSetChangedListener;

public class ExerciseRecordAdapter extends CursorAdapter {
    private static final String TAG = "ExerciseRecordAdapter";
    private Context context; 
    private int titleColor;
    private int contentColor;
    
    private String mCaloricUnit;
    private  float mUnifyUnit;
    
    
    
    
    private OnDataSetChangedListener mOnDataSetChangedListener;
    protected LayoutInflater mInflater;
    
    public ExerciseRecordAdapter(Context context, Cursor c, String caloriUnit, float unitfyUnit) {
        super(context, c, FLAG_REGISTER_CONTENT_OBSERVER);      
        this.context = context;
        this.titleColor = context.getResources().getColor(R.color.black);
        this.contentColor = context.getResources().getColor(R.color.gray);
        mCaloricUnit = caloriUnit;
        mUnifyUnit = unitfyUnit;
        mInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);       
    }
    
   
    class ViewHolder {
        TextView type;
        TextView des;
        TextView calorie;
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
        
        String  sportType = cursor.getString(ExerciseRecordActivity.SPORT_TYPE_INDEX);
        String  sportIntensity = cursor.getString(ExerciseRecordActivity.SPORT_INTENSITY_INDEX);
        int     sportQuantity = cursor.getInt(ExerciseRecordActivity.SPORT_QUANTITY_INDEX);
        String  sportUnit = cursor.getString(ExerciseRecordActivity.SPORT_UNIT_INDEX);
        float   calorie = cursor.getFloat(ExerciseRecordActivity.CALORIE_INDEX);
        
        holder.type.setText(sportType);
        if ( sportQuantity >= EXERCISE_TIMER_HOUR ) {
            DecimalFormat df2 = new DecimalFormat("###.00");
            final double timer = sportQuantity / EXERCISE_TIMER_HOUR;
            holder.des.setText( context.getString(R.string.exercise_hour_description, 
                    df2.format(timer)) );
        } else {
            holder.des.setText( context.getString(R.string.exercise_minite_description, 
                    sportQuantity) );
        }
        
        DecimalFormat fnum = new DecimalFormat("##0.0"); 
        holder.calorie.setText( context.getString(R.string.exercise_calorie, 
                fnum.format(calorie * mUnifyUnit) , mCaloricUnit) );
        
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
    
  
    
    private static final double EXERCISE_TIMER_HOUR = 60.00;

}
