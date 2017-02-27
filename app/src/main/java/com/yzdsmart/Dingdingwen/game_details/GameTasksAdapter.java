package com.yzdsmart.Dingdingwen.game_details;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
        holder.setTaskName(" " + gameTask.getTaskName(), gameTask.getGameStatus());
        if ("未完成".equals(gameTask.getGameStatus())) {
            holder.setTaskTime("正在进行中", gameTask.getGameStatus());
        } else if ("完成".equals(gameTask.getGameStatus()) || "放弃".equals(gameTask.getGameStatus())) {
            holder.setTaskTime(gameTask.getGameTime(), gameTask.getGameStatus());
        } else {
            holder.setTaskTime("待进行", gameTask.getGameStatus());
        }
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

        public void setTaskName(String taskName, String gameStatus) {
            Drawable drawable;
            if ("完成".equals(gameStatus)) {
                taskNameTV.setTextColor(context.getResources().getColor(R.color.game_status_finish));
                drawable = context.getResources().getDrawable(R.mipmap.status_finish_icon);
            } else if ("放弃".equals(gameStatus)) {
                taskNameTV.setTextColor(context.getResources().getColor(R.color.game_status_giveup));
                drawable = context.getResources().getDrawable(R.mipmap.status_giveup_icon);
            } else if ("未完成".equals(gameStatus)) {
                taskNameTV.setTextColor(context.getResources().getColor(R.color.game_status_ongoing));
                drawable = context.getResources().getDrawable(R.mipmap.status_ongoing_icon);
            } else {
                taskNameTV.setTextColor(context.getResources().getColor(R.color.game_status_wait));
                drawable = context.getResources().getDrawable(R.mipmap.status_wait_icon);
            }
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            taskNameTV.setCompoundDrawables(drawable, null, null, null);
            taskNameTV.setText(taskName);
        }

        public void setTaskTime(String taskTime, String gameStatus) {
            if ("完成".equals(gameStatus)) {
                taskTimeTV.setTextColor(context.getResources().getColor(R.color.game_status_finish));
            } else if ("放弃".equals(gameStatus)) {
                taskTimeTV.setTextColor(context.getResources().getColor(R.color.game_status_giveup));
            } else if ("未完成".equals(gameStatus)) {
                taskTimeTV.setTextColor(context.getResources().getColor(R.color.game_status_ongoing));
            } else {
                taskTimeTV.setTextColor(context.getResources().getColor(R.color.game_status_wait));
            }
            taskTimeTV.setText(taskTime);
        }
    }
}
