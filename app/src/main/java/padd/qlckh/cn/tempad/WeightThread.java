package padd.qlckh.cn.tempad;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * @author Andy
 * @date 2018/9/18 0:55
 * Desc:
 */
public class WeightThread extends Thread{

    private TextView tvDate;
    private TextView tvWeight;
    private int weight=0;
    private int msgKey1 = 22;

    public WeightThread(TextView tvDate, TextView tvweight, int wt) {
        this.tvDate = tvDate;
        this.tvWeight=tvweight;
        this.weight=wt;
    }

    @Override
    public void run() {
        do {
            try {
                Thread.sleep(10*1000);
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
                    Random rand = new Random();
                    int i = rand.nextInt(50) + 50;
                    tvDate.setText(MessageFormat.format("{0}g", String.valueOf(i)));
                    tvWeight.setText(MessageFormat.format("{0}g", String.valueOf(i+weight)));
                    break;

                default:
                    break;
            }
        }
    };
}
