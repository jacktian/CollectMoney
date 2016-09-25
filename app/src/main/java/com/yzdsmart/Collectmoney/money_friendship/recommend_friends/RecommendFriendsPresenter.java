package com.yzdsmart.Collectmoney.money_friendship.recommend_friends;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.RecommendFriendsRequestResponse;

/**
 * Created by YZD on 2016/9/25.
 */

public class RecommendFriendsPresenter implements RecommendFriendsContract.RecommendFriendsPresenter {
    private Context context;
    private RecommendFriendsContract.RecommendFriendsView mView;
    private RecommendFriendsModel mModel;

    public RecommendFriendsPresenter(Context context, RecommendFriendsContract.RecommendFriendsView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new RecommendFriendsModel();
        mView.setPresenter(this);
    }

    @Override
    public void getRecommendFriends(String action, String submitCode, String custCode, Integer recomNum) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getRecommendFriends(action, submitCode, custCode, recomNum, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                RecommendFriendsRequestResponse requestResponse = (RecommendFriendsRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    if (null != requestResponse.getLists()) {
                        mView.onGetRecommendFriends(requestResponse.getLists());
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
