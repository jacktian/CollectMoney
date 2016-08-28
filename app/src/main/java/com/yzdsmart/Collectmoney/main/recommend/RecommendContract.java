package com.yzdsmart.Collectmoney.main.recommend;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

/**
 * Created by YZD on 2016/8/28.
 */
public interface RecommendContract {
    interface RecommendView extends BaseView<RecommendPresenter> {

    }

    interface RecommendPresenter extends BasePresenter {
        /**
         * 获取推荐列表
         *
         * @param submitCode
         * @param pageIndex
         * @param pageSize
         */
        void getExpandList(String submitCode, Integer pageIndex, Integer pageSize);

        void unRegisterSubscribe();
    }
}
