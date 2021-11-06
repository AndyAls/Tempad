package padd.qlckh.cn.tempad.yipingfang;

/**
 * @author Andy
 * @date   2021/11/4 16:38
 * @link   {http://blog.csdn.net/andy_l1}
 * Desc:    AddCodeResq.java
 */
public class AddCodeResq {
    private String msg;
    private RowBean row;
    private int status;

    public static class RowBean {
        private String address;
        private String addtime;
        private String code;
        private String id;
        private String items;
        private String lat;
        private String lng;
        private String spin;
        private String status;

        public String getId() {
            return this.id;
        }

        public void setId(String str) {
            this.id = str;
        }

        public String getItems() {
            return this.items;
        }

        public void setItems(String str) {
            this.items = str;
        }

        public String getAddress() {
            return this.address;
        }

        public void setAddress(String str) {
            this.address = str;
        }

        public String getCode() {
            return this.code;
        }

        public void setCode(String str) {
            this.code = str;
        }

        public String getAddtime() {
            return this.addtime;
        }

        public void setAddtime(String str) {
            this.addtime = str;
        }

        public String getSpin() {
            return this.spin;
        }

        public void setSpin(String str) {
            this.spin = str;
        }

        public String getLng() {
            return this.lng;
        }

        public void setLng(String str) {
            this.lng = str;
        }

        public String getLat() {
            return this.lat;
        }

        public void setLat(String str) {
            this.lat = str;
        }

        public String getStatus() {
            return this.status;
        }

        public void setStatus(String str) {
            this.status = str;
        }
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String str) {
        this.msg = str;
    }

    public RowBean getRow() {
        return this.row;
    }

    public void setRow(RowBean rowBean) {
        this.row = rowBean;
    }
}