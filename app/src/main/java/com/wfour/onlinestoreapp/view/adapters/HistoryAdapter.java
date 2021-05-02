package com.wfour.onlinestoreapp.view.adapters;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.base.ApiResponse;
import com.wfour.onlinestoreapp.globals.GlobalFunctions;
import com.wfour.onlinestoreapp.interfaces.IConfirmation;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.network1.NetworkUtility;
import com.wfour.onlinestoreapp.objects.HistoryObj;
import com.wfour.onlinestoreapp.utils.AppUtil;
import com.wfour.onlinestoreapp.utils.StringUtil;
import com.wfour.onlinestoreapp.widgets.textview.TextViewBold;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Suusoft on 15/12/2016.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private ArrayList<HistoryObj> listHistory;
    private Context context;

    public HistoryAdapter(Context context, ArrayList<HistoryObj> listHistory) {
        this.listHistory = listHistory;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_reskin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(listHistory.get(position));
    }

    @Override
    public int getItemCount() {
        return listHistory.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextViewBold tvCredits, tvName;
        private TextViewRegular tvTime, tvEmail, lblEmail;
        private FrameLayout btnClose;
        private LinearLayout rlParent;
        private FrameLayout frDivider;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCredits = (TextViewBold) itemView.findViewById(R.id.tv_credits);
            tvName = (TextViewBold) itemView.findViewById(R.id.lbl_deal_name);
            tvTime = (TextViewRegular) itemView.findViewById(R.id.tv_time);
            tvEmail = (TextViewRegular) itemView.findViewById(R.id.tv_email);
            btnClose = (FrameLayout) itemView.findViewById(R.id.btn_close);
            rlParent = (LinearLayout) itemView.findViewById(R.id.ll_parent);
            frDivider = (FrameLayout) itemView.findViewById(R.id.divider);
            lblEmail = (TextViewRegular) itemView.findViewById(R.id.lbl_email);
            btnClose.setOnClickListener(this);
        }

        public void bindData(HistoryObj historyObj) {
            tvCredits.setText(StringUtil.convertNumberToString(Float.parseFloat(historyObj.getCredits()), 1));
            tvTime.setText(historyObj.getTime());
            tvName.setText(historyObj.getName());
            switch (historyObj.getType()) {
                case HistoryObj.CREDIT:
                    tvName.setTextColor(ContextCompat.getColor(context, R.color.green));
                    frDivider.setBackgroundResource(R.drawable.bg_devider_history_credits);
                    rlParent.setVisibility(View.GONE);
                    break;
                case HistoryObj.REDEEM:
                    tvName.setTextColor(ContextCompat.getColor(context, R.color.red));
                    frDivider.setBackgroundResource(R.drawable.bg_devider_history_redem);
                    rlParent.setVisibility(View.GONE);
                    break;
                case HistoryObj.TRANSFER:
                    tvName.setTextColor(ContextCompat.getColor(context, R.color.yellows));
                    frDivider.setBackgroundResource(R.drawable.bg_devider_history_transfer);
                    tvEmail.setText(historyObj.getEmail());
                    rlParent.setVisibility(View.VISIBLE);
                    if (historyObj.getTypeTransaction() == HistoryObj.TRANSFER_TO) {
                        lblEmail.setText("To: ");
                    } else if (historyObj.getTypeTransaction() == HistoryObj.RECEIVE) {
                        lblEmail.setText("From: ");
                    }
                    break;
            }

        }

        @Override
        public void onClick(View view) {
            if (NetworkUtility.getInstance(context).isNetworkAvailable()) {
                GlobalFunctions.showConfirmationDialog(context, context.getString(R.string.msg_delete_item_history_success),
                        context.getString(R.string.yes), context.getString(R.string.no), true, new IConfirmation() {
                    @Override
                    public void onPositive() {
                        deleteItemHistory(getAdapterPosition());
                    }

                    @Override
                    public void onNegative() {

                    }
                });


            } else {
                AppUtil.showToast(context, R.string.msg_network_not_available);
            }

        }

        private void deleteItemHistory(int pos) {
            ModelManager.deleteHistory(context, listHistory.get(pos).getId(), new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    org.json.JSONObject jsonObject = (JSONObject) object;
                    ApiResponse apiResponse = new ApiResponse(jsonObject);
                    if (!apiResponse.isError()) {
                        listHistory.remove(listHistory.get(getAdapterPosition()));
                        notifyItemRemoved(getAdapterPosition());
                        AppUtil.showToast(context, R.string.msg_delete_history_success);
                    }
                }

                @Override
                public void onError() {

                }
            });
        }
    }


}
