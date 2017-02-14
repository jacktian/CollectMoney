# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#指定代码的压缩级别
-optimizationpasses 5

#包明不混合大小写
-dontusemixedcaseclassnames

#指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses

#指定不去忽略非公共库的类
-dontskipnonpubliclibraryclassmembers

#保护注解
-keepattributes *Annotation*

#抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

#避免混淆泛型 如果混淆报错建议关掉
-keepattributes Signature
-keepattributes EnclosingMethod

 #预校验
-dontpreverify

 #混淆时是否记录日志
-verbose

#忽略警告
-ignorewarning

 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
#如果引用了v4或者v7包
-dontwarn android.support.**
#保留support下的所有类及其内部类
-keep class android.support.** {*;}
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**
-keep class * extends android.**{*;}

#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
    *** get*();
}

#不混淆资源类
-keepclassmembers class **.R$* {*;}

#保持枚举 enum 类不被混淆
-keepclassmembers enum * {
  public static **[] values();
  public static ** valueOf(java.lang.String);
}

#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclassmembers class * {
    public void *ButtonClicked(android.view.View);
    void *(**On*Event);
    void *(**On*Listener);
}

#移除log代码
-assumenosideeffects class android.util.Log {
    public static *** v(...);
    public static *** i(...);
    public static *** d(...);
    public static *** w(...);
    public static *** e(...);
}

##记录生成的日志数据,gradle build时在本项目根目录输出##
#apk 包内所有 class 的内部结构
-dump proguard/class_files.txt
#未混淆的类和成员
-printseeds proguard/seeds.txt
#列出从 apk 中删除的代码
-printusage proguard/unused.txt
#混淆前后的映射
-printmapping proguard/mapping.txt
########记录生成的日志数据，gradle build时 在本项目根目录输出-end######

#############################################################################################
########################                 以上通用           ##################################
#############################################################################################

#对于实体类我们不能混淆
-keep public class com.yzdsmart.Dingdingwen.bean.** {*;}
-keep class com.yzdsmart.Dingdingwen.http.response.** {*;}

#######################     常用第三方模块的混淆选项         ###################################
## ----------------------------------
##      ButterKnife相关
## ----------------------------------
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

## ----------------------------------
##      rxjava、rxandroid相关
## ----------------------------------
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

## ----------------------------------
##      retroift相关
## ----------------------------------
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

## ----------------------------------
##      OkHttp3相关
## ----------------------------------
-dontwarn okhttp3.**
-keep class okhttp3.** {*;}

## ----------------------------------
##      Okio相关
## ----------------------------------
-dontwarn okio.**
-keep class okio.** {*;}
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }

## ----------------------------------
##      fastjson相关
## ----------------------------------
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** {*;}

## ----------------------------------
##      sharesdk相关
## ----------------------------------
-keep class android.net.http.SslError
-keep class android.webkit.**{*;}
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class m.framework.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-dontwarn cn.sharesdk.**
-dontwarn **.R$*

## ----------------------------------
##      Gson相关
## ----------------------------------
#如果用用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错。
-keep class com.google.**{*;}
# Gson specific classes
-keep class sun.misc.Unsafe {*;}
# Application classes that will be serialized/deserialized over Gson
-dontwarn com.google.**

## ----------------------------------
##      Glide相关
## ----------------------------------
-keep class com.bumptech.glide.** {*;}
#-keep classmembers class com.bumptech.glide.** { *; }
-dontwarn com.bumptech.glide.**
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

## ----------------------------------
##      joda相关
## ----------------------------------
-dontwarn org.joda.time.**
-keep class org.joda.time.** { *; }

## ----------------------------------
##      高德地图相关
## ----------------------------------
#3D 地图
-keep class com.amap.api.maps.**{*;}
-keep class com.autonavi.amap.mapcore.*{*;}
-keep class com.amap.api.trace.**{*;}

#定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

#搜索
-keep class com.amap.api.services.**{*;}

#2D 地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}

#导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}

## ----------------------------------
##      腾讯云通信相关
## ----------------------------------
-keep class com.tencent.**{*;}
-dontwarn com.tencent.**

-keep class com.qq.**{*;}
-dontwarn com.qq.**

-keep class tencent.**{*;}
-dontwarn tencent.**

-keep class qalsdk.**{*;}
-dontwarn qalsdk.**

## ----------------------------------
##      ping++相关
## ----------------------------------
-dontwarn com.alipay.**
-keep class com.alipay.** {*;}

-dontwarn  com.ta.utdid2.**
-keep class com.ta.utdid2.** {*;}

-dontwarn  com.ut.device.**
-keep class com.ut.device.** {*;}

-dontwarn  com.tencent.**
-keep class com.tencent.** {*;}

-dontwarn  com.unionpay.**
-keep class com.unionpay.** {*;}

-dontwarn com.pingplusplus.**
-keep class com.pingplusplus.** {*;}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

## ----------------------------------
##      jpush相关
## ----------------------------------
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

## ----------------------------------
##      友盟相关
## ----------------------------------
-keepclassmembers class * {
    public <init> (org.json.JSONObject);
}

-keep public class [com.yzdsmart.Dingdingwen].R$*{
    public static final int *;
}

-dontwarn org.apache.http.**
-keep class org.apache.http.** { *; }

-dontwarn cn.bingoogolapple.**
-keep class cn.bingoogolapple.** { *; }

## ----------------------------------
##      映客相关
## ----------------------------------
-dontwarn com.morgoo.**
-keep class com.morgoo.** { *; }

-dontwarn com.meelive.**
-keep class com.meelive.** { *; }

## ----------------------------------
##      阿里百川相关
## ----------------------------------
-keep class * extends java.lang.annotation.Annotation
-keep class com.alipay.euler.andfix.**{ *; }
-keep class com.taobao.hotfix.aidl.**{ *;}
-keep class com.ta.utdid2.device.**{ *;}
-keep class com.taobao.hotfix.HotFixManager{
    public *;
}