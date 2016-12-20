package com.yzdsmart.Dingdingwen.publish_tasks_log;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.PublishTaskLog;

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
 * Created by YZD on 2016/9/5.
 */
public class PublishTasksAdapter extends UltimateViewAdapter<PublishTasksAdapter.ViewHolder> {
    private Context context;
    private List<PublishTaskLog> logList;
    private DateTimeFormatter dtf;
    private DecimalFormat decimalFormat;

    public PublishTasksAdapter(Context context) {
        this.context = context;
        logList = new ArrayList<PublishTaskLog>();
        dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
        decimalFormat = new DecimalFormat("#0.00");
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
    }

    /**
     * 添加列表
     *
     * @param list
     */
    public void appendList(List<PublishTaskLog> list) {
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
        View itemView = LayoutInflater.from(context).inflate(R.layout.publish_task_log_item, parent, false);
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
        PublishTaskLog log = logList.get(position);
        DateTime beginTime = dtf.parseDateTime(log.getBeginTime());
        DateTime endTime = dtf.parseDateTime(log.getEndTime());
        holder.coinCountsTV.setText("-" + decimalFormat.format(log.getTotalGold()));
        holder.startDateTV.setText("起 : " + beginTime.toString("yyyy-MM-dd"));
        holder.endDateTV.setText("止 : " + endTime.toString("yyyy-MM-dd"));
        holder.leftCoinCountsTV.setText(decimalFormat.format(log.getOverGold()));
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
        @BindView(R.id.start_date)
        TextView startDateTV;
        @Nullable
        @BindView(R.id.end_date)
        TextView endDateTV;
        @Nullable
        @BindView(R.id.coin_counts)
        TextView coinCountsTV;
        @Nullable
        @BindView(R.id.left_coin_counts)
        TextView leftCoinCountsTV;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
