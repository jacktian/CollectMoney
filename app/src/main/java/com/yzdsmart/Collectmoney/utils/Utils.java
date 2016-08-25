package com.yzdsmart.Collectmoney.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;

import java.util.regex.Pattern;

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
        return false;
    }

    /**
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     *
     * @param mobile 移动、联通、电信运营商的号码段
     *               <p>移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     *               、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p>
     *               <p>联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）</p>
     *               <p>电信的号段：133、153、180（未启用）、189</p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkMobile(String mobile) {
        String regex = "(\\+\\d+)?1[34578]\\d{9}$";
        return Pattern.matches(regex, mobile);
    }

    /**
     * dp 转化为 px
     *
     * @param context context
     * @param dpValue dpValue
     * @return int
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
