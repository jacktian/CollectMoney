package com.yzdsmart.Dingdingwen.views.navi_picker;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class APPUtil {

    public static String[] paks = new String[]{"com.baidu.BaiduMap",
            "com.autonavi.minimap"};

    //######################################
    //通过URI API接口启调地图工具
    //######################################

    public static void startNative_Baidu(Context context, Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null) {
            return;
        }
        if (loc1.getAddress() == null || "".equals(loc1.getAddress())) {
            loc1.setAddress("我的位置");
        }
        if (loc2.getAddress() == null || "".equals(loc2.getAddress())) {
            loc2.setAddress("目的地");
        }
        try {
            Intent intent = Intent.getIntent("intent://map/direction?origin=latlng:" + loc1.getStringLatLng() + "|name:" + loc1.getAddress() + "&destination=latlng:" + loc2.getStringLatLng() + "|name:" + loc2.getAddress() + "&mode=driving&src=重庆快易科技|CC房车-车主#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "地址解析错误", Toast.LENGTH_SHORT).show();
        }
    }

    public static void startNative_Gaode(Context context, Location loc) {
        if (loc == null) {
            return;
        }
        if (loc.getAddress() == null || "".equals(loc.getAddress())) {
            loc.setAddress("目的地");
        }
        try {
            Intent intent = new Intent("android.intent.action.VIEW",
                    Uri.parse("androidamap://navi?sourceApplication=叮叮蚊&lat=" + loc.getLat() + "&lon=" + loc.getLng() + "&dev=0&style=2&mode=walking"));
            intent.setPackage("com.autonavi.minimap");
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "地址解析错误", Toast.LENGTH_SHORT).show();
        }
    }

    public static void startNative_Tengxun(Context context, Location loc1, Location loc2) {
        /**
         * 目前腾讯还没有可以启调的接口
         */
        return;
    }

    //######################################
    //通启调web地图
    //######################################

    public static String getWebUrl_Baidu(Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null) {
            return null;
        }
        if (loc1.getAddress() == null || "".equals(loc1.getAddress())) {
            loc1.setAddress("我的位置");
        }
        if (loc2.getAddress() == null || "".equals(loc2.getAddress())) {
            loc2.setAddress("目的地");
        }
        return "http://api.map.baidu.com/direction?origin=latlng:" + loc1.getStringLatLng() + "|name:" + loc1.getAddress() + "&destination=latlng:" + loc2.getStringLatLng() + "|name:" + loc2.getAddress() + "&mode=driving&src=重庆快易科技|CC房车-车主";
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        // 获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);

        packageManager.getInstalledApplications(packageManager.GET_META_DATA);
        // 用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

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
