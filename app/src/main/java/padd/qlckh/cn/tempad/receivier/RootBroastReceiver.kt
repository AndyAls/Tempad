package padd.qlckh.cn.tempad.receivier

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import padd.qlckh.cn.tempad.QidianActivity

/**
 * @author Andy
 * @date   2019/6/3 14:00
 * Desc:
 */
class RootBroastReceiver :BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {


        if (Intent.ACTION_BOOT_COMPLETED==intent!!.action){

            val startIntent = Intent(context, QidianActivity::class.java).apply {
                action = "android.intent.action.MAIN"
                addCategory(Intent.CATEGORY_LAUNCHER)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context!!.startActivity(startIntent)
        }

    }
}