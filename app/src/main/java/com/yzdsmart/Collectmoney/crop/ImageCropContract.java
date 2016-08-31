package com.yzdsmart.Collectmoney.crop;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

/**
 * Created by YZD on 2016/8/24.
 */
public class ImageCropContract {
    interface ImageCropView extends BaseView<ImageCropPresenter> {
        void onUploadPortraitSuccess(String faceUrl);
    }

    interface ImageCropPresenter extends BasePresenter {
        void uploadPortrait(String action, String fileName, String fileData, String tcAccount);

        void setFaceUrl(String faceUrl);

        void unRegisterSubscribe();
    }
}
