package padd.qlckh.cn.tempad;


import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Andy
 * @date 2018/10/10 19:55
 * Desc:
 */
public class MyTimeTask {

    private Timer timer;
    private TimerTask task;
    private long time;

    public MyTimeTask(long time, TimerTask task) {
        this.task = task;
        this.time = time;
        if (timer == null){
            timer=new Timer();
        }
    }

    public void start(){
        timer.schedule(task, 1000, time);//每隔time时间段就执行一次
    }

    public void stop(){
        if (timer != null) {
            timer.cancel();
        }
    }
}
