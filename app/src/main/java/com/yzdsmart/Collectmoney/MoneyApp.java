package com.yzdsmart.Collectmoney;

import android.app.Activity;
import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YZD on 2016/8/17.
 */
public class MoneyApp extends Application {
    private static MoneyApp appContext;
    //保存代开的Activity
    private List<Activity> storedActivities;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        storedActivities = new ArrayList<Activity>();
        SDKInitializer.initialize(this);
    }

    /**
     * 获取应用上下文
     *
     * @return
     */
    public static MoneyApp getAppInstance() {
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
