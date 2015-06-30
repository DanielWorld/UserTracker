# This is Proguard setting for UserTracker SDK jar #

-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclasseswithmembers class com.danielworld.usertracker.UserTracker {
    synchronized public static final com.danielworld.usertracker.UserTracker getInstance(android.content.Context);
    public final void sendFirstRun();
    public final void sendForeground(java.lang.String);
    public final void sendBackground(java.lang.String);
    public final void sendAction(java.lang.String);
}

-keep class com.danielworld.usertracker.receiver.InstallReferrerReceiver
-keep class com.danielworld.usertracker.service.TrackingService