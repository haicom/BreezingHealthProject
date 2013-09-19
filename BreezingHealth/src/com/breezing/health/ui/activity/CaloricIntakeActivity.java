package com.breezing.health.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import com.breezing.health.R;
import com.breezing.health.adapter.FoodAdapter;
import com.breezing.health.adapter.FoodCatagoryAdapter;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.util.ExtraName;
import com.breezing.health.widget.MultiDirectionSlidingDrawer;

public class CaloricIntakeActivity extends ActionBarActivity {

    public enum CaloricIntakeType {
        BREAKFAST, LUNCH, DINNER, OTHER
    }

    private CaloricIntakeType mCaloricIntakeType;

    private MultiDirectionSlidingDrawer mDrawer;
    private GridView mCatagoryGridView;
    private FoodCatagoryAdapter mCatagoryAdapter;
    private ListView mFoodList;
    private FoodAdapter mFoodAdapter;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        switch(keyCode) {
        case KeyEvent.KEYCODE_BACK: {
            if (mDrawer.isOpened()) {
                mDrawer.animateToggle();
                return true;
            }
        }

        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentFrame(R.layout.activity_caloric_intake);
        initValues();
        initViews();
        valueToView();
        initListeners();
    }

    private void initValues() {
        mCaloricIntakeType = CaloricIntakeType.values()[getIntent().getIntExtra(ExtraName.EXTRA_TYPE, CaloricIntakeType.BREAKFAST.ordinal())];
    }

    private void initViews() {
        switch(mCaloricIntakeType) {
        case BREAKFAST:
            setActionBarTitle(R.string.breakfast);
            break;
        case LUNCH:
            setActionBarTitle(R.string.lunch);
            break;
        case DINNER:
            setActionBarTitle(R.string.dinner);
            break;
        case OTHER:
            setActionBarTitle(R.string.other);
            break;
        }
        addLeftActionItem(new ActionItem(ActionItem.ACTION_BACK));
        addRightActionItem(new ActionItem(ActionItem.ACTION_MORE));

        mDrawer = (MultiDirectionSlidingDrawer) findViewById(R.id.drawer);
        mCatagoryGridView = (GridView) findViewById(R.id.gridView);
        mFoodList = (ListView) findViewById(R.id.list);

    }

    private void valueToView() {
        mCatagoryAdapter = new FoodCatagoryAdapter(this);
        mCatagoryGridView.setAdapter(mCatagoryAdapter);
        mFoodAdapter = new FoodAdapter(this);
        mFoodList.setAdapter(mFoodAdapter);
    }

    private void initListeners() {

    }

    @Override
    public void onClickActionBarItems(ActionItem item, View v) {
        switch(item.getActionId()) {
        case ActionItem.ACTION_MORE:
            mDrawer.animateToggle();
            return ;
        }
        super.onClickActionBarItems(item, v);
    }

}
