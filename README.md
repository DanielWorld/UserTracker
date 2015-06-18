# UserTracker
UserTracking SDK : Tracks Users action

## Development Environment
- Apple OS X 10.10 & Windows 7
- Android Studio 1.3.0
- Java version 1.8.0_45
- Android 5.1.1 (22)

## Environment Settings
- in app/build.gradle
    minSdkVersion should be 10 or higher level

### AndroidManifest.xml Settings
INTERNET, GET_ACCOUNTS are required
<pre>
&lt;uses-permission android:name="android.permission.INTERNET" /&gt; <!-- Required -->
&lt;uses-permission android:name="android.permission.GET_ACCOUNTS" /&gt; <!-- Required -->
&lt;uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /&gt;
</pre>

set app id in string.xml
<pre>
&lt;resources&gt;
    ...
    &lt;string name="app_id"&gt;0123456789&lt;/string&gt;
&lt;/resources&gt;
</pre>

To read app id, set meta-data in AndroidManifest.xml
<pre>
&lt;application
    ...&gt;
    ...
    &lt;meta-data
        android:name="com.namgyuworld.utility.appId"
        android:value="@string/app_id" /&gt;
</pre>

Tracking data is transmitting through service
<pre>
&lt;service
      android:name="com.namgyuworld.usertracker.service.TrackingService /&gt;
</pre>

Set Receiver to get InstallReferrer
<pre>
&lt;receiver
      android:name="com.namgyuworld.usertracker.receiver.InstallReferrerReceiver"
      android:enabled="true"
      android:exported="true"&gt;
        &lt;intent-filter&gt;
            &lt;action android:name="com.android.vending.INSTALL_REFERRER" />
        &lt;/intent-fileter&gt;
&lt;/receiver&gt;
</pre>

## How to use UserTracker

## How to test InstallReferrer in local
Using terminal, you can deliver install referrer to your device <br>
Go to folder where adb is installed

$ adb shell
$ am broadcast -a com.android.vending.INSTALL_REFERRER 
-n namgyuworld.usertracker/com.namgyuworld.usertracker.receiver.InstallReferrerReceiver --es "referrer" "com=userTracker&name=daniel&com=namgyuworld&id=234"

