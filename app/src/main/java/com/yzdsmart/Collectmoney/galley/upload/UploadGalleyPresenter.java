package com.yzdsmart.Collectmoney.galley.upload;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
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
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.uploading));
        mModel.uploadGalley(action, fileName, fileData, custCode, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(err);
            }

            @Override
            public void onComplete() {
                ((BaseActivity) context).hideProgressDialog();
                mView.onUploadImageSuccess();
            }
        });
    }

    @Override
    public void uploadShopImage(String action, String fileName, String fileData, String bazaCode) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.uploading));
        mModel.uploadShopImage(action, fileName, fileData, bazaCode, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(err);
            }

            @Override
            public void onComplete() {
                ((BaseActivity) context).hideProgressDialog();
                mView.onUploadImageSuccess();
            }
        });
    }

    @Override
    public void unRegisterSubscribe() {
        mModel.unRegisterSubscribe();
    }
}
