package com.yzdsmart.Collectmoney.publish_tasks;

import android.content.Context;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.RequestListener;
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
    public void publishTask(String submitCode, String bazaCode, Integer totalGold, Integer sMinGold, Integer sMaxGold, String beginTime, String endTime) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.publishing));
        mModel.publishTask(submitCode, bazaCode, totalGold, sMinGold, sMaxGold, beginTime, endTime, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
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
                ((BaseActivity) context).hideProgressDialog();
            }
        });
    }

    @Override
    public void unRegisterSubscribe() {
        mModel.unRegisterSubscribe();
    }
}
