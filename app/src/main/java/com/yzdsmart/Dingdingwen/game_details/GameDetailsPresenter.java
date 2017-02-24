package com.yzdsmart.Dingdingwen.game_details;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.GameTaskRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;

/**
 * Created by YZD on 2017/2/24.
 */

public class GameDetailsPresenter implements GameDetailsContract.GameDetailsPresenter {
    private Context context;
    private GameDetailsContract.GameDetailsView mView;
    private GameDetailsModel mModel;

    public GameDetailsPresenter(Context context, GameDetailsContract.GameDetailsView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new GameDetailsModel();
        mView.setPresenter(this);
    }

    @Override
    public void getGameTasks(String action, String submitCode, String custCode, String gameCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, "正在加载......");
        mModel.getGameTasks(action, submitCode, custCode, gameCode, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                GameTaskRequestResponse requestResponse = (GameTaskRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    mView.onGameScanQRCode(true, requestResponse.getErrorInfo(), requestResponse.getData());
                } else if ("FAIL".equals(requestResponse.getActionStatus())) {
                    mView.onGameScanQRCode(false, requestResponse.getErrorInfo(), null);
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
