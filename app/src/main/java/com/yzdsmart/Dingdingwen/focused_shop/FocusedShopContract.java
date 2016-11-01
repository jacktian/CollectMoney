package com.yzdsmart.Dingdingwen.focused_shop;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;
import com.yzdsmart.Dingdingwen.bean.FocusedShop;

import java.util.List;

/**
 * Created by YZD on 2016/9/20.
 */

public interface FocusedShopContract {
    interface FocusedShopView extends BaseView<FocusedShopPresenter> {
        void onGetFocusedShopList(List<FocusedShop> focusedShops);
    }

    interface FocusedShopPresenter extends BasePresenter {
        /**
         * 获取用户关注的店铺信息
         *
         * @param action
         * @param submitCode
         * @param custCode
         * @param pageIndex
         * @param pageSize
         */
        void getFocusedShopList(String action, String submitCode, String custCode, Integer pageIndex, Integer pageSize);

        void unRegisterSubscribe();
    }
}
