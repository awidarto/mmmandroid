package com.kickstartlab.android.mmm.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.kickstartlab.android.mmm.R;
import com.kickstartlab.android.mmm.events.ScannerEvent;

import de.greenrobot.event.EventBus;

public class MessageDialogFragment extends DialogFragment {
    public interface MessageDialogListener {
        public void onDialogPositiveClick(MessageDialogFragment dialog, String mDeliveryId);
    }

    private String mTitle;
    private String mMessage;
    private String mDevice;
    private String mCourier;
    private String mMerchant;
    public String mDeliveryId;
    private String mInvoice;
    private String mBuyerName;
    private String mRecipientName;
    private String mDeliveryDate;

    private MessageDialogListener mListener;

    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRetainInstance(true);
    }

    public static MessageDialogFragment newInstance(String title, String message,
                                                    String device, String buyer, String recipient,String deliverydate,
                                                    String courier,
                                                    String merchant, String deliveryid,
                                                    String invoice,
                                                    MessageDialogListener listener) {
        MessageDialogFragment fragment = new MessageDialogFragment();
        fragment.mTitle = title;
        fragment.mMessage = message;
        fragment.mListener = listener;
        fragment.mDevice = device;
        fragment.mCourier = courier;
        fragment.mMerchant = merchant;
        fragment.mDeliveryId = deliveryid;
        fragment.mInvoice = invoice;
        fragment.mBuyerName = buyer;
        fragment.mRecipientName = recipient;
        fragment.mDeliveryDate = deliverydate;


        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View mview = inflater.inflate(R.layout.dialog_message_layout,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //builder.setMessage(mMessage)
        //        .setTitle(mTitle);

        builder.setView(mview);

        TextView txtDevice = (TextView) mview.findViewById(R.id.deviceValue);
        TextView txtCourier = (TextView) mview.findViewById(R.id.courierValue);
        TextView txtDeliveryId = (TextView) mview.findViewById(R.id.deliveryIdValue);
        TextView txtInvoice = (TextView) mview.findViewById(R.id.invoiceValue);
        TextView txtMerchant = (TextView) mview.findViewById(R.id.merchantValue);
        TextView txtDeliveryDate = (TextView) mview.findViewById(R.id.dateValue);
        TextView txtRecipientName = (TextView) mview.findViewById(R.id.recipientValue);
        TextView txtBuyerName = (TextView) mview.findViewById(R.id.buyerValue);

        txtDevice.setText(mDevice);
        txtCourier.setText(mCourier);
        txtDeliveryId.setText(mDeliveryId);
        txtMerchant.setText(mMerchant);
        txtInvoice.setText(mInvoice);
        txtBuyerName.setText(mBuyerName);
        txtRecipientName.setText(mRecipientName);
        txtDeliveryDate.setText(mDeliveryDate);

        builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(mListener != null) {
                    Log.i("delivery id on dialog", mDeliveryId);
                    mListener.onDialogPositiveClick(MessageDialogFragment.this, mDeliveryId);
                    //EventBus.getDefault().post(new OrderEvent("status", mDeliveryId));
                }
            }
        });

        /*
        builder.setNeutralButton(R.string.action_to_ds, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EventBus.getDefault().post(new ScannerEvent("resume"));
            }
        });
        */

        builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EventBus.getDefault().post(new ScannerEvent("resume"));
            }
        });



        return builder.create();
    }
}
