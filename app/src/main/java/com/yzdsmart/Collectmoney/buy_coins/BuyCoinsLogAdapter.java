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
        View itemView = LayoutInflater.from(context).inflate(R.layout.coins_log_item, parent, false);
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
        holder.coins1TV.setText("" + log.getTotalGold());
        holder.time1TV.setText(dateTime.toString("yyyy-MM-dd"));
        holder.time2TV.setText(dateTime.toString("HH:mm:ss"));
        holder.coins2TV.setText("" + log.getOverGold());
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
        @BindView(R.id.coins_1)
        TextView coins1TV;
        @Nullable
        @BindView(R.id.time_1)
        TextView time1TV;
        @Nullable
        @BindView(R.id.time_2)
        TextView time2TV;
        @Nullable
        @BindView(R.id.coins_2)
        TextView coins2TV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
