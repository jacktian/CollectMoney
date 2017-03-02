package com.yzdsmart.Dingdingwen.main.recommend;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.response.RecommendBannerRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.RecommendNewsRequestResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by YZD on 2016/8/19.
 */
public class RecommendAdapter extends UltimateViewAdapter<UltimateRecyclerviewViewHolder> {
    private Context context;
    private List<RecommendBannerRequestResponse.ListsBean> bannerList;
    private List<RecommendBannerRequestResponse.ListsBean> liveList;
    private List<RecommendNewsRequestResponse.ListsBean> newsList;
    private LayoutInflater layoutInflater;

    /**
     * 3种类型
     */
    /**
     * 类型1：黑色星期五--使用banner实现
     */
    private static final int RECOMMEND_BANNER = 0;
    /**
     * 类型2：今日新品--使用GridView实现
     */
    private static final int RECOMMEND_LIVE = 1;
    /**
     * 类型3：品牌福利--使用ImageView实现
     */
    private static final int RECOMMEND_NEWS = 2;

    /**
     * 当前类型
     */
    private int currentType = RECOMMEND_BANNER;

    public RecommendAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        bannerList = new ArrayList<RecommendBannerRequestResponse.ListsBean>();
        liveList = new ArrayList<RecommendBannerRequestResponse.ListsBean>();
        newsList = new ArrayList<RecommendNewsRequestResponse.ListsBean>();
    }

    /**
     * 添加纪录
     *
     * @param list
     */
    public void appendBannerList(List<RecommendBannerRequestResponse.ListsBean> list) {
        if (null != bannerList) {
            bannerList.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除记录
     */
    public void clearBannerList() {
        if (null != bannerList && bannerList.size() > 0) {
            bannerList.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * 添加纪录
     *
     * @param list
     */
    public void appendLiveList(List<RecommendBannerRequestResponse.ListsBean> list) {
        if (null != liveList) {
            liveList.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除记录
     */
    public void clearLiveList() {
        if (null != liveList && liveList.size() > 0) {
            liveList.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * 添加纪录
     *
     * @param list
     */
    public void appendNewsList(List<RecommendNewsRequestResponse.ListsBean> list) {
        if (null != newsList) {
            newsList.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除记录
     */
    public void clearNewsList() {
        if (null != newsList && newsList.size() > 0) {
            newsList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public UltimateRecyclerviewViewHolder newFooterHolder(View view) {
        return null;
    }

    @Override
    public UltimateRecyclerviewViewHolder newHeaderHolder(View view) {
        return null;
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        UltimateRecyclerviewViewHolder holder = null;
        switch (viewType) {
            case RECOMMEND_BANNER:
                itemView = layoutInflater.inflate(R.layout.recommend_banner_layout, parent, false);
                holder = new BannerViewHolder(itemView);
                break;
            case RECOMMEND_LIVE:
                itemView = layoutInflater.inflate(R.layout.recommend_live_layout, parent, false);
                holder = new BannerViewHolder(itemView);
                break;
            case RECOMMEND_NEWS:
                itemView = layoutInflater.inflate(R.layout.recommend_news_layout, parent, false);
                holder = new BannerViewHolder(itemView);
                break;
        }
        return holder;
    }

    @Override
    public int getAdapterItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case RECOMMEND_BANNER:
                currentType = RECOMMEND_BANNER;
                break;
            case RECOMMEND_LIVE:
                currentType = RECOMMEND_LIVE;
                break;
            case RECOMMEND_NEWS:
                currentType = RECOMMEND_NEWS;
                break;
        }
        return currentType;
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(UltimateRecyclerviewViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case RECOMMEND_BANNER:
                if (0 < bannerList.size()) {
                    ((BannerViewHolder) holder).setRecommendBanner(bannerList);
                }
                break;
            case RECOMMEND_LIVE:
                if (0 < liveList.size()) {

                }
                break;
            case RECOMMEND_NEWS:
                if (0 < newsList.size()) {

                }
                break;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    class BannerViewHolder extends UltimateRecyclerviewViewHolder {
        @Nullable
        @BindView(R.id.recommend_banner)
        BGABanner recommendBanner;

        public BannerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setRecommendBanner(List<RecommendBannerRequestResponse.ListsBean> dataBeans) {
            //得到图片地址的集合
            List<String> bannerImgs = new ArrayList<String>();
            for (int i = 0; i < dataBeans.size(); i++) {
                bannerImgs.add(dataBeans.get(i).getImageUrl());
            }
            recommendBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
                @Override
                public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                    Glide.with(context).load(model).placeholder(R.mipmap.ic_holder_light).error(R.mipmap.ic_holder_light).centerCrop().dontAnimate().into(itemView);
                }
            });
            recommendBanner.setData(bannerImgs, null);
        }
    }

    class LiveViewHolder extends UltimateRecyclerviewViewHolder {

        public LiveViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    class NewsViewHolder extends UltimateRecyclerviewViewHolder {
        @Nullable
        @BindView(R.id.news_list)
        RecyclerView newsListRV;

        LinearLayoutManager mLinearLayoutManager;

        public NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mLinearLayoutManager = new LinearLayoutManager(context);
        }
    }

}
