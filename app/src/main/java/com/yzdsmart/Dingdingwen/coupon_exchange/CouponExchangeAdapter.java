package com.yzdsmart.Dingdingwen.coupon_exchange;

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
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.CouponBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by YZD on 2016/12/19.
 */

public class CouponExchangeAdapter extends UltimateViewAdapter<CouponExchangeAdapter.ViewHolder> {
    private Context context;
    private List<CouponBean> couponBeanList;

    public CouponExchangeAdapter(Context context) {
        this.context = context;
        couponBeanList = new ArrayList<CouponBean>();
    }

    /**
     * 添加纪录
     *
     * @param list
     */
    public void appendList(List<CouponBean> list) {
        if (null != couponBeanList) {
            couponBeanList.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除记录
     */
    public void clearList() {
        if (null != couponBeanList && couponBeanList.size() > 0) {
            couponBeanList.clear();
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
        View itemView = LayoutInflater.from(context).inflate(R.layout.coupon_exchange_item, parent, false);
        CouponExchangeAdapter.ViewHolder holder = new CouponExchangeAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public int getAdapterItemCount() {
        return couponBeanList.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CouponBean couponBean = couponBeanList.get(position);
        holder.setCoinLogo(couponBean.getLogoLink());
        holder.setCoinCounts(couponBean.getGoldNum());
        holder.setCoinName(couponBean.getGoldName());
        holder.setCouponContent(couponBean.getShow());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CouponExchangeActivity) context).exchangeCoupon(couponBean);
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
        @Nullable
        @BindView(R.id.coin_name)
        TextView coinNameTV;
        @Nullable
        @BindView(R.id.coupon_content)
        TextView couponContentTV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setCoinLogo(String coinLogo) {
            Glide.with(context).load((null == coinLogo || "".equals(coinLogo)) ? R.mipmap.yzd_coin : coinLogo).asBitmap().placeholder(R.mipmap.ic_holder_light).error(R.mipmap.ic_holder_light).into(coinLogoIV);
        }

        public void setCoinCounts(Integer coinCounts) {
            coinCountsTV.setText(coinCounts + "");
        }

        public void setCoinName(String coinName) {
            coinNameTV.setText(coinName);
        }

        public void setCouponContent(String couponContent) {
            couponContentTV.setText(couponContent);
        }
    }
}
