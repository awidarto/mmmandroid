package com.kickstartlab.android.mmm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kickstartlab.android.mmm.R;
import com.kickstartlab.android.mmm.rest.models.Comment;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by awidarto on 12/3/14.
 */
public class CommentAdapter extends BaseAdapter {

    LayoutInflater layoutInflater ;
    private List<Comment> list;
    private Context mContext;

    public CommentAdapter(Context context) {
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<Comment> list) {
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
        ImageView img;

        if(convertView == null){
            convertView     = layoutInflater.inflate(R.layout.comment_item_row, null);
            holder          = new Holder();
            holder.message   = (TextView) convertView.findViewById(R.id.message);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatarpic);

            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }

        holder.message.setText(list.get(i).getMessage() );

        Picasso.with(mContext)
                .load(list.get(i).getCoverUrl())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_cloud_done_white_24dp)
                .into(holder.avatar);

        return convertView;
    }

    static class Holder{
        ImageView avatar;
        TextView message;
    }
}
