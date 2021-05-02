package com.wfour.onlinestoreapp.view.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wfour.onlinestoreapp.R;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManager;
import com.wfour.onlinestoreapp.network.modelmanager.ModelManagerListener;
import com.wfour.onlinestoreapp.objects.RevenueObj;
import com.wfour.onlinestoreapp.parsers.JSONParser;
import com.wfour.onlinestoreapp.utils.DateTimeUtil;
import com.wfour.onlinestoreapp.utils.NetworkUtility;
import com.wfour.onlinestoreapp.utils.StringUtil;
import com.wfour.onlinestoreapp.widgets.textview.TextViewBold;
import com.wfour.onlinestoreapp.widgets.textview.TextViewRegular;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Suusoft on 30/11/2016.
 */

public class AccountingFragment extends BaseFragment {

    private static final String TAG = AccountingFragment.class.getSimpleName();

    public static final String TYPE_DEAL = "deal";
    public static final String TYPE_TRIP = "trip";

    private static final String PARAM_TYPE = "type";

    private static final int MONTH_COLUMN = 4;

    private TextViewBold mLblTotalMonthRevenue, mLblTotalYearRevenue;
    private TextViewRegular mLblRevenue, mLblExpense, mLblYear;

    private RecyclerView mRclMonth;
    private int mSelectedMonth;

    private ArrayList<RevenueObj> mRevenueObjs;

    private String mType = "";

