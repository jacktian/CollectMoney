package com.yzdsmart.Dingdingwen.game_details;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.response.GameTaskRequestResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by YZD on 2017/2/24.
 */

public class GameTasksAdapter extends RecyclerView.Adapter<GameTasksAdapter.ViewHolder> {
    private Context context;
    private List<GameTaskRequestResponse.DataBean.TaskListsBean> tasksList;

    public GameTasksAdapter(Context context) {
        this.context = context;
        tasksList = new ArrayList<GameTaskRequestResponse.DataBean.TaskListsBean>();
    }

    /**
     * 添加列表
     *
     * @param list
     */
    public void appendList(List<GameTaskRequestResponse.DataBean.TaskListsBean> list) {
        if (null != tasksList) {
            tasksList.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除列表
     */
    public void clearList() {
        if (null != tasksList && tasksList.size() > 0) {
            tasksList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.game_task_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GameTaskRequestResponse.DataBean.TaskListsBean gameTask = tasksList.get(position);
        holder.setTaskName(gameTask.getTaskName());
        holder.setTaskTime(gameTask.getGameTime().length() > 0 ? gameTask.getGameTime() : "正在进行中");
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.task_name)
        TextView taskNameTV;
        @Nullable
        @BindView(R.id.task_time)
        TextView taskTimeTV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setTaskName(String taskName) {
            taskNameTV.setText(taskName);
        }

        public void setTaskTime(String taskTime) {
            taskTimeTV.setText(taskTime);
        }
    }
}
