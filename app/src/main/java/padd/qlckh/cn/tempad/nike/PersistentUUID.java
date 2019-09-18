package padd.qlckh.cn.tempad.nike;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import java.io.File;
import java.util.Arrays;
import java.util.UUID;

public class PersistentUUID {
    private static final String UUID_FILENAME = "nr_installation";
    public static final String METRIC_UUID_RECOVERED = "UUIDRecovered";
    private static File UUID_FILE = new File(Environment.getDataDirectory(), UUID_FILENAME);
    private static final String UUID_KEY = "nr_uuid";

    public PersistentUUID(Context context) {
        UUID_FILE = new File(context.getFilesDir(), UUID_FILENAME);
    }

    public String getDeviceId(Context context) {
        String generateUniqueID = generateUniqueID(context);
        return TextUtils.isEmpty(generateUniqueID) ? UUID.randomUUID().toString() : generateUniqueID;
    }

    @SuppressLint({"MissingPermission"})
    private String generateUniqueID(Context context) {
        String str = Build.SERIAL;
        String str2 = Build.ID;
        try {
            str2 = Secure.getString(context.getContentResolver(), "android_id");
            if (TextUtils.isEmpty(str2)) {
                return UUID.randomUUID().toString();
            }
            StringBuilder stringBuilder;
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (telephonyManager != null) {
                    str = telephonyManager.getDeviceId();
                }
            } catch (Exception unused) {
                str = "badf00d";
            }
            if (TextUtils.isEmpty(str)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(Build.HARDWARE);
                stringBuilder.append(Build.DEVICE);
                stringBuilder.append(Build.BOARD);
                stringBuilder.append(Build.BRAND);
                str = stringBuilder.toString();
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(intToHexString(str2.hashCode(), 8));
            stringBuilder.append("-");
            stringBuilder.append(intToHexString(str.hashCode(), 4));
            stringBuilder.append("-");
            stringBuilder.append(intToHexString(VERSION.SDK_INT, 4));
            stringBuilder.append("-");
            stringBuilder.append(intToHexString(VERSION.RELEASE.hashCode(), 12));
            return stringBuilder.toString();
        } catch (Exception unused2) {
            return UUID.randomUUID().toString();
        }
    }

    private String intToHexString(int i, int i2) {
        String str = "";
        String toHexString = Integer.toHexString(i);
        char[] cArr = new char[(i2 - toHexString.length())];
        Arrays.fill(cArr, '0');
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(new String(cArr));
        stringBuilder.append(toHexString);
        toHexString = stringBuilder.toString();
        int i3 = 0;
        for (int length = toHexString.length() - 1; length >= 0; length--) {
            i3++;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(toHexString.substring(length, length + 1));
            stringBuilder2.append(str);
            str = stringBuilder2.toString();
            if (i3 % i2 == 0) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("-");
                stringBuilder2.append(str);
                str = stringBuilder2.toString();
            }
        }
        return str.startsWith("-") ? str.substring(1, str.length()) : str;
    }

    /* access modifiers changed from: protected */
    public void setPersistedUUID(String str) {
        putUUIDToFileStore(str);
    }
    public java.lang.String getUUIDFromFileStore() {
        throw new UnsupportedOperationException("Method not decompiled: com.newrelic.agent.android.util.PersistentUUID.getUUIDFromFileStore():java.lang.String");
    }

    public void putUUIDToFileStore(java.lang.String r6) {
        throw new UnsupportedOperationException("Method not decompiled: com.newrelic.agent.android.util.PersistentUUID.putUUIDToFileStore(java.lang.String):void");
    }
}