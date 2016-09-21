package com.yzdsmart.Collectmoney.galley.upload;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

/**
 * Created by jacks on 2016/9/17.
 */
public interface UploadGalleyContract {
    interface UploadImageView extends BaseView<UploadImagePresenter> {
        void onUploadImageSuccess();
    }

    interface UploadImagePresenter extends BasePresenter {
        /**
         * 上传个人相册
         *
         * @param action
         * @param fileName
         * @param fileData
         * @param custCode
         */
        void uploadGalley(String action, String fileName, String fileData, String custCode);

        /**
         * 上传商铺相册
         *
         * @param action
         * @param fileName
         * @param fileData
         * @param bazaCode
         */
        void uploadShopImage(String action, String fileName, String fileData, String bazaCode);

        void unRegisterSubscribe();
    }
}
