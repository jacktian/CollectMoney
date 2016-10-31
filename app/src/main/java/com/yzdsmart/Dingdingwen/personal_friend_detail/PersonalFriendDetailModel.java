package com.yzdsmart.Dingdingwen.personal_friend_detail;

import android.util.Log;

import com.tencent.TIMAddFriendRequest;
import com.tencent.TIMCallBack;
import com.tencent.TIMDelFriendType;
import com.tencent.TIMFriendResult;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMValueCallBack;
import com.yzdsmart.Collectmoney.http.RequestAdapter;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.CustDetailInfoRequestResponse;
import com.yzdsmart.Collectmoney.http.response.CustInfoRequestResponse;
import com.yzdsmart.Collectmoney.http.response.GetGalleyRequestResponse;

import java.util.Collections;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by YZD on 2016/8/29.
 */
public class PersonalFriendDetailModel {
    //网络请求监听
    private Subscriber<CustInfoRequestResponse> getCustInfoSubscriber;
    private Subscriber<CustDetailInfoRequestResponse> getCustDetailSubscriber;
    private Subscriber<GetGalleyRequestResponse> getGalleySubscriber;

    void getCustInfo(String submitcode, String custCode, final RequestListener listener) {
        getCustInfoSubscriber = new Subscriber<CustInfoRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(CustInfoRequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        RequestAdapter.getRequestService().getCustInfo(submitcode, custCode)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getCustInfoSubscriber);
    }

    void getCustDetailInfo(String actioncode, String submitCode, String custCode, String selfCustCode, final RequestListener listener) {
        getCustDetailSubscriber = new Subscriber<CustDetailInfoRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(CustDetailInfoRequestResponse requestResponse) {
                listener.onSuccess(requestResponse);
            }
        };
        RequestAdapter.getRequestService().getCustDetailInfo(actioncode, submitCode, custCode, selfCustCode)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getCustDetailSubscriber);
    }

    void getPersonalGalley(String action, String submitCode, String custCode, final RequestListener listener) {
        getGalleySubscriber = new Subscriber<GetGalleyRequestResponse>() {
            @Override
            public void onCompleted() {
                listener.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e.getMessage());
            }

            @Override
            public void onNext(GetGalleyRequestResponse response) {
                listener.onSuccess(response);
            }
        };
        RequestAdapter.getRequestService().getPersonalGalley(action, submitCode, custCode)
                .subscribeOn(Schedulers.io())// 指定subscribe()发生在IO线程请求网络/io () 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(getGalleySubscriber);
    }

    void addFriend(String identify, final RequestListener listener) {
        //添加好友请求
        TIMAddFriendRequest req = new TIMAddFriendRequest();
        req.setIdentifier(identify);
        //申请添加好友
        TIMFriendshipManager.getInstance().addFriend(Collections.singletonList(req), new TIMValueCallBack<List<TIMFriendResult>>() {
            @Override
            public void onError(int i, String s) {
                listener.onError(s);
            }

            @Override
            public void onSuccess(List<TIMFriendResult> timFriendResults) {
                listener.onSuccess(timFriendResults);
            }
        });
    }

    void deleteFriend(String identify, final RequestListener listener) {
        TIMAddFriendRequest req = new TIMAddFriendRequest();
        req.setIdentifier(identify);
        TIMFriendshipManager.getInstance().delFriend(TIMDelFriendType.TIM_FRIEND_DEL_BOTH, Collections.singletonList(req), new TIMValueCallBack<List<TIMFriendResult>>() {
            @Override
            public void onError(int i, String s) {
                listener.onError(s);
            }

            @Override
            public void onSuccess(List<TIMFriendResult> timFriendResults) {
                listener.onSuccess(timFriendResults);
            }
        });
    }

    void remarkFriend(String identify, final String remark, final RequestListener listener) {
        TIMFriendshipManager.getInstance().setFriendRemark(identify, remark,
                new TIMCallBack() {//回调接口

                    @Override
                    public void onSuccess() {//成功
                        listener.onSuccess(remark);
                    }

                    @Override
                    public void onError(int code, String desc) {//失败
                        listener.onError(desc);
                    }
                });
    }

    void unRegisterSubscribe() {
        //解除引用关系，以避免内存泄露的发生
        if (null != getCustInfoSubscriber) {
            getCustInfoSubscriber.unsubscribe();
        }
        if (null != getCustDetailSubscriber) {
            getCustDetailSubscriber.unsubscribe();
        }
        if (null != getGalleySubscriber) {
            getGalleySubscriber.unsubscribe();
        }
    }
}
