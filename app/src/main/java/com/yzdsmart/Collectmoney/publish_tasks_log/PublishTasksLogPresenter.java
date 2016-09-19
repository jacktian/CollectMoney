package com.yzdsmart.Collectmoney.publish_tasks_log;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.PublishTaskLogRequestResponse;

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
    public void publishTaskLog(String action, String submitCode, String bazaCode, Integer pageIndex, Integer pageSize) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.publishTaskLog(action, submitCode, bazaCode, pageIndex, pageSize, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                PublishTaskLogRequestResponse response = (PublishTaskLogRequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    if (null != response.getLists() && response.getLists().size() > 0) {
                        mView.onPublishTaskLog(response.getLists());
                    }
                } else {
                    ((BaseActivity) context).showSnackbar(response.getErrorInfo());
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
