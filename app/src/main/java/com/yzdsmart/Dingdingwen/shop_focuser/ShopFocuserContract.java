package com.yzdsmart.Dingdingwen.shop_focuser;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;
import com.yzdsmart.Dingdingwen.bean.ShopFocuser;

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
        void getShopFocuser(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize, String authorization);

        void unRegisterSubscribe();
    }
}
