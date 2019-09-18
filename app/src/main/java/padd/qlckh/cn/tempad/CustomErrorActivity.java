package padd.qlckh.cn.tempad;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.bugly.crashreport.BuglyLog;
import com.tencent.bugly.crashreport.CrashReport;

import butterknife.BindView;
import butterknife.ButterKnife;
import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.activity.DefaultErrorActivity;
import cat.ereza.customactivityoncrash.config.CaocConfig;

/**
 * @author Andy
 * @date 2019/6/27 16:08
 * Desc:
 */
public class CustomErrorActivity extends BaseActivity {
    @BindView(R.id.btRestart)
    Button btRestart;
    @BindView(R.id.btErrDetail)
    TextView btErrDetail;

    @Override
    protected int getContentView() {
        return R.layout.activity_custom_error;
    }

    @Override
    public void initView() {
        final CaocConfig config = CustomActivityOnCrash.getConfigFromIntent(getIntent());
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1f, 0.8f,
                1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        scaleAnimation.setDuration(1200);
        scaleAnimation.setRepeatCount(Integer.MAX_VALUE);
        btRestart.setAnimation(scaleAnimation);
        btRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String details = CustomActivityOnCrash.getAllErrorDetailsFromIntent(CustomErrorActivity.this, getIntent());
                Throwable throwable = new Throwable("收集的bug:\n"+details);
                CrashReport.postCatchedException(throwable);
                BuglyLog.e("收集的bug",details);
                CustomActivityOnCrash.restartApplication(CustomErrorActivity.this, config);
            }
        });


        if (config.isShowErrorDetails()) {
            btErrDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //We retrieve all the error data and show it

                    AlertDialog dialog = new AlertDialog.Builder(CustomErrorActivity.this)
                            .setTitle("详情日志")
                            .setMessage(CustomActivityOnCrash.getAllErrorDetailsFromIntent(CustomErrorActivity.this, getIntent()))
                            .setPositiveButton("取消", null)
                            .setNeutralButton("复制日志",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            copyErrorToClipboard();
                                        }
                                    })
                            .show();
                    TextView textView = dialog.findViewById(android.R.id.message);
                    if (textView != null) {
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.customactivityoncrash_error_activity_error_details_text_size));
                    }
                }
            });
        } else {
            btErrDetail.setVisibility(View.GONE);
        }

    }

    @Override
    public void initDate() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void release() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    private void copyErrorToClipboard() {
        String errorInformation = CustomActivityOnCrash.getAllErrorDetailsFromIntent(CustomErrorActivity.this, getIntent());

        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText(getString(R.string.customactivityoncrash_error_activity_error_details_clipboard_label), errorInformation);
            clipboard.setPrimaryClip(clip);
            showShort("复制成功");
        }
    }
}
