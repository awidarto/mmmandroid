package com.kickstartlab.android.mmm.adapters;

import android.content.Context;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.kickstartlab.android.mmm.R;
import com.kickstartlab.android.mmm.activities.MainActivity;
import com.kickstartlab.android.mmm.events.FeedEvent;
import com.kickstartlab.android.mmm.rest.models.Feed;
import com.squareup.picasso.Picasso;

import java.util.List;

import at.markushi.ui.CircleButton;
import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by awidarto on 12/3/14.
 */
public class FeedAdapter extends BaseAdapter {

    LayoutInflater layoutInflater ;
    private List<Feed> list;
    private Context mContext;

    public FeedAdapter(Context context) {
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<Feed> list) {
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
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        Holder holder;
        ImageView img;


        if(convertView == null){
            convertView     = layoutInflater.inflate(R.layout.feed_item_row, null);
            holder          = new Holder();
            holder.mediatitle = (TextView) convertView.findViewById(R.id.media_title);
            holder.message     = (TextView) convertView.findViewById(R.id.message);
            holder.feedpic = (ImageView) convertView.findViewById(R.id.feedpic);
            holder.username = (TextView) convertView.findViewById(R.id.user_name);
            holder.activity = (TextView) convertView.findViewById(R.id.activity_line);
            holder.avatar = (CircleImageView) convertView.findViewById(R.id.avatarpic);

            holder.btComment = (CircleButton) convertView.findViewById(R.id.button_comment);
            holder.btLike = (CircleButton) convertView.findViewById(R.id.button_like);

            holder.extMenu = (ImageButton) convertView.findViewById(R.id.context_menu);

            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }

        holder.mediatitle.setText(list.get(i).getMediaTitle());
        holder.message.setText(list.get(i).getMessage() );
        holder.username.setText(list.get(i).getOriginatorName());

        String activity = "";

        if(list.get(i).getType().equalsIgnoreCase("listen")){
            activity = "is listening to";
        }else if(list.get(i).getType().equalsIgnoreCase("upload")){
            activity = "has uploaded";
        }else if(list.get(i).getType().equalsIgnoreCase("album")){
            activity = "uploads an album";
        }

        holder.activity.setText(activity);

        holder.feedpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new FeedEvent("select", list.get(i)));
            }
        });

        holder.btComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new FeedEvent("comment", list.get(i)));
            }
        });

        holder.btLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, list.get(i).getMediaId(), Toast.LENGTH_SHORT).show();
                //EventBus.getDefault().post(new FeedEvent("select",list.get(i)));
            }
        });

        holder.extMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterPopup(view, list.get(i));
            }
        });

        Picasso.with(mContext)
                .load(list.get(i).getCoverUrl())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_cloud_download_grey600_24dp)
                .into(holder.feedpic);

        if("default".equalsIgnoreCase(list.get(i).getOriginatorAvatar()) == false){
            Picasso.with(mContext)
                    .load(list.get(i).getOriginatorAvatar())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.default_cover_small)
                    .into(holder.avatar);
        }


        return convertView;
    }

    private void showFilterPopup(View v, Feed f) {
        PopupMenu popup = new PopupMenu(mContext, v);
        final Feed feed;

        feed = f;
        // Inflate the menu from xml
        popup.getMenuInflater().inflate(R.menu.context_feed_menu, popup.getMenu());
        // Setup menu item selection
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.context_buy:
                        Toast.makeText(mContext, feed.getMediaId(), Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.context_viral:
                        Toast.makeText(mContext, "Viralize!", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        // Handle dismissal with: popup.setOnDismissListener(...);
        // Show the menu
        popup.show();
    }

    static class Holder{
        ImageView feedpic;
        CircleImageView avatar;
        TextView username;
        TextView activity;
        TextView message;
        TextView mediatitle;
        CircleButton btLike;
        CircleButton btComment;
        ImageButton extMenu;
    }
}
