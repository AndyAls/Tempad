package padd.qlckh.cn.tempad.yipingfang

import android.app.TimePickerDialog
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.widget.TimePicker
import padd.qlckh.cn.tempad.setClickListener
import padd.qlckh.cn.tempad.toast
import kotlinx.android.synthetic.main.activity_yi_pannel.boli
import kotlinx.android.synthetic.main.activity_yi_pannel.btOff
import kotlinx.android.synthetic.main.activity_yi_pannel.btOffCancle
import kotlinx.android.synthetic.main.activity_yi_pannel.btOn
import kotlinx.android.synthetic.main.activity_yi_pannel.btOnCancle
import kotlinx.android.synthetic.main.activity_yi_pannel.colse
import kotlinx.android.synthetic.main.activity_yi_pannel.colse2
import kotlinx.android.synthetic.main.activity_yi_pannel.etScan
import kotlinx.android.synthetic.main.activity_yi_pannel.jinshu
import kotlinx.android.synthetic.main.activity_yi_pannel.suliao
import kotlinx.android.synthetic.main.activity_yi_pannel.tvResult
import kotlinx.android.synthetic.main.activity_yi_pannel.tvState
import kotlinx.android.synthetic.main.activity_yi_pannel.zhizhang
import padd.qlckh.cn.tempad.BaseActivity
import padd.qlckh.cn.tempad.ConvertUtils
import padd.qlckh.cn.tempad.JsonUtil
import padd.qlckh.cn.tempad.R
import padd.qlckh.cn.tempad.manager.OnOpenSerialPortListener
import padd.qlckh.cn.tempad.manager.OnSerialPortDataListener
import java.io.File


private const val s1 = "串口打开失败"

/**
 * @author Andy
 * @date   2021/11/4 17:10
 * Desc:
 */
class YiPannelActivity : BaseActivity() {

    private val WEIGHT_WHAT = 1000
    private val WEIGHT_WHAT_REQ = 1001
    private val SCAN_WHAT = 1004
    private val handler = Handler {
        when (it.what) {
            WEIGHT_WHAT -> {
                handWeight(it.obj as ByteArray)
            }

            WEIGHT_WHAT_REQ -> {
                handWeightReq(it.obj as ByteArray)
            }
            SCAN_WHAT->{
                handScan(it.obj as ByteArray)
            }
        }
        false
    }

    private fun handScan(bytes: ByteArray) {

        buidler.append("接收的数据-")
            .append("\n")
            .append(ConvertUtils.bytes2HexString(bytes))
            .append(ConvertUtils.hexStringToAscii(ConvertUtils.bytes2HexString(bytes)))
            .append("\n")
        tvResult.text = buidler.toString()
    }

    private fun handWeightReq(bytes: ByteArray) {
        buidler1.append("发送的数据")
            .append("\n")
            .append(ConvertUtils.bytes2Json(bytes))
            .append("\n")
        tvState.text = buidler1.toString()
    }

    var builder2: StringBuilder = StringBuilder()
    var buidler = StringBuilder()
    var buidler1 = StringBuilder()
    private fun handWeight(bytes: ByteArray) {
        buidler.append("接收的数据")
            .append("\n")
            .append(ConvertUtils.bytes2Json(bytes))
            .append("\n")
        tvResult.text = buidler.toString()

        val s = ConvertUtils.bytes2Json(bytes)
        builder2.append(s)
        if (!builder2.startsWith("{")) {
            builder2.delete(0, builder2.length)
        }
        if (JsonUtil.isJsonValid(builder2.toString()) && builder2.contains("v")) {
            val deviceResp = JsonUtil.json2Object2(builder2.toString(), DeviceResp::class.java)
            val replace = YiConstant.LOGIN.replace("4A23020105", deviceResp.d.devCode).replace("1.0.9", deviceResp.d.v)
            Handler().postDelayed({ mPanelManager.sendBytes(ConvertUtils.json2Bytes(replace)) }, 300)
            Handler().postDelayed({
                mPanelManager.sendBytes(ConvertUtils.json2Bytes(YiConstant.HEART))
            }, 500)
        }
    }

