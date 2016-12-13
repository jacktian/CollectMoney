package com.yzdsmart.Dingdingwen.money_friendship.recommend_friends;

import com.yzdsmart.Dingdingwen.http.RequestAdapter;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.RecommendFriendsRequestResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/9/25.
 */

public class RecommendFriendsModel {
    //网络请求监听
    private Subscriber<RecommendFriendsRequestResponse> getRecommendFriendsSubscriber;

    void getRecommendFriends(String action, String submitCode, String custCode, Integer recomNum, String authorization, final RequestListener listener) {
        getRecommendFriendsSubscriber = new Subscriber<RecommendFriendsRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(RecommendFriendsRequestResponse response) {
                listener.onSuccess(response);
            }
        };
        RequestAdapter.getDDWRequestService().getRecommendFriends(action, submitCode, custCode, recomNum, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getRecommendFriendsSubscriber);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != getRecommendFriendsSubscriber) {
            getRecommendFriendsSubscriber.unsubscribe();
        }
    }
}
