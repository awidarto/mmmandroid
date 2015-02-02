package com.kickstartlab.android.mmm.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kickstartlab.android.mmm.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import nl.changer.audiowife.AudioWife;

public class FeedViewActivity extends ActionBarActivity {

    Toolbar mToolbar;
    RelativeLayout mRootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_view);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        mRootLayout = (RelativeLayout) findViewById(R.id.root_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView message = (TextView) findViewById(R.id.status_text);
        final ImageView cover = (ImageView) findViewById(R.id.head_image);
        TextView mediaTitle = (TextView) findViewById(R.id.media_title);

        Intent i = getIntent();

        getSupportActionBar().setTitle(i.getStringExtra("mediaTitle"));

        mToolbar.setBackgroundColor(getResources().getColor(R.color.black));
        mToolbar.getBackground().setAlpha(70);

        message.setText(i.getStringExtra("message"));
        String coverUrl = i.getStringExtra("coverUrl");
        mediaTitle.setText(i.getStringExtra("mediaTitle"));

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                final Bitmap bm;
                bm = bitmap;
                Palette.generateAsync(bm, new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        cover.setImageBitmap(bm);
                        int defaultColor = getResources().getColor(R.color.primary_color_dark);
                        mRootLayout.setBackgroundColor(palette.getDarkVibrantColor(defaultColor));
                    }
                });
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(this)
                .load(i.getStringExtra("coverUrl"))
                //.fit()
                //.centerCrop()
                .placeholder(R.drawable.ic_cloud_download_grey600_18dp)
                .error(R.drawable.ic_cloud_download_grey600_18dp)
                .into(target);

        // send this audio uri to player service
        //Uri uri = Uri.parse(i.getStringExtra("mediaUrl"));


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
