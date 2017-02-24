package com.yzdsmart.Dingdingwen.game_details;

import com.yzdsmart.Dingdingwen.http.RequestAdapter;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.GameTaskRequestResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2017/2/24.
 */

public class GameDetailsModel {
    //网络请求监听
    private Subscriber<GameTaskRequestResponse> gameSubscriber;

    void getGameTasks(String action, String submitCode, String custCode, String gameCode, String authorization, final RequestListener listener) {
        gameSubscriber = new Subscriber<GameTaskRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(GameTaskRequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        RequestAdapter.getDDWRequestService().getGameTasks(action, submitCode, custCode, gameCode, authorization)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(gameSubscriber);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != gameSubscriber) {
            gameSubscriber.unsubscribe();
        }
    }
}
