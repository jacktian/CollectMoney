package com.yzdsmart.Dingdingwen.publish_tasks;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.GetCoinRequestResponse;
import com.yzdsmart.Collectmoney.http.response.RequestResponse;

/**
 * Created by YZD on 2016/9/4.
 */
public class PublishTasksPresenter implements PublishTasksContract.PublishTasksPresenter {
    private Context context;
    private PublishTasksContract.PublishTasksView mView;
    private PublishTasksModel mModel;

    public PublishTasksPresenter(Context context, PublishTasksContract.PublishTasksView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new PublishTasksModel();
        mView.setPresenter(this);
    }

    @Override
    public void getLeftCoins(String action, String submitCode, String bazaCode) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getLeftCoins(action, submitCode, bazaCode, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                GetCoinRequestResponse requestResponse = (GetCoinRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onGetLeftCoins(requestResponse.getGoldNum());
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
    public void publishTask(String submitCode, String bazaCode, Integer totalGold, Integer totalNum, String beginTime, String endTime) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.publishing));
        mModel.publishTask(submitCode, bazaCode, totalGold, totalNum, beginTime, endTime, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ((BaseActivity) context).hideProgressDialog();
                RequestResponse response = (RequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    mView.onPublishTask(true, context.getResources().getString(R.string.publish_task_success));
                } else {
                    mView.onPublishTask(false, response.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
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
