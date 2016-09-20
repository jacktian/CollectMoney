package com.yzdsmart.Collectmoney.focused_shop;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.FocusedShop;
import com.yzdsmart.Collectmoney.personal_coin_list.PersonalCoinsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by YZD on 2016/9/20.
 */

public class FocusedShopAdapter extends RecyclerView.Adapter<FocusedShopAdapter.ViewHolder> {
    private Context context;
    private List<FocusedShop> focusedShops;

    public FocusedShopAdapter(Context context) {
        this.context = context;
        focusedShops = new ArrayList<FocusedShop>();
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
        if (null != focusedShops) {
            focusedShops.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.focused_shop_item, parent, false);
        FocusedShopAdapter.ViewHolder holder = new FocusedShopAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FocusedShop shop = focusedShops.get(position);
        holder.setShopName(shop.getName());
        holder.setShopPers(shop.getPers());
        holder.setShopTel(shop.getTel());
        holder.setDailyCoins("" + shop.getTodayGlodNum());
        holder.setShopAddress(shop.getAddr());
    }

    @Override
    public int getItemCount() {
        return focusedShops.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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
