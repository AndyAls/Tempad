package padd.qlckh.cn.tempad;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author Andy
 * @date 2018/10/8 15:34
 * Desc: 四通道
 */
public class RateUtil {

    //"000816AB0007FFB80007FCE50007FFB9"
//    010410000816AB0007FFB80007FCE50007FFB9BE6F
    //空桶
    /**
     * 数据位
     * <p>
     * 3 代表有毒有害 2 纸张 1 塑料 4 金属
     */
    private static final String EMPTY_WEIGHT1 = "0410000898CC0008D830000893070008868E6D38";
    private static final String EMPTY_WEIGHT2 = "0410000898CC0008D833000892FF0008869319C4";
    private static final String EMPTY_WEIGHT3 = "0410000898C70008D83400089305000889633CB9";
    private static final String EMPTY_WEIGHT4 = "0410000898D00008D831000892FF00088699AFFF";
    private static final String EMPTY_WEIGHT5 = "0410000898CC0008D82D000893020008869D75A5";
    //1000g
    private static final String KG_WEIGHT1 = "041000089E7D0008DE060008984A00088C34E256";
    private static final String KG_WEIGHT2 = "041000089E790008DE050008984E00088C4009C5";
    private static final String KG_WEIGHT3 = "041000089E7C0008DE090008984800088C3B98E3";
    private static final String KG_WEIGHT4 = "041000089E7B0008DE060008984D00088C419FB7";
    private static final String KG_WEIGHT5 = "041000089E750008DE0A0008985200088C3FC9DB";



    private static final String WEIGHT= "0410000898C70008D83600089852000886A250B9";

    /**
     * @param hexStr 秤接收的hex值
     * @return 4个桶的重量
     */
    public static String[] getWeight(String hexStr) {
        String hexDatas = hexStr.substring(4, hexStr.length() - 4);
        String[] hexs = group(hexDatas);
        double[] eAvg = getEAvg();
        double[] avg = getAvg();
        String[] rates=new String[4];
        for (int i = 0; i < hexs.length; i++) {
            double l = (Long.parseLong(hexs[i], 16) - eAvg[i])/ avg[i];
            BigDecimal bigDecimal = new BigDecimal(l);
            double v = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            rates[i] = new DecimalFormat("0.0").format(v);
        }
        return rates;
    }

