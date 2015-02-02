package com.kickstartlab.android.mmm.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
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
import com.kickstartlab.android.mmm.adapters.OrderItemAdapter;
import com.kickstartlab.android.mmm.events.OrderEvent;
import com.kickstartlab.android.mmm.rest.models.OrderItem;
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
public class OrderListFragment extends Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Integer mParam1;
    private String mParam2;

    List<OrderItem> mData;

    SearchView mSearchDate, mSearchView;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private OrderItemAdapter mAdapter;


    // TODO: Rename and change types of parameters
    public static OrderListFragment newInstance(Integer param1, String param2) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OrderListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mSearchDate = (SearchView) view.findViewById(R.id.searchDateView);
        mSearchView = (SearchView) view.findViewById(R.id.searchView);

        mSearchDate.setIconifiedByDefault(false);
        mSearchView.setIconifiedByDefault(false);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchList(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchList(s);
                return false;
            }
        });

        mAdapter = new OrderItemAdapter(getActivity());
        //((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        EventBus.getDefault().post(new OrderEvent("refreshById", String.valueOf(mParam1)));
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);

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

    /* Events */

    public void onEvent(OrderEvent me){
        if("refresh".equalsIgnoreCase(me.getAction())){
            refreshList();
            //Toast.makeText(getActivity(), me.getAction(), Toast.LENGTH_LONG).show();
        }

    }

    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem menuItem;

        if(menu.getItem(0).isVisible()){
            menu.getItem(0).setVisible(false);
        }

        menuItem = menu.add(Menu.NONE, R.id.action_scan, 0, R.string.action_scan);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

        menuItem = menu.add(Menu.NONE, R.id.action_refresh, 0, R.string.action_refresh).setIcon(R.drawable.ic_cloud_download_white_24dp);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);


    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            //mListener.onLocations(DummyContent.ITEMS.get(position).id);

            String locationId = mData.get(position).getExtId();
            String locationName = mData.get(position).getName();
            Log.i("locationId", locationId);
            mListener.onMerchant(locationId, locationName);
        }
        */

        EventBus.getDefault().post(new OrderEvent("select",mData.get(position)));

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

    public void searchList(String s){
        //mData = Merchant.listAll(Merchant.class);

        String ql = "SELECT * FROM ORDER_ITEM WHERE merchant_id = ?  AND ( buyerdeliverytime LIKE ? OR buyer_name LIKE ? OR recipient_name LIKE ? ) ORDER BY buyerdeliverytime desc";
        String q = "%" + s + "%";
        String mid = String.valueOf(mParam1);
        /*
        mData =  Select.from(OrderItem.class)
                .where(Condition.prop("merchant_id").eq(String.valueOf(mParam1)))
                .whereOr(Condition.prop("buyerdeliverytime").like("%" + s + "%"), Condition.prop("buyer_name").like("%" + s + "%"), Condition.prop("recipient_name").like("%" + s + "%"))
                .orderBy("buyerdeliverytime desc").list();
        */
        //mAdapter = new ArrayAdapter<OrderItem>(getActivity(),
        //        android.R.layout.simple_list_item_1, android.R.id.text1, mData);

        mData = OrderItem.findWithQuery(OrderItem.class, ql, mid ,q, q, q);
        mAdapter = new OrderItemAdapter(getActivity());
        mAdapter.setData(mData);

        mListView.setAdapter(mAdapter);

    }

    public void refreshList(){
        //mData = Merchant.listAll(Merchant.class);
        mData =  Select.from(OrderItem.class)
                .where(Condition.prop("merchant_id").eq(String.valueOf(mParam1)))
                .orderBy("buyerdeliverytime desc").list();

        //mAdapter = new ArrayAdapter<OrderItem>(getActivity(),
        //        android.R.layout.simple_list_item_1, android.R.id.text1, mData);

        mAdapter = new OrderItemAdapter(getActivity());
        mAdapter.setData(mData);

        mListView.setAdapter(mAdapter);

    }

}
