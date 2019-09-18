/*
 * Copyright (c) 2015, 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package padd.qlckh.cn.tempad.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.UUID;

/**
 * @author Andy
 * @date 2019/3/25 15:48
 * @link {http://blog.csdn.net/andy_l1}
 * Desc:    AppUtil.java
 */
public class AppUtil {

    private static String TAG = AppUtil.class.getSimpleName();

    public static final Application INSTANCE;

    static {
        Application app = null;
        try {
            app = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null);
            if (app == null) {
                throw new IllegalStateException("Static initialization of Applications must be on main thread.");
            }
        } catch (final Exception e) {
            Log.e(TAG, "Failed to get current application from AppGlobals." + e.getMessage());
            try {
                app = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null);
            } catch (final Exception ex) {
                Log.e(TAG, "Failed to get current application from ActivityThread." + e.getMessage());
            }
        } finally {
            INSTANCE = app;
        }
    }

    private static Activity findActivityFrom(final Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof Application || context instanceof Service) {
            return null;
        }
        if (!(context instanceof ContextWrapper)) {
            return null;
        }
        final Context base_context = ((ContextWrapper) context).getBaseContext();
        if (base_context == context) {
            return null;
        }
        return findActivityFrom(base_context);
    }

    public static void startActivity(final Context context, final Intent intent) {
        final Activity activity = findActivityFrom(context);
        if (activity != null) {
            activity.startActivity(intent);
        } else {
            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    public static String getVersionName() {
        try {
            PackageManager pm = INSTANCE.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(INSTANCE.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            return pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getVersionCode() {
        try {
            PackageManager pm = INSTANCE.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(INSTANCE.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            String versionCode = "";
            versionCode = String.valueOf(pi.versionCode);
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @SuppressLint("MissingPermission")
    public static String getunqueId(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, tmPhone, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        return uniqueId;
    }
}