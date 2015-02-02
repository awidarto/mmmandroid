package com.kickstartlab.android.mmm.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.kickstartlab.android.mmm.R;
import com.kickstartlab.android.mmm.events.FeedEvent;
import com.kickstartlab.android.mmm.events.OrderEvent;
import com.kickstartlab.android.mmm.events.ScannerEvent;
import com.kickstartlab.android.mmm.fragments.FeedListFragment;
import com.kickstartlab.android.mmm.fragments.MessageDialogFragment;
import com.kickstartlab.android.mmm.fragments.ScannerFragment;
import com.kickstartlab.android.mmm.rest.interfaces.MmmApiInterface;
import com.kickstartlab.android.mmm.rest.models.Feed;
import com.kickstartlab.android.mmm.rest.models.OrderItem;
import com.kickstartlab.android.mmm.rest.models.ResultObject;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ActionBarActivity implements
        //FragmentManager.OnBackStackChangedListener,
        MessageDialogFragment.MessageDialogListener {

    private static final String DATABASE_NAME = "jwh.db";
    private static final String SCREEN_LOCATION = "location";
    private static final String SCREEN_SCAN = "scan";

    private String current_mode;
    private String current_location;
    private String current_rack;
    private String current_asset;


    Toolbar mToolbar;

    SmoothProgressBar mProgressBar;

    SharedPreferences spref;
    String akey;
    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mProgressBar = (SmoothProgressBar) findViewById(R.id.loadProgressBar);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        mProgressBar.setVisibility(View.GONE);

        initFab();

        EventBus.getDefault().register(this);
        //getSupportFragmentManager().addOnBackStackChangedListener(this);

        spref = PreferenceManager.getDefaultSharedPreferences(this);

        akey = spref.getString("auth","");

        uid = spref.getString("uid","");

        if("".equalsIgnoreCase(akey)){
            showLogin();
        }else{

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new FeedListFragment(),"feed_fragment")
                        .setBreadCrumbTitle(getResources().getString(R.string.app_name))
                                //.addToBackStack("merchant_fragment")
                        .commit();
                current_mode = "feed";
            }

        }

    }

    public void initFab(){
        ImageView fabIcon = new ImageView(this);
        fabIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_white_36dp));

        FloatingActionButton fabAction = new FloatingActionButton.Builder(this)
                .setBackgroundDrawable(R.drawable.fab_red_selector)
                .setContentView(fabIcon).build();

        SubActionButton.Builder subActionButton = new SubActionButton.Builder(this);
            //subActionButton.setLayoutParams(new FloatingActionButton.LayoutParams(70,70) );
            subActionButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.fab_red_selector));

        ImageView fabMusic = new ImageView(this);
            fabMusic.setImageDrawable(getResources().getDrawable(R.drawable.ic_headset_white_18dp));
        ImageView fabPlaylist = new ImageView(this);
            fabPlaylist.setImageDrawable(getResources().getDrawable(R.drawable.ic_format_list_bulleted_white_18dp));
        ImageView fabUpload = new ImageView(this);
            fabUpload.setImageDrawable(getResources().getDrawable(R.drawable.ic_file_upload_white_18dp));

        FloatingActionMenu floatingActionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(subActionButton.setContentView(fabMusic).build())
                .addSubActionView(subActionButton.setContentView(fabPlaylist).build())
                .addSubActionView(subActionButton.setContentView(fabUpload).build())
                .attachTo(fabAction)
                .build();

        fabMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ListenViewActivity.class);
                startActivity(intent);
            }
        });
    }

    /*
    @Override
    public void onBackStackChanged() {
        String title;
        FragmentManager fm = getSupportFragmentManager();
        int bc = fm.getBackStackEntryCount();
        Log.i("BC",String.valueOf(bc));
        if(bc > 0){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            title = fm.getBackStackEntryAt(bc - 1).getBreadCrumbTitle().toString();
            getSupportActionBar().setTitle(title);
        }else{
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        }
    }
    */

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

        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class );
            startActivity(i);
        }

        if(id == R.id.action_refresh_feed){
            refreshFeed();
        }

        if(id == R.id.action_logout){
            doSignOut();
        }
        /*


        if(id == R.id.action_asset){
            refreshAsset();
        }
        */

        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /* EVENTS */
    public void onEvent(FeedEvent fe){
        if(fe.getAction() == "select") {

            //Toast.makeText(this, fe.getFeed().getMediaTitle(),Toast.LENGTH_SHORT ).show();

            Intent intent = new Intent(this, FeedViewActivity.class);

            intent.putExtra("mediaId", fe.getFeed().getMediaId());
            intent.putExtra("mediaTitle", fe.getFeed().getMediaTitle());
            intent.putExtra("mediaUrl", fe.getFeed().getMediaUrl());
            intent.putExtra("mediaType", fe.getFeed().getMediaType());
            intent.putExtra("coverUrl", fe.getFeed().getCoverUrl());
            intent.putExtra("message", fe.getFeed().getMessage());


            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if(fe.getAction() == "comment"){

            //Toast.makeText(this, fe.getFeed().getMediaTitle(),Toast.LENGTH_SHORT ).show();

            Intent intent = new Intent(this,CommentViewActivity.class);

            intent.putExtra("mediaId",fe.getFeed().getMediaId());
            intent.putExtra("mediaTitle",fe.getFeed().getMediaTitle());
            intent.putExtra("mediaUrl",fe.getFeed().getMediaUrl());
            intent.putExtra("mediaType",fe.getFeed().getMediaType());
            intent.putExtra("coverUrl",fe.getFeed().getCoverUrl());
            intent.putExtra("message",fe.getFeed().getMessage());


            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }else if(fe.getAction() == "refreshDb"){
            refreshFeed();
        }
    }

    public void showMessageDialog(String message,String device,
                                  String buyer, String recipient,String deliverydate,
                                  String courier,String merchant,String deliveryid,String invoice ) {
        DialogFragment fragment = MessageDialogFragment.newInstance("Order Info", message, device,
                buyer, recipient,deliverydate,
                courier, merchant, deliveryid, invoice, this);
        fragment.show( getSupportFragmentManager(), "scan_results");
    }

    public void closeMessageDialog() {
        closeDialog("scan_results");
    }

    public void closeDialog(String dialogName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFragment fragment = (DialogFragment) fragmentManager.findFragmentByTag(dialogName);
        if(fragment != null) {
            fragment.dismiss();
        }
        Log.i("dialog", "closed");
        EventBus.getDefault().post(new ScannerEvent("resume"));
    }

    @Override
    public void onDialogPositiveClick(MessageDialogFragment dialog, String mDeliveryId) {
        // Resume the camera
        String deliveryId = mDeliveryId;
        Log.i("current delivery Id", deliveryId);
        //updateStatus(deliveryId);
        EventBus.getDefault().post(new ScannerEvent("resume"));
    }

    public void refreshFeed(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.api_base_url))
                .build();

        MmmApiInterface mmmApiInterface = restAdapter.create(MmmApiInterface.class);

        setProgressVisibility(true);

        mmmApiInterface.getFeed(new Callback<List<Feed>>() {
            @Override
            public void success(List<Feed> feeds, Response response) {
                for(int i = 0; i < feeds.size(); i++){
                    Select select = Select.from(Feed.class).where(Condition.prop("ext_id").eq(feeds.get(i).getExtId()))
                            .limit("1");

                    if(select.count() > 0) {
                        Feed fdb = (Feed) select.first();
                        Feed fin = feeds.get(i);

                        fdb.setOriginatorAvatar(fin.getOriginatorAvatar());
                        fdb.save();
                    }else{
                        feeds.get(i).save();
                    }
                }
                setProgressVisibility(false);

                EventBus.getDefault().post(new FeedEvent("refresh"));

            }

            @Override
            public void failure(RetrofitError error) {
                setProgressVisibility(false);
                Log.i("order get failure", error.toString());
                EventBus.getDefault().post(new FeedEvent("refresh"));

            }
        });

    }


    private void setProgressVisibility(Boolean v){
        if(v == true){
            mProgressBar.setVisibility(View.VISIBLE);
        }else{
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public static void extractDb(File sourceFile, File destFile) {

        FileChannel source = null;
        FileChannel destination = null;

        try {
            if (!destFile.exists()) {
                destFile.createNewFile();
            }

            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (source != null) {
                    source.close();
                }
                if (destination != null) {
                    destination.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void doScan(String id, String mode){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, ScannerFragment.newInstance(id, mode), "scan_fragment")
                .addToBackStack("scan_fragment")
                .setBreadCrumbTitle(getResources().getString(R.string.action_scan))
                .commit();
        getSupportActionBar().setTitle(getResources().getString(R.string.action_scan));
    }

    private void showLogin(){
        Intent intent;
        intent = new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        //finish();
    }

    private void doSignOut(){
        SharedPreferences.Editor editor = spref.edit();
        editor.putString("auth","");
        editor.putString("uid","");
        editor.commit();
        showLogin();
    }


}
