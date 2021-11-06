package padd.qlckh.cn.tempad.yipingfang;

import java.util.List;
/**
 * @author Andy
 * @date   2021/11/5 17:03
 * @link   {http://blog.csdn.net/andy_l1}
 * Desc:    YiMarkDetailBean.java
 */
public class YiMarkDetailBean {
    private String count;
    private String msg;
    private List<RowBean> row;
    private int status;

    public static class RowBean {
        private String addtime;
        private String content;
        private String id;
        private String isread;
        private String num;
        private Object readtime;
        private String residid;
        private String status;
        private String title;
        private String userid;

        public String getId() {
            return this.id;
        }

        public void setId(String str) {
            this.id = str;
        }

        public String getUserid() {
            return this.userid;
        }

        public void setUserid(String str) {
            this.userid = str;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String str) {
            this.title = str;
        }

        public String getContent() {
            return this.content;
        }

        public void setContent(String str) {
            this.content = str;
        }

        public String getNum() {
            return this.num;
        }

        public void setNum(String str) {
            this.num = str;
        }

        public String getAddtime() {
            return this.addtime;
        }

        public void setAddtime(String str) {
            this.addtime = str;
        }

        public Object getReadtime() {
            return this.readtime;
        }

        public void setReadtime(Object obj) {
            this.readtime = obj;
        }

        public String getIsread() {
            return this.isread;
        }

        public void setIsread(String str) {
            this.isread = str;
        }

        public String getResidid() {
            return this.residid;
        }

        public void setResidid(String str) {
            this.residid = str;
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

    public String getCount() {
        return this.count;
    }

    public void setCount(String str) {
        this.count = str;
    }

    public List<RowBean> getRow() {
        return this.row;
    }

    public void setRow(List<RowBean> list) {
        this.row = list;
    }
}