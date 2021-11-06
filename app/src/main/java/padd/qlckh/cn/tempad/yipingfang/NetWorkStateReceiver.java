package padd.qlckh.cn.tempad.yipingfang;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Andy
 * @date 2021/11/6 18:38
 * Desc:
 */
public class NetWorkStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        EventBus.getDefault().postSticky(new NetWorkStateEvent());
    }
}
