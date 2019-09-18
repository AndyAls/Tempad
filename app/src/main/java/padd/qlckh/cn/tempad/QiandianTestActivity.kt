package padd.qlckh.cn.tempad

import android.os.Handler
import android.os.Message
import kotlinx.android.synthetic.main.activity_qidian_test.*
import padd.qlckh.cn.tempad.manager.OnOpenSerialPortListener
import padd.qlckh.cn.tempad.manager.OnSerialPortDataListener
import java.io.File
import java.util.*

/**
 * @author Andy
 * @date   2019/5/31 16:40
 * Desc:
 */
class QiandianTestActivity : BaseActivity() {
    private val TASK_DELAY_TIME = 5000L
    private val TIMER = 299993

    private val SCAN_WHAT = 1010100
    private val PANEL_WHAT = 39999

    private var task: MyTimeTask? = null


    private val mHandler = Handler(Handler.Callback { msg ->
        val what = msg.what
        when (what) {
            //定时发送秤的数据
            TIMER -> {
                tv8.text = System.currentTimeMillis().toString()
                mPanelManager.sendBytes(ConvertUtils.hexString2Bytes(Constant.QIAN_READ_STATUS))
            }

            SCAN_WHAT -> handScan(msg.obj as ByteArray)

            PANEL_WHAT -> handPanl(msg.obj as ByteArray)
        }
        false
    })


    var buff = StringBuffer()
    var i = 0
    var j = 0
    private fun handPanl(bytes: ByteArray) {

        i += 1
        tv9.text="i="+i
        val result1 = ConvertUtils.bytes2HexString(bytes)
        tv1.text="0"
        tv2.text="0"
        tv3.text="0"
        buff.append(result1)
        if (i == 2) {
            val result = buff.toString()
            tv1.text=result

            //清除结果
            if (result.toUpperCase().substring(4, 6) == "A3") {
                tv2.text="清除结果0"+result
                Handler().postDelayed({
                    mPanelManager.sendBytes(ConvertUtils.hexString2Bytes("200002040100003215"))
                },2000)

            }
            //发动机转一圈 后 要定时检查机器状态
            if (result.toUpperCase().substring(4, 6) == "A2") {
                tv3.text="转圈"+result
                Handler().postDelayed({
                    if (task==null){
                        task = MyTimeTask(TASK_DELAY_TIME, object : TimerTask() {
                            override fun run() {
                                mHandler.sendEmptyMessage(TIMER)
                                runOnUiThread {

                                }

                            }
                        })
                    }
                    task!!.start()
                },2000)

            }
            //读取机器状态
            if (result.toUpperCase().substring(4, 6) == "A1") {
                //读取出货结果: 20 00 A1 03 00 00 1A 98
                //0: 空闲等待
                //1: 正在出货
                //2: 出货成功
                //3: 出货失败
                val status = result.substring(10, 12)
                when (status) {
                    "00" -> tv10.setText("空闲等待")
                    "01" -> tv10.setText("正在出货...")
                    //出货成功  提交后台 停止task
                    "02" -> {
                        task!!.stop()
                        task=null
                        tv10.setText("出货成功!")
                    }
                    "03" -> {

                        task!!.stop()
                        task=null
                        tv10.setText("出货失败")

                    }
                }
            }
            buff.delete(0, result.length)
            i = 0
        }
    }


    private fun handScan(bytes: ByteArray) {
        bu.append(String(bytes))
        j += 1
        tv9.text="j="+j
        val string = String(bytes)
        tv2.text = "0"
        if (j==2){
            tv1.text = "扫码原始数据:" + string
            tv2.text = "扫码16进制数据:" + bu.toString()
            bu.delete(0, bu.toString().length)
            j = 0
        }
    }


    val bu = StringBuffer()
    override fun initView() {
        setTimer()
        setScanListerer()
        setPanelListener()
        tv3.text = Constant.QIAN_CLEAR


        bt1.setOnClickListener {
            mPanelManager.sendBytes(ConvertUtils.hexString2Bytes(Constant.QIAN_CLEAR))
        }
        bt2.setOnClickListener {
            mPanelManager.sendBytes(ConvertUtils.hexString2Bytes("200002040100003215"))
        }
        bt0.setOnClickListener {
//            finish()
            mPanelManager.sendBytes(ConvertUtils.hexString2Bytes(Constant.QIAN_READ_STATUS))
        }
    }

    private fun setPanelListener() {
        mPanelManager.setOnOpenSerialPortListener(object : OnOpenSerialPortListener {
            override fun onSuccess(device: File) {
                tv7.text = device.path

            }

            override fun onFail(device: File, status: OnOpenSerialPortListener.Status) {

            }
        })

        mPanelManager.setOnSerialPortDataListener(object : OnSerialPortDataListener {
            override fun onDataReceived(bytes: ByteArray) {
                val message = Message()
                message.what = PANEL_WHAT
                message.obj = bytes
                mHandler.sendMessage(message)
            }

            override fun onDataSent(bytes: ByteArray) {

            }
        })


    }

    private fun setScanListerer() {
        mScanManager.setOnOpenSerialPortListener(object : OnOpenSerialPortListener {
            override fun onSuccess(device: File) {}

            override fun onFail(device: File, status: OnOpenSerialPortListener.Status) {}
        })
        mScanManager.setOnSerialPortDataListener(object : OnSerialPortDataListener {
            override fun onDataReceived(bytes: ByteArray) {
                val message = Message()
                message.what = SCAN_WHAT
                message.obj = bytes
                mHandler.sendMessage(message)
            }

            override fun onDataSent(bytes: ByteArray) {

            }
        })
    }

    private fun setTimer() {
        task = MyTimeTask(TASK_DELAY_TIME, object : TimerTask() {
            override fun run() {
                mHandler.sendEmptyMessage(TIMER)
                runOnUiThread {

                }

            }
        })
    }

    override fun showError(msg: String?) {
    }

    override fun getContentView(): Int {

        return R.layout.activity_qidian_test
    }

    override fun initDate() {
        tv5.text = CommUtils.getIMEI(this)
    }

    override fun release() {
    }
}