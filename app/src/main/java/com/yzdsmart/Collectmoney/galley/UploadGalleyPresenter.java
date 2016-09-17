package com.yzdsmart.Collectmoney.galley;

import android.content.Context;

import com.yzdsmart.Collectmoney.http.RequestListener;

/**
 * Created by jacks on 2016/9/17.
 */
public class UploadGalleyPresenter implements UploadGalleyContract.UploadImagePresenter {
    private Context context;
    private UploadGalleyContract.UploadImageView mView;
    private UploadGalleyModel mModel;

    public UploadGalleyPresenter(Context context, UploadGalleyContract.UploadImageView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new UploadGalleyModel();
        mView.setPresenter(this);
    }

    @Override
    public void uploadGalley(String action, String fileName, String fileData, String custCode) {
        mModel.uploadGalley(action, fileName, fileData, custCode, new RequestListener() {
            @Override
            public void onSuccess(Object result) {

            }

            @Override
            public void onError(String err) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void unRegisterSubscribe() {
        mModel.unRegisterSubscribe();
    }
}
