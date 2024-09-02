package padd.qlckh.cn.tempad.yipingfang;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author Andy
 * @date 2024/9/2 12:49
 * Desc:
 */
public class DeviceResp {

    @JSONField(name = "o")
    private Integer o;
    @JSONField(name = "d")
    private D d;

    public Integer getO() {
        return o;
    }

    public void setO(Integer o) {
        this.o = o;
    }

    public D getD() {
        return d;
    }

    public void setD(D d) {
        this.d = d;
    }

    public static class D {
        @JSONField(name = "dev_code")
        private String devCode;
        @JSONField(name = "v")
        private String v;

        public String getDevCode() {
            return devCode;
        }

        public void setDevCode(String devCode) {
            this.devCode = devCode;
        }

        public String getV() {
            return v;
        }

        public void setV(String v) {
            this.v = v;
        }
    }
}
