# This is Proguard setting for UserTracker SDK jar #

-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclasseswithmembers class com.namgyuworld.usertracker.UserTracker {
    synchronized public static final com.namgyuworld.usertracker.UserTracker getInstance(android.content.Context);
    public final void sendForeground(java.lang.String);
    public final void sendBackground(java.lang.String);
    public final void sendAction(java.lang.String);
}

-keep class com.namgyuworld.usertracker.receiver.InstallReferrerReceiver
-keep class com.namgyuworld.usertracker.service.TrackingService