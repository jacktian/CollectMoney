package com.yzdsmart.Dingdingwen.time_keeper;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.SignDataRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;

/**
 * Created by YZD on 2017/1/20.
 */

public class TimeKeeperPresenter implements TimeKeeperContract.TimeKeeperPresenter {
    private Context context;
    private TimeKeeperContract.TimeKeeperView mView;
    private TimeKeeperModel mModel;

    public TimeKeeperPresenter(Context context, TimeKeeperContract.TimeKeeperView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new TimeKeeperModel();
        mView.setPresenter(this);
    }

    @Override
    public void getSignActivityList(String action, String submitCode, String custCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getSignActivityList(action, submitCode, custCode, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                SignDataRequestResponse requestResponse= (SignDataRequestResponse) result;
                if("OK".equals(requestResponse.getActionStatus())){

                }else {

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
