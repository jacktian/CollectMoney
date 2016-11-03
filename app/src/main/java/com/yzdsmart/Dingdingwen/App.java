package com.yzdsmart.Dingdingwen;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.tencent.TIMGroupReceiveMessageOpt;
import com.tencent.TIMManager;
import com.tencent.TIMOfflinePushListener;
import com.tencent.TIMOfflinePushNotification;
import com.tencent.qalsdk.sdk.MsfSdkUtils;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by YZD on 2016/8/17.
 */
public class App extends MultiDexApplication {
    private static App appContext;
    //保存代开的Activity
    private List<Activity> storedActivities;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        storedActivities = new ArrayList<Activity>();
        //云通信离线消息推送，只能在主进程进行离线推送监听器注册
        if (MsfSdkUtils.isMainProcess(this)) {
            // 设置离线推送监听器
            TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
                @Override
                public void handleNotification(TIMOfflinePushNotification timOfflinePushNotification) {
                    if (timOfflinePushNotification.getGroupReceiveMsgOpt() == TIMGroupReceiveMessageOpt.ReceiveAndNotify) {
                        //消息被设置为需要提醒
                        timOfflinePushNotification.doNotify(getApplicationContext(), R.mipmap.notification_icon);
                    }
                }
            });
        }
        JPushInterface.setDebugMode(false);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 获取应用上下文
     *
     * @return
     */
    public static App getAppInstance() {
        return appContext;
    }

    /**
     * 保存已启动的Activity
     *
     * @param activity
     */
    public void storeActivity(Activity activity) {
        storedActivities.add(activity);
    }

    /**
     * 移除
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (null != activity && storedActivities.contains(activity)) {
            storedActivities.remove(activity);
        }
    }

    /**
     * 退出程序
     */
    public void exitApp() {
        for (Activity activity : storedActivities) {
            if (null != activity) {
                activity.finish();
            }
        }
        storedActivities.clear();
    }
}
