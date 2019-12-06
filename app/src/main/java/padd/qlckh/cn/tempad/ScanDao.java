package padd.qlckh.cn.tempad;

/**
 * @author Andy
 * @date 2019/5/30 17:56
 * Desc:
 */
public class ScanDao {


    /**
     * status : 1
     * msg : 成功
     * data : {"id":17040,"name":"","company":"罗兰小镇绿城经营用房->8幢->中泰路->2-38号-","addtime":1559742474,"phone":"13858167608","cardid":"330184198107224924","img":"","fullname":"姚利英","topflag":0,"adduser":"","items":"16,39,40,41,47","sex":"","pwd":"e10adc3949ba59abbe56e057f20f883e","jifen":0,"huiid":30,"jicode":0,"dangid":0,"erimg":"./Uploads/QRcode/1559457034.jpg","com":"罗兰小镇","card":"4017515658","weight":"0","jjifen":null,"qjifen":null,"n_code":"ZJLY010219048250172","h_code":"0100","status":0}
     */

    private String status;
    private String msg;
    private DataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 17040
         * name :
         * company : 罗兰小镇绿城经营用房->8幢->中泰路->2-38号-
         * addtime : 1559742474
         * phone : 13858167608
         * cardid : 330184198107224924
         * img :
         * fullname : 姚利英
         * topflag : 0
         * adduser :
         * items : 16,39,40,41,47
         * sex :
         * pwd : e10adc3949ba59abbe56e057f20f883e
         * jifen : 0
         * huiid : 30
         * jicode : 0
         * dangid : 0
         * erimg : ./Uploads/QRcode/1559457034.jpg
         * com : 罗兰小镇
         * card : 4017515658
         * weight : 0
         * jjifen : null
         * qjifen : null
         * n_code : ZJLY010219048250172
         * h_code : 0100
         * status : 0
         */

        private String id;
        private String name;
        private String company;
        private String addtime;
        private String phone;
        private String cardid;
        private String img;
        private String fullname;
        private String topflag;
        private String adduser;
        private String items;
        private String sex;
        private String pwd;
        private String jifen;
        private String huiid;
        private String jicode;
        private String dangid;
        private String erimg;
        private String com;
        private String card;
        private String weight;
        private Object jjifen;
        private Object qjifen;
        private String n_code;
        private String h_code;
        private String status;
        private String h_code_id;

        public String getH_code_id() {
            return h_code_id;
        }

        public void setH_code_id(String h_code_id) {
            this.h_code_id = h_code_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCardid() {
            return cardid;
        }

        public void setCardid(String cardid) {
            this.cardid = cardid;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getTopflag() {
            return topflag;
        }

        public void setTopflag(String topflag) {
            this.topflag = topflag;
        }

        public String getAdduser() {
            return adduser;
        }

        public void setAdduser(String adduser) {
            this.adduser = adduser;
        }

        public String getItems() {
            return items;
        }

        public void setItems(String items) {
            this.items = items;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        public String getJifen() {
            return jifen;
        }

        public void setJifen(String jifen) {
            this.jifen = jifen;
        }

        public String getHuiid() {
            return huiid;
        }

        public void setHuiid(String huiid) {
            this.huiid = huiid;
        }

        public String getJicode() {
            return jicode;
        }

        public void setJicode(String jicode) {
            this.jicode = jicode;
        }

        public String getDangid() {
            return dangid;
        }

        public void setDangid(String dangid) {
            this.dangid = dangid;
        }

        public String getErimg() {
            return erimg;
        }

        public void setErimg(String erimg) {
            this.erimg = erimg;
        }

        public String getCom() {
            return com;
        }

        public void setCom(String com) {
            this.com = com;
        }

        public String getCard() {
            return card;
        }

        public void setCard(String card) {
            this.card = card;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public Object getJjifen() {
            return jjifen;
        }

        public void setJjifen(Object jjifen) {
            this.jjifen = jjifen;
        }

        public Object getQjifen() {
            return qjifen;
        }

        public void setQjifen(Object qjifen) {
            this.qjifen = qjifen;
        }

        public String getN_code() {
            return n_code;
        }

        public void setN_code(String n_code) {
            this.n_code = n_code;
        }

        public String getH_code() {
            return h_code;
        }

        public void setH_code(String h_code) {
            this.h_code = h_code;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
