package com.breezing.health.adapter;

import java.util.ArrayList;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.breezing.health.R;

public class BluetoothDeviceAdapter extends BaseAdapter {

    private ArrayList<BluetoothDevice> bluetoothDevices;
    private Context context;
    
    public BluetoothDeviceAdapter(Context context) {
        this.context = context;
        this.bluetoothDevices = new ArrayList<BluetoothDevice>();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return bluetoothDevices.size();
    }

    @Override
    public BluetoothDevice getItem(int position) {
        // TODO Auto-generated method stub
        return bluetoothDevices.get(position);
    }
    
    public void addItem(BluetoothDevice device) {
        bluetoothDevices.add(device);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
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
        
        final BluetoothDevice item = bluetoothDevices.get(position);
        if (item.getName() != null) {
            holder.name.setText(item.getName());
        }
        holder.mac.setText(item.getAddress());
        
        return convertView;
    }

    class ViewHolder {
        TextView name;
        TextView mac;
        ImageView detail;
    }

}
