package br.com.dina.ui.widget;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.dina.ui.R;
import br.com.dina.ui.model.BasicItem;
import br.com.dina.ui.model.GroupIndex;
import br.com.dina.ui.model.IListItem;
import br.com.dina.ui.model.UIHeaderItem;
import br.com.dina.ui.model.ViewItem;

public class UITableView extends LinearLayout {
	
    private final int TAG_ACTION = R.id.tag_action;
    
	private int mIndexController = 0;
	private LayoutInflater mInflater;
	private LinearLayout mMainContainer;
	private LinearLayout mListContainer;
	private List<IListItem> mItemList;
	private List<UIHeaderItem> mHeaderItems;
	private HashMap<UIHeaderItem, List<IListItem>> mGroupItems;
	private OnItemClickListener mItemClickListener;
	private UIHeaderItem mCurrentHeaderItem;
	
	public UITableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mItemList = new ArrayList<IListItem>();
		mHeaderItems = new ArrayList<UIHeaderItem>();
		mGroupItems = new HashMap<UIHeaderItem, List<IListItem>>();
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMainContainer = (LinearLayout)  mInflater.inflate(R.layout.list_container, null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		addView(mMainContainer, params);				
		mListContainer = (LinearLayout) mMainContainer.findViewById(R.id.buttonsContainer);		
	}
	
	/**
	 * 
	 * @param title
	 * @param summary
	 */
	public void addBasicItem(String title, String action) {
		mItemList.add(new BasicItem(title, action));
	}
	
	/**
	 * 
	 * @param title
	 * @param summary
	 */
	public void addBasicItem(String title, String summary, String action) {
		mItemList.add(new BasicItem(title, summary, action));
	}
	
	/**
	 * 
	 * @param title
	 * @param summary
	 * @param color
	 */
	public void addBasicItem(String title, String summary, int color, String action) {
		mItemList.add(new BasicItem(title, summary, color, action));
	}
	
	/**
	 * 
	 * @param drawable
	 * @param title
	 * @param summary
	 */
	public void addBasicItem(int drawable, String title, String summary, String action) {
		mItemList.add(new BasicItem(drawable, title, summary, action));
	}
	
	/**
	 * 
	 * @param drawable
	 * @param title
	 * @param summary
	 */
	public void addBasicItem(int drawable, String title, String summary, int color, String action) {
		mItemList.add(new BasicItem(drawable, title, summary, color, action));
	}
	
	/**
	 * 
	 * @param item
	 */
	public void addBasicItem(BasicItem item) {
		mItemList.add(item);
	}
	
	public void addHeaderView(String title) {
	    if (mCurrentHeaderItem != null) {
	        mHeaderItems.add(mCurrentHeaderItem);
	        mGroupItems.put(mCurrentHeaderItem, mItemList);
	        mItemList = new ArrayList<IListItem>();
	    } else if (mItemList.size() != 0) {
	        UIHeaderItem item = new UIHeaderItem("", false);
            mHeaderItems.add(item);
            mGroupItems.put(item, mItemList);
            mItemList = new ArrayList<IListItem>();
	    }
	    mCurrentHeaderItem = new UIHeaderItem(title, true);
	}
	
	/**
	 * 
	 * @param itemView
	 */
	public void addViewItem(ViewItem itemView) {
		mItemList.add(itemView);
	}
	
