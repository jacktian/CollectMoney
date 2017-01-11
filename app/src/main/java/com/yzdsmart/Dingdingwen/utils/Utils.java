package com.yzdsmart.Dingdingwen.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.List;
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

    /**
     * px 转化为 dp
     *
     * @param context context
     * @param pxValue pxValue
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 判断字符串是否为null或长度为0
     *
     * @param s 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(CharSequence s) {
        return (s == null || s.toString().trim().length() == 0);
    }

    /**
     * 判断App是否安装
     *
     * @param context     上下文
     * @param packageName 包名
     * @return {@code true}: 已安装<br>{@code false}: 未安装
     */
    public static boolean isInstallApp(Context context, String packageName) {
        return !isEmpty(packageName) && IntentUtils.getLaunchAppIntent(context, packageName) != null;
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
     * 关闭键盘
     *
     * @param activity Activity
     */
    public static void hideSoftInput(Activity activity) {
        ((InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE))
                .toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 根据mipmap文件的名字取得id
     *
     * @param context context
     * @param name    name
     * @return int
     */
    public static int getMipmapId(Context context, String name) {
        return context.getResources().getIdentifier(name, "mipmap", context.getPackageName());
    }

    /**
     * 银行卡号，保留最后4位，其他星号替换
     *
     * @param cardId 卡号
     * @return
     */
    public static String cardIdHide(String cardId) {
        return cardId.replaceAll("\\d{15}(\\d{3})", "**** **** **** **** $1");
    }

    /**
     * MD5加密
     *
     * @param plainText 需要加密的字符串
     * @return 加密后字符串
     */
    public static String md5(String plainText) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString().toLowerCase();// 32位的加密（转成小写）

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static float getScreenRatio(Context context) {
        // 字体大小随分辨率大小变化
        // 计算与你开发时设定的屏幕大小的纵横比
        int screenWidth = Utils.deviceWidth(context);
        int screenHeight = Utils.deviceHeight(context);
        float ratioWidth = (float) screenWidth / 480;
        float ratioHeight = (float) screenHeight / 800;

        float ratio = Math.min(ratioWidth, ratioHeight);
        return ratio;
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {

        }
        return null;
    }

    /**
     * FilePath To Bitmap
     *
     * @param context  上下文
     * @param filePath 文件路径
     */
    public static Bitmap getBitmapFromFilePath(Context context, String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        int bmpWidth = options.outWidth;
        int bmpHeight = options.outHeight;

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;

        options.inSampleSize = 1;
        if (bmpWidth > bmpHeight) {
            if (bmpWidth > screenWidth)
                options.inSampleSize = bmpWidth / screenWidth;
        } else {
            if (bmpHeight > screenHeight)
                options.inSampleSize = bmpHeight / screenHeight;
        }
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static byte[] decodeBitmap(String path) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
        BitmapFactory.decodeFile(path, opts);
        opts.inSampleSize = computeSampleSize(opts, -1, 1024 * 800);
        opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        opts.inDither = false;
        opts.inPurgeable = true;
        opts.inTempStorage = new byte[16 * 1024];
        FileInputStream is = null;
        Bitmap bmp = null;
        ByteArrayOutputStream baos = null;
        try {
            is = new FileInputStream(path);
            bmp = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
            double scale = getScaling(opts.outWidth * opts.outHeight,
                    1024 * 600);
            Bitmap bmp2 = Bitmap.createScaledBitmap(bmp,
                    (int) (opts.outWidth * scale),
                    (int) (opts.outHeight * scale), true);
            bmp.recycle();
            baos = new ByteArrayOutputStream();
            bmp2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            bmp2.recycle();
            return baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.gc();
        }
        return baos.toByteArray();
    }

    private static double getScaling(int src, int des) {
        /**
         * 48 目标尺寸÷原尺寸 sqrt开方，得出宽高百分比 49
         */
        double scale = Math.sqrt((double) des / (double) src);
        return scale;
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 获取设备宽度（px）
     *
     * @param context context
     * @return int
     */
    public static int deviceWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取设备高度（px）
     */
    public static int deviceHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static boolean isEmpty(String s) {
        if (null == s)
            return true;
        if (s.length() == 0)
            return true;
        if (s.trim().length() == 0)
            return true;
        return false;
    }

    /**
     * 获取设备的唯一标识，deviceId
     *
     * @param context context
     * @return String
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(
                Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        if (deviceId == null) {
            return "-";
        } else {
            return deviceId;
        }
    }

    public static boolean isForeground(Context context) {
//        List<ActivityManager.RunningAppProcessInfo> appProcesses = AndroidProcesses.getRunningAppProcessInfo(context);
//        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
//            if (appProcess.processName.equals(context.getPackageName())) {
//                /*
//                BACKGROUND=400 EMPTY=500 FOREGROUND=100
//                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
//                 */
//                Log.i(context.getPackageName(), "此appimportace ="
//                        + appProcess.importance
//                        + ",context.getClass().getName()="
//                        + context.getClass().getName());
//                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//                    Log.i(context.getPackageName(), "处于后台"
//                            + appProcess.processName);
//                    return true;
//                } else {
//                    Log.i(context.getPackageName(), "处于前台"
//                            + appProcess.processName);
//                    return false;
//                }
//            }
//        }


//        List<AndroidAppProcess> processes = AndroidProcesses.getRunningAppProcesses();
//        for (AndroidAppProcess process : processes) {
//            String processName = process.name;
//            System.out.println(processName + "---->" + process.foreground);
//            if (processName.equals(context.getPackageName())) {
//                return process.foreground;
//            }
//        }
        return false;
    }

    /**
     * 判断程序是否在前台运行
     *
     * @param context
     * @return
     */
    public static boolean isAppInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                //前台程序
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }

    /**
     * 程序是否在前台运行
     */
    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();
        /**
         * 获取Android设备中所有正在运行的App
         */
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }
}
