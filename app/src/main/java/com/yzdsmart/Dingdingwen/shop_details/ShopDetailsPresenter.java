package com.yzdsmart.Dingdingwen.shop_details;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.ShopScanner;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.RequestResponse;
import com.yzdsmart.Dingdingwen.http.response.ShopInfoRequestResponse;

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
    public void getShopInfo(String actioncode, String submitCode, String bazaCode, String custCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getShopInfo(actioncode, submitCode, bazaCode, custCode, authorization, new RequestListener() {
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
    public void setFollow(final String action, String submitCode, String custCode, String bazaCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.setFollow(action, submitCode, custCode, bazaCode, authorization, new RequestListener() {
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
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getShopFollowers(action, submitCode, bazaCode, custCode, pageIndex, pageSize, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                List<ShopScanner> shopScanners = (List<ShopScanner>) result;
                if (null != shopScanners && shopScanners.size() > 0) {
                    mView.onGetShopFollowers(shopScanners);
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
