package com.yzdsmart.Collectmoney.main.recommend;

import android.content.Context;

import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.ExpandListRequestResponse;

import java.util.List;

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
    public void getExpandList(String submitCode, Integer pageIndex, Integer pageSize) {
        mModel.getExpandList(submitCode, pageIndex, pageSize, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                List<ExpandListRequestResponse> expands = (List<ExpandListRequestResponse>) result;
                if (null != expands) {
                    mView.onGetExpandList(expands);
                }
            }

            @Override
            public void onError(String err) {

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
