package com.yzdsmart.Dingdingwen.main;

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
import com.yzdsmart.Dingdingwen.bean.MarketShop;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jacks on 2017/3/8.
 */

public class MarketShopsAdapter extends UltimateViewAdapter<MarketShopsAdapter.ViewHolder> {
    private Context context;
    private List<MarketShop> marketShops;
    private DecimalFormat decimalFormat;

    public MarketShopsAdapter(Context context) {
        this.context = context;
        marketShops = new ArrayList<MarketShop>();
        decimalFormat = new DecimalFormat("#0.00");
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
    }

    /**
     * 添加列表
     *
     * @param list
     */
    public void appendList(List<MarketShop> list) {
        if (null != marketShops) {
            marketShops.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除列表
     */
    public void clearList() {
        if (null != marketShops && marketShops.size() > 0) {
            marketShops.clear();
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
        View itemView = LayoutInflater.from(context).inflate(R.layout.market_shops_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public int getAdapterItemCount() {
        return marketShops.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MarketShop marketShop = marketShops.get(position);
        holder.setShopNameTV(marketShop.getName());
        holder.setCoinCountsTV(decimalFormat.format(marketShop.getReleGold()));
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setShopNameTV(String shopName) {
            shopNameTV.setText(shopName);
        }

        public void setCoinCountsTV(String coinCounts) {
            coinCountsTV.setText(coinCounts);
        }
    }
}
