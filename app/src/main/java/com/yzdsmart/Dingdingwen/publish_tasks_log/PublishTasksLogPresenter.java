package com.yzdsmart.Dingdingwen.publish_tasks_log;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.GetCoinRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.PublishTaskLogRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;

/**
 * Created by YZD on 2016/9/18.
 */
public class PublishTasksLogPresenter implements PublishTasksLogContract.PublishTasksLogPresenter {
    private Context context;
    private PublishTasksLogContract.PublishTasksLogView mView;
    private PublishTasksLogModel mModel;

    public PublishTasksLogPresenter(Context context, PublishTasksLogContract.PublishTasksLogView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new PublishTasksLogModel();
        mView.setPresenter(this);
    }

    @Override
    public void getLeftCoins(String action, String submitCode, String bazaCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getLeftCoins(action, submitCode, bazaCode, authorization, new RequestListener() {
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
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_get_left_coins));
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
    public void publishTaskLog(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize, Integer lastsequence, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.publishTaskLog(action, submitCode, bazaCode, pageIndex, pageSize, lastsequence, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                PublishTaskLogRequestResponse response = (PublishTaskLogRequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    mView.onPublishTaskLog(response.getLists(), response.getLastsequence());
                } else {
                    ((BaseActivity) context).showSnackbar(response.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_get_publish_task_log));
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
