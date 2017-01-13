package com.yzdsmart.Dingdingwen.main;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.CoinType;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by YZD on 2016/12/2.
 */

public class BackgroundBagAdapter extends UltimateViewAdapter<BackgroundBagAdapter.ViewHolder> {
    private Context context;
    private DecimalFormat decimalFormat;
    private List<CoinType> coinTypeList;

    public BackgroundBagAdapter(Context context) {
        this.context = context;
        coinTypeList = new ArrayList<CoinType>();
        decimalFormat = new DecimalFormat("#0.00");
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
    }

    /**
     * 添加纪录
     *
     * @param list
     */
    public void appendList(List<CoinType> list) {
        if (null != coinTypeList) {
            coinTypeList.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除记录
     */
    public void clearList() {
        if (null != coinTypeList && coinTypeList.size() > 0) {
            coinTypeList.clear();
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
        View itemView = LayoutInflater.from(context).inflate(R.layout.background_bag_item, parent, false);
        BackgroundBagAdapter.ViewHolder holder = new BackgroundBagAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public int getAdapterItemCount() {
        return coinTypeList.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CoinType coinType = coinTypeList.get(position);
        holder.setCoinLogo(coinType.getLogoLink());
        holder.setCoinCounts(coinType.getGCount());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (0 == coinType.getGoldType()) {
                    ((MainActivity) context).onDismissBackgroundBag();
                    ((BaseActivity) context).showSnackbar("普通金币暂不支持兑换");
                    return;
                }
                ((MainActivity) context).exchangeCoupon(coinType.getGoldType());
            }
        });
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
        @BindView(R.id.coin_logo)
        ImageView coinLogoIV;
        @Nullable
        @BindView(R.id.coin_counts)
        TextView coinCountsTV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setCoinLogo(String logoUrl) {
            Glide.with(context).load((null == logoUrl || "".equals(logoUrl)) ? R.mipmap.yzd_coin : logoUrl).asBitmap().placeholder(context.getResources().getDrawable(R.mipmap.ic_holder_light)).error(context.getResources().getDrawable(R.mipmap.coin_logo_fail_default)).into(coinLogoIV);
        }

        public void setCoinCounts(Double coinCounts) {
            coinCountsTV.setText(decimalFormat.format(coinCounts));
        }
    }
}
