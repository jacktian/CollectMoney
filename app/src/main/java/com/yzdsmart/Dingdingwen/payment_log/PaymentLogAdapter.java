package com.yzdsmart.Dingdingwen.payment_log;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.PaymentLog;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by YZD on 2016/12/26.
 */

public class PaymentLogAdapter extends UltimateViewAdapter<PaymentLogAdapter.ViewHolder> {
    private Context context;
    private List<PaymentLog> logList;
    private DecimalFormat decimalFormat;

    public PaymentLogAdapter(Context context) {
        this.context = context;
        logList = new ArrayList<PaymentLog>();
        decimalFormat = new DecimalFormat("#0.00");
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
    }

    /**
     * 添加列表
     *
     * @param list
     */
    public void appendList(List<PaymentLog> list) {
        if (null != logList) {
            logList.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除列表
     */
    public void clearList() {
        if (null != logList && logList.size() > 0) {
            logList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public ViewHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.payment_log_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public int getAdapterItemCount() {
        return logList.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PaymentLog paymentLog = logList.get(position);
        holder.setShopName(paymentLog.getBazaName());
        holder.setCoinCounts(paymentLog.getGoldNum());
        holder.setTotalAmount(paymentLog.getAmount());
        holder.setActualAmount(paymentLog.getPayAmount());
        holder.setTimeDuration(paymentLog.getTimeStr());
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    class ViewHolder extends UltimateRecyclerviewViewHolder {
        @Nullable
        @BindView(R.id.shop_name)
        TextView shopNameTV;
        @Nullable
        @BindView(R.id.coin_counts)
        TextView coinCountsTV;
        @Nullable
        @BindView(R.id.total_amount)
        TextView totalAmountTV;
        @Nullable
        @BindView(R.id.actual_amount)
        TextView actualAmountTV;
        @Nullable
        @BindView(R.id.time_duration)
        TextView timeDurationTV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setShopName(String shopName) {
            shopNameTV.setText(shopName);
        }

        public void setCoinCounts(Double coinCounts) {
            coinCountsTV.setText(decimalFormat.format(coinCounts));
        }

        public void setTotalAmount(Double totalAmount) {
            totalAmountTV.setText(decimalFormat.format(totalAmount));
        }

        public void setActualAmount(Double actualAmount) {
            actualAmountTV.setText(decimalFormat.format(actualAmount));
        }

        public void setTimeDuration(String timeDuration) {
            timeDurationTV.setText(timeDuration);
        }
    }
}
