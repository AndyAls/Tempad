package padd.qlckh.cn.tempad.yipingfang

class OrderReply {
    var d: D? = null
    var r: Int = -1

    fun isOpen(): Boolean {
        return r == 8001 && d?.code == 1;
    }

    fun isClose(): Boolean {
        return r == 8001 && d?.code == 0;
    }

    override fun toString(): String {
        return "OrderReply(d=$d, r=$r)"
    }


}

class D {

    var bin_no: Int = -1
    var code: Int = -1
    override fun toString(): String {
        return "D(bin_no=$bin_no, code=$code)"
    }
}