package padd.qlckh.cn.tempad.yipingfang

import kotlinx.android.synthetic.main.test_activity_yi.*
import padd.qlckh.cn.tempad.BaseActivity
import padd.qlckh.cn.tempad.ConvertUtils
import padd.qlckh.cn.tempad.R
import padd.qlckh.cn.tempad.manager.OnOpenSerialPortListener
import padd.qlckh.cn.tempad.manager.OnSerialPortDataListener
import java.io.File

/**
 * @author Andy
 * @date   2021/11/5 17:03
 * @link   {http://blog.csdn.net/andy_l1}
 * Desc:    YiTestActivity.kt
 */
class YiTestActivity : BaseActivity() {
    private var serialMultiPorts = SerialMultiPorts()
    override fun initView() {

//        serialMultiPorts.init(this)
        mPanelManager.setOnSerialPortDataListener(object : OnSerialPortDataListener {
            override fun onDataReceived(bytes: ByteArray?) {

                runOnUiThread {

                    tvResult.text = "接收的数据-" + ConvertUtils.bytes2HexString(bytes)
                }
            }

            override fun onDataSent(bytes: ByteArray?) {

                tvResult.text = "发送的数据-" + ConvertUtils.bytes2HexString(bytes)
            }

        })

        mPanelManager.setOnOpenSerialPortListener(object : OnOpenSerialPortListener {
            override fun onSuccess(device: File?) {

                tvStatus.text = "串口状态-打开成功-" + device?.path

            }

            override fun onFail(device: File?, status: OnOpenSerialPortListener.Status?) {
                tvStatus.text = "串口状态-打开失败-" + status?.name
            }


        })

    }

    override fun initDate() {

        open.setOnClickListener {

            showShort("开门")
            mPanelManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.OPEN_ALL))
        }
        close.setOnClickListener {
            showShort("关门")
            mPanelManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.CLOSE_ALL))
        }
    }

    override fun showError(msg: String?) {
    }

    override fun release() {
    }

    override fun onResume() {
        super.onResume()
//        serialMultiPorts.resume()
    }


    override fun onDestroy() {
        super.onDestroy()
//        serialMultiPorts.destroy()
    }

    override fun getContentView(): Int {

        return R.layout.test_activity_yi
    }

}