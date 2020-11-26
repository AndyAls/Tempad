package padd.qlckh.cn.tempad;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import padd.qlckh.cn.tempad.manager.SerialPortManager;
import padd.qlckh.cn.tempad.serial.App;
import padd.qlckh.cn.tempad.view.IToast;

/**
 * @author Andy
 * @date 2018/5/14 11:24
 * Desc:  基类Activity
 */
public abstract class BaseFragment extends Fragment{
    private static final String TAG = "BaseActivity";
    protected float textsize;
    protected BaseActivity mActivity;

    protected App mApplication;
    protected SerialPortManager mWeightManager;
    protected SerialPortManager mScanManager;
    protected SerialPortManager mPrintManager;
    protected SerialPortManager mPanelManager;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        mApplication = (App) getActivity().getApplication();
        getSerialPort();
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    /* //</editor-fold>

    //<editor-fold desc="基类初始化">
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getContentView());
        // 全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initView();
        initDate();

        XLog.e(TAG,"onCreate");

    }*/

    private void getSerialPort() {
        mPanelManager = mApplication.getmPanelManager();
        mPrintManager = mApplication.getmPrintManager();
        mScanManager = mApplication.getmScanManager();
        mWeightManager = mApplication.getmWeightManager();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        super.onDestroy();
    }


    protected void showShort(String msg) {
        IToast.Config build = new IToast.Builder().setTextSize(28).build();
        IToast.showShort(msg, build);
    }

    protected void showLong(String msg) {

        IToast.Config build = new IToast.Builder().setTextSize(28).build();
        IToast.showLong(msg, build);
    }

    protected boolean isEmpty(Object msg) {
        if (msg instanceof String) {
            return TextUtils.isEmpty((String) msg) || "".equals(msg) || "null".equals(msg);
        } else {
            return msg == null;
        }

    }

    protected void loading() {
        LoadingView.showLoading(getActivity(), "请稍等...", false);
    }

    protected void cancelLoading() {
        LoadingView.cancelLoading();
    }

    /**
     * 显示提示框
     *
     * @param message message
     */
    protected void showDialog(String message) {
        new AlertDialog.Builder(getActivity())
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
