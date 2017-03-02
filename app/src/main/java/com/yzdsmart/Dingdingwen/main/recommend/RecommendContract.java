package com.yzdsmart.Dingdingwen.main.recommend;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;
import com.yzdsmart.Dingdingwen.http.response.RecommendBannerRequestResponse;

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

        void unRegisterSubscribe();
    }
}
