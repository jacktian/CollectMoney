package com.yzdsmart.Collectmoney.scan_coin_log;

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
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.GetCoinsLog;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by YZD on 2016/9/5.
 */
public class ScanCoinsLogAdapter extends UltimateViewAdapter<ScanCoinsLogAdapter.ViewHolder> {
    private Context context;
    private List<GetCoinsLog> logList;
    private DateTimeFormatter dtf;

    public ScanCoinsLogAdapter(Context context) {
        this.context = context;
        logList = new ArrayList<GetCoinsLog>();
        dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 添加列表
     *
     * @param list
     */
    public void appendList(List<GetCoinsLog> list) {
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
        View itemView = LayoutInflater.from(context).inflate(R.layout.scan_coin_log_item, parent, false);
        ScanCoinsLogAdapter.ViewHolder holder = new ScanCoinsLogAdapter.ViewHolder(itemView);
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
        GetCoinsLog log = logList.get(position);
        DateTime dateTime = dtf.parseDateTime(log.getCreateTime());
        holder.scanCointCountsTV.setText("+" + log.getGetGold());
        holder.scanCoinDateTV.setText(dateTime.toString("yyyy-MM-dd"));
        holder.scanCoinTimeTV.setText(dateTime.toString("HH:mm:ss"));
        Glide.with(context).load(log.getLogoImageUrl()).asBitmap().placeholder(context.getResources().getDrawable(R.mipmap.ic_holder_light)).error(context.getResources().getDrawable(R.mipmap.ic_holder_light)).into(holder.shopAvaterIV);
        holder.shopNameTV.setText(log.getBazaName());
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
        @BindView(R.id.scan_coin_date)
        TextView scanCoinDateTV;
        @Nullable
        @BindView(R.id.scan_coin_time)
        TextView scanCoinTimeTV;
        @Nullable
        @BindView(R.id.shop_avater)
        ImageView shopAvaterIV;
        @Nullable
        @BindView(R.id.shop_name)
        TextView shopNameTV;
        @Nullable
        @BindView(R.id.scan_coin_counts)
        TextView scanCointCountsTV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
