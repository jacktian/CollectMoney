package com.yzdsmart.Dingdingwen.buy_coins;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.ShopPayLog;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by YZD on 2016/11/17.
 */

class ShopPayLogAdapter extends UltimateViewAdapter<ShopPayLogAdapter.ViewHolder> {
    private Context context;
    private List<ShopPayLog> logList;
    private DateTimeFormatter dtf;

    public ShopPayLogAdapter(Context context) {
        this.context = context;
        logList = new ArrayList<ShopPayLog>();
        dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    /**
     * 添加列表
     *
     * @param list
     */
    public void appendList(List<ShopPayLog> list) {
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
        View itemView = LayoutInflater.from(context).inflate(R.layout.shop_pay_log_item, parent, false);
        ShopPayLogAdapter.ViewHolder holder = new ShopPayLogAdapter.ViewHolder(itemView);
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // 如果设置了回调，则设置点击事件
        if (null != mOnItemClickListener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });
        }
        ShopPayLog log = logList.get(position);
        DateTime dateTime = dtf.parseDateTime(log.getCreateTime());
        holder.coinCountsTV.setText("+" + log.getGold());
        holder.payDateTV.setText(dateTime.toString("yyyy-MM-dd"));
        holder.payTimeTV.setText(dateTime.toString("HH:mm:ss"));
        holder.payAmountTV.setText("" + log.getAmount());
        holder.chargeStateIV.setImageDrawable(("未支付".equals(log.getPayStatus())) ? context.getResources().getDrawable(R.mipmap.charge_not_pay) : context.getResources().getDrawable(R.mipmap.charge_already_pay));
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
        @BindView(R.id.pay_date)
        TextView payDateTV;
        @Nullable
        @BindView(R.id.pay_time)
        TextView payTimeTV;
        @Nullable
        @BindView(R.id.coin_counts)
        TextView coinCountsTV;
        @Nullable
        @BindView(R.id.pay_amount)
        TextView payAmountTV;
        @Nullable
        @BindView(R.id.charge_state)
        ImageView chargeStateIV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
