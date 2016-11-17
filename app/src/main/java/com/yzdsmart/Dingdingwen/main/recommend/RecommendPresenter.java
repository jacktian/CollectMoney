package com.yzdsmart.Dingdingwen.main.recommend;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.ExpandListRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;

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
    public void getExpandList(String submitCode, Integer pageIndex, Integer pageSize, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getExpandList(submitCode, pageIndex, pageSize, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                List<ExpandListRequestResponse> expands = (List<ExpandListRequestResponse>) result;
                if (null != expands) {
                    mView.onGetExpandList(expands);
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(err);
                if (err.contains("HTTP 401 Unauthorized")) {
                    MainActivity.getInstance().refreshAccessToken();
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
