package padd.qlckh.cn.tempad.yipingfang;

import java.util.List;

/**
 * @author Andy
 * @date   2021/11/5 17:03
 * @link   {http://blog.csdn.net/andy_l1}
 * Desc:    YiPutBean.java
 */
public class YiPutBean {
    private String msg;
    private List<RowBean> row;
    private int status;

    public static class RowBean {
        private String addtime;
        private String classname;
        private String flag;
        private String id;
        private String img;
        private String img1;
        private String jifen;
        private String status;

        public String getId() {
            return this.id;
        }

        public void setId(String str) {
            this.id = str;
        }

        public String getClassname() {
            return this.classname;
        }

        public void setClassname(String str) {
            this.classname = str;
        }

        public String getImg1() {
            return this.img1;
        }

        public void setImg1(String str) {
            this.img1 = str;
        }

        public String getJifen() {
            return this.jifen;
        }

        public void setJifen(String str) {
            this.jifen = str;
        }

        public String getFlag() {
            return this.flag;
        }

        public void setFlag(String str) {
            this.flag = str;
        }

        public String getImg() {
            return this.img;
        }

        public void setImg(String str) {
            this.img = str;
        }

        public String getAddtime() {
            return this.addtime;
        }

        public void setAddtime(String str) {
            this.addtime = str;
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

    public List<RowBean> getRow() {
        return this.row;
    }

    public void setRow(List<RowBean> list) {
        this.row = list;
    }
}