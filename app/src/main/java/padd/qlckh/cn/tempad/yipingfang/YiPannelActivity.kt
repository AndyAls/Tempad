package padd.qlckh.cn.tempad.yipingfang

import android.os.Handler
import android.os.Message
import com.golong.commlib.util.setClickListener
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


    override fun initView() {
        zhizhang.setClickListener {
            mPanelManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.CLOSE_DIANCHI))
        }
        boli.setClickListener {
            mPanelManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.CLOSE_BOLI))
        }
        jinshu.setClickListener {
            mPanelManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.CLOSE_JINSHU))
        }
        suliao.setClickListener {
            mPanelManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.CLOSE_SULIAO))
        }
        colse.setClickListener {
            mPanelManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.OPEN_ALL))
        }
        tvResult.setClickListener {
            tvResult.text = ""
        }
    }
    override fun initDate() {
        mWeightManager.setOnSerialPortDataListener(object : OnSerialPortDataListener {
            override fun onDataReceived(bytes: ByteArray?) {
            }

            override fun onDataSent(bytes: ByteArray?) {
                val message = Message()
                message.what = WEIGHT_WHAT
                message.obj = bytes
                handler.sendMessage(message)
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