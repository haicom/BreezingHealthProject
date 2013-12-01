package com.breezing.health.adapter;

import java.util.ArrayList;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.breezing.health.R;

public class BluetoothDeviceAdapter extends BaseExpandableListAdapter {

    private ArrayList<BluetoothDevice> bluetoothDevices;
    private ArrayList<BluetoothDevice> boundBluetoothDevices;
    private Context context;
    
    public BluetoothDeviceAdapter(Context context) {
        this.context = context;
        this.bluetoothDevices = new ArrayList<BluetoothDevice>();
        this.boundBluetoothDevices = new ArrayList<BluetoothDevice>();
    }
    
    public void addBoundDevices(BluetoothDevice device) {
        this.boundBluetoothDevices.add(device);
    }
    
    public void addDevice(BluetoothDevice device) {
        this.bluetoothDevices.add(device);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public BluetoothDevice getChild(int groupPosition, int childPosition) {
        switch (groupPosition) {
        case 0:
            return boundBluetoothDevices.get(childPosition);
        case 1:
            return bluetoothDevices.get(childPosition);
        default:
            break;
        }   
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_bluetooth_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.mac = (TextView) convertView.findViewById(R.id.mac);
            holder.detail = (ImageView) convertView.findViewById(R.id.detail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        final BluetoothDevice item = getChild(groupPosition, childPosition);
        holder.name.setText("Breezing" + childPosition);
        holder.mac.setText("E0:E5:21:55");
        
        return convertView;
    }
    
    class ViewHolder {
        TextView name;
        TextView mac;
        ImageView detail;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        switch (groupPosition) {
        case 0:
            return boundBluetoothDevices.size();
        case 1:
            return bluetoothDevices.size();
        default:
            break;
        }   
        return 0;
    }

    @Override
    public String getGroup(int groupPosition) {
        switch (groupPosition) {
        case 0:
            return context.getString(R.string.paired_bluetooth_device);
        case 1:
            return context.getString(R.string.enable_bluetooth_device);
        default:
            break;
        }   
        return "";
    }

    @Override
    public int getGroupCount() {
        return 2;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        GroupViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_bluetooth_group_item, null);
            holder = new GroupViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        
        holder.name.setText(getGroup(groupPosition));
        
        return convertView;
    }
    
    class GroupViewHolder {
        TextView name;
    }

}