	public void commit() {
		if (mCurrentHeaderItem == null) {
		    UIHeaderItem item = new UIHeaderItem("", false);
		    mHeaderItems.add(item);
		    mGroupItems.put(item, mItemList);
		} else {
            mHeaderItems.add(mCurrentHeaderItem);
            mGroupItems.put(mCurrentHeaderItem, mItemList);
		}
		
		final int headerSize = mHeaderItems.size();
		for (int index = 0; index < headerSize; index++) {
		    
		    UIHeaderItem headerItem = mHeaderItems.get(index);
	        if(headerItem.isVisiable()) {
	            View headerView = mInflater.inflate(R.layout.list_item_headerview, null);
	            TextView title = (TextView) headerView.findViewById(R.id.title);
	            title.setText(headerItem.getTitle());
	            mListContainer.addView(headerView);
	        }
		    
		    List<IListItem> items = mGroupItems.get(mHeaderItems.get(index));
		    mIndexController = 0;
		    
		    if(items.size() > 1) {
	            //when the list has more than one item
	            final int size = items.size();
	            
	            for(int i = 0; i < size; i++) {
	                View tempItemView;
	                if(mIndexController == 0) {
	                    tempItemView = mInflater.inflate(R.layout.list_item_top, null);
	                }
	                else if(mIndexController == items.size()-1) {
	                    tempItemView = mInflater.inflate(R.layout.list_item_bottom, null);
	                }
	                else {
	                    tempItemView = mInflater.inflate(R.layout.list_item_middle, null);
	                }   
	                setupItem(tempItemView, items.get(i), new GroupIndex(index, mIndexController));
	                tempItemView.setClickable(items.get(i).isClickable());
	                mListContainer.addView(tempItemView);
	                mIndexController++;
	            }
	        } else if(items.size() == 1) {
	            //when the list has only one item
	            
	            View tempItemView = mInflater.inflate(R.layout.list_item_single, null);
	            IListItem obj = items.get(0);
	            setupItem(tempItemView, obj, new GroupIndex(index, mIndexController));
	            tempItemView.setClickable(obj.isClickable());
	            mListContainer.addView(tempItemView);
	        }
		}
		
	}
	
	private void setupItem(View view, IListItem item, GroupIndex index) {
		if(item instanceof BasicItem) {
			BasicItem tempItem = (BasicItem) item;
			setupBasicItem(view, tempItem, index);
		}
		else if(item instanceof ViewItem) {
			ViewItem tempItem = (ViewItem) item;
			setupViewItem(view, tempItem, index);
		}
	}
	
	/**
	 * 
	 * @param view
	 * @param item
	 * @param index
	 */
	private void setupBasicItem(View view, BasicItem item, GroupIndex index) {
		if(item.getDrawable() > -1) {
			((ImageView) view.findViewById(R.id.image)).setBackgroundResource(item.getDrawable());
		}
		if(item.getSubtitle() != null) {
			((TextView) view.findViewById(R.id.subtitle)).setText(item.getSubtitle());
		}
		else {
			((TextView) view.findViewById(R.id.subtitle)).setVisibility(View.GONE);
		}		
		((TextView) view.findViewById(R.id.title)).setText(item.getTitle());
		if(item.getColor() > -1) {
			((TextView) view.findViewById(R.id.title)).setTextColor(item.getColor());
		}
		view.setTag(index);
		view.setTag(TAG_ACTION, item.getAction());
		if(item.isClickable()) {
			view.setOnClickListener( new View.OnClickListener() {
	
				@Override
				public void onClick(View view) {
					if(mItemClickListener != null)
					    mItemClickListener.onClick(view, mListContainer, String.valueOf(view.getTag(TAG_ACTION)), (GroupIndex) view.getTag());
				}
				
			});	
		}
		else {
			((ImageView) view.findViewById(R.id.chevron)).setVisibility(View.GONE);
		}
	}
	
	/**
	 * 
	 * @param view
	 * @param itemView
	 * @param index
	 */
	private void setupViewItem(View view, ViewItem itemView, GroupIndex index) {
		if(itemView.getView() != null) {
			LinearLayout itemContainer = (LinearLayout) view.findViewById(R.id.itemContainer);
			itemContainer.removeAllViews();
			//itemContainer.removeAllViewsInLayout();
			itemContainer.addView(itemView.getView());
			
			if(itemView.isClickable()) {
		        	itemContainer.setTag(index);
		               	itemContainer.setOnClickListener( new View.OnClickListener() {
		                	@Override
		                    	public void onClick(View view) {
		                        	if(mItemClickListener != null)
		                        	    mItemClickListener.onClick(view, mListContainer, null, (GroupIndex) view.getTag());
					}
		                });
		           }
		}
	}
	
	public interface OnItemClickListener {		
		void onClick(View view, ViewGroup contentView, String action, GroupIndex index);		
	}
	
	/**
	 * 
	 * @param listener
	 */
	public void setOnItemClickListener(OnItemClickListener listener) {
		this.mItemClickListener = listener;
	}
	
	/**
	 * 
	 */
	public void removeClickListener() {
		this.mItemClickListener = null;
	}

}
