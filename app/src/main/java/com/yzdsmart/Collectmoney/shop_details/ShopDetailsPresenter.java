package com.yzdsmart.Collectmoney.shop_details;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.bean.ShopDetails;
import com.yzdsmart.Collectmoney.http.RequestListener;

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
    public void getShopDetails(String submitCode, String bazaCode) {
        mModel.getShopDetails(submitCode, bazaCode, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ShopDetails shopDetails = (ShopDetails) result;
                if (null != shopDetails) {
                    mView.onGetShopDetails(shopDetails);
                }
            }

            @Override
            public void onError(String err) {
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
