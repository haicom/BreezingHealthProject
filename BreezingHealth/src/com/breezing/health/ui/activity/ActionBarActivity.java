package com.breezing.health.ui.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.breezing.health.R;
import com.breezing.health.entity.ActionItem;
import com.breezing.health.tools.Tools;

public class ActionBarActivity extends BaseActivity {

    private LinearLayout mLeftActionLayout;
    private LinearLayout mRightActionLayout;
    private LinearLayout mCenterActionLayout;
    private RelativeLayout mActionBar;
    private FrameLayout mViewContainer;
    private TextView mTitle;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {      
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_actionbar);
        
        mActionBar = (RelativeLayout) findViewById(R.id.actionBar);
        mViewContainer = (FrameLayout) findViewById(R.id.viewContainer);
    }
    
    /**
     * use setContentFrame() replace setContentView() if your class extends ActionBarActivity
     * @param layoutResID
     */
    public void setContentFrame(int layoutResID) {        
        View contentView = getLayoutInflater().inflate(layoutResID, null);
        mViewContainer.addView(contentView);
    }
    
    public void setContentFrame(View view) {        
        mViewContainer.addView(view);
    }
    
    public void setContentFrame(View view, LayoutParams params) {      
        mViewContainer.addView(view, params);
    }
    
    /**
     * add the action item to the right of the actionbar
     * @param item    the action item
     */
    public void addRightActionItem(ActionItem item) {
        createRightActionLayout();
        
        //init action item
        ImageButton button = new ImageButton(this);
        button.setBackgroundResource(R.drawable.bg_action_item_selector);
        button.setPadding(Tools.dip2px(this, 5), Tools.dip2px(this, 5), Tools.dip2px(this, 5), Tools.dip2px(this, 5));
        button.setAdjustViewBounds(true);
        button.setScaleType(ScaleType.CENTER_INSIDE);
        button.setImageResource(item.getActionIconRes());
        button.setTag(item);
        button.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ActionItem item = (ActionItem) v.getTag();
                onClickActionBarItems(item, v);
            }
        });
        
        //add action item
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(this.getResources().getDimensionPixelSize(R.dimen.actionbar_item_height), this.getResources().getDimensionPixelSize(R.dimen.actionbar_height));
        mRightActionLayout.addView(button, params);
    }
    
    /**
     * create the right action container
     */
    private void createRightActionLayout() {
        if (mRightActionLayout == null) {
            //init right action layout
            mRightActionLayout = new LinearLayout(this);
            mRightActionLayout.setGravity(Gravity.CENTER_VERTICAL);
            mRightActionLayout.setOrientation(LinearLayout.HORIZONTAL);
            
            //add right action layout to action bar
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            mActionBar.addView(mRightActionLayout,params);
        }
    }
    
    /**
     * add the action item to the left of the actionbar
     * @param item    the action item
     */
    public void addLeftActionItem(ActionItem item) {
        createLeftActionLayout();
        
        //init action item
        ImageButton button = new ImageButton(this);
        button.setBackgroundResource(R.drawable.bg_action_item_selector);
        button.setPadding(Tools.dip2px(this, 5), Tools.dip2px(this, 5), Tools.dip2px(this, 5), Tools.dip2px(this, 5));
        button.setAdjustViewBounds(true);
        button.setScaleType(ScaleType.CENTER_INSIDE);
        button.setImageResource(item.getActionIconRes());
        button.setTag(item);
        button.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ActionItem item = (ActionItem) v.getTag();
                onClickActionBarItems(item, v);
            }
        });
        
        //add action item
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(this.getResources().getDimensionPixelSize(R.dimen.actionbar_item_height),
                this.getResources().getDimensionPixelSize(R.dimen.actionbar_height));
        mLeftActionLayout.addView(button, params);
    }
    
    /**
     * create the left action container
     */
    private void createLeftActionLayout() {
        if (mLeftActionLayout == null) {
            //init left action layout
            mLeftActionLayout = new LinearLayout(this);
            mLeftActionLayout.setGravity(Gravity.CENTER_VERTICAL);
            mLeftActionLayout.setOrientation(LinearLayout.HORIZONTAL);
            
            //add left action layout to action bar
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            mActionBar.addView(mLeftActionLayout, params);
        }
    }
    
    /**
     * show the title in the center of the actionbar
     * @param titleString
     */
    public void setActionBarTitle(String titleString){
        
        createCenterActionLayout();
        
        if (mTitle == null) {
            mTitle = new TextView(this);
            mTitle.setTextColor(getResources().getColor(android.R.color.white));
            mTitle.setTextSize(22f);
            mTitle.setSingleLine(true);
            mTitle.setShadowLayer(1, 1, 1, getResources().getColor(android.R.color.black));
            mTitle.setGravity(Gravity.CENTER);
            mCenterActionLayout.addView(mTitle);
        }
        
        mTitle.setText(titleString);
    }
    
    /**
     * show the title in the center of the actionbar
     * @param title string resource id
     */
    public void setActionBarTitle(int titleRes){
        
        createCenterActionLayout();
        
        if (mTitle == null) {
            mTitle = new TextView(this);
            mTitle.setTextColor(getResources().getColor(R.color.black));
            mTitle.setTextSize(22f);
            mTitle.setSingleLine(true);
            mTitle.setShadowLayer(1, 1, 1, getResources().getColor(R.color.white));
            mTitle.setGravity(Gravity.CENTER);
            mCenterActionLayout.addView(mTitle);
        }
        
        mTitle.setText(titleRes);
    }
    
    private void createCenterActionLayout() {
        if (mCenterActionLayout == null) {
            //init right action layout
            mCenterActionLayout = new LinearLayout(this);
            mCenterActionLayout.setGravity(Gravity.CENTER_VERTICAL);
            mCenterActionLayout.setOrientation(LinearLayout.HORIZONTAL);
            
            //add right action layout to action bar
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            mActionBar.addView(mCenterActionLayout,params);
        }
    }

    
    /**
     * hide the action bar
     */
    public void hideActionBar() {
        if (mActionBar != null)
        mActionBar.setVisibility(View.GONE);
    }
    
    /**
     * show the action bar
     */
    public void showActionBar() {
        if (mActionBar != null)
        mActionBar.setVisibility(View.VISIBLE);
    }
    
    /**
     * action bar item click callback
     * @param item clicked action item
     * @param v the view of the action item
     */
    public void onClickActionBarItems(ActionItem item, View v) {
        if (item.getActionId() == ActionItem.ACTION_BACK) {
            finish();
            return ;
        }
    }
    
}
