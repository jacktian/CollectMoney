package com.yzdsmart.Collectmoney.main.personal;

import com.yzdsmart.Collectmoney.bean.User;
import com.yzdsmart.Collectmoney.http.RequestAdapter;
import com.yzdsmart.Collectmoney.http.RequestListener;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/8/27.
 */
public class PersonalModel {
    //网络请求监听
    private Subscriber<User> getUserGraStaSubscriber;

    /**
     * 获取用户等级和星级
     *
     * @param custcode
     * @param submitcode
     */
    void getUserGraSta(String custcode, String submitcode, final RequestListener listener) {
        getUserGraStaSubscriber = new Subscriber<User>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(User userGraSta) {
                listener.onSuccess(userGraSta);
            }
        };
        RequestAdapter.getRequestService().getUserGraSta(custcode, submitcode)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getUserGraStaSubscriber);
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != getUserGraStaSubscriber) {
            getUserGraStaSubscriber.unsubscribe();
        }
    }
}
