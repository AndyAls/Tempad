package padd.qlckh.cn.tempad.yipingfang

import android.os.Handler
import android.os.Message
import com.golong.commlib.util.setClickListener
import com.golong.commlib.util.toast
import kotlinx.android.synthetic.main.activity_yi_pannel.*
import padd.qlckh.cn.tempad.BaseActivity
import padd.qlckh.cn.tempad.ConvertUtils
import padd.qlckh.cn.tempad.R
import padd.qlckh.cn.tempad.manager.OnSerialPortDataListener


/**
 * @author Andy
 * @date   2021/11/4 17:10
 * Desc:
 */
class YiPannelActivity : BaseActivity() {

    private val WEIGHT_WHAT = 1000
    private val handler = Handler {
        when (it.what) {
            WEIGHT_WHAT -> {
                handWeight(it.obj as ByteArray)
            }
        }
        false
    }

    var buidler = StringBuilder()
    private fun handWeight(bytes: ByteArray) {
        buidler.append("接收的数据")
                .append("\n")
                .append(ConvertUtils.bytes2HexString(bytes))
                .append("\n")
        tvResult.text = buidler.toString()
    }

    private fun peelWeight() {

        Handler().postDelayed({
            mWeightManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.PEEL_DIANCHI))
        }, 150)
        Handler().postDelayed({
            mWeightManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.PEEL_BOLI))
        }, 300)
        Handler().postDelayed({
            mWeightManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.PEEL_JINSHU))
        }, 450)
        Handler().postDelayed({
            mWeightManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.PEEL_SULIAO))
        }, 600)

    }

    override fun initView() {
        zhizhang.setClickListener {
//            mPanelManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.CLOSE_DIANCHI))
            peelWeight()
            clearText()
            toast("去皮")
        }
        boli.setClickListener {
//            mPanelManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.CLOSE_BOLI))
            mWeightManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.ZERO_JINSHU))
            clearText()
            toast("标零点")
        }
        jinshu.setClickListener {
//            mPanelManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.CLOSE_JINSHU))
            toast("标定20kg")
            clearText()
            mWeightManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.CHECK_JINSHU))
        }



        suliao.setClickListener {
            mPanelManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.CLOSE_SULIAO))
        }
        colse.setClickListener {
            mPanelManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.OPEN_ALL))
        }
        tvResult.setClickListener {
            clearText()
        }
    }

    fun clearText() {
        buidler.clear()
        tvResult.text = ""
    }

    override fun initDate() {
        mWeightManager.setOnSerialPortDataListener(object : OnSerialPortDataListener {
            override fun onDataReceived(bytes: ByteArray?) {
                val message = Message.obtain()
                message.what = WEIGHT_WHAT
                message.obj = bytes
                handler.sendMessageDelayed(message, 200)
            }

            override fun onDataSent(bytes: ByteArray?) {
            }

        })
    }

    override fun showError(msg: String?) {
    }

    override fun release() {
    }

    override fun getContentView(): Int {
        return R.layout.activity_yi_pannel
    }
}