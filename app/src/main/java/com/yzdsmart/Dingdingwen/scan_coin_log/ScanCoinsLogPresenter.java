package com.yzdsmart.Dingdingwen.scan_coin_log;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.GetCoinsLogRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;

/**
 * Created by YZD on 2016/9/5.
 */
public class ScanCoinsLogPresenter implements ScanCoinsLogContract.ScanCoinsLogPresenter {
    private Context context;
    private ScanCoinsLogContract.ScanCoinsLogView mView;
    private ScanCoinsLogModel mModel;

    public ScanCoinsLogPresenter(Context context, ScanCoinsLogContract.ScanCoinsLogView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new ScanCoinsLogModel();
        mView.setPresenter(this);
    }

    @Override
    public void getCoinsLog(String action, String submitCode, String custCode, Integer pageIndex, Integer pageSize, Integer lastsequence, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getCoinsLog(action, submitCode, custCode, pageIndex, pageSize, lastsequence, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                GetCoinsLogRequestResponse response = (GetCoinsLogRequestResponse) result;
                if ("OK".equals(response.getActionStatus())) {
                    mView.onGetCoinsLog(response.getLists(), response.getLastsequence());
                } else {
                    ((BaseActivity) context).showSnackbar(response.getErrorInfo());
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_get_scan_coin_log));
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
