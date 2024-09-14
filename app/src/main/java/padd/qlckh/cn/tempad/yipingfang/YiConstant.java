package padd.qlckh.cn.tempad.yipingfang;

/**
 * @author Andy
 * @date 2021/11/5 17:02
 * @link {http://blog.csdn.net/andy_l1}
 * Desc:    YiConstant.java
 */
public interface YiConstant {


    /**
     * 操作桶的接收数据
     */
    String RECIVE_TUIGAN = "55000005 - 01 0x -01 0x -01 0x - 01 0x - 05 - 61 - 03";

    String TIME_OUT = "{\"o\":2601,\"d\":{\"mia\":\"150\"}}";
    /**
     * 四个桶推杆伸出指令
     */
    String OPEN_DIANCHI = "{\"o\":8001,\"d\":{\"bin_no\":1,\"code\":1}}";
    String OPEN_BOLI = "{\"o\":8001,\"d\":{\"bin_no\":2,\"code\":1}}";
    String OPEN_JINSHU = "{\"o\":8001,\"d\":{\"bin_no\":3,\"code\":1}}";
    String OPEN_SULIAO = "{\"o\":8001,\"d\":{\"bin_no\":4,\"code\":1}}";
    String OPEN_ALL = "5505010101015003";

    String UN_Auth = "{\"o\":1803}";
    String HEART = "{\"o\":1101}";
    String QUERY = "：{\"o\":1103}";
    //
    String LOGIN = "{\"o\":1001, \"d\":{\"dev_code\" : \"4A23020105\" ,\"v\" : \"1.0.9\"}}";
    /**
     * 四个桶推杆缩回指令
     */
    String CLOSE_DIANCHI = "{\"o\":8001,\"d\":{\"bin_no\":1,\"code\":0}}";
    String CLOSE_BOLI = "{\"o\":8001,\"d\":{\"bin_no\":2,\"code\":0}}";
    String CLOSE_JINSHU = "{\"o\":8001,\"d\":{\"bin_no\":3,\"code\":0}}";
    String CLOSE_SULIAO = "{\"o\":8001,\"d\":{\"bin_no\":4,\"code\":0}}";
    String CLOSE_ALL = "5505020202025003";
/*
    String OPEN_DIANCHI = "5505010000005103";
    String OPEN_BOLI =    "5505000100005103";
    String OPEN_JINSHU =  "5505000001005103";
    String OPEN_SULIAO =  "5505000000015103";
    String OPEN_ALL =     "5505010101015003";

    *//**
     * 四个桶推杆缩回指令
     *//*
    String CLOSE_DIANCHI = "5505020000005203";
    String CLOSE_BOLI =    "5505000200005203";
    String CLOSE_JINSHU =  "5505000002005203";
    String CLOSE_SULIAO =  "5505000000025203";
    String CLOSE_ALL =     "5505020202025003";*/


    /**
     * 操作称的接收数据
     */
    String RECIVE_CHENG = "55000004 - 00 01 01 - 01 01 01  - 02 01 01 - 03 01 01 - 69 - 03";
    /**
     * 四个称去皮
     */
    String PEEL_DIANCHI = "{\"o\":1401}";
    String PEEL_BOLI = "{\"o\":1401}";
    String PEEL_JINSHU = "{\"o\":1401}";
    String PEEL_SULIAO = "{\"o\":1401}";


    /**
     * 四个称标定零点
     */
    String ZERO_DIANCHI = "{\"o\":1900}";
    String ZERO_BOLI = "{\"o\":1900}";
    String ZERO_JINSHU = "{\"o\":1900}";
    String ZERO_SULIAO = "{\"o\":1900}";


    /**
     * 四个称1kg标定1000
     */
    String CHECK_DIANCHI = "{\"o\":1901,\"d\":{\"fix\":\"2000\"}}";
    String CHECK_BOLI = "{\"o\":1902,\"d\":{\"fix\":\"2000\"}}";
    String CHECK_JINSHU = "{\"o\":1903,\"d\":{\"fix\":\"2000\"}}";
    String CHECK_SULIAO = "{\"o\":1904,\"d\":{\"fix\":\"2000\"}}";

}
