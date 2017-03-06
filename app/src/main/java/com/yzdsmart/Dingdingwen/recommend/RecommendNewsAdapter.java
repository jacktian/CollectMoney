package com.yzdsmart.Dingdingwen.recommend;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.response.RecommendNewsRequestResponse;
import com.yzdsmart.Dingdingwen.recommend_details.RecommendDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

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
        View itemView = LayoutInflater.from(context).inflate(R.layout.recommend_news_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecommendNewsRequestResponse.ListsBean news = newsList.get(position);
        holder.setNewsTitle(news.getDiscoverTitle());
        holder.setNewsContent(news.getDiscoverContent());
        holder.setNewsImage(news.getImageUrl());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.news_title)
        TextView newsTitleTV;
        @Nullable
        @BindView(R.id.news_content)
        TextView newsContentTV;
        @Nullable
        @BindView(R.id.news_image)
        ImageView newsImageIV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Optional
        @OnClick({R.id.news_item})
        void onClick(View view) {
            switch (view.getId()) {
                case R.id.news_item:
                    RecommendNewsRequestResponse.ListsBean newsBean = newsList.get(getAdapterPosition());
                    Bundle bundle = new Bundle();
                    bundle.putString("pageUrl", newsBean.getFileUrl());
                    ((BaseActivity) context).openActivity(RecommendDetailsActivity.class, bundle, 0);
                    break;
            }
        }

        public void setNewsTitle(String newsTitle) {
            newsTitleTV.setText(newsTitle);
        }

        public void setNewsContent(String newsContent) {
            newsContentTV.setText(newsContent);
        }

        public void setNewsImage(String newsImage) {
            Glide.with(context).load(newsImage).asBitmap().placeholder(context.getResources().getDrawable(R.mipmap.ic_holder_light)).error(context.getResources().getDrawable(R.mipmap.ic_holder_light)).into(newsImageIV);
        }
    }
}
