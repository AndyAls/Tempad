package padd.qlckh.cn.tempad.yipingfang;

import android.app.Activity;
import android.view.View;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author Andy
 * @date 2021/11/6 13:04
 * Desc:
 */
public class YiUtils {
    public static final String WEIGHT_KG = "55000004011BE7021BE7031BE7041BE76903";

    public static String getWeight(String weight, String tag) {
        switch (tag) {
            //纸张
            case YiMainActivity.ZHIZHANG:
                return transfromKg("2710", weight.substring(10, 14));
            //玻璃
            case YiMainActivity.BOLI:
                return transfromKg("07D0", weight.substring(16, 20));
            //金属
            case YiMainActivity.JINSHU:
                return transfromKg("07D0", weight.substring(22, 26));

            case YiMainActivity.SULIAO:
                //塑料
                return transfromKg("07D0", weight.substring(28, 32));
            default:
                return "0";
        }
    }

    private static String transfromKg(String str, String str2) {
        long parseLong = Long.parseLong(str, 16);
        double parseLong2 = (double) (Long.parseLong(str2, 16) * 20);
        double d = (double) parseLong;
        Double.isNaN(parseLong2);
        Double.isNaN(d);
        return new DecimalFormat("0.00").format(new BigDecimal(parseLong2 / d).setScale(2, 4).doubleValue());
    }
    /**
     * 隐藏或显示 导航栏
     *
     * @param activity
     */
    public static void setNavigationBarVisible(Activity activity, boolean isHide) {
        if (isHide) {
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        } else {
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

}
