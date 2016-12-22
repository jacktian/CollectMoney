package com.yzdsmart.Dingdingwen.utils;

import android.content.Context;
import android.content.Intent;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;

/**
 * Created by YZD on 2016/4/20.
 */
public class IntentUtils {
    public static void startActivity(Context context, Intent intent,
                                     int requestCode) {
        if (requestCode == 0) {
            context.startActivity(intent);
        } else {
            ((BaseActivity) context).startActivityForResult(intent,
                    requestCode);
        }
        ((BaseActivity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * 获取打开App的意图
     *
     * @param context     上下文
     * @param packageName 包名
     * @return intent
     */
    public static Intent getLaunchAppIntent(Context context, String packageName) {
        return context.getPackageManager().getLaunchIntentForPackage(packageName);
    }
}
