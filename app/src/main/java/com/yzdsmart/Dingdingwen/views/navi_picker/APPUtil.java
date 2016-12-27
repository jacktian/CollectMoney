package com.yzdsmart.Dingdingwen.views.navi_picker;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.LinkedList;
import java.util.List;

public class APPUtil {

    public static String[] paks = new String[]{"com.baidu.BaiduMap",
            "com.autonavi.minimap"};

    /**
     * 通过包名获取应用信息
     *
     * @param context
     * @param packageName
     * @return
     */
    public static AppInfo getAppInfoByPak(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo : packageInfos) {
            if (packageName.equals(packageInfo.packageName)) {
                AppInfo tmpInfo = new AppInfo();
                tmpInfo.setAppName(packageInfo.applicationInfo.loadLabel(packageManager).toString());
                tmpInfo.setPackageName(packageInfo.packageName);
                tmpInfo.setVersionName(packageInfo.versionName);
                tmpInfo.setVersionCode(packageInfo.versionCode);
                tmpInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(packageManager));
                return tmpInfo;
            }
        }
        return null;
    }

    /**
     * 返回当前设备上的地图应用集合
     *
     * @param context
     * @return
     */
    public static List<AppInfo> getMapApps(Context context) {
        LinkedList<AppInfo> apps = new LinkedList<AppInfo>();

        for (String pak : paks) {
            AppInfo appinfo = getAppInfoByPak(context, pak);
            if (appinfo != null) {
                apps.add(appinfo);
            }
        }
        return apps;
    }

    /**
     * 获取应用中所有浏览器集合
     *
     * @param context
     * @return
     */
    public static List<AppInfo> getWebApps(Context context) {
        LinkedList<AppInfo> apps = new LinkedList<AppInfo>();

        String default_browser = "android.intent.category.DEFAULT";
        String browsable = "android.intent.category.BROWSABLE";
        String view = "android.intent.action.VIEW";

        Intent intent = new Intent(view);
        intent.addCategory(default_browser);
        intent.addCategory(browsable);
        Uri uri = Uri.parse("http://");
        intent.setDataAndType(uri, null);

        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, PackageManager.GET_INTENT_FILTERS);

        for (ResolveInfo resolveInfo : resolveInfoList) {
            AppInfo tmpInfo = new AppInfo();
            tmpInfo.setAppName(resolveInfo.loadLabel(packageManager).toString());
            tmpInfo.setAppIcon(resolveInfo.loadIcon(packageManager));
            tmpInfo.setPackageName(resolveInfo.activityInfo.packageName);
            apps.add(tmpInfo);
        }

        return apps;
    }
}
