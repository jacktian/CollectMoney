package com.yzdsmart.Dingdingwen;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.morgoo.droidplugin.PluginHelper;
import com.taobao.hotfix.HotFixManager;
import com.taobao.hotfix.PatchLoadStatusListener;
import com.taobao.hotfix.util.PatchStatusCode;
import com.tencent.TIMGroupReceiveMessageOpt;
import com.tencent.TIMManager;
import com.tencent.TIMOfflinePushListener;
import com.tencent.TIMOfflinePushNotification;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;

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
        PluginHelper.getInstance().applicationOnCreate(this);//360插件机制
        HotFixManager.getInstance().setContext(this)
                .setAppVersion(Utils.getAppVersionName(this))
                .setAppId("93879-1")
//                .setAesKey(null)
//                .setSupportHotpatch(true)
//                .setEnableDebug(false)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onload(final int mode, final int code, final String info, final int handlePatchVersion) {
                        // 补丁加载回调通知
                        if (code == PatchStatusCode.CODE_SUCCESS_LOAD) {
                            //表明补丁加载成功
                        } else if (code == PatchStatusCode.CODE_ERROR_NEEDRESTART) {
                            //表明新补丁生效需要重启. 业务方可自行实现逻辑, 提示用户或者强制重启, 建议: 用户可以监听进入后台事件, 然后应用自杀
                        } else if (code == PatchStatusCode.CODE_ERROR_INNERENGINEFAIL) {
                            // 内部引擎加载异常, 推荐此时清空本地补丁, 但是不清空本地版本号, 防止失败补丁重复加载
                            //HotFixManager.getInstance().cleanPatches(false);
                        } else {
                            //其它错误信息, 查看PatchStatusCode类说明
                        }
                    }
                }).initialize();//阿里百川初始化
    }

    @Override
    protected void attachBaseContext(Context base) {
        PluginHelper.getInstance().applicationAttachBaseContext(base);//360插件机制
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
        System.exit(0);
    }
}
