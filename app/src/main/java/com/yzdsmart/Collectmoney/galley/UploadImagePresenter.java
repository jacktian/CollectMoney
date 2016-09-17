package com.yzdsmart.Collectmoney.galley;

import android.content.Context;

/**
 * Created by jacks on 2016/9/17.
 */
public class UploadImagePresenter implements UploadImageContract.UploadImagePresenter {
    private Context context;
    private UploadImageContract.UploadImageView mView;
    private UploadImageModel mModel;

    public UploadImagePresenter(Context context, UploadImageContract.UploadImageView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new UploadImageModel();
        mView.setPresenter(this);
    }
}