    private fun peelWeight() {

        Handler().postDelayed({
            mPanelManager.sendBytes(ConvertUtils.json2Bytes(YiConstant.PEEL_DIANCHI))
        }, 150)
       /* Handler().postDelayed({
            mPanelManager.sendBytes(ConvertUtils.json2Bytes(YiConstant.PEEL_BOLI))
        }, 300)
        Handler().postDelayed({
            mPanelManager.sendBytes(ConvertUtils.json2Bytes(YiConstant.PEEL_JINSHU))
        }, 450)
        Handler().postDelayed({
            mPanelManager.sendBytes(ConvertUtils.json2Bytes(YiConstant.PEEL_SULIAO))
        }, 600)*/

    }

    private fun open() {

        Handler().postDelayed({
            mPanelManager.sendBytes(ConvertUtils.json2Bytes(YiConstant.OPEN_DIANCHI))
        }, 150)
        Handler().postDelayed({
            mPanelManager.sendBytes(ConvertUtils.json2Bytes(YiConstant.OPEN_BOLI))
        }, 300)
        Handler().postDelayed({
            mPanelManager.sendBytes(ConvertUtils.json2Bytes(YiConstant.OPEN_JINSHU))
        }, 450)
        Handler().postDelayed({
            mPanelManager.sendBytes(ConvertUtils.json2Bytes(YiConstant.OPEN_SULIAO))
        }, 600)

    }

    private fun close() {

        Handler().postDelayed({
            mPanelManager.sendBytes(ConvertUtils.json2Bytes(YiConstant.CLOSE_DIANCHI))
        }, 150)
        Handler().postDelayed({
            mPanelManager.sendBytes(ConvertUtils.json2Bytes(YiConstant.CLOSE_BOLI))
        }, 300)
        Handler().postDelayed({
            mPanelManager.sendBytes(ConvertUtils.json2Bytes(YiConstant.CLOSE_JINSHU))
        }, 450)
        Handler().postDelayed({
            mPanelManager.sendBytes(ConvertUtils.json2Bytes(YiConstant.CLOSE_SULIAO))
        }, 600)

    }

