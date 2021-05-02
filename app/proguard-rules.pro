# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html
#
# Starting with version 2.2 of the Android plugin for Gradle, these files are no longer used. Newer
# versions are distributed with the plugin and unpacked at build time. Files in this directory are
# no longer maintained.
#
# Optimizations: If you don't want to optimize, use the
# proguard-android.txt configuration file instead of this one, which
# turns off the optimization flags.  Adding optimization introduces
# certain risks, since for example not all optimizations performed by
# ProGuard works on all versions of Dalvik.  The following flags turn
# off various optimizations known to have issues, but the list may not
# be complete or up to date. (The "arithmetic" optimization can be
# used if you are only targeting Android 2.0 or later.)  Make sure you
# test thoroughly if you go this route.

-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-dontpreverify

# The remainder of this file is identical to the non-optimized version
# of the Proguard configuration file (except that the other file has
# flags to turn off optimization).

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

-keepattributes *Annotation*
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClickSize
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers class * extends android.service.wallpaper.WallpaperService {
   public void *(android.view.View);
}
# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**

# Understand the @Keep support annotation.
-keep class android.support.annotation.Keep

-keep @android.support.annotation.Keep class * {*;}

-keep public class * extends android.preference.Preference
-keep class org.apache.commons.logging.**
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class android.support.v7.widget.* { *; }
-keep public class android.support.v7.internal.widget.* { *; }
-keep public class android.support.v7.internal.view.menu.* { *; }

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}
-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

## admob
-keep class com.google.android.gms.** # Don't proguard AdMob classes
-dontwarn com.google.android.gms.**


# Picasso
-dontwarn com.squareup.okhttp.**

#-------My Class ------------------------
-keep class com.suusoft.ecommerce.objects.** { *; }
-keep class com.suusoft.ecommerce.base.** { *; }
-keep class com.suusoft.ecommerce.network1.** { *; }
-keep class com.suusoft.ecommerce.network.** { *; }
-keep class * extends com.suusoft.ecommerce.base.BaseActivity { *; }
-keep class * extends com.suusoft.ecommerce.base.BaseFragment { *; }


#quickblox sample chat

-keep class com.quickblox.auth.parsers.** { *; }
-keep class com.quickblox.auth.model.* { *; }
-keep class com.quickblox.core.parser.* { *; }
-keep class com.quickblox.core.model.* { *; }
-keep class com.quickblox.core.server.* { *; }
-keep class com.quickblox.core.rest.* { *; }
-keep class com.quickblox.core.error.* { *; }
-keep class com.quickblox.core.Query { *; }

-keep class com.quickblox.users.parsers.** { *; }
-keep class com.quickblox.users.model.* { *; }

-keep class com.quickblox.chat.parser.** { *; }
-keep class com.quickblox.chat.model.* { *; }

-keep class com.quickblox.messages.parsers.** { *; }
-keep class com.quickblox.messages.model.* { *; }

-keep class com.quickblox.content.parsers.** { *; }
-keep class com.quickblox.content.model.* { *; }

-keep class org.jivesoftware.** { *; }
-dontwarn org.jivesoftware.**
##---------------End: proguard configuration for quickblox ----------

-dontwarn okio.**

-repackageclasses com.suusoft.ecommerce



