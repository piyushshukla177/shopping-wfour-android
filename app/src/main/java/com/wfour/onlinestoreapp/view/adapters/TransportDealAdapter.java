package com.wfour.onlinestoreapp.view.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.interfaces.ITransportDeal;
import com.wfour.onlinestoreapp.objects.TransportDealObj;
import com.wfour.onlinestoreapp.utils.DateTimeUtil;
import com.wfour.onlinestoreapp.utils.StringUtil;
import com.wfour.onlinestoreapp.widgets.textview.TextViewBold;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

import java.util.ArrayList;

/**
 * Created by Suusoft on 10/15/2015.
 */
public class TransportDealAdapter extends RecyclerView.Adapter<TransportDealAdapter.ViewHolder> {

    private Context context;
    private ArrayList<TransportDealObj> transportDeals;
    private ITransportDeal iTransportDeal;

    public TransportDealAdapter(Context context, ArrayList<TransportDealObj> dealObjs, ITransportDeal iTransportDeal) {
        this.context = context;
        this.transportDeals = dealObjs;
        this.iTransportDeal = iTransportDeal;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transport_deal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (getItemCount() > 0) {
            final TransportDealObj transportDealObj = transportDeals.get(position);
            if (transportDealObj != null) {
                holder.lblTripId.setText(String.format(context.getString(R.string.trip_value), transportDealObj.getId()));
                holder.lblTime.setText(DateTimeUtil.convertTimeStampToDate(transportDealObj.getTime(), "HH:mm, EEE dd/MM/yyyy"));
                holder.lblFrom.setText(transportDealObj.getPickup());
                holder.lblTo.setText(transportDealObj.getDestination());

                String routeDistance = "";
                if (transportDealObj.getRouteDistance() >= 100) {
                    routeDistance = String.format(context.getString(R.string.value_km),
                            StringUtil.convertNumberToString((transportDealObj.getRouteDistance() / 1000), 1));
                } else {
                    routeDistance = String.format(context.getString(R.string.value_meter),
                            StringUtil.convertNumberToString(transportDealObj.getRouteDistance(), 0));
                }
                holder.lblDistance.setText(routeDistance);

                holder.lblETA.setText(DateTimeUtil.convertTimeStampToDate(transportDealObj.getRouteDuration() + transportDealObj.getTime(),
                        "HH:mm"));
                holder.lblFare.setText(String.format(context.getString(R.string.dollar_value),
                        StringUtil.convertNumberToString(transportDealObj.getEstimateFare(), 1)));

                holder.imgCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        iTransportDeal.onCancel(transportDealObj);
                    }
                });

                holder.lblTrack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        iTransportDeal.onTracking(transportDealObj);
                    }
                });

                holder.lblFinish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        iTransportDeal.onFinish(transportDealObj);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        try {
            return transportDeals.size();
        } catch (NullPointerException ex) {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgCancel;
        private TextViewRegular lblTripId, lblTime, lblFrom, lblTo, lblDistance, lblETA, lblFare;
        private TextViewBold lblTrack, lblFinish;

        public ViewHolder(View view) {
            super(view);

            imgCancel = (ImageView) view.findViewById(R.id.img_cancel);
            lblTripId = (TextViewRegular) view.findViewById(R.id.lbl_trip_id);
            lblTime = (TextViewRegular) view.findViewById(R.id.lbl_time);
            lblFrom = (TextViewRegular) view.findViewById(R.id.lbl_from);
            lblTo = (TextViewRegular) view.findViewById(R.id.lbl_to);
            lblDistance = (TextViewRegular) view.findViewById(R.id.lbl_distance);
            lblETA = (TextViewRegular) view.findViewById(R.id.lbl_eta);
            lblFare = (TextViewRegular) view.findViewById(R.id.lbl_fare);
            lblTrack = (TextViewBold) view.findViewById(R.id.lbl_track);
            lblFinish = (TextViewBold) view.findViewById(R.id.lbl_finish);
        }
    }
}
