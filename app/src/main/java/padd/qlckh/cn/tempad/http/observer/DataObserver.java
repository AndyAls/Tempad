package padd.qlckh.cn.tempad.http.observer;

import android.app.Dialog;
import io.reactivex.disposables.Disposable;
import padd.qlckh.cn.tempad.http.RxHttpUtils;
import padd.qlckh.cn.tempad.http.base.BaseDataObserver;
import padd.qlckh.cn.tempad.http.bean.BaseData;
import padd.qlckh.cn.tempad.http.utils.ToastUtils;

/**
 * @author Andy
 * @date   2018/5/15 18:47
 * @link   {http://blog.csdn.net/andy_l1}
 * Desc:    DataObserver.java
 */

public abstract class DataObserver<T> extends BaseDataObserver<T> {

    private Dialog mProgressDialog;

    public DataObserver() {
    }

    public DataObserver(Dialog progressDialog) {
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
    protected abstract void onSuccess(T data);

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
    public void doOnNext(BaseData<T> data) {
        onSuccess(data.getData());
        //可以根据需求对code统一处理
//        switch (data.getCode()) {
//            case 200:
//                onSuccess(data.getData());
//                break;
//            case 300:
//            case 500:
//                onError(data.getMsg());
//                break;
//            default:
//        }
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
