package com.yzdsmart.Dingdingwen.personal_friend_detail;

import android.content.Context;

import com.tencent.TIMUserProfile;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.CustDetailInfoRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.CustInfoRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.GetGalleyRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;
import com.yzdsmart.Dingdingwen.tecent_im.event.FriendshipEvent;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by YZD on 2016/8/29.
 */
public class PersonalFriendDetailPresenter implements PersonalFriendDetailContract.PersonalFriendDetailPresenter, Observer {
    private Context context;
    private PersonalFriendDetailContract.PersonalFriendDetailView mView;
    private PersonalFriendDetailModel mModel;
    private String friendIdentify;

    public PersonalFriendDetailPresenter(Context context, PersonalFriendDetailContract.PersonalFriendDetailView mView, String friendIdentify) {
        this.context = context;
        this.mView = mView;
        this.friendIdentify = friendIdentify;
        mModel = new PersonalFriendDetailModel();
        mView.setPresenter(this);
        //注册好友关系链监听
        FriendshipEvent.getInstance().addObserver(this);
    }

    @Override
    public void getCustInfo(String submitcode, String custCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getCustInfo(submitcode, custCode, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                CustInfoRequestResponse requestResponse = (CustInfoRequestResponse) result;
                if (null != requestResponse) {
                    mView.onGetCustInfo(requestResponse);
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
    public void getPersonalGalley(String action, String submitCode, String custCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getPersonalGalley(action, submitCode, custCode, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                GetGalleyRequestResponse requestResponse = (GetGalleyRequestResponse) result;
                if ("OK".equals(requestResponse.getActionStatus())) {
                    if (null != requestResponse.getLists() && requestResponse.getLists().size() > 0) {
                        mView.onGetPersonalGalley(requestResponse.getLists());
                    }
                } else {
                    ((BaseActivity) context).showSnackbar(requestResponse.getErrorInfo());
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
    public void getCustDetailInfo(String actioncode, String submitCode, String custCode, String selfCustCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getCustDetailInfo(actioncode, submitCode, custCode, selfCustCode, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                CustDetailInfoRequestResponse response = (CustDetailInfoRequestResponse) result;
                if (null != response) {
                    mView.onGetCustDetailInfo(response);
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
    public void addFriend(String identify) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.requesting));
        mModel.addFriend(identify, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.request_success));
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

            }
        });
    }

    @Override
    public void deleteFriend(String identify) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.requesting));
        mModel.deleteFriend(identify, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.request_success));
                mView.refreshFriendship();
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

            }
        });
    }

    @Override
    public void remarkFriend(String identify, final String remark) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.requesting));
        mModel.remarkFriend(identify, remark, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                ((BaseActivity) context).hideProgressDialog();
                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.request_success));
                mView.onRemarkFriend(remark);
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

            }
        });
    }

    @Override
    public void unRegisterObserver() {
        //解除好友关系链监听
        FriendshipEvent.getInstance().deleteObserver(this);
    }

    @Override
    public void unRegisterSubscribe() {
        mModel.unRegisterSubscribe();
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof FriendshipEvent) {
            FriendshipEvent.NotifyCmd fcmd = (FriendshipEvent.NotifyCmd) data;
            String identify = "";
            switch (fcmd.type) {
                case ADD:
                    identify = ((List<TIMUserProfile>) fcmd.data).get(0).getIdentifier();
                    break;
                case DEL:
                    identify = ((List<String>) fcmd.data).get(0);
                    break;
            }
            if (friendIdentify.equals(identify)) {
                mView.refreshFriendship();
            }
        }
    }
}
