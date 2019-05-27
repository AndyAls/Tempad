package padd.qlckh.cn.tempad.view;

/**
 * @author Andy
 * @date 2018/10/12 17:21
 * Desc:
 */
public class RateDao {

    /**
     * status : 1
     * msg : 查询成功
     * row : {"id":"1","yd":"2","fz":"2","bl":"2","zl":"2","code":"22"}
     */

    private int status;
    private String msg;
    private RateBean row;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public RateBean getRow() {
        return row;
    }

    public void setRow(RateBean row) {
        this.row = row;
    }

    public static class RateBean {
        /**
         * id : 1
         * yd : 2
         * fz : 2
         * bl : 2
         * zl : 2
         * code : 22
         */

        private String id;
        private String yd;
        private String fz;
        private String bl;
        private String zl;
        private String code;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getYd() {
            return yd;
        }

        public void setYd(String yd) {
            this.yd = yd;
        }

        public String getFz() {
            return fz;
        }

        public void setFz(String fz) {
            this.fz = fz;
        }

        public String getBl() {
            return bl;
        }

        public void setBl(String bl) {
            this.bl = bl;
        }

        public String getZl() {
            return zl;
        }

        public void setZl(String zl) {
            this.zl = zl;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
