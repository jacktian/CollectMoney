package com.yzdsmart.Dingdingwen.focused_shop;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.FocusedShopRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;

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
    public void getFocusedShopList(String action, String submitCode, String custCode, Integer pageIndex, Integer pageSize, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getFocusedShopList(action, submitCode, custCode, pageIndex, pageSize, authorization, new RequestListener() {
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
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_get_focused_shop));
                if (err.contains("401 Unauthorized")) {
                    MainActivity.getInstance().updateAccessToken();
                }
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