    public static AccountingFragment newInstance(String type) {
        AccountingFragment fragment = new AccountingFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_TYPE, type);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(PARAM_TYPE)) {
                mType = bundle.getString(PARAM_TYPE);
            }
        }
    }

    @Override
    View inflateLayout(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accounting, container, false);
    }

    @Override
    void initUI(View view) {
        mLblTotalYearRevenue = (TextViewBold) view.findViewById(R.id.lbl_year_revenue);
        mLblTotalMonthRevenue = (TextViewBold) view.findViewById(R.id.lbl_month_revenue);
        mLblRevenue = (TextViewRegular) view.findViewById(R.id.lbl_revenue);
        mLblExpense = (TextViewRegular) view.findViewById(R.id.lbl_expense);
        mLblYear = (TextViewRegular) view.findViewById(R.id.lbl_year);

        mRclMonth = (RecyclerView) view.findViewById(R.id.rcl_months);
        mRclMonth.setLayoutManager(new GridLayoutManager(self, MONTH_COLUMN, GridLayoutManager.VERTICAL, false));
        mRclMonth.setHasFixedSize(true);

        // Should call this method at the end of declaring UI
        initData();
    }

    @Override
    void initControl() {
    }

    @Override
    public void onResume() {
        super.onResume();

        // Get revenue from server
        getRevenue();
    }

    private void initData() {
        // Get current time
        mSelectedMonth = Integer.parseInt(DateTimeUtil.convertTimeStampToDate(DateTimeUtil.getCurrentTime(), "MM"));

        // Init year, month
        mLblYear.setText(DateTimeUtil.convertTimeStampToDate(DateTimeUtil.getCurrentTime(), "yyyy"));
        mRclMonth.setAdapter(new MonthAdapter());
    }

    private void getRevenue() {
        if (mRevenueObjs == null) {
            mRevenueObjs = new ArrayList<>();
        } else {
            mRevenueObjs.clear();
        }
        if (NetworkUtility.isNetworkAvailable()) {
            ModelManager.getAccounting(self, mType, new ModelManagerListener() {
                @Override
                public void onSuccess(Object object) {
                    JSONObject jsonObject = (JSONObject) object;
                    if (JSONParser.responseIsSuccess(jsonObject)) {
                        ArrayList<RevenueObj> arrTemp = JSONParser.parseRevenues(jsonObject);
                        if (arrTemp.size() > 0) {
                            mRevenueObjs.addAll(arrTemp);

                            fillRevenue(mRevenueObjs.get(mSelectedMonth - 1));
                        }
                    } else {
                        Toast.makeText(self, JSONParser.getMessage(jsonObject), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError() {
                }
            });
        }
    }

    private void fillRevenue(RevenueObj revenueObj) {
        if (revenueObj != null) {
            float yearRevenue = 0;
            for (int i = 0; i < mRevenueObjs.size(); i++) {
                yearRevenue += mRevenueObjs.get(i).getRevenue() - mRevenueObjs.get(i).getExpense();
            }
            String totalYearRevenue = String.format(getString(R.string.dollar_value), StringUtil.convertNumberToString(yearRevenue, 1));
            if (yearRevenue < 0) {
                float absRevenue = yearRevenue * -1;
                totalYearRevenue = String.format(getString(R.string.negative_dollar_value), StringUtil.convertNumberToString(absRevenue, 1));
            }
            mLblTotalYearRevenue.setText(totalYearRevenue);

            String totalMonthRevenue = String.format(getString(R.string.dollar_value),
                    StringUtil.convertNumberToString(revenueObj.getTotalRevenuePerMonth(), 1));
            if (revenueObj.getTotalRevenuePerMonth() < 0) {
                float absRevenue = revenueObj.getTotalRevenuePerMonth() * -1;
                totalMonthRevenue = String.format(getString(R.string.negative_dollar_value),
                        StringUtil.convertNumberToString(absRevenue, 1));
            }
            mLblTotalMonthRevenue.setText(totalMonthRevenue);
            mLblRevenue.setText(String.format(getString(R.string.dollar_value),
                    StringUtil.convertNumberToString(revenueObj.getRevenue(), 1)));
            mLblExpense.setText(String.format(getString(R.string.dollar_value),
                    StringUtil.convertNumberToString(revenueObj.getExpense(), 1)));
        }
    }

    class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.ViewHolder> {

        private String[] months;
        private ArrayList<Integer> monthsInLastRow, monthsInLastColumn;

        public MonthAdapter() {
            if (months == null) {
                months = getResources().getStringArray(R.array.months);

                int row = months.length / MONTH_COLUMN;

                monthsInLastRow = new ArrayList<>();
                monthsInLastColumn = new ArrayList<>();

                int currentRow = 0, currentColumn = 0;
                for (int i = 0; i < months.length; i++) {
                    if (currentRow < row) {
                        // Keep months in last row
                        if (currentRow == row - 1) {
                            monthsInLastRow.add(i);
                        }

                        currentColumn++;
                        if (currentColumn == MONTH_COLUMN) {
                            // Keep months in last column
                            monthsInLastColumn.add(i);

                            // Move to next row
                            currentRow++;

                            currentColumn = 0;
                        }
                    }
                }
            }
        }

        @Override
        public MonthAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_month, parent, false);

            return new MonthAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MonthAdapter.ViewHolder holder, final int position) {
            if (getItemCount() > 0) {
                final String month = months[position];
                if (!month.isEmpty()) {
                    holder.lblMonth.setText(month);
                    if (position == (mSelectedMonth - 1)) {
                        holder.lblMonth.setTextColor(ContextCompat.getColor(self, R.color.white));
                        holder.lblMonth.setBackgroundResource(R.drawable.bg_cycle_primary);
                    } else {
                        holder.lblMonth.setTextColor(ContextCompat.getColor(self, R.color.textColorPrimary));
                        holder.lblMonth.setBackgroundColor(ContextCompat.getColor(self, R.color.white));
                    }

                    // Hide the last dividers
                    if (!monthsInLastRow.contains(position)) {
                        holder.horizontalDivider.setVisibility(View.VISIBLE);
                    } else {
                        holder.horizontalDivider.setVisibility(View.GONE);
                    }
                    if (!monthsInLastColumn.contains(position)) {
                        holder.verticalDivider.setVisibility(View.VISIBLE);
                    } else {
                        holder.verticalDivider.setVisibility(View.GONE);
                    }

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mRevenueObjs != null && mRevenueObjs.size() > 0) {
                                if (position != (mSelectedMonth - 1)) {
                                    mSelectedMonth = position + 1;

                                    notifyDataSetChanged();

                                    // Update revenue data by month
                                    fillRevenue(mRevenueObjs.get(mSelectedMonth - 1));
                                }
                            }
                        }
                    });
                }
            }
        }

        @Override
        public int getItemCount() {
            try {
                return months.length;
            } catch (NullPointerException ex) {
                return 0;
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextViewRegular lblMonth;
            private View horizontalDivider, verticalDivider;

            public ViewHolder(View view) {
                super(view);

                lblMonth = (TextViewRegular) view.findViewById(R.id.lbl_month);
                horizontalDivider = view.findViewById(R.id.horizontal_divider);
                verticalDivider = view.findViewById(R.id.vertical_divider);
            }
        }
    }
}
