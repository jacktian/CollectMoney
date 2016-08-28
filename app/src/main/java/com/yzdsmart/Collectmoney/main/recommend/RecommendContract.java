package com.yzdsmart.Collectmoney.main.recommend;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;
import com.yzdsmart.Collectmoney.bean.Expand;

import java.util.List;

/**
 * Created by YZD on 2016/8/28.
 */
public interface RecommendContract {
    interface RecommendView extends BaseView<RecommendPresenter> {
        /**
         * 获取推荐列表
         *
         * @param expands
         */
        void onGetExpandList(List<Expand> expands);
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
