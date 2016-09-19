package com.yzdsmart.Collectmoney.shop_details;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.ShopFollower;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.RequestResponse;
import com.yzdsmart.Collectmoney.http.response.ShopInfoRequestResponse;

import java.util.List;

/**
 * Created by YZD on 2016/8/26.
 */
public class ShopDetailsPresenter implements ShopDetailsContract.ShopDetailsPresenter {
    private Context context;
    private ShopDetailsContract.ShopDetailsView mView;
    private ShopDetailsModel mModel;

    public ShopDetailsPresenter(Context context, ShopDetailsContract.ShopDetailsView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new ShopDetailsModel();
        mView.setPresenter(this);
    }

    @Override
    public void getShopInfo(String actioncode, String submitCode, String bazaCode, String custCode) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading);
        mModel.getShopInfo(actioncode, submitCode, bazaCode, custCode, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ShopInfoRequestResponse shopInfo = (ShopInfoRequestResponse) result;
                if (null != shopInfo) {
                    mView.onGetShopInfo(shopInfo);
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
    public void setFollow(final String action, String submitCode, String custCode, String bazaCode) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading);
        mModel.setFollow(action, submitCode, custCode, bazaCode, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                RequestResponse response = (RequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    mView.onSetFollow(true, action, response.getErrorInfo());
                } else if ("FAIL".equals(response.getActionStatus())) {
                    mView.onSetFollow(false, action, response.getErrorInfo());
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
    public void getShopFollowers(String action, String submitCode, String bazaCode, String custCode, Integer pageIndex, Integer pageSize) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading);
        mModel.getShopFollowers(action, submitCode, bazaCode, custCode, pageIndex, pageSize, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                List<ShopFollower> shopFollowers = (List<ShopFollower>) result;
                if (null != shopFollowers && shopFollowers.size() > 0) {
                    mView.onGetShopFollowers(shopFollowers);
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
