package com.yzdsmart.Dingdingwen.crop;

import android.content.Context;

import com.tencent.TIMCallBack;
import com.tencent.TIMFriendshipManager;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.UploadFileRequestResponse;

/**
 * Created by YZD on 2016/8/24.
 */
public class ImageCropPresenter implements ImageCropContract.ImageCropPresenter {
    private Context context;
    private ImageCropContract.ImageCropView mView;
    private ImageCropModel mModel;

    public ImageCropPresenter(Context context, ImageCropContract.ImageCropView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new ImageCropModel();
        mView.setPresenter(this);
    }


    @Override
    public void uploadPortrait(String action, String fileName, String fileData, String tcAccount) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.uploading));
        mModel.uploadPortrait(action, fileName, fileData, tcAccount, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                UploadFileRequestResponse response = (UploadFileRequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    mView.onUploadPortraitSuccess(response.getRelaImageUrl());
                } else {
                    ((BaseActivity) context).showSnackbar("上传头像失败!");
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar("上传头像异常!");
            }

            @Override
            public void onComplete() {
                ((BaseActivity) context).hideProgressDialog();
            }
        });
    }

    @Override
    public void setFaceUrl(String faceUrl) {
        TIMFriendshipManager.getInstance().setFaceUrl(faceUrl, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code列表请参见错误码表
                System.out.println("setFaceUrl failed: " + code + " desc" + desc);
            }

            @Override
            public void onSuccess() {
            }
        });
    }

    @Override
    public void unRegisterSubscribe() {
        mModel.unRegisterSubscribe();
    }
}
