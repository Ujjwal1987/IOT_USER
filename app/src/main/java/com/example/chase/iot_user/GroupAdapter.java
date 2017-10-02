package com.example.chase.iot_user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chase on 30-04-2017.
 */

public class GroupAdapter extends BaseAdapter {

    List<Group> grp;
    Context c1;


    public GroupAdapter(List<Group> grp, Context c1) {
        this.grp = grp;
        this.c1 = c1;
    }

    @Override
    public int getCount() {
        return grp.size();
    }

    @Override
    public Object getItem(int position) {
        return grp.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v1 = View.inflate(c1, R.layout.grouplist, null);
        TextView group_name = (TextView) v1.findViewById(R.id.Group_name);
        TextView group_id = (TextView)v1.findViewById(R.id.Group_id);

        //Set Text for Text View

        group_name.setText(grp.get(position).getGrp_name());
        group_id.setText(grp.get(position).getGrp_ID());


        v1.setTag(grp.get(position).getGrp_name());
        return v1;
    }
}
