package com.yzdsmart.Collectmoney.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;

/**
 * Created by YZD on 2016/5/16.
 */
public class Utils {
    private static long lastClickTime;
    private final static int SPACE_TIME = 100;

    /**
     * 判断用户是否重复点击
     *
     * @return
     */
    public synchronized static boolean isFastDoubleClick() {
        long currentTime = System.currentTimeMillis();
        if (SPACE_TIME > (currentTime - lastClickTime)) {
            return true;
        }
        lastClickTime = currentTime;
        return false;
    }

    public static boolean isNetUsable(Context context) {
        //1、获取ConnectivityManager对象
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            //2、获取NetworkInfo对象
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            //3、判断当前网络状态是否为连接状态
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.net_unusable));
        return false;
    }
}
