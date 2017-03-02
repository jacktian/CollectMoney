package com.yzdsmart.Dingdingwen.main.recommend;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.RecommendBannerRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.RecommendNewsRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;

/**
 * Created by YZD on 2016/8/28.
 */
public class RecommendPresenter implements RecommendContract.RecommendPresenter {
    private Context context;
    private RecommendContract.RecommendView mView;
    private RecommendModel mModel;

    public RecommendPresenter(Context context, RecommendContract.RecommendView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new RecommendModel();
        mView.setPresenter(this);
    }

    @Override
    public void getRecommendBanner(String submitCode, String actionCode, Integer pageIndex, Integer pageSize, Integer lastsequence, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getRecommendBanner(submitCode, actionCode, pageIndex, pageSize, lastsequence, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                RecommendBannerRequestResponse requestResponse = (RecommendBannerRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onGetRecommendBanner(requestResponse.getLists());
                } else if ("FAIL".equals(requestResponse.getActionStatus())) {
                    ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
//                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_get_expand_shop_list));
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
    public void getRecommendNews(String submitCode, String actionCode, Integer pageIndex, Integer pageSize, Integer lastsequence, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getRecommendNews(submitCode, actionCode, pageIndex, pageSize, lastsequence, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                RecommendNewsRequestResponse requestResponse = (RecommendNewsRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onGetRecommendNews(requestResponse.getLists());
                } else if ("FAIL".equals(requestResponse.getActionStatus())) {
                    ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
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
