package com.yzdsmart.Dingdingwen.focused_shop;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.FocusedShopRequestResponse;

/**
 * Created by YZD on 2016/9/20.
 */

public class FocusedShopPresenter implements FocusedShopContract.FocusedShopPresenter {
    private Context context;
    private FocusedShopContract.FocusedShopView mView;
    private FocusedShopModel mModel;

    public FocusedShopPresenter(Context context, FocusedShopContract.FocusedShopView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new FocusedShopModel();
        mView.setPresenter(this);
    }

    @Override
    public void getFocusedShopList(String action, String submitCode, String custCode, Integer pageIndex, Integer pageSize) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getFocusedShopList(action, submitCode, custCode, pageIndex, pageSize, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                FocusedShopRequestResponse requestResponse = (FocusedShopRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    if (requestResponse.getLists().size() > 0) {
                        mView.onGetFocusedShopList(requestResponse.getLists());
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
    public void unRegisterSubscribe() {
        mModel.unRegisterSubscribe();
    }
}
