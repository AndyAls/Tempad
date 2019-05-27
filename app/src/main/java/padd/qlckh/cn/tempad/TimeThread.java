package padd.qlckh.cn.tempad;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Andy
 * @date 2018/9/16 23:47
 * Desc:
 */
public class TimeThread extends Thread {

    public TextView tvDate;
    private int msgKey1 = 22;

    public TimeThread(TextView tvDate) {
        this.tvDate = tvDate;
    }

    @Override
    public void run() {
        do {
            try {
                Thread.sleep(60*1000);
                Message msg = new Message();
                msg.what = msgKey1;
                mHandler.sendMessage(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (true);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 22:
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                    String date = sdf.format(System.currentTimeMillis());
                    tvDate.setText(date);
                    break;

                default:
                    break;
            }
        }
    };
}

