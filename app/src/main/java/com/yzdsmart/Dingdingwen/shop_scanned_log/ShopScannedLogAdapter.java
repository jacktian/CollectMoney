package com.yzdsmart.Dingdingwen.shop_scanned_log;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.ScannedLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by YZD on 2016/10/9.
 */

public class ShopScannedLogAdapter extends UltimateViewAdapter<ShopScannedLogAdapter.ViewHolder> {
    private Context context;
    private List<ScannedLog> scannedLogs;

    public ShopScannedLogAdapter(Context context) {
        this.context = context;
        scannedLogs = new ArrayList<ScannedLog>();
    }

    /**
     * 添加列表
     *
     * @param list
     */
    public void appendList(List<ScannedLog> list) {
        if (null != scannedLogs) {
            scannedLogs.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除列表
     */
    public void clearList() {
        if (null != scannedLogs && scannedLogs.size() > 0) {
            scannedLogs.clear();
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
        View itemView = LayoutInflater.from(context).inflate(R.layout.shop_scanner_list_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public int getAdapterItemCount() {
        return scannedLogs.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ScannedLog scannedLog = scannedLogs.get(position);
        holder.setScannerAvater(scannedLog.getImageUrl());
        holder.setScannerName(scannedLog.getCNickName());
        holder.setCoinCounts(scannedLog.getGold());
        holder.setGetTime(scannedLog.getTimeStr());
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
        @BindView(R.id.scanner_user_avater)
        CircleImageView scannerAvaterIV;
        @Nullable
        @BindView(R.id.scanner_user_name)
        TextView scannerNameTV;
        @Nullable
        @BindView(R.id.get_coin_counts)
        TextView coinCountsTV;
        @Nullable
        @BindView(R.id.get_time)
        TextView getTimeTV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setScannerAvater(String followerAvater) {
            Glide.with(context).load(followerAvater).error(context.getResources().getDrawable(R.mipmap.user_avater)).into(scannerAvaterIV);
        }

        public void setScannerName(String followerName) {
            scannerNameTV.setText(followerName);
        }

        public void setCoinCounts(Float coinCounts) {
            coinCountsTV.setText("" + coinCounts);
        }

        public void setGetTime(String getTime) {
            getTimeTV.setText(getTime);
        }
    }
}
