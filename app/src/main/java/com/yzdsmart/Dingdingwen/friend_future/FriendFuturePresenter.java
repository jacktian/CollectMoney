package com.yzdsmart.Dingdingwen.friend_future;

import android.content.Context;

import com.tencent.TIMGetFriendFutureListSucc;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.tecent_im.event.FriendshipEvent;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by YZD on 2016/9/2.
 */
public class FriendFuturePresenter implements FriendFutureContract.FriendFuturePresenter, Observer {
    private Context context;
    private FriendFutureContract.FriendFutureView mView;
    private FriendFutureModel mModel;

    public FriendFuturePresenter(Context context, FriendFutureContract.FriendFutureView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new FriendFutureModel();
        mView.setPresenter(this);

        //注册好友关系链监听
        FriendshipEvent.getInstance().addObserver(this);
    }

    @Override
    public void getFutureFriends(int pageSize, long pendSeq, long decideSeq, long recommendSeq) {
        mModel.getFutureFriends(pageSize, pendSeq, decideSeq, recommendSeq, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                TIMGetFriendFutureListSucc listSucc = (TIMGetFriendFutureListSucc) result;
                mView.onGetFutureFriends(listSucc.getItems(), listSucc.getMeta().getPendencySeq(), listSucc.getMeta().getDecideSeq(), listSucc.getMeta().getRecommendSeq());
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).showSnackbar("获取朋友异常!");
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void unRegisterObserver() {
        //注册好友关系链监听
        FriendshipEvent.getInstance().deleteObserver(this);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof FriendshipEvent) {
            FriendshipEvent.NotifyCmd fcmd = (FriendshipEvent.NotifyCmd) data;
            switch (fcmd.type) {
                case ADD_REQ:
                case DEL:
                    mView.refreshFriendFuture();
                    break;
            }
        }
    }
}
