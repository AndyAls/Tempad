package padd.qlckh.cn.tempad.yipingfang;

import java.util.List;

/**
 * @author Andy
 * @date   2021/11/5 17:09
 * @link   {http://blog.csdn.net/andy_l1}
 * Desc:    YiUserInfo.java
 */
public class YiUserInfo {
    private String msg;
    private List<RowBean> row;
    private int status;

    public static class RowBean {
        private String addtime;
        private String adduser;
        private String card;
        private String cardid;
        private String com;
        private String company;
        private String dangid;
        private String fullname;
        private String huiid;
        private String id;
        private String items;
        private String jicode;
        private String jifen;
        private String phone;
        private String pwd;
        private String s;
        private String sex;
        private String topflag;
        private String weight;

        public String getAddtime() {
            return this.addtime;
        }

        public void setAddtime(String str) {
            this.addtime = str;
        }

        public String getAdduser() {
            return this.adduser;
        }

        public void setAdduser(String str) {
            this.adduser = str;
        }

        public String getCard() {
            return this.card;
        }

        public void setCard(String str) {
            this.card = str;
        }

        public String getCardid() {
            return this.cardid;
        }

        public void setCardid(String str) {
            this.cardid = str;
        }

        public String getCom() {
            return this.com;
        }

        public void setCom(String str) {
            this.com = str;
        }

        public String getCompany() {
            return this.company;
        }

        public void setCompany(String str) {
            this.company = str;
        }

        public String getDangid() {
            return this.dangid;
        }

        public void setDangid(String str) {
            this.dangid = str;
        }

        public String getFullname() {
            return this.fullname;
        }

        public void setFullname(String str) {
            this.fullname = str;
        }

        public String getHuiid() {
            return this.huiid;
        }

        public void setHuiid(String str) {
            this.huiid = str;
        }

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

        public String getJicode() {
            return this.jicode;
        }

        public void setJicode(String str) {
            this.jicode = str;
        }

        public String getJifen() {
            return this.jifen;
        }

        public void setJifen(String str) {
            this.jifen = str;
        }

        public String getPhone() {
            return this.phone;
        }

        public void setPhone(String str) {
            this.phone = str;
        }

        public String getPwd() {
            return this.pwd;
        }

        public void setPwd(String str) {
            this.pwd = str;
        }

        public String getS() {
            return this.s;
        }

        public void setS(String str) {
            this.s = str;
        }

        public String getSex() {
            return this.sex;
        }

        public void setSex(String str) {
            this.sex = str;
        }

        public String getTopflag() {
            return this.topflag;
        }

        public void setTopflag(String str) {
            this.topflag = str;
        }

        public String getWeight() {
            return this.weight;
        }

        public void setWeight(String str) {
            this.weight = str;
        }
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String str) {
        this.msg = str;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public List<RowBean> getRow() {
        return this.row;
    }

    public void setRow(List<RowBean> list) {
        this.row = list;
    }
}