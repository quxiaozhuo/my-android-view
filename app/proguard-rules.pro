# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/wd/Library/Android/sdk/tools/proguard/proguard-android.txt
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
#-renamesourcefileattribute SourceFile
#############################################
# 对于一些基本指令的添加
#############################################
# 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
-optimizationpasses 5
# 混合时不使用大小写混合，混合后的类名为小写
-dontusemixedcaseclassnames
# 指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses
# 这句话能够使我们的项目混淆后产生映射文件
# 包含有类名->混淆后类名的映射关系
-verbose
# 指定不去忽略非公共库的类成员
-dontskipnonpubliclibraryclassmembers
# 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
-dontpreverify
# 忽略警告
-ignorewarnings
# 保留Annotation不混淆
-keepattributes *Annotation*,InnerClasses
# 避免混淆泛型
-keepattributes Signature
#-renamesourcefileattribute UnknownFile
# 抛出异常时保留代码行号
-keepattributes  SourceFile,LineNumberTable
# 指定混淆是采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/cast,!field/*,!class/merging/*
#############################################
# Android开发中一些需要保留的公共部分
#############################################
# 保留我们使用的四大组件，自定义的Application等等这些类不被混淆
# 因为这些子类都有可能被外部调用
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
# 保留support下的所有类及其内部类
-keep class android.support.** {*;}
# 保留继承的
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**
# 保留R下面的资源
-keep class **.R$* {*;}
# 保留本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
# 保留在Activity中的方法参数是view的方法，
# 这样以来我们在layout中写的onClick就不会被影响
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}
# 保留枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
# 保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
# 保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
# 保留Serializable序列化的类不被混淆
-keepnames class * implements java.io.Serializable
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
# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

#-----------处理js交互---------------
-keepattributes *JavascriptInterface*
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
public *;
}

-keepclassmembers class * extends android.webkit.WebViewClient {
 public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
 public boolean *(android.webkit.WebView, java.lang.String);
}

-keepclassmembers class * extends android.webkit.WebViewClient {
 public void *(android.webkit.WebView, java.lang.String);
}
#腾讯X5
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
#-renamesourcefileattribute TbsSdkJava
-keep class com.tencent.smtt.sdk.**{ *; }
-keep class com.tencent.smtt.export.external.**{
    *;
}

-keep class com.tencent.tbs.video.interfaces.IUserStateChangedListener {
    *;
}
-keep public class com.tencent.smtt.export.external.extension.interfaces.* {
    public protected *;
}

-keep public class com.tencent.smtt.export.external.interfaces.* {
    public protected *;
}


#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}

#okio
-dontwarn okio.**
-keep class okio.**{*;}

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# For using GSON @Expose annotation
-keepattributes *Annotation*
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.gson.** { *; }
-keep class com.ke.gson.** { *; }


##---------------BaseAdapter  ----------

-keep class com.chad.library.adapter.** {
*;
}
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keep public class * extends com.chad.library.adapter.base.BaseViewHolder
-keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
     <init>(...);
}
##Glide
-dontwarn com.bumptech.glide.**
-keep class com.bumptech.glide.**{*;}
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# 从glide4.0开始，GifDrawable没有提供getDecoder()方法，
# 需要通过反射获取gifDecoder字段值，所以需要保持GifFrameLoader和GifState类不被混淆
-keep class com.bumptech.glide.load.resource.gif.GifDrawable$GifState{*;}
-keep class com.bumptech.glide.load.resource.gif.GifFrameLoader {*;}
#AgentWeb  WebView
-keep class com.just.agentweb.** {
    *;
}
-dontwarn com.just.agentweb.**
-keepclassmembers class com.just.agentweb.sample.common.AndroidInterface{ *; }


# banner 的混淆代码
-keep class com.youth.banner.** {
    *;
 }

#############################################
# Library中特殊处理部分
#############################################
-keep class com.gyf.immersionbar.* {*;}
 -dontwarn com.gyf.immersionbar.**




-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}


-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }


-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.event.Subscribe <methods>;
}
-keep enum org.greenrobot.event.ThreadMode { *; }
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.event.util.ThrowableFailureEvent {
    <init>(Java.lang.Throwable);
}

-keepclassmembers class ** {
    public void onEvent*(**);
}


-keep class com.library.base.http.** {
*;
}

-keep class com.library.view.web.** {
*;
}


##腾讯地图
-keep class com.tencent.tencentmap.**{*;}
-keep class com.tencent.map.**{*;}
-keep class com.tencent.beacontmap.**{*;}
-keep class navsns.**{*;}
-dontwarn com.qq.**
-dontwarn com.tencent.**
-keepclassmembers class ** {
    public void on*Event(...);
}
-keep class c.t.**{*;}
-keep class com.tencent.map.geolocation.**{*;}


#############################################
# 项目中特殊处理部分
#############################################
-dontoptimize
-dontpreverify
-keep class com.my.view.data.** {
*;
}
-keep class com.my.view.data.prefs.** {
*;
}
-keep class com.my.view.data.http.** {
*;
}
-keep class com.my.view.data.http.banner.** {
*;
}
-keep class com.my.view.data.http.home.** {
*;
}
-keep class com.my.view.data.http.js.** {
*;
}
-keep class com.my.view.data.http.main.** {
*;
}
-keep class com.my.view.data.http.push.** {
*;
}
-keep class com.my.view.data.http.read.** {
*;
}
-keep class com.my.view.data.http.task.** {
*;
}
-keep class com.my.view.data.http.user.** {
*;
}
-keep class com.my.view.data.http.work.** {
*;
}


#############################################
# 图片选择
#############################################

#PictureSelector 2.0
-keep class com.luck.picture.lib.** { *; }
#Ucrop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }
#Okio
-dontwarn org.codehaus.mojo.animal_sniffer.*