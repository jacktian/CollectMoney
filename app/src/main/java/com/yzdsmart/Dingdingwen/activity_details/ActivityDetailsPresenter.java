package com.yzdsmart.Dingdingwen.activity_details;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.SignDataRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;

/**
 * Created by YZD on 2017/1/20.
 */

public class ActivityDetailsPresenter implements ActivityDetailsContract.ActivityDetailsPresenter {
    private Context context;
    private ActivityDetailsContract.ActivityDetailsView mView;
    private ActivityDetailsModel mModel;

    public ActivityDetailsPresenter(Context context, ActivityDetailsContract.ActivityDetailsView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new ActivityDetailsModel();
        mView.setPresenter(this);
    }

    @Override
    public void getSignActivityList(String action, String submitCode, String custCode, String activityCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getSignActivityList(action, submitCode, custCode, activityCode, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                SignDataRequestResponse requestResponse = (SignDataRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onGetSignActivityList(true, null, requestResponse.getData());
                } else {
                    mView.onGetSignActivityList(false, requestResponse.getErrorInfo(), requestResponse.getData());
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
