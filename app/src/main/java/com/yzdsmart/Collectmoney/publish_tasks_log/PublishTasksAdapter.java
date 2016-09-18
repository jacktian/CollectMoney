package com.yzdsmart.Collectmoney.publish_tasks_log;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.PublishTaskLog;

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
public class PublishTasksAdapter extends RecyclerView.Adapter<PublishTasksAdapter.ViewHolder> {
    private Context context;
    private List<PublishTaskLog> logList;
    private DateTimeFormatter dtf;

    public PublishTasksAdapter(Context context) {
        this.context = context;
        logList = new ArrayList<PublishTaskLog>();
        dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
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
        if (null != logList) {
            logList.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.coins_log_item, parent, false);
        PublishTasksAdapter.ViewHolder holder = new PublishTasksAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PublishTaskLog log = logList.get(position);
        DateTime beginTime = dtf.parseDateTime(log.getBeginTime());
        DateTime endTime = dtf.parseDateTime(log.getEndTime());
        holder.coins1TV.setText("" + log.getTotalGold());
        holder.time1TV.setText("起 : " + beginTime.toString("yyyy-MM-dd"));
        holder.time2TV.setText("止 : " + endTime.toString("yyyy-MM-dd"));
        holder.coins2TV.setText("" + log.getOverGold());
    }

    @Override
    public int getItemCount() {
        return logList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.coins_1)
        TextView coins1TV;
        @Nullable
        @BindView(R.id.time_1)
        TextView time1TV;
        @Nullable
        @BindView(R.id.time_2)
        TextView time2TV;
        @Nullable
        @BindView(R.id.coins_2)
        TextView coins2TV;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
