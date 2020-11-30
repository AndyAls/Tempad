package padd.qlckh.cn.tempad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import padd.qlckh.cn.tempad.manager.SerialPortManager;
import padd.qlckh.cn.tempad.serial.App;
import padd.qlckh.cn.tempad.view.IToast;

import static android.os.Build.getSerial;

/**
 * @author Andy
 * @date 2018/5/14 11:24
 * Desc:  基类Activity
 */
public abstract class BaseActivity extends AppCompatActivity implements IBaseView {
    private static final String TAG = "BaseActivity";
    //<editor-fold desc="控件初始化">
    protected FrameLayout flContainer;
    protected BaseActivity mActivity;
    protected float textsize;
    public static final String TEXT_SIZE = "TEXT_SIZE";
    private Unbinder bind;

    protected App mApplication;
    protected SerialPortManager mWeightManager;
    protected SerialPortManager mScanManager;
    protected SerialPortManager mPrintManager;
    protected SerialPortManager mPanelManager;

    //</editor-fold>

    //<editor-fold desc="基类初始化">
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getContentView());
        // 全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        bind = ButterKnife.bind(this);
        mApplication = (App) getApplication();
        getSerialPort();
        mActivity = this;
        initView();
        initDate();

        XLog.e(TAG,"onCreate");

    }

    private void getSerialPort() {
        mPanelManager=mApplication.getmPanelManager(this);
        mPrintManager=mApplication.getmPrintManager();
        mScanManager=mApplication.getmScanManager();
        mWeightManager=mApplication.getmWeightManager();
    }
    //</editor-fold>

    //<editor-fold desc="视图初始化">
    protected abstract int getContentView();
    //</editor-fold>

    //<editor-fold desc="释放资源">
    @Override
    protected void onDestroy() {
        release();
        bind.unbind();
        super.onDestroy();
    }
//</editor-fold>

    //<editor-fold desc="软键盘控制">
    /**
     * 分配触摸事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 判断动作，如点击，按下等
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 得到获取焦点的view
            View v = getCurrentFocus();
            // 点击的位子
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        // 判断view是否为空，
        if (v != null && (v instanceof EditText)) {
            // view是否为EditText
            int[] l = {0, 0};
            // 判断view是否为空，
            v.getLocationInWindow(l);
            // 计算坐标
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            // 比较坐标
            // 点击EditText的事件，忽略它。
            return !(event.getX() > left) || !(event.getX() < right)
                    || !(event.getY() > top) || !(event.getY() < bottom);
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }


    /**
     * 多种隐藏软件盘方法的其中一种
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert im != null;
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    //</editor-fold>

    //<editor-fold desc="基类抽取">
    protected void showShort(String msg) {
        IToast.Config build = new IToast.Builder().setTextSize(28).build();
        IToast.showShort(msg,build);
    }

    protected void showLong(String msg) {

        IToast.Config build = new IToast.Builder().setTextSize(28).build();
        IToast.showLong(msg,build);
    }

    protected boolean isEmpty(Object msg){
        if (msg instanceof String){
            return TextUtils.isEmpty((String)msg)||"".equals(msg)||"null".equals(msg);
        }else {
            return msg==null;
        }

    }

    protected void loading(){
        LoadingView.showLoading(mActivity,"请稍等...",false);
    }

    protected void cancelLoading(){
        LoadingView.cancelLoading();
    }

    /**
     * 显示提示框
     *
     * @param message message
     */
    protected void showDialog( String message) {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage(message)
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }
}
