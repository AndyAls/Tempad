package padd.qlckh.cn.tempad.yipingfang;

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
            case YiMainActivity.JINSHU:
                return transfromKg("1BE7", weight.substring(22, 26));
            case YiMainActivity.SULIAO:
                return transfromKg("1BE7", weight.substring(28, 32));
            case YiMainActivity.BOLI:
                return transfromKg("1BE7", weight.substring(16, 20));
            case YiMainActivity.DIANCHI:
                return transfromKg("1BE7", weight.substring(10, 14));
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


}
