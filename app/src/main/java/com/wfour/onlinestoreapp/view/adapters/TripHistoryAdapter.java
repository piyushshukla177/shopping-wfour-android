package com.wfour.onlinestoreapp.view.adapters;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.IConfirmation;
import com.wfour.onlinestoreapp.interfaces.IDeleting;
import com.wfour.onlinestoreapp.objects.PaymentMethodObj;
import com.wfour.onlinestoreapp.objects.TransportDealObj;
import com.wfour.onlinestoreapp.utils.DateTimeUtil;
import com.wfour.onlinestoreapp.utils.StringUtil;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

import java.util.ArrayList;

/**
 * Created by Suusoft on 10/15/2015.
 */
public class TripHistoryAdapter extends RecyclerView.Adapter<TripHistoryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<TransportDealObj> tripHistories;
    private IDeleting iDeleting;

    public TripHistoryAdapter(Context context, ArrayList<TransportDealObj> dealObjs, IDeleting iDeleting) {
        this.context = context;
        this.tripHistories = dealObjs;
        this.iDeleting = iDeleting;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip_history, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (getItemCount() > 0) {
            final TransportDealObj transportDealObj = tripHistories.get(position);
            if (transportDealObj != null) {
                holder.lblTripId.setText(String.format(context.getString(R.string.trip_value), transportDealObj.getId()));
                holder.lblTime.setText(DateTimeUtil.convertTimeStampToDate(transportDealObj.getTime(), "HH:mm, EEE dd/MM/yyyy"));
                holder.lblFrom.setText(transportDealObj.getPickup());
                holder.lblTo.setText(transportDealObj.getDestination());

                if (transportDealObj.isCanceled()) {
                    holder.llCanceledTrip.setVisibility(View.GONE);
                    holder.llEstimateFare.setVisibility(View.GONE);

                    holder.lblStatus.setText(context.getString(R.string.canceled));
                    holder.lblStatus.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                } else {
                    boolean isPaid = transportDealObj.isPaid();
                    if (isPaid) {
                        holder.llCanceledTrip.setVisibility(View.VISIBLE);
                        holder.llEstimateFare.setVisibility(View.GONE);

                        String paymentMethod = "";
                        String fare = String.format(context.getString(R.string.dollar_value),
                                StringUtil.convertNumberToString(transportDealObj.getActualFare(), 1));
                        if (transportDealObj.getPaymentMethod().equals(PaymentMethodObj.CASH)) {
                            paymentMethod = context.getString(R.string.cash);
                        } else if (transportDealObj.getPaymentMethod().equals(PaymentMethodObj.PAYPAL)) {
                            paymentMethod = context.getString(R.string.paypal);
                        } else if (transportDealObj.getPaymentMethod().equals(PaymentMethodObj.CREDITS)) {
                            paymentMethod = context.getString(R.string.credits);
                            fare = String.format(context.getString(R.string.value_credits),
                                    StringUtil.convertNumberToString(transportDealObj.getActualFare(), 0));
                        } else if (transportDealObj.getPaymentMethod().equals(PaymentMethodObj.STRIPE)) {
                            paymentMethod = context.getString(R.string.stripe);
                        } else if (transportDealObj.getPaymentMethod().equals(PaymentMethodObj.OTHER)) {
                            paymentMethod = context.getString(R.string.other);
                        }

                        holder.lblPayment.setText(paymentMethod);
                        holder.lblFare.setText(fare);

                        holder.lblStatus.setText(context.getString(R.string.finished));
                        holder.lblStatus.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                    } else {
                        holder.llCanceledTrip.setVisibility(View.GONE);
                        holder.llEstimateFare.setVisibility(View.VISIBLE);

                        holder.lblStatus.setText(context.getString(R.string.not_paid));
                        holder.lblStatus.setTextColor(ContextCompat.getColor(context, R.color.primary));

                        String estimateFare = String.format(context.getString(R.string.dollar_value),
                                StringUtil.convertNumberToString(transportDealObj.getEstimateFare(), 1));
                        holder.lblEstimateFare.setText(estimateFare);
                    }
                }

                holder.imgRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GlobalFunctions.showConfirmationDialog(context, context.getString(R.string.msg_confirm_deleting_a_trip),
                                context.getString(R.string.delete), context.getString(R.string.no_thank), true, new IConfirmation() {
                                    @Override
                                    public void onPositive() {
                                        iDeleting.onDeleted(transportDealObj);
                                    }

                                    @Override
                                    public void onNegative() {
                                        iDeleting.onCancel();
                                    }
                                });
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        try {
            return tripHistories.size();
        } catch (NullPointerException ex) {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgRemove;
        private TextViewRegular lblTripId, lblTime, lblFrom, lblTo, lblPayment, lblStatus, lblFare, lblEstimateFare;
        private LinearLayout llCanceledTrip, llEstimateFare;

        public ViewHolder(View view) {
            super(view);

            imgRemove = (ImageView) view.findViewById(R.id.img_remove);
            lblTripId = (TextViewRegular) view.findViewById(R.id.lbl_trip_id);
            lblTime = (TextViewRegular) view.findViewById(R.id.lbl_time);
            lblFrom = (TextViewRegular) view.findViewById(R.id.lbl_from);
            lblTo = (TextViewRegular) view.findViewById(R.id.lbl_to);
            lblPayment = (TextViewRegular) view.findViewById(R.id.lbl_payment);
            lblStatus = (TextViewRegular) view.findViewById(R.id.lbl_status);
            lblFare = (TextViewRegular) view.findViewById(R.id.lbl_fare);
            llCanceledTrip = (LinearLayout) view.findViewById(R.id.ll_canceled_trip);
            llEstimateFare = (LinearLayout) view.findViewById(R.id.ll_estimate_fare);
            lblEstimateFare = (TextViewRegular) view.findViewById(R.id.lbl_estimate_fare);
        }
    }
}
