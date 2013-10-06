package com.breezing.health.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.breezing.health.R;



public class AccountRecordAdapter extends SimpleCursorAdapter {
   

    private static final String TAG = "AccountRecordAdapter";
    
    private Context mContext;        
    private OnDataSetChangedListener mOnDataSetChangedListener;
    protected LayoutInflater mInflater;
    
    public AccountRecordAdapter(Context context, int layout, Cursor c,
            String[] from, int[] to) {
        super(context, layout, c, from, to, FLAG_REGISTER_CONTENT_OBSERVER);
        
    }
    
//    public AccountRecordAdapter(Context context, Cursor c) {            
//        super(context, c, FLAG_REGISTER_CONTENT_OBSERVER);      
//        this.mContext = context;            
//        mInflater = (LayoutInflater) context.getSystemService(
//                Context.LAYOUT_INFLATER_SERVICE);
//       
//    }
    
    
    public void setOnDataSetChangedListener(OnDataSetChangedListener l) {
        mOnDataSetChangedListener = l;
    }

//    @Override
//    public void bindView(View view, Context context, Cursor cursor) {
//        ViewHolder holder;
//        holder = (ViewHolder) view.getTag();           
//        
//        String  name = cursor.getString(0);
//      
//        
//        holder.name.setText(name);         
//        
//    }
//
//    @Override
//    public View newView(Context context, Cursor cursor, ViewGroup parent) { 
//        ViewHolder holder = new ViewHolder();            
//        View view =  mInflater.inflate(R.layout.list_account_record_item,
//                parent, false);            
//        holder.name = (TextView) view.findViewById(R.id.name);            
//        view.setTag(holder);            
//        return view;
//    }
    
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
    
    public interface OnDataSetChangedListener {
        void onDataSetChanged(AccountRecordAdapter adapter);
        void onContentChanged(AccountRecordAdapter adapter);
    }
    
//    public class ViewHolder {
//        TextView name;           
//    }
}
