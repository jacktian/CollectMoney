package com.yzdsmart.Collectmoney.buy_coins;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.BuyCoinsLog;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by YZD on 2016/9/5.
 */
public class BuyCoinsLogAdapter extends UltimateViewAdapter<BuyCoinsLogAdapter.ViewHolder> {
    private Context context;
    private List<BuyCoinsLog> logList;
    private DateTimeFormatter dtf;

    public BuyCoinsLogAdapter(Context context) {
        this.context = context;
        logList = new ArrayList<BuyCoinsLog>();
        dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 添加列表
     *
     * @param list
     */
    public void appendList(List<BuyCoinsLog> list) {
        if (null != logList) {
            logList.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除列表
     */
    public void clearList() {
        if (null != logList) {
            logList.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.buy_coins_log_item, parent, false);
        BuyCoinsLogAdapter.ViewHolder holder = new BuyCoinsLogAdapter.ViewHolder(itemView);
        return holder;
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
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BuyCoinsLog log = logList.get(position);
        DateTime dateTime = dtf.parseDateTime(log.getCreateTime());
        holder.coinCountsTV.setText("+" + log.getTotalGold());
        holder.buyDateTV.setText(dateTime.toString("yyyy-MM-dd"));
        holder.buyTimeTV.setText(dateTime.toString("HH:mm:ss"));
        holder.leftCoinCountsTV.setText("" + log.getOverGold());
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return logList.size();
    }

    @Override
    public int getAdapterItemCount() {
        return logList.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.buy_date)
        TextView buyDateTV;
        @Nullable
        @BindView(R.id.buy_time)
        TextView buyTimeTV;
        @Nullable
        @BindView(R.id.coin_counts)
        TextView coinCountsTV;
        @Nullable
        @BindView(R.id.left_coin_counts)
        TextView leftCoinCountsTV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
