package com.yzdsmart.Collectmoney.add_friend;

import android.content.Context;

import com.tencent.TIMUserProfile;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.http.RequestListener;

import java.util.Collections;

/**
 * Created by YZD on 2016/9/2.
 */
public class AddFriendPresenter implements AddFriendContract.AddFriendPresenter {
    private Context context;
    private AddFriendContract.AddFriendView mView;
    private AddFriendModel mModel;

    public AddFriendPresenter(Context context, AddFriendContract.AddFriendView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new AddFriendModel();
        mView.setPresenter(this);
    }

    @Override
    public void searchFriendById(String identify) {
        mModel.searchFriendById(identify, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                TIMUserProfile profile = (TIMUserProfile) result;
                mView.showUserInfo(Collections.singletonList(profile));
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).showSnackbar(err);
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
