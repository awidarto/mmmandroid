package com.kickstartlab.android.mmm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kickstartlab.android.mmm.R;
import com.kickstartlab.android.mmm.rest.models.Location;

import java.util.List;

/**
 * Created by awidarto on 12/3/14.
 */
public class LocationAdapter extends BaseAdapter {

    LayoutInflater layoutInflater ;
    private List<Location> list;

    public LocationAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<Location> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return list.get(i).getId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Holder holder;

        if(convertView == null){
            convertView     = layoutInflater.inflate(R.layout.item_row, null);
            holder          = new Holder();
            holder.head   = (TextView) convertView.findViewById(R.id.head);
            holder.subhead     = (TextView) convertView.findViewById(R.id.subhead);

            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }

        holder.head.setText(list.get(i).getName());
        holder.subhead.setText(list.get(i).getAddress() );

        return convertView;
    }

    static class Holder{
        TextView head;
        TextView subhead;
    }
}
