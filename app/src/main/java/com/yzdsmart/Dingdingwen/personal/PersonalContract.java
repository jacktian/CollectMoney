package com.yzdsmart.Dingdingwen.personal;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;
import com.yzdsmart.Collectmoney.bean.GalleyInfo;
import com.yzdsmart.Collectmoney.http.response.CustInfoRequestResponse;
import com.yzdsmart.Collectmoney.http.response.ShopInfoByPersRequestResponse;

import java.util.List;

/**
 * Created by YZD on 2016/10/25.
 */

public interface PersonalContract {
    interface PersonalView extends BaseView<PersonalPresenter> {
        /**
         * 获取用户等级和星级
         *
         * @param gra
         * @param sta
         */
        void onGetCustLevel(Integer gra, Integer sta);

        /**
         * 获取用户信息
         *
         * @param requestResponse
         */
        void onGetCustInfo(CustInfoRequestResponse requestResponse);

        /**
         * 商家商铺详细信息
         *
         * @param shopDetails
         */
        void onGetShopInfo(ShopInfoByPersRequestResponse shopDetails);

        /**
         * 获取商铺图片列表
         *
         * @param galleyInfos
         */
        void onGetShopGalley(List<GalleyInfo> galleyInfos);

        /**
         * 上传店铺头像
         *
         * @param relaImageUrl
         */
        void onUploadShopAvater(String relaImageUrl);
    }

    interface PersonalPresenter extends BasePresenter {
        /**
         * 获取用户等级和星级
         *
         * @param code
         * @param submitcode
         * @param action
         */
        void getCustLevel(String code, String submitcode, String action);

        /**
         * 获取用户信息
         *
         * @param submitcode
         * @param custCode
         */
        void getCustInfo(String submitcode, String custCode);

        /**
         * 商家获取商铺详情
         *
         * @param submitCode
         * @param bazaCode
         */
        void getShopInfo(String actioncode, String submitCode, String bazaCode);

        /**
         * 获取商铺图片列表
         *
         * @param action
         * @param submitCode
         * @param bazaCode
         */
        void getShopGalley(String action, String submitCode, String bazaCode);

        /**
         * 上传商铺相册
         *
         * @param action
         * @param fileName
         * @param fileData
         * @param bazaCode
         */
        void uploadShopAvater(String action, String fileName, String fileData, String bazaCode);

        void unRegisterSubscribe();
    }
}
