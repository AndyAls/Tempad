package padd.qlckh.cn.tempad.yipingfang

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.uniwin.UniwinAPI
import padd.qlckh.cn.tempad.http.utils.SPUtils
import padd.qlckh.cn.tempad.receivier.PowerOnReceiver
import java.util.Calendar


/**
 * @author Andy
 * @date   2024/9/29 19:40
 * Desc:
 */
interface DeviceManager {

    companion object {

        const val ON_TIME = "ON_TIME";
        const val OFF_TIME = "OFF_TIME";

        /**
         * 获取开机时间
         */
        fun getBootUpTime(): String {
            return try {
                SPUtils.get(ON_TIME, "")
            } catch (e: Exception) {
                ""
            }
        }

        /**
         * 获取关机时间 格式16:00:00
         */
        fun getShutUpTime(): String {
            return try {
                SPUtils.get(OFF_TIME, "")
            } catch (e: Exception) {
                ""
            }

        }

        /**
         * 获取开机时间
         */
        fun setBootUpTime(time: String) {
            SPUtils.put(ON_TIME, time);
        }

        /**
         * 获取关机时间 格式16:00:00
         */
        fun setShutUpTime(time: String) {
            SPUtils.put(ON_TIME, time);

        }

        /**
         * 获取开机时间的毫秒值
         */
        fun getBootUpTimeMillisecond(): Long {
            val bootUpTime = getBootUpTime()
            val shutUpTime = getShutUpTime()
            val currentCalendar = Calendar.getInstance()
            var offTimeLong = 0L
            val offCalendar = Calendar.getInstance()
            if (shutUpTime.isNotEmpty() && shutUpTime.contains(":") && shutUpTime.split(":").size > 1) {
                val off = shutUpTime.split(":")
                val offHour = off[0]
                val offMinute = off[1]
                offCalendar.set(Calendar.HOUR_OF_DAY, offHour.toInt())
                offCalendar.set(Calendar.MINUTE, offMinute.toInt())
                offCalendar.set(Calendar.SECOND, 0)
                offTimeLong = offCalendar.timeInMillis
            }
            if (bootUpTime.isNotEmpty() && bootUpTime.contains(":") && bootUpTime.split(":").size > 1) {
                val on = bootUpTime.split(":")
                val onHour = on[0]
                val onMinute = on[1]
                val onCalendar = Calendar.getInstance()
                onCalendar.set(Calendar.HOUR_OF_DAY, onHour.toInt())
                onCalendar.set(Calendar.MINUTE, onMinute.toInt())
                onCalendar.set(Calendar.SECOND, 0)

                if (offTimeLong < onCalendar.timeInMillis) {
                    //开机时间大于关机时间,如果是同一天而且当前时间没过
                    if (offCalendar.get(Calendar.DAY_OF_MONTH) == onCalendar.get(Calendar.DAY_OF_MONTH)) {
                        if (currentCalendar.timeInMillis > onCalendar.timeInMillis) {
                            onCalendar.add(Calendar.DAY_OF_MONTH, 1)
                        }
                    } else {
                        onCalendar.add(Calendar.DAY_OF_MONTH, 1)
                    }

                } else {
                    //如果关机时间大于开机时间 开机时间向后推一天
                    onCalendar.add(Calendar.DAY_OF_MONTH, 1)
                }
                return onCalendar.timeInMillis
            }
            return 0L
        }

        /**
         * 获取关机时间的毫秒值
         */
        fun getShutDownMillisecond(): Long {
            val shutUpTime = getShutUpTime()
            if (shutUpTime.isNotEmpty() && shutUpTime.contains(":") && shutUpTime.split(":").size > 1) {
                val currentCalendar = Calendar.getInstance()
                val off = shutUpTime.split(":")
                val offHour = off[0]
                val offMinute = off[1]
                val offCalendar = Calendar.getInstance()
                offCalendar.set(Calendar.HOUR_OF_DAY, offHour.toInt())
                offCalendar.set(Calendar.MINUTE, offMinute.toInt())
                offCalendar.set(Calendar.SECOND, 0)
                //现在时间大于关机时间,说明关机时间已过,设置一天后的日期
                if (currentCalendar.timeInMillis > offCalendar.timeInMillis) {
                    offCalendar.add(Calendar.DAY_OF_MONTH, 1)
                }
                return offCalendar.timeInMillis
            }
            return 0L
        }

        fun powerOn(context: Context){
            val bootUpTimeMillisecond = getBootUpTimeMillisecond()
            if (bootUpTimeMillisecond>0){
                val intent: Intent = Intent(context, PowerOnReceiver::class.java) //定义一个广播接收
                val poweronSender =
                    PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT)
                UniwinAPI.enableAlertPowerOn(context, bootUpTimeMillisecond, poweronSender)
            }else{
                disPowerOn(context);
            }

        }

        fun  disPowerOn(context: Context){
            setBootUpTime("")
            val intent: Intent = Intent(context, PowerOnReceiver::class.java) //定义一个广播接收
            val poweronSender =
                PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_NO_CREATE)
            UniwinAPI.disableAlertPowerOn(context, poweronSender)
        }
    }


}