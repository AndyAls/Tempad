package padd.qlckh.cn.tempad;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Andy
 * @date 2018/9/17 17:04
 * Desc:
 */
public class ResponeDao {


    /**
     * status : 1
     * msg : 查询成功
     * row : {"id":"40","user":"于汉波","address":"北京","jifen":"22","djifen":"10","dwight":"20","wight":"10","userid":"107","addtime":"1537174966","fwight":"0","zwight":"0","ydwight":"0","wendu":"0","bwight":"10","wyid":"22","zongzhong":10}
     */

    private int status;
    private String msg;
    private ResponeBean row;

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

    public ResponeBean getRow() {
        return row;
    }

    public void setRow(ResponeBean row) {
        this.row = row;
    }

    public static class ResponeBean implements Parcelable {
        /**
         * id : 40
         * user : 于汉波
         * address : 北京
         * jifen : 22
         * djifen : 10
         * dwight : 20
         * wight : 10
         * userid : 107
         * addtime : 1537174966
         * fwight : 0  金属
         * zwight : 0  纸张
         * ydwight : 0 有毒有害
         * wendu : 0
         * bwight : 10  塑料
         * wyid : 22
         * zongzhong : 10
         */

        private String id;
        private String user;
        private String address;
        private String jifen;
        private String djifen="0.0";
        private String dwight;
        private String wight;
        private String userid;
        private String addtime;
        private String fwight="0.0";
        private String zwight="0.0";
        private String ydwight="0.0";
        private String wendu;
        private String bwight="0.0";
        private String wyid;
        private String iid;
        private int zongzhong;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getJifen() {
            return jifen;
        }

        public void setJifen(String jifen) {
            this.jifen = jifen;
        }

        public String getDjifen() {
            return djifen;
        }

        public void setDjifen(String djifen) {
            this.djifen = djifen;
        }

        public String getDwight() {
            return dwight;
        }

        public void setDwight(String dwight) {
            this.dwight = dwight;
        }

        public String getWight() {
            return wight;
        }

        public void setWight(String wight) {
            this.wight = wight;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getFwight() {
            return fwight;
        }

        public void setFwight(String fwight) {
            this.fwight = fwight;
        }

        public String getZwight() {
            return zwight;
        }

        public void setZwight(String zwight) {
            this.zwight = zwight;
        }

        public String getYdwight() {
            return ydwight;
        }

        public void setYdwight(String ydwight) {
            this.ydwight = ydwight;
        }

        public String getWendu() {
            return wendu;
        }

        public void setWendu(String wendu) {
            this.wendu = wendu;
        }

        public String getBwight() {
            return bwight;
        }

        public void setBwight(String bwight) {
            this.bwight = bwight;
        }

        public String getWyid() {
            return wyid;
        }

        public void setWyid(String wyid) {
            this.wyid = wyid;
        }

        public int getZongzhong() {
            return zongzhong;
        }

        public void setZongzhong(int zongzhong) {
            this.zongzhong = zongzhong;
        }

        public String getIid() {
            return iid;
        }

        public void setIid(String iid) {
            this.iid = iid;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.user);
            dest.writeString(this.address);
            dest.writeString(this.jifen);
            dest.writeString(this.djifen);
            dest.writeString(this.dwight);
            dest.writeString(this.wight);
            dest.writeString(this.userid);
            dest.writeString(this.addtime);
            dest.writeString(this.fwight);
            dest.writeString(this.zwight);
            dest.writeString(this.ydwight);
            dest.writeString(this.wendu);
            dest.writeString(this.bwight);
            dest.writeString(this.wyid);
            dest.writeString(this.iid);
            dest.writeInt(this.zongzhong);
        }

        public ResponeBean() {
        }

        protected ResponeBean(Parcel in) {
            this.id = in.readString();
            this.user = in.readString();
            this.address = in.readString();
            this.jifen = in.readString();
            this.djifen = in.readString();
            this.dwight = in.readString();
            this.wight = in.readString();
            this.userid = in.readString();
            this.addtime = in.readString();
            this.fwight = in.readString();
            this.zwight = in.readString();
            this.ydwight = in.readString();
            this.wendu = in.readString();
            this.bwight = in.readString();
            this.wyid = in.readString();
            this.iid = in.readString();
            this.zongzhong = in.readInt();
        }

        public static final Parcelable.Creator<ResponeBean> CREATOR = new Parcelable.Creator<ResponeBean>() {
            @Override
            public ResponeBean createFromParcel(Parcel source) {
                return new ResponeBean(source);
            }

            @Override
            public ResponeBean[] newArray(int size) {
                return new ResponeBean[size];
            }
        };
    }
}
