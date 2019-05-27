package padd.qlckh.cn.tempad;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import padd.qlckh.cn.tempad.serial.MainMenu;

/**
 * @author Andy
 * @date 2018/9/13 12:52
 * Desc:
 */
public class WelcomeActivity extends BaseActivity {
    @BindView(R.id.iv)
    ImageView iv;

    @Override
    protected int getContentView() {
        return R.layout.activity_welcom;
    }

    @Override
    public void initView() {

        // 以view中心为缩放点，由初始状态放大两倍
        ScaleAnimation animation = new ScaleAnimation(
                1.0f, 1.5f, 1.0f, 1.5f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        );
        animation.setRepeatCount(0);
        animation.setDuration(2000);
        animation.setFillAfter(true);
        iv.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                overridePendingTransition(0, 0);
                startActivity(new Intent(WelcomeActivity.this, ScreenActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

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
}
