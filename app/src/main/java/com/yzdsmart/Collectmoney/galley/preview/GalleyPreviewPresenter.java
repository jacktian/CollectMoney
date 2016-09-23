package com.yzdsmart.Collectmoney.galley.preview;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.GetGalleyRequestResponse;
import com.yzdsmart.Collectmoney.http.response.RequestResponse;

import java.util.List;

/**
 * Created by YZD on 2016/9/22.
 */

public class GalleyPreviewPresenter implements GalleyPreviewContract.GalleyPreviewPresenter {
    private Context context;
    private GalleyPreviewContract.GalleyPreviewView mView;
    private GalleyPreviewModel mModel;

    public GalleyPreviewPresenter(Context context, GalleyPreviewContract.GalleyPreviewView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new GalleyPreviewModel();
        mView.setPresenter(this);
    }

    @Override
    public void getPersonalGalley(String action, String submitCode, String custCode) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getPersonalGalley(action, submitCode, custCode, new RequestListener() {
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
    public void deletePersonalGalley(String action, String submitCode, String custCode, List<Integer> fileIdList) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.deleting));
        mModel.deletePersonalGalley(action, submitCode, custCode, fileIdList, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ((BaseActivity) context).hideProgressDialog();
                RequestResponse requestResponse = (RequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onDeletePersonalGalley();
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
            }
        });
    }

    @Override
    public void getShopGalley(String action, String submitCode, String bazaCode) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getShopGalley(action, submitCode, bazaCode, new RequestListener() {
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
    public void deleteShopGalley(String action, String submitCode, String bazaCode, List<Integer> fileIdList) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.deleting));
        mModel.deleteShopGalley(action, submitCode, bazaCode, fileIdList, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ((BaseActivity) context).hideProgressDialog();
                RequestResponse requestResponse = (RequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onDeleteShopGalley();
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

            }
        });
    }

    @Override
    public void unRegisterSubscribe() {
        mModel.unRegisterSubscribe();
    }
}