    private static double[] getEAvg(){
        String e1 = EMPTY_WEIGHT1.substring(4, EMPTY_WEIGHT1.length() - 4);
        String e2 = EMPTY_WEIGHT2.substring(4, EMPTY_WEIGHT2.length() - 4);
        String e3 = EMPTY_WEIGHT3.substring(4, EMPTY_WEIGHT3.length() - 4);
        String e4 = EMPTY_WEIGHT4.substring(4, EMPTY_WEIGHT4.length() - 4);
        String e5 = EMPTY_WEIGHT5.substring(4, EMPTY_WEIGHT5.length() - 4);
        String[] es1 = group(e1);
        String[] es2 = group(e2);
        String[] es3 = group(e3);
        String[] es4 = group(e4);
        String[] es5 = group(e5);
        long add1 = Long.parseLong(es1[0], 16) + Long.parseLong(es2[0], 16) + Long.parseLong(es3[0], 16) + Long.parseLong(es4[0], 16) + Long.parseLong(es5[0], 16);
        long add2 = Long.parseLong(es1[1], 16) + Long.parseLong(es2[1], 16) + Long.parseLong(es3[1], 16) + Long.parseLong(es4[1], 16) + Long.parseLong(es5[1], 16);
        long add3 = Long.parseLong(es1[2], 16) + Long.parseLong(es2[2], 16) + Long.parseLong(es3[2], 16) + Long.parseLong(es4[2], 16) + Long.parseLong(es5[2], 16);
        long add4 = Long.parseLong(es1[3], 16) + Long.parseLong(es2[3], 16) + Long.parseLong(es3[3], 16) + Long.parseLong(es4[3], 16) + Long.parseLong(es5[3], 16);
        double evg1 = add1 / 5;
        double evg2 = add2 / 5;
        double evg3 = add3 / 5;
        double evg4 = add4 / 5;
        double[] doubles=new double[4];
        doubles[0]=evg1;
        doubles[1]=evg2;
        doubles[2]=evg3;
        doubles[3]=evg4;
        return doubles;
    }
    private static double[] getAvg(){
        String e1 = EMPTY_WEIGHT1.substring(4, EMPTY_WEIGHT1.length() - 4);
        String e2 = EMPTY_WEIGHT2.substring(4, EMPTY_WEIGHT2.length() - 4);
        String e3 = EMPTY_WEIGHT3.substring(4, EMPTY_WEIGHT3.length() - 4);
        String e4 = EMPTY_WEIGHT4.substring(4, EMPTY_WEIGHT4.length() - 4);
        String e5 = EMPTY_WEIGHT5.substring(4, EMPTY_WEIGHT5.length() - 4);
        String[] es1 = group(e1);
        String[] es2 = group(e2);
        String[] es3 = group(e3);
        String[] es4 = group(e4);
        String[] es5 = group(e5);
        long add1 = Long.parseLong(es1[0], 16) + Long.parseLong(es2[0], 16) + Long.parseLong(es3[0], 16) + Long.parseLong(es4[0], 16) + Long.parseLong(es5[0], 16);
        long add2 = Long.parseLong(es1[1], 16) + Long.parseLong(es2[1], 16) + Long.parseLong(es3[1], 16) + Long.parseLong(es4[1], 16) + Long.parseLong(es5[1], 16);
        long add3 = Long.parseLong(es1[2], 16) + Long.parseLong(es2[2], 16) + Long.parseLong(es3[2], 16) + Long.parseLong(es4[2], 16) + Long.parseLong(es5[2], 16);
        long add4 = Long.parseLong(es1[3], 16) + Long.parseLong(es2[3], 16) + Long.parseLong(es3[3], 16) + Long.parseLong(es4[3], 16) + Long.parseLong(es5[3], 16);
        double evg1 = add1 / 5;
        double evg2 = add2 / 5;
        double evg3 = add3 / 5;
        double evg4 = add4 / 5;

        //================================================================================================================================//
        String k1 = KG_WEIGHT1.substring(4, KG_WEIGHT1.length() - 4);
        String k2 = KG_WEIGHT2.substring(4, KG_WEIGHT2.length() - 4);
        String k3 = KG_WEIGHT3.substring(4, KG_WEIGHT3.length() - 4);
        String k4 = KG_WEIGHT4.substring(4, KG_WEIGHT4.length() - 4);
        String k5 = KG_WEIGHT5.substring(4, KG_WEIGHT5.length() - 4);
        String[] ks1 = group(k1);
        String[] ks2 = group(k2);
        String[] ks3 = group(k3);
        String[] ks4 = group(k4);
        String[] ks5 = group(k5);
        long kdd1 = Long.parseLong(ks1[0], 16) + Long.parseLong(ks2[0], 16) + Long.parseLong(ks3[0], 16) + Long.parseLong(ks4[0], 16) + Long.parseLong(ks5[0], 16);
        long kdd2 = Long.parseLong(ks1[1], 16) + Long.parseLong(ks2[1], 16) + Long.parseLong(ks3[1], 16) + Long.parseLong(ks4[1], 16) + Long.parseLong(ks5[1], 16);
        long kdd3 = Long.parseLong(ks1[2], 16) + Long.parseLong(ks2[2], 16) + Long.parseLong(ks3[2], 16) + Long.parseLong(ks4[2], 16) + Long.parseLong(ks5[2], 16);
        long kdd4 = Long.parseLong(ks1[3], 16) + Long.parseLong(ks2[3], 16) + Long.parseLong(ks3[3], 16) + Long.parseLong(ks4[3], 16) + Long.parseLong(ks5[3], 16);
        double kvg1 = kdd1 / 5;
        double kvg2 = kdd2 / 5;
        double kvg3 = kdd3 / 5;
        double kvg4 = kdd4 / 5;
        double[] avg=new double[4];
        avg[0]=kvg1-evg1;
        avg[1]=kvg2-evg2;
        avg[2]=kvg3-evg3;
        avg[3]=kvg4-evg4;
        return avg;
    }
    private static String[] group(String datas) {
        String[] arr = new String[datas.length() % 8 == 0 ? datas.length() / 8 : datas.length() / 8 + 1];
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            if (index + 8 > datas.length()) {
                arr[i] = datas.substring(index);
            } else {
                arr[i] = datas.substring(index, (index = index + 8));
            }
        }
        return arr;
    }

    /**
     * 选出一个称重和上次提交的总重量最大的
     */
    public static String getCurrentWeight(String[] hexStr, ResponeDao.ResponeBean responeBean, String order) {


        if (responeBean == null) {
            return "0.00";
        }
        double b1 = Double.parseDouble(hexStr[0]);
        double b2 = Double.parseDouble(hexStr[1]);
        double b3 = Double.parseDouble(hexStr[2]);
        double b4 = Double.parseDouble(hexStr[3]);

        double v1 = b1 - Double.parseDouble(responeBean.getBwight());
        double v2 = b2 - Double.parseDouble(responeBean.getZwight());
        double v3 = b3 - Double.parseDouble(responeBean.getYdwight());
        double v4 = b4 - Double.parseDouble(responeBean.getFwight());
        double max;
        switch (order) {
            case "01":
                max = v3;
                break;
            case "02":
                max = v2;
                break;
            case "03":
                max = v1;
                break;
            case "04":
                max = v4;
                break;
            default:
                max = 0.1;
                break;
        }


        BigDecimal bigDecimal = new BigDecimal(max);
        double vs = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return new DecimalFormat("0.0").format(vs);

    }
}