    override fun initView() {
        zhizhang.setClickListener {
//            mPanelManager.sendBytes(ConvertUtils.json2Bytes(YiConstant.CLOSE_DIANCHI))
            peelWeight()
            clearText()
            toast("去皮")

        }
        boli.setClickListener {
//            mPanelManager.sendBytes(ConvertUtils.json2Bytes(YiConstant.CLOSE_BOLI))
            mPanelManager.sendBytes(ConvertUtils.json2Bytes(YiConstant.ZERO_JINSHU))
            clearText()
            toast("标零点")
        }
        jinshu.setClickListener {
//            mPanelManager.sendBytes(ConvertUtils.json2Bytes(YiConstant.CLOSE_JINSHU))
            toast("1号标定1kg")
            clearText()
            mPanelManager.sendBytes(ConvertUtils.json2Bytes(YiConstant.CHECK_DIANCHI))
        }



        suliao.setClickListener {

//            mPanelManager.sendBytes(ConvertUtils.json2Bytes(YiConstant.UN_Auth))
            toast("2号标定1kg")
            clearText()
            mPanelManager.sendBytes(ConvertUtils.json2Bytes(YiConstant.CHECK_BOLI))

//            open();
        }
        colse.setClickListener {
//            mPanelManager.sendBytes(ConvertUtils.json2Bytes(YiConstant.UN_Auth))
            toast("3号标定1kg")
            clearText()
            mPanelManager.sendBytes(ConvertUtils.json2Bytes(YiConstant.CHECK_JINSHU))

//            close();
        }
        colse2.setClickListener {
//            mPanelManager.sendBytes(ConvertUtils.json2Bytes(YiConstant.UN_Auth))
            toast("4号标定1kg")
            clearText()
            mPanelManager.sendBytes(ConvertUtils.json2Bytes(YiConstant.CHECK_SULIAO))

//            close();
        }
        tvResult.setClickListener {
            clearText()
        }

        etScan.addTextChangedListener(watcher)

        btOn.setClickListener {
                val timePickerDialog = TimePickerDialog(this,
                    fun(view: TimePicker, hourOfDay: Int, minute: Int) {
                        DeviceManager.setBootUpTime("$hourOfDay:$minute:00")
                        refreshOnOff()
                        DeviceManager.powerOn(this@YiPannelActivity)

                    }, 1, 0, true)
            timePickerDialog.show();
        }
        btOff.setClickListener {
            val timePickerDialog = TimePickerDialog(this,
                fun(view: TimePicker, hourOfDay: Int, minute: Int) {
                    DeviceManager.setShutUpTime("$hourOfDay:$minute:00")
                    refreshOnOff()
                }, 1, 0, true)
            timePickerDialog.show();
        }
        btOnCancle.setClickListener {
            DeviceManager.setBootUpTime("")
            refreshOnOff()
            DeviceManager.disPowerOn(this@YiPannelActivity)
        }
        btOffCancle.setClickListener {
            DeviceManager.setShutUpTime("");
            refreshOnOff()
        }
    }
    private var watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            buidler.append("接收的数据:")
                .append("\n")
                .append(s?.toString())
                .append("\n")
            tvResult.text = buidler.toString()
        }
    }
    fun clearText() {
        buidler.clear()
        tvResult.text = ""
        buidler1.clear()
        tvState.text = ""
    }

    override fun initDate() {
        mPanelManager.setOnSerialPortDataListener(object : OnSerialPortDataListener {
            override fun onDataReceived(bytes: ByteArray?) {
                val message = Message.obtain()
                message.what = WEIGHT_WHAT
                message.obj = bytes
                handler.sendMessageDelayed(message, 200)
            }

            override fun onDataSent(bytes: ByteArray?) {
                val message = Message.obtain()
                message.what = WEIGHT_WHAT_REQ
                message.obj = bytes
                handler.sendMessageDelayed(message, 200)
            }

        })
        mScanManager.setOnSerialPortDataListener(object :OnSerialPortDataListener{
            override fun onDataReceived(bytes: ByteArray?) {
                val message = Message.obtain()
                message.what = SCAN_WHAT
                message.obj = bytes
                handler.sendMessageDelayed(message, 200)
            }

            override fun onDataSent(bytes: ByteArray?) {


            }

        })
        sendLogin();
    }

    private fun sendLogin() {

        refreshOnOff()

        Handler().postDelayed({
            mScanManager.sendBytes(ConvertUtils.hexString2Bytes("200061014AD503"))
        }, 100)
        Handler().postDelayed({
            mPanelManager.sendBytes(ConvertUtils.json2Bytes(YiConstant.TIME_OUT))
        }, 300)
    }

    private fun refreshOnOff() {
        btOn.text = if (DeviceManager.getBootUpTime().isEmpty()) "开机时间" else "开机时间(${DeviceManager.getBootUpTime()})"
        btOff.text = if (DeviceManager.getShutUpTime().isEmpty()) "关机时间" else "关机时间(${DeviceManager.getShutUpTime()})"
    }

    override fun onOpenSuccess(device: File?) {
        super.onOpenSuccess(device)
        tvState.text = "串口打开成功(${device?.absolutePath})"
    }

    override fun onOpenFail(device: File?, status: OnOpenSerialPortListener.Status?) {
        super.onOpenFail(device, status)
        tvState.text = "串口打开失败(${device?.absolutePath})-status(${status})"
    }

    override fun showError(msg: String?) {
    }

    override fun release() {
    }

    override fun getContentView(): Int {
        return R.layout.activity_yi_pannel
    }
}