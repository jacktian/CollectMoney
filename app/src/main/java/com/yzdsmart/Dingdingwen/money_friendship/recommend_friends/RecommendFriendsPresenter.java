package com.yzdsmart.Dingdingwen.money_friendship.recommend_friends;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.RecommendFriendsRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;

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
    public void getRecommendFriends(String action, String submitCode, String custCode, Integer recomNum, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getRecommendFriends(action, submitCode, custCode, recomNum, authorization, new RequestListener() {
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
