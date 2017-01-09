package com.yzdsmart.Dingdingwen.utils;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by YZD on 2017/1/9.
 */

public class RxUtils {
    public static void unsubscribeIfNotNull(Subscription subscription) {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    public static CompositeSubscription getNewCompositeSubIfUnsubscribed(CompositeSubscription subscription) {
        if (subscription == null || subscription.isUnsubscribed()) {
            return new CompositeSubscription();
        }

        return subscription;
    }
}
