package padd.qlckh.cn.tempad.http.observer;


import android.app.Dialog;

import io.reactivex.disposables.Disposable;
import padd.qlckh.cn.tempad.http.RxHttpUtils;
import padd.qlckh.cn.tempad.http.base.BaseObserver;
import padd.qlckh.cn.tempad.http.utils.ToastUtils;

/**
 * @author Andy
 * @date   2018/5/15 18:47
 * @link   {http://blog.csdn.net/andy_l1}
 * Desc:    CommonObserver.java
 */
public abstract class CommonObserver<T> extends BaseObserver<T> {


    private Dialog mProgressDialog;

    public CommonObserver() {
    }

    public CommonObserver(Dialog progressDialog) {
        mProgressDialog = progressDialog;
    }

    /**
     * 失败回调
     *
     * @param errorMsg
     */
    protected abstract void onError(String errorMsg);

    /**
     * 成功回调
     *
     * @param t
     */
    protected abstract void onSuccess(T t);



    @Override
    public void doOnSubscribe(Disposable d) {
        RxHttpUtils.addDisposable(d);
    }

    @Override
    public void doOnError(String errorMsg) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        if (!isHideToast()) {
            ToastUtils.showToast(errorMsg);
        }
        onError(errorMsg);
    }

    @Override
    public void doOnNext(T t) {
        onSuccess(t);
    }

    @Override
    public void doOnCompleted() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
