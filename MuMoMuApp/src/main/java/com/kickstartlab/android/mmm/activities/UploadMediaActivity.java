package com.kickstartlab.android.mmm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.kickstartlab.android.mmm.R;
import com.squareup.picasso.Picasso;

public class UploadMediaActivity extends ActionBarActivity {

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_view);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView message = (TextView) findViewById(R.id.status_text);
        ImageView cover = (ImageView) findViewById(R.id.head_image);
        TextView mediaTitle = (TextView) findViewById(R.id.media_title);

        Intent i = getIntent();

        message.setText(i.getStringExtra("message"));
        String coverUrl = i.getStringExtra("coverUrl");
        mediaTitle.setText(i.getStringExtra("mediaTitle"));

        Picasso.with(this)
                .load(i.getStringExtra("coverUrl"))
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_cloud_download_grey600_24dp)
                .into(cover);

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
