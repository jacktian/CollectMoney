package com.yzdsmart.Collectmoney.main.recommend;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.ExpandListRequestResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

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
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getExpandList(submitCode, pageIndex, pageSize, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
//                List<ExpandListRequestResponse> expands = (List<ExpandListRequestResponse>) result;
//                if (null != expands) {
//                    mView.onGetExpandList(expands);
//                }
                ResponseBody responseBody = (ResponseBody) result;
                try {
                    ((BaseActivity) context).showSnackbar(responseBody.string());
                    Gson gson = new Gson();
                    List<LinkedTreeMap> expands = gson.fromJson(responseBody.string(), ArrayList.class);
                    for (LinkedTreeMap expand : expands) {
                        System.out.println("---->" + expand.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
