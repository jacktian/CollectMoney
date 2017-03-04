package com.yzdsmart.Dingdingwen.main.recommend;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;
import com.yzdsmart.Dingdingwen.http.response.RecommendBannerRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.RecommendNewsRequestResponse;

import java.util.List;

/**
 * Created by YZD on 2016/8/28.
 */
public interface RecommendContract {
    interface RecommendView extends BaseView<RecommendPresenter> {
        /**
         * 获取推荐banner列表
         *
         * @param recommendBanners
         */
        void onGetRecommendBanner(List<RecommendBannerRequestResponse.ListsBean> recommendBanners);

        /**
         * 获取推荐新闻列表
         *
         * @param recommendNews
         * @param lastsequence
         */
        void onGetRecommendNews(List<RecommendNewsRequestResponse.ListsBean> recommendNews,Integer lastsequence);
    }

    interface RecommendPresenter extends BasePresenter {
        /**
         * 获取推荐轮播
         *
         * @param submitCode
         * @param actionCode
         * @param pageIndex
         * @param pageSize
         * @param lastsequence
         * @param authorization
         */
        void getRecommendBanner(String submitCode, String actionCode, Integer pageIndex, Integer pageSize, Integer lastsequence, String authorization);

        /**
         * 获取推荐新闻列表
         *
         * @param submitCode
         * @param actionCode
         * @param pageIndex
         * @param pageSize
         * @param lastsequence
         * @param authorization
         */
        void getRecommendNews(String submitCode, String actionCode, Integer pageIndex, Integer pageSize, Integer lastsequence, String authorization);

        void unRegisterSubscribe();
    }
}
