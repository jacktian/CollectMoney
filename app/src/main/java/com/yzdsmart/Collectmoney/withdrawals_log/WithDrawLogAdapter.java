package com.yzdsmart.Collectmoney.withdrawals_log;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.PersonalWithdrawLog;
import com.yzdsmart.Collectmoney.bean.ShopWithdrawLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by YZD on 2016/9/24.
 */

public class WithDrawLogAdapter extends RecyclerView.Adapter<WithDrawLogAdapter.ViewHolder> {
    private Integer userType;
    private Context context;
    private List<PersonalWithdrawLog> personalWithdrawLogs;
    private List<ShopWithdrawLog> shopWithdrawLogs;

    public WithDrawLogAdapter(Integer userType, Context context) {
        this.userType = userType;
        this.context = context;
        personalWithdrawLogs = new ArrayList<PersonalWithdrawLog>();
        shopWithdrawLogs = new ArrayList<ShopWithdrawLog>();
    }

    /**
     * 添加列表
     *
     * @param list
     */
    public void appendPersonalLogList(List<PersonalWithdrawLog> list) {
        if (null != personalWithdrawLogs) {
            personalWithdrawLogs.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除列表
     */
    public void clearPersonalList() {
        if (null != personalWithdrawLogs) {
            personalWithdrawLogs.clear();
        }
        notifyDataSetChanged();
    }

    /**
     * 添加列表
     *
     * @param list
     */
    public void appendShopLogList(List<ShopWithdrawLog> list) {
        if (null != shopWithdrawLogs) {
            shopWithdrawLogs.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除列表
     */
    public void clearShopList() {
        if (null != shopWithdrawLogs) {
            shopWithdrawLogs.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.withdraw_log_item, parent, false);
        WithDrawLogAdapter.ViewHolder holder = new WithDrawLogAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (userType) {
            case 0:
                PersonalWithdrawLog personalLog = personalWithdrawLogs.get(position);
                holder.setCoinCounts("" + personalLog.getGold());
                holder.setRmbCounts(personalLog.getCash());
                holder.setWithdrawTime(personalLog.getTimeStr());
                break;
            case 1:
                ShopWithdrawLog shopLog = shopWithdrawLogs.get(position);
                holder.setCoinCounts("" + shopLog.getGold());
                holder.setRmbCounts(shopLog.getCash());
                holder.setWithdrawTime(shopLog.getTimeStr());
                break;
        }
    }

    @Override
    public int getItemCount() {
        switch (userType) {
            case 0:
                return personalWithdrawLogs.size();
            case 1:
                return shopWithdrawLogs.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.coin_counts)
        TextView coinCountsTV;
        @Nullable
        @BindView(R.id.rmb_counts)
        TextView rmbCountsTV;
        @Nullable
        @BindView(R.id.withdraw_time)
        TextView withdrawTimeTV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setCoinCounts(String coinCounts) {
            coinCountsTV.setText(coinCounts);
        }

        public void setRmbCounts(String rmbCounts) {
            rmbCountsTV.setText(rmbCounts);
        }

        public void setWithdrawTime(String withdrawTime) {
            withdrawTimeTV.setText(withdrawTime);
        }
    }
}
