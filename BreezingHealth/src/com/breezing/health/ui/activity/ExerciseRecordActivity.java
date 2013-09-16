package com.breezing.health.ui.activity;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.breezing.health.R;
import com.breezing.health.adapter.ExerciseRecordAdapter;
import com.breezing.health.entity.ActionItem;

public class ExerciseRecordActivity extends ActionBarActivity {

    private ListView mRecordList;
    private View mRecordHeader;
    private ExerciseRecordAdapter mRecordAdapter;
    private TextView mTotalCaloric;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_exercise_record);
        initValues();
        initViews();
        valueToView();
        initListeners();
    }

    private void initValues() {
        
    }

    private void initViews() {
        setActionBarTitle(R.string.manual_input_exercise_record);
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));
        
        mRecordList = (ListView) findViewById(R.id.list);
        mRecordHeader = getLayoutInflater().inflate(R.layout.list_exercise_record_header, null);
        mTotalCaloric = (TextView) mRecordHeader.findViewById(R.id.totalCaloric);
        mRecordList.addHeaderView(mRecordHeader);
    }

    private void valueToView() {
        updateTotalCaloric(1000);
        
        mRecordAdapter = new ExerciseRecordAdapter(this);
        mRecordList.setAdapter(mRecordAdapter);
    }

    private void initListeners() {
        
    }
    
    private void updateTotalCaloric(long count) {
        final String title = getString(R.string.title_total_exercise_caloric);
        final String unit = getString(R.string.kilojoule);
        
        SpannableString span = new SpannableString(title + String.valueOf(count) + unit);
        span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), title.length(), title.length() + String.valueOf(count).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new RelativeSizeSpan(1.5f), title.length(), title.length() + String.valueOf(count).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        
        mTotalCaloric.setText(span);
    }
    
}
