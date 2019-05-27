package padd.qlckh.cn.tempad.http.observer;

import android.app.Dialog;


import io.reactivex.disposables.Disposable;
import padd.qlckh.cn.tempad.http.RxHttpUtils;
import padd.qlckh.cn.tempad.http.base.BaseStringObserver;
import padd.qlckh.cn.tempad.http.utils.ToastUtils;


/**
 * @author Andy
 * @date   2018/5/15 18:48
 * @link   {http://blog.csdn.net/andy_l1}
 * Desc:    StringObserver.java
 */

public abstract class StringObserver extends BaseStringObserver {

    private Dialog mProgressDialog;

    public StringObserver() {
    }

    public StringObserver(Dialog progressDialog) {
        mProgressDialog = progressDialog;
    }

    /**
     * 失败回调
     *
     * @param errorMsg 错误信息
     */
    protected abstract void onError(String errorMsg);

    /**
     * 成功回调
     *
     * @param data 结果
     */
    protected abstract void onSuccess(String data);


    @Override
    public void doOnSubscribe(Disposable d) {
        RxHttpUtils.addDisposable(d);
    }

    @Override
    public void doOnError(String errorMsg) {
        dismissLoading();
        if (!isHideToast()) {
            ToastUtils.showToast(errorMsg);
        }
        onError(errorMsg);
    }

    @Override
    public void doOnNext(String string) {
        onSuccess(string);
    }


    @Override
    public void doOnCompleted() {
        dismissLoading();
    }

    /**
     * 隐藏loading对话框
     */
    private void dismissLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
