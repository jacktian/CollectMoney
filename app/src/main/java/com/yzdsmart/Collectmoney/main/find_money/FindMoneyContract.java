package com.yzdsmart.Collectmoney.main.find_money;

import com.baidu.mapapi.map.MarkerOptions;
import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

import java.util.List;

/**
 * Created by YZD on 2016/8/23.
 */
public class FindMoneyContract {
    interface FindMoneyView extends BaseView<FindMoneyPresenter> {
        /**
         * 获取周边商铺列表
         *
         * @param optionsList
         */
        void onGetShopList(List<MarkerOptions> optionsList);
    }

    interface FindMoneyPresenter extends BasePresenter {
        /**
         * 获取周边店铺
         *
         * @param submitCode
         * @param coor
         * @param pageIndex
         * @param pageSize
         */
        void getShopList(String submitCode, String coor, Integer pageIndex, Integer pageSize);

        /**
         * 取消网络请求
         */
        void unRegisterSubscribe();
    }
}
