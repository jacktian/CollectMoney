package com.yzdsmart.Collectmoney.galley.preview;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;
import com.yzdsmart.Collectmoney.bean.GalleyInfo;

import java.util.List;

/**
 * Created by YZD on 2016/9/22.
 */

public interface GalleyPreviewContract {
    interface GalleyPreviewView extends BaseView<GalleyPreviewPresenter> {
        /**
         * 获取个人的图片列表
         *
         * @param galleyInfos
         */
        void onGetPersonalGalley(List<GalleyInfo> galleyInfos);

        /**
         * 删除用户个人相册图片
         */
        void onDeletePersonalGalley();
    }

    interface GalleyPreviewPresenter extends BasePresenter {
        /**
         * 获取个人的图片列表
         *
         * @param action
         * @param submitCode
         * @param custCode
         */
        void getPersonalGalley(String action, String submitCode, String custCode);

        /**
         * 删除用户个人相册图片
         *
         * @param action
         * @param submitCode
         * @param custCode
         * @param fileIdList
         */
        void deletePersonalGalley(String action, String submitCode, String custCode, List<Integer> fileIdList);

        void unRegisterSubscribe();
    }
}
