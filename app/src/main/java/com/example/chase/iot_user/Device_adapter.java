package com.example.chase.iot_user;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by chase on 06-08-2017.
 */

    public class Device_adapter extends BaseAdapter {

    List<Device> dev;
    Context c2;

    public Device_adapter(List<Device> dev, Context c2) {
        this.dev = dev;
        this.c2 = c2;
    }

    @Override
    public int getCount() {
        return dev.size();
    }

    @Override
    public Object getItem(int position) {
        return dev.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v1 = View.inflate(c2, R.layout.activity_devicelist, null);
        TextView Device_id = (TextView) v1.findViewById(R.id.Device_id);
        TextView Device_type = (TextView)v1.findViewById(R.id.Device_type);

        //Set Text for Text View

        Device_id.setText(dev.get(position).getDev_id());
        Device_type.setText(dev.get(position).getDev_type());
        v1.setTag(dev.get(position).getDev_id());
        return v1;
    }
}
