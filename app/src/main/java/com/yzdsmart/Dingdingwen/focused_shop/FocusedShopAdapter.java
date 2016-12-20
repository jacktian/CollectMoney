package com.yzdsmart.Dingdingwen.focused_shop;

import android.content.Context;
import android.os.Bundle;
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
import com.yzdsmart.Dingdingwen.bean.FocusedShop;
import com.yzdsmart.Dingdingwen.shop_details.ShopDetailsActivity;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by YZD on 2016/9/20.
 */

public class FocusedShopAdapter extends UltimateViewAdapter<FocusedShopAdapter.ViewHolder> {
    private Context context;
    private List<FocusedShop> focusedShops;
    private DecimalFormat decimalFormat;

    public FocusedShopAdapter(Context context) {
        this.context = context;
        focusedShops = new ArrayList<FocusedShop>();
        decimalFormat = new DecimalFormat("#0.00");
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
    }

    /**
     * 添加列表
     *
     * @param list
     */
    public void appendList(List<FocusedShop> list) {
        if (null != focusedShops) {
            focusedShops.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除列表
     */
    public void clearList() {
        if (null != focusedShops && focusedShops.size() > 0) {
            focusedShops.clear();
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
        View itemView = LayoutInflater.from(context).inflate(R.layout.focused_shop_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public int getAdapterItemCount() {
        return focusedShops.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final FocusedShop shop = focusedShops.get(position);
        holder.setShopAvaterIV(shop.getLogoImageUrl());
        holder.setShopName(shop.getName());
        holder.setShopPers(shop.getPers());
        holder.setShopTel(shop.getTel());
        holder.setDailyCoins(decimalFormat.format(shop.getTodayGlodNum()));
        holder.setShopAddress(shop.getAddr());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("bazaCode", shop.getBazaCode());
                ((BaseActivity) context).openActivity(ShopDetailsActivity.class, bundle, 0);
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
        @BindView(R.id.focused_shop_avater)
        ImageView shopAvaterIV;
        @Nullable
        @BindView(R.id.focused_shop_name)
        TextView shopNameTV;
        @Nullable
        @BindView(R.id.focused_shop_pers)
        TextView shopPersTV;
        @Nullable
        @BindView(R.id.focused_shop_tel)
        TextView shopTelTV;
        @Nullable
        @BindView(R.id.focused_shop_daily_coins)
        TextView dailyCoinsTV;
        @Nullable
        @BindView(R.id.focused_shop_address)
        TextView shopAddressTV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setShopAvaterIV(String shopAvater) {
            Glide.with(context).load(shopAvater).asBitmap().placeholder(context.getResources().getDrawable(R.mipmap.ic_holder_light)).error(context.getResources().getDrawable(R.mipmap.ic_holder_light)).into(shopAvaterIV);
        }

        public void setShopName(String shopName) {
            shopNameTV.setText(shopName);
        }

        public void setShopPers(String shopPers) {
            shopPersTV.setText(shopPers);
        }

        public void setShopTel(String shopTel) {
            shopTelTV.setText(shopTel);
        }

        public void setDailyCoins(String dailyCoins) {
            dailyCoinsTV.setText(dailyCoins);
        }

        public void setShopAddress(String shopAddress) {
            shopAddressTV.setText(shopAddress);
        }
    }
}
