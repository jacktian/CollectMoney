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
        void onGetRecommendBanner(List<RecommendBannerRequestResponse> recommendBanners);
    }

    interface RecommendPresenter extends BasePresenter {
        /**
         * 获取推荐列表
         *
         * @param submitCode
         * @param pageIndex
         * @param pageSize
         */
        void getRecommendBanner(String submitCode, Integer pageIndex, Integer pageSize, String authorization);

        void unRegisterSubscribe();
    }
}
