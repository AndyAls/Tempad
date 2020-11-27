package padd.qlckh.cn.tempad.yipingfang;

/**
 * @author Andy
 * @date 2020/11/26 14:47
 * Desc:
 */
public interface YiConstant {


    /**
     * 操作桶的接收数据
     */
    String RECIVE_TUIGAN = "55000005 - 01 0x -01 0x -01 0x - 01 0x - 05 - 61 - 03";
    /**
     * 四个桶推杆伸出指令
     */
    String OPEN_DIANCHI = "5505010000005103";
    String OPEN_BOLI = "5505000100005103";
    String OPEN_JINSHU = "5505000001005103";
    String OPEN_SULIAO = "5505000000015103";

    /**
     * 四个桶推杆关闭指令
     */
    String CLOSE_DIANCHI = "5505020000005203";
    String CLOSE_BOLI = "5505000200005203";
    String CLOSE_JINSHU = "5505000002005203";
    String CLOSE_SULIAO = "5505000000025203";


    /**
     * 操作称的接收数据
     */
    String RECIVE_CHENG = "55000004 - 00 01 01 - 01 01 01  - 02 01 01 - 03 01 01 - 69 - 03";
    /**
     * 四个称去皮
     */
    String PEEL_DIANCHI = "5504000000207103";
    String PEEL_BOLI = "5504000001207003";
    String PEEL_JINSHU = "5504000002207303";
    String PEEL_SULIAO = "5504000003207203";


    /**
     * 四个称标定零点
     */
    String ZERO_DIANCHI = "5504000000306103";
    String ZERO_BOLI = "5504000001306003";
    String ZERO_JINSHU = "5504000002306303";
    String ZERO_SULIAO = "5504000003306203";


    /**
     * 四个称20kg标定2000--> 01代表10g
     */
    String CHECK_DIANCHI = "5504000000326303";
    String CHECK_BOLI = "5504000001326203";
    String CHECK_JINSHU = "5504000002326103";
    String CHECK_SULIAO = "5504000003326003";

}
