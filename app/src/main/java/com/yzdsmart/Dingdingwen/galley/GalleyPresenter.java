package com.yzdsmart.Dingdingwen.galley;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.GetGalleyRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.RequestResponse;

import java.util.List;

/**
 * Created by YZD on 2016/9/22.
 */

public class GalleyPresenter implements GalleyContract.GalleyPreviewPresenter {
    private Context context;
    private GalleyContract.GalleyPreviewView mView;
    private GalleyModel mModel;

    public GalleyPresenter(Context context, GalleyContract.GalleyPreviewView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new GalleyModel();
        mView.setPresenter(this);
    }

    @Override
    public void getPersonalGalley(String action, String submitCode, String custCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getPersonalGalley(action, submitCode, custCode, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                GetGalleyRequestResponse requestResponse = (GetGalleyRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    if (null != requestResponse.getLists()) {
                        mView.onGetPersonalGalley(requestResponse.getLists());
                    }
                } else {
                    ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(err);
            }

            @Override
            public void onComplete() {
                ((BaseActivity) context).hideProgressDialog();
            }
        });
    }

    @Override
    public void deletePersonalGalley(String action, String submitCode, String custCode, List<Integer> fileIdList, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.deleting));
        mModel.deletePersonalGalley(action, submitCode, custCode, fileIdList, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ((BaseActivity) context).hideProgressDialog();
                RequestResponse requestResponse = (RequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onDeletePersonalGalley();
                } else {
                    ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
                    mView.onDeleteGalleyFail();
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(err);
                mView.onDeleteGalleyFail();
            }

            @Override
            public void onComplete() {
            }
        });
    }

    @Override
    public void getShopGalley(String action, String submitCode, String bazaCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getShopGalley(action, submitCode, bazaCode, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                GetGalleyRequestResponse requestResponse = (GetGalleyRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    if (null != requestResponse.getLists()) {
                        mView.onGetShopGalley(requestResponse.getLists());
                    }
                } else {
                    ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(err);
            }

            @Override
            public void onComplete() {
                ((BaseActivity) context).hideProgressDialog();
            }
        });
    }

    @Override
    public void deleteShopGalley(String action, String submitCode, String bazaCode, List<Integer> fileIdList, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.deleting));
        mModel.deleteShopGalley(action, submitCode, bazaCode, fileIdList, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ((BaseActivity) context).hideProgressDialog();
                RequestResponse requestResponse = (RequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onDeleteShopGalley();
                } else {
                    ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
                    mView.onDeleteGalleyFail();
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(err);
                mView.onDeleteGalleyFail();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void uploadGalley(String action, String fileName, String fileData, String custCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.uploading));
        mModel.uploadGalley(action, fileName, fileData, custCode, authorization, new RequestListener() {
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
                mView.onUploadGalley();
            }
        });
    }

    @Override
    public void uploadShopImage(String action, String fileName, String fileData, String bazaCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.uploading));
        mModel.uploadShopImage(action, fileName, fileData, bazaCode, authorization, new RequestListener() {
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
                mView.onUploadGalley();
            }
        });
    }

    @Override
    public void unRegisterSubscribe() {
        mModel.unRegisterSubscribe();
    }
}
