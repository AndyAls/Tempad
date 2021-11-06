package padd.qlckh.cn.tempad;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author Andy
 * @date 2021/11/6 17:12
 * Desc:
 */
public class NetUtils {
    /**
     * 有网没网
     *
     * @param context
     * @return true： 有网   false：没网
     */
    public static boolean hasNetwork(Context context) {
        if (isWifiConnected(context)) {
            return true;
        }
        if (isMobileConnected(context)) {
            return true;

        }

        return false;

    }

    /**
     * 判断wifi有没有连接
     * <p>
     * return：true：连上wifi false：未连上wifi
     */

    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context

                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo mWiFiNetworkInfo = mConnectivityManager

                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isConnected()&&mWiFiNetworkInfo.isAvailable();

            }

        }

        return false;

    }

    /**
     * 判断数据网络有没有连接
     * <p>
     * return：true：连接 false：未连接
     */

    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context

                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo mMobileNetworkInfo = mConnectivityManager

                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isConnected()&&mMobileNetworkInfo.isAvailable();

            }

        }

        return false;

    }
}
