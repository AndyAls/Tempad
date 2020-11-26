package padd.qlckh.cn.tempad

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.TelephonyManager

/**
 * @author Andy
 * @date 2019/5/30 16:59
 * Desc:
 */
object CommUtils {

    /**
     * 获取手机IMEI唯一标识
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    @JvmStatic
    fun getIMEI(context: Context): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.deviceId
    }

    @JvmStatic
    fun addCheckNum(source: String): String {

        val group2 = RateUtil.group2(source)
        val xor = group2[0].toInt(16) xor (group2[1].toInt(16)) xor (group2[2].toInt(16)) xor (group2[3]
                .toInt(16)) xor (group2[4].toInt(16)) xor (group2[5].toInt(16)) xor (group2[6]
                .toInt(16)) xor (group2[7].toInt(16))
        return source+xor.toString(16)
    }

}
