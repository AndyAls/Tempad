package padd.qlckh.cn.tempad.yipingfang;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author Andy
 * @date 2024/8/22 13:53
 * Desc:
 */
class WeightBean {

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
        @JSONField(name = "bin_info")
        private BinInfo binInfo;

        public BinInfo getBinInfo() {
            return binInfo;
        }

        public void setBinInfo(BinInfo binInfo) {
            this.binInfo = binInfo;
        }

        public static class BinInfo {
            @JSONField(name = "no")
            private Integer no;
            @JSONField(name = "phone")
            private String phone;
            @JSONField(name = "weight")
            private double weight;
            @JSONField(name = "unit")
            private String unit;

            public Integer getNo() {
                return no;
            }

            public void setNo(Integer no) {
                this.no = no;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public double getWeight() {
                return weight;
            }

            public void setWeight(double weight) {
                this.weight = weight;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }
        }
    }
}
