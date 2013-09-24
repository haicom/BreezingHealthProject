package com.breezing.health.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import com.breezing.health.R;
import com.breezing.health.adapter.FoodAdapter;
import com.breezing.health.adapter.FoodCatagoryAdapter;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.ui.fragment.FoodIntakeDialogFragment;
import com.breezing.health.util.ExtraName;
import com.breezing.health.widget.MultiDirectionSlidingDrawer;
import com.breezing.health.widget.MultiDirectionSlidingDrawer.OnDrawerCloseListener;
import com.haarman.listviewanimations.itemmanipulation.OnDismissCallback;

public class CaloricIntakeActivity extends ActionBarActivity implements OnClickListener {

    public enum CaloricIntakeType {
        BREAKFAST, LUNCH, DINNER, OTHER
    }

    private CaloricIntakeType mCaloricIntakeType;

    private MultiDirectionSlidingDrawer mDrawer;
    private GridView mCatagoryGridView;
    private FoodCatagoryAdapter mCatagoryAdapter;
    private ListView mFoodList;
    private FoodAdapter mFoodAdapter;
    
    private Button mDetail;

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
        mDetail = (Button) findViewById(R.id.detail);

    }

    private void valueToView() {
        mCatagoryAdapter = new FoodCatagoryAdapter(this);
        mCatagoryGridView.setAdapter(mCatagoryAdapter);
        mFoodAdapter = new FoodAdapter(this, mCatagoryAdapter);
        mFoodList.setAdapter(mFoodAdapter);
    }

    private void initListeners() {
    	mDetail.setOnClickListener(this);
    	mCatagoryGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				mCatagoryAdapter.toggle(position);
				mCatagoryAdapter.notifyDataSetChanged();
			}
		});
    	
    	mDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			
			@Override
			public void onDrawerClosed() {
				// TODO Auto-generated method stub
				mFoodAdapter.refreshCatagoryItems();
				mFoodAdapter.notifyDataSetChanged();
			}
		});
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mDetail) {
			showDetailDialog();
			return ;
		}
	}
	
	private void showDetailDialog() {
        FoodIntakeDialogFragment detailDialog = (FoodIntakeDialogFragment) getSupportFragmentManager().findFragmentByTag("detailPicker");
        if (detailDialog != null) {
            getSupportFragmentManager().beginTransaction().remove(detailDialog);
        }
        getSupportFragmentManager().beginTransaction().addToBackStack(null);
        detailDialog = FoodIntakeDialogFragment.newInstance();
        detailDialog.setSelectedFoods(mFoodAdapter.getSelectedFoods());
        detailDialog.setOnDeleteListener(new OnDismissCallback() {
			
			@Override
			public void onDismiss(AbsListView listView, int[] reverseSortedPositions) {
				// TODO Auto-generated method stub
				for (int position : reverseSortedPositions) {
					mFoodAdapter.removeSelectedFood(position);
				}
				mFoodAdapter.notifyDataSetChanged();
			}
		});
        detailDialog.show(getSupportFragmentManager(), "detailPicker");
    }

}
