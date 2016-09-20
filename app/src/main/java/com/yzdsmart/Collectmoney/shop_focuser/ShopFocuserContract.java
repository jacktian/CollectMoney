package com.yzdsmart.Collectmoney.shop_focuser;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;
import com.yzdsmart.Collectmoney.bean.ShopFocuser;

import java.util.List;

/**
 * Created by YZD on 2016/9/20.
 */

public interface ShopFocuserContract {
    interface ShopFocuserView extends BaseView<ShopFocuserPresenter> {
        void onGetShopFocuser(List<ShopFocuser> shopFocusers);
    }

    interface ShopFocuserPresenter extends BasePresenter {
        /**
         * 获取关注店铺的用户信息
         *
         * @param action
         * @param submitCode
         * @param bazaCode
         * @param pageIndex
         * @param pageSize
         */
        void getShopFocuser(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize);

        void unRegisterSubscribe();
    }
}
