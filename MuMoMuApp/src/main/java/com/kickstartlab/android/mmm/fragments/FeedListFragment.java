package com.kickstartlab.android.mmm.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.TextView;

import com.kickstartlab.android.mmm.R;
import com.kickstartlab.android.mmm.activities.FeedViewActivity;
import com.kickstartlab.android.mmm.adapters.FeedAdapter;
import com.kickstartlab.android.mmm.adapters.LocationAdapter;
import com.kickstartlab.android.mmm.events.FeedEvent;
import com.kickstartlab.android.mmm.events.LocationEvent;
import com.kickstartlab.android.mmm.rest.models.Feed;
import com.kickstartlab.android.mmm.rest.models.Location;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class FeedListFragment extends Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private boolean mStreamActive;
    List<Feed> mData;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;
    private SwipeRefreshLayout swipeLayout;
    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private FeedAdapter mAdapter;

    //private MerchantAdapter mAdapter;

    SearchView mSearchView;


    // TODO: Rename and change types of parameters
    public static FeedListFragment newInstance(String param1, String param2) {
        FeedListFragment fragment = new FeedListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FeedListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);
        //EventBus.getDefault().register(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_list, container, false);

        mStreamActive = false;
        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);

        swipeLayout.setColorSchemeResources(R.color.gplus_color_1,R.color.gplus_color_2, R.color.gplus_color_3,R.color.gplus_color_4);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                EventBus.getDefault().post(new FeedEvent("refreshDb"));
            }
        });

        mAdapter = new FeedAdapter(getActivity());

        refreshList();
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().post(new FeedEvent("refreshDb"));

    }


    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem menuItem;

        if(menu.getItem(0).isVisible()){
            menu.getItem(0).setVisible(false);
        }
        menuItem = menu.add(Menu.NONE, R.id.action_radio, 0, R.string.action_radio).setIcon(R.drawable.ic_cast_white_24dp);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

        menuItem = menu.add(Menu.NONE, R.id.action_refresh_feed, 0, R.string.action_refresh).setIcon(R.drawable.ic_sync_white_24dp);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_radio:
                mStreamActive = !mStreamActive;
                if(mStreamActive){
                    item.setIcon(R.drawable.ic_cast_connected_white_24dp);
                }else{
                    item.setIcon(R.drawable.ic_cast_white_24dp);
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*
        try {
            mListener = (OnMerchantInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnLocationInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        super.onDetach();
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

/* Events */

    public void onEvent(FeedEvent fe){
        if("refresh".equalsIgnoreCase(fe.getAction())){
            refreshList();
            swipeLayout.setRefreshing(false);
            //Toast.makeText(getActivity(), me.getAction(), Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(getActivity(), FeedViewActivity.class);

        Feed fe = mData.get(position);

        intent.putExtra("mediaId", fe.getMediaId());
        intent.putExtra("mediaTitle", fe.getMediaTitle());
        intent.putExtra("mediaUrl", fe.getMediaUrl());
        intent.putExtra("mediaType", fe.getMediaType());
        intent.putExtra("coverUrl", fe.getCoverUrl());
        intent.putExtra("message", fe.getMessage());

        startActivity(intent);

        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        //EventBus.getDefault().post(new FeedEvent("select",mData.get(position)));

    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    public void searchList(String word){

        //mData = Merchant.listAll(Merchant.class);
        mData =  Select.from(Feed.class)
                .where( Condition.prop("message").like( "%" + word + "%" ) )
                .orderBy("utime desc").list();

        Log.i("SEARCH",word);

        mAdapter = new FeedAdapter(getActivity().getApplicationContext());

        mAdapter.setData(mData);
        mListView.setAdapter(mAdapter);
        swipeLayout.setRefreshing(false);

    }

    public void refreshList(){

        //mData = Merchant.listAll(Merchant.class);
        mData =  Select.from(Feed.class).orderBy("utime desc").list();
        /*
        mAdapter = new ArrayAdapter<Location>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, mData);
        */
        mAdapter = new FeedAdapter(getActivity().getApplicationContext());
        mAdapter.setData(mData);
        mListView.setAdapter(mAdapter);
        swipeLayout.setRefreshing(false);

    }

}
