# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# Giữ annotation để Firestore/Gson đọc metadata
-keepattributes *Annotation*, Signature

# (Tuỳ chọn) Giữ Firebase/Play services – tránh cảnh báo
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# GIỮ các model dùng để (de)serialize
# -> thay com.gig.zendo.domain.model bằng package model thực tế của bạn
-keep class com.gig.zendo.domain.model.** { *; }

# Quan trọng: giữ NO-ARG CONSTRUCTOR và FIELDS (để reflection map tên trường)
-keepclassmembers class com.gig.zendo.domain.model.** {
    public <init>();
    <fields>;
}

# Nếu bạn dùng Gson
-keep class com.google.gson.** { *; }
-dontwarn sun.misc.**
