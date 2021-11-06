package padd.qlckh.cn.tempad.http.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import padd.qlckh.cn.tempad.SHAUtil;
import padd.qlckh.cn.tempad.http.RxHttpUtils;

/**
 * @author Andy
 * @date 2018/5/15 18:51
 * @link {http://blog.csdn.net/andy_l1}
 * Desc:    AppUtils.java
 */

public class AppUtils {
    public static String getCode() {
        //we make this look like a valid IMEI
        return "35" +
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits
    }

    /**
     * 获取设备唯一号  不涉及权限
     */
    public static String getDeviceId(Context context) {

        try {
            StringBuilder sbDeviceId = new StringBuilder();

            sbDeviceId.append(Build.BRAND);
            sbDeviceId.append("|");
            //获得AndroidId
            String androidid = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            //获得设备序列号（无需权限）
            String serial = Build.SERIAL;
            //追加androidid
            if (androidid != null && androidid.length() > 0) {
                sbDeviceId.append(androidid);
                sbDeviceId.append("|");
            }
            //追加serial
            if (serial != null && serial.length() > 0) {
                sbDeviceId.append(serial);
                sbDeviceId.append("|");
            }
            return SHAUtil.sha1(getCode() + sbDeviceId.toString());
        } catch (Exception e) {
            return SHAUtil.sha1(getCode());
        }
    }
    /**
     * 获取手机版本号
     *
     * @return 返回版本号
     */
    public static String getAppVersion() {
        PackageInfo pi;
        String versionNum;
        try {
            PackageManager pm = RxHttpUtils.getContext().getPackageManager();
            pi = pm.getPackageInfo(RxHttpUtils.getContext().getPackageName(), PackageManager.GET_CONFIGURATIONS);
            versionNum = pi.versionName;
        } catch (Exception e) {
            versionNum = "0";
        }
        return versionNum;
    }

    /**
     * 获取手机唯一标识码UUID
     *
     * @return 返回UUID
     * <p>
     * 记得添加相应权限
     * android.permission.READ_PHONE_STATE
     */
    public static String getUUID() {

        Context context = RxHttpUtils.getContext();

        String uuid = (String) SPUtils.get(context, "PHONE_UUID", "");

        if (TextUtils.isEmpty(uuid)) {

            try {
                TelephonyManager telephonyManager = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                @SuppressLint({"MissingPermission", "HardwareIds"}) String tmDevice = telephonyManager.getDeviceId();
                @SuppressLint({"MissingPermission", "HardwareIds"}) String tmSerial = telephonyManager.getSimSerialNumber();

                @SuppressLint("HardwareIds") String androidId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
                String uniqueId = deviceUuid.toString();
                uuid = uniqueId;
                SPUtils.put(context, "PHONE_UUID", uuid);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return uuid;

    }

}
