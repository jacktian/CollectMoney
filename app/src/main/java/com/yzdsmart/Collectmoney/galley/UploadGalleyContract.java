package com.yzdsmart.Collectmoney.galley;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

/**
 * Created by jacks on 2016/9/17.
 */
public interface UploadGalleyContract {
    interface UploadImageView extends BaseView<UploadImagePresenter> {

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

        void unRegisterSubscribe();
    }
}
