package com.yzdsmart.Dingdingwen.withdrawals_log;

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
import com.yzdsmart.Dingdingwen.bean.PersonalWithdrawLog;
import com.yzdsmart.Dingdingwen.bean.ShopWithdrawLog;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by YZD on 2016/9/24.
 */

public class WithDrawLogAdapter extends UltimateViewAdapter<WithDrawLogAdapter.ViewHolder> {
    private Integer userType;
    private Context context;
    private List<PersonalWithdrawLog> personalWithdrawLogs;
    private List<ShopWithdrawLog> shopWithdrawLogs;
    private DateTimeFormatter dtf;
    private DecimalFormat decimalFormat;

    public WithDrawLogAdapter(Integer userType, Context context) {
        this.userType = userType;
        this.context = context;
        personalWithdrawLogs = new ArrayList<PersonalWithdrawLog>();
        shopWithdrawLogs = new ArrayList<ShopWithdrawLog>();
        dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        decimalFormat = new DecimalFormat("#0.00");
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
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
        if (null != personalWithdrawLogs && personalWithdrawLogs.size() > 0) {
            personalWithdrawLogs.clear();
            notifyDataSetChanged();
        }
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
        if (null != shopWithdrawLogs && shopWithdrawLogs.size() > 0) {
            shopWithdrawLogs.clear();
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
        View itemView = LayoutInflater.from(context).inflate(R.layout.withdraw_log_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public int getAdapterItemCount() {
        switch (userType) {
            case 0:
                return personalWithdrawLogs.size();
            case 1:
                return shopWithdrawLogs.size();
        }
        return 0;
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DateTime dateTime = null;
        String payStatus = "";
        switch (userType) {
            case 0:
                PersonalWithdrawLog personalLog = personalWithdrawLogs.get(position);
                holder.setCoinCounts("-" + decimalFormat.format(personalLog.getGold()));
                holder.setRmbCounts("+" + decimalFormat.format(Double.valueOf(personalLog.getCash())));
                dateTime = dtf.parseDateTime(personalLog.getCreateTime());
                payStatus = personalLog.getPayStatus();
                break;
            case 1:
                ShopWithdrawLog shopLog = shopWithdrawLogs.get(position);
                holder.setCoinCounts("-" + decimalFormat.format(shopLog.getGold()));
                holder.setRmbCounts("+" + decimalFormat.format(Double.valueOf(shopLog.getCash())));
                dateTime = dtf.parseDateTime(shopLog.getCreateTime());
                payStatus = shopLog.getPayStatus();
                break;
        }
        holder.setWithdrawDate(dateTime.toString("yyyy-MM-dd"));
        holder.setWithdrawTime(dateTime.toString("HH:mm:ss"));
        if ("异常".equals(payStatus)) {
            holder.setLogState(R.mipmap.withdraw_error);
        } else if ("未支付".equals(payStatus)) {
            holder.setLogState(R.mipmap.withdraw_wait);
        } else if ("已经支付".equals(payStatus)) {
            holder.setLogState(R.mipmap.withdraw_finish);
        } else if ("退款".equals(payStatus)) {
            holder.setLogState(R.mipmap.withdraw_return);
        }
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
        @BindView(R.id.withdraw_date)
        TextView withdrawDateTV;
        @Nullable
        @BindView(R.id.withdraw_time)
        TextView withdrawTimeTV;
        @Nullable
        @BindView(R.id.coin_counts)
        TextView coinCountsTV;
        @Nullable
        @BindView(R.id.rmb_counts)
        TextView rmbCountsTV;
        @Nullable
        @BindView(R.id.log_state)
        ImageView logStateIV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setWithdrawDate(String withdrawDate) {
            withdrawDateTV.setText(withdrawDate);
        }

        public void setWithdrawTime(String withdrawTime) {
            withdrawTimeTV.setText(withdrawTime);
        }

        public void setCoinCounts(String coinCounts) {
            coinCountsTV.setText(coinCounts);
        }

        public void setRmbCounts(String rmbCounts) {
            rmbCountsTV.setText(rmbCounts);
        }

        public void setLogState(int resId) {
            Glide.with(context).load(resId).asBitmap().placeholder(R.mipmap.ic_holder_light).error(R.mipmap.ic_holder_light).into(logStateIV);
        }
    }
}
