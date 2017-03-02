package com.yzdsmart.Dingdingwen.main.recommend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yzdsmart.Dingdingwen.http.response.RecommendNewsRequestResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by jacks on 2017/3/3.
 */

public class RecommendNewsAdapter extends RecyclerView.Adapter<RecommendNewsAdapter.ViewHolder> {
    private Context context;
    private List<RecommendNewsRequestResponse.ListsBean> newsList;

    public RecommendNewsAdapter(Context context) {
        this.context = context;
        newsList = new ArrayList<RecommendNewsRequestResponse.ListsBean>();
    }

    /**
     * 添加纪录
     *
     * @param list
     */
    public void appendList(List<RecommendNewsRequestResponse.ListsBean> list) {
        if (null != newsList) {
            newsList.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除记录
     */
    public void clearList() {
        if (null != newsList && newsList.size() > 0) {
            newsList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
