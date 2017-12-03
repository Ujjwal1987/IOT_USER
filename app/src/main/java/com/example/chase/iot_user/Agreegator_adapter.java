package com.example.chase.iot_user;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by chase on 20-11-2017.
 */

public class Agreegator_adapter extends BaseAdapter {

    List<Agreegator> agree;
    Context c3;

    public Agreegator_adapter(List<Agreegator> agree, Context c3) {
        this.agree = agree;
        this.c3 = c3;
    }

    @Override
    public int getCount() {
        return agree.size();
    }

    @Override
    public Object getItem(int position) {
        return agree.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v1 = View.inflate(c3, R.layout.activity_agreegator_list, null);
        TextView Agreegator_name = (TextView)v1.findViewById(R.id.Agreegator_name);
        TextView Agreegator_id = (TextView) v1.findViewById(R.id.Agreegator_id);

        //Set Text for Text View

        Agreegator_name.setText(agree.get(position).getAgree_name());
        Agreegator_id.setText(agree.get(position).getAgree_id());
        v1.setTag(agree.get(position).getAgree_id());
        return v1;
    }
}
