package com.yzdsmart.Dingdingwen.payment_log;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.yzdsmart.Dingdingwen.bean.PaymentLog;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by YZD on 2016/12/26.
 */

public class PaymentLogAdapter extends UltimateViewAdapter<PaymentLogAdapter.ViewHolder> {
    private Context context;
    private List<PaymentLog> logList;
    private DecimalFormat decimalFormat;
    private Integer userType;

    public PaymentLogAdapter(Context context) {
        this.context = context;
        logList = new ArrayList<PaymentLog>();
        decimalFormat = new DecimalFormat("#0.00");
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
        userType = SharedPreferencesUtils.getString(context, "baza_code", "").length() > 0 ? 1 : 0;
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
        holder.setUserAvater(userType == 1 ? paymentLog.getImageUrl() : paymentLog.getLogoImageUrl());
        holder.setUserName(userType == 1 ? paymentLog.getCNickName() : paymentLog.getBazaName());
        holder.setCoinCounts(paymentLog.getGoldNum());
        holder.setActualAmount(paymentLog.getPayAmount());
        holder.setCreateTime(paymentLog.getCreateTime());
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
        @BindView(R.id.user_avater)
        CircleImageView userAvaterIV;
        @Nullable
        @BindView(R.id.shop_avater)
        ImageView shopAvaterIV;
        @Nullable
        @BindView(R.id.user_name)
        TextView userNameTV;
        @Nullable
        @BindView(R.id.coin_counts)
        TextView coinCountsTV;
        @Nullable
        @BindView(R.id.actual_amount)
        TextView actualAmountTV;
        @Nullable
        @BindView(R.id.create_time)
        TextView createTimeTV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            switch (userType) {
                case 1:
                    ButterKnife.apply(shopAvaterIV, ((BaseActivity) context).BUTTERKNIFEGONE);
                    ButterKnife.apply(userAvaterIV, ((BaseActivity) context).BUTTERKNIFEVISIBLE);
                    break;
                case 0:
                    ButterKnife.apply(shopAvaterIV, ((BaseActivity) context).BUTTERKNIFEVISIBLE);
                    ButterKnife.apply(userAvaterIV, ((BaseActivity) context).BUTTERKNIFEGONE);
                    break;
            }
        }

        public void setUserAvater(String userAvater) {
            switch (userType) {
                case 1:
                    Glide.with(context).load(userAvater).asBitmap().placeholder(R.mipmap.background_bag_holder_light).error(R.mipmap.background_bag_holder_light).into(userAvaterIV);
                    break;
                case 0:
                    Glide.with(context).load(userAvater).asBitmap().placeholder(R.mipmap.background_bag_holder_light).error(R.mipmap.background_bag_holder_light).into(shopAvaterIV);
                    break;
            }
        }

        public void setUserName(String userName) {
            userNameTV.setText(userName);
        }

        public void setCoinCounts(Double coinCounts) {
            Drawable drawable = getResources().getDrawable(R.mipmap.yzd_coin);
            drawable.setBounds(0, 0, (int) Utils.getScreenRatio(context) * 24, (int) Utils.getScreenRatio(context) * 24);
            coinCountsTV.setCompoundDrawables(drawable, null, null, null);
            switch (userType) {
                case 0:
                    coinCountsTV.setText("0.00".equals(decimalFormat.format(coinCounts)) ? decimalFormat.format(coinCounts) : (" -" + decimalFormat.format(coinCounts)));
                    break;
                case 1:
                    coinCountsTV.setText("0.00".equals(decimalFormat.format(coinCounts)) ? decimalFormat.format(coinCounts) : (" +" + decimalFormat.format(coinCounts)));
                    break;
            }
        }

        public void setActualAmount(Double actualAmount) {
            switch (userType) {
                case 0:
                    actualAmountTV.setText("0.00".equals(decimalFormat.format(actualAmount)) ? decimalFormat.format(actualAmount) : ("-" + decimalFormat.format(actualAmount)));
                    break;
                case 1:
                    actualAmountTV.setText("0.00".equals(decimalFormat.format(actualAmount)) ? decimalFormat.format(actualAmount) : ("+" + decimalFormat.format(actualAmount)));
                    break;
            }
        }

        public void setCreateTime(String createTime) {
            createTimeTV.setText(createTime);
        }
    }
}
