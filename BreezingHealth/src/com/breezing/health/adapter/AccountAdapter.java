package com.breezing.health.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.breezing.health.R;
import com.breezing.health.entity.AccountEntity;
import com.breezing.health.util.BreezingQueryViews;
import com.breezing.health.util.LocalSharedPrefsUtil;

public class AccountAdapter extends BaseAdapter {

	private ArrayList<AccountEntity> accounts;
    private Context context;
    private int selectedAccount = -1;
    
    public AccountAdapter(Context context) {
        this.context = context;
        BreezingQueryViews query = new BreezingQueryViews(context);
        this.accounts = query.queryAllAccountViews();
        selectedAccount = LocalSharedPrefsUtil.getSharedPrefsValueInt(context,
                LocalSharedPrefsUtil.PREFS_ACCOUNT_ID);
    }

    @Override
    public int getCount() {        
        return accounts.size();
    }

    @Override
    public AccountEntity getItem(int position) {        
        return accounts.get(position);
    }

    @Override
    public long getItemId(int position) {       
        return position;
    }
    
    public void setSelected(int position) {
    	selectedAccount = this.accounts.get(position).getAccountId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {        
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_account_item, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.selected = (ImageView) convertView.findViewById(R.id.selected);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        final AccountEntity account = getItem(position);
        holder.title.setText(account.getAccountName());
        
        if (account.getAccountId() == selectedAccount) {
        	holder.selected.setVisibility(View.VISIBLE);
        } else {
        	holder.selected.setVisibility(View.INVISIBLE);
        }
        
        return convertView;
    }

    class ViewHolder {
        TextView title;
        ImageView selected;
    }

}
