package padd.qlckh.cn.tempad;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import padd.qlckh.cn.tempad.http.RxHttpUtils;
import padd.qlckh.cn.tempad.http.interceptor.Transformer;
import padd.qlckh.cn.tempad.http.observer.CommonObserver;

/**
 * @author Andy
 * @date 2019/6/24 11:17
 * Desc:
 */
public class InfoErrorActivity extends BaseActivity {
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.btSumbit)
    Button btSumbit;
    private static final Long mills = 80 * 1000L;
    @BindView(R.id.tvTime)
    TextView tvTime;
    private CountDownTimer countDownTimer;

    @Override
    protected int getContentView() {
        return R.layout.activity_info_error;
    }

    @Override
    public void initView() {

        countDownTimer = new CountDownTimer(mills, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                tvTime.setText(String.format("请在%d秒内完成", millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
               startActivity(new Intent(InfoErrorActivity.this,QidianActivity.class));
                overridePendingTransition(0,0);
            }
        };
        countDownTimer.start();

    }

    @Override
    public void initDate() {
        etCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    sumbit();
                }
                return false;
            }
        });
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void release() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer=null;
        }
    }


    @OnClick(R.id.btSumbit)
    public void onViewClicked() {
       sumbit();
    }
    private void sumbit() {
        if (isEmpty(etCode.getText().toString().trim())) {
            showShort("请输入芯片内码");
        } else {
            loading();
            RxHttpUtils.createApi(ApiService.class)
                    .bindCode(etCode.getText().toString().trim(), getIntent().getStringExtra("item"))
//                .scanResult("4017515658", ids, "867012039777450")
                    .compose(Transformer.<Object>switchSchedulers())
                    .subscribe(new CommonObserver<Object>() {
                        @Override
                        protected void onError(String errorMsg) {
                            cancelLoading();
                            showShort("激活失败,请联系客服解决");
                            startActivity(new Intent(InfoErrorActivity.this,QidianActivity.class));
                            overridePendingTransition(0,0);
                        }

                        @Override
                        protected void onSuccess(Object responeDao) {
                            cancelLoading();
                            showShort("激活成功,请再次扫描领取垃圾袋");
                            startActivity(new Intent(InfoErrorActivity.this,QidianActivity.class));
                            overridePendingTransition(0,0);
                        }
                    });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return true;
    }
}
