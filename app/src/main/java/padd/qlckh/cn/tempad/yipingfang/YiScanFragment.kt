package padd.qlckh.cn.tempad.yipingfang

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.bertsir.zbar.utils.QRUtils
import com.golong.commlib.util.setClickListener
import com.golong.commlib.util.setViewVisible
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.scan_fragment_yi.*
import padd.qlckh.cn.tempad.*
import padd.qlckh.cn.tempad.http.RxHttpUtils
import padd.qlckh.cn.tempad.http.interceptor.Transformer
import padd.qlckh.cn.tempad.http.observer.CommonObserver
import padd.qlckh.cn.tempad.http.utils.AppUtils
import padd.qlckh.cn.tempad.manager.OnOpenSerialPortListener
import padd.qlckh.cn.tempad.manager.OnSerialPortDataListener
import java.io.File
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * @author Andy
 * @date   2021/11/5 17:03
 * @link   {http://blog.csdn.net/andy_l1}
 * Desc:    YiScanFragment.kt
 */

class YiScanFragment : BaseFragment() {

    private val TAG = "YiScanFragment"
    private var tagCheck = ""
    var boli = 0.0
    var jinshu = 0.0
    var suliao = 0.0
    var canGoHome = true
    var canScan = true
    private var count = 0
    var delay: Disposable? = null
    private var disposable: Disposable? = null
    private var p = 0
    private var recorderTime = System.currentTimeMillis()
    private val sbPanel = StringBuilder()
    private var startPanel = false
    private var startWeight = false
    var timer: CountDownTimer? = null
    var userInfo: YiUserInfo.RowBean? = null
    private val w = 0
    private val weightBuilder = StringBuilder()
    var zhizhang = 0.0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.scan_fragment_yi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setSerialListener()
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        initData()


    }

    private var watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            val scanStr = s?.toString() ?: ""
            recorderTime = System.currentTimeMillis()
            if (scanStr.isEmpty()) return
            if (scanStr.length == 10 && canScan) {
                canScan = false
                canGoHome = false
                setGoHome()
                etScan.setText("")
                queryUser(scanStr, "1")
            } else if (scanStr.length > 10) {
                etScan.setText("")
            }
        }
    }

    private fun queryUser(scanStr: String, s: String) {
        canGoHome = false
        setGoHome()
        recorderTime = System.currentTimeMillis()
        loading("门正在打开,请稍等.....")
        RxHttpUtils.createApi(ApiService::class.java)
                .queryYiInfo(scanStr)
                .compose(Transformer.switchSchedulers())
                .subscribe(object : CommonObserver<YiUserInfo?>() {
                    override fun onError(errorMsg: String) {
                        canScan = true
                        cancelLoading()
                        recorderTime = System.currentTimeMillis()
                        canGoHome = true
                        showLong("获取用户信息失败")
                        setGoHome()
                    }

                    override fun onSuccess(t: YiUserInfo?) {
                        if (t != null && !t.row.isNullOrEmpty()) {
                            userInfo = t.row[0]
                            canGoHome = false
                            setGoHome()
                            recorderTime=System.currentTimeMillis()
                            Handler().postDelayed({
                                openTrash()
                            }, 200)
                            Handler().postDelayed({
                                startPanel = true
                            }, 800)

                        } else {
                            showLong("获取用户信息失败")
                            canScan = true
                            canGoHome = true
                            setGoHome()
                        }
                    }
                })
    }

    private fun setGoHome() {
        if (mActivity != null && mActivity is YiMainActivity) {
            val yiMainActivity = mActivity as YiMainActivity
            yiMainActivity.canGoHome(canGoHome)
        }
    }

    private fun initListener() {
        /*layoutScan.setClickListener {
            layoutScan.setViewVisible(false)
            layoutDump.setViewVisible(true)
            layoutLoading.setViewVisible(false)
            layoutSuccess.setViewVisible(false)

        }

        layoutDump.setClickListener {
            layoutScan.setViewVisible(false)
            layoutDump.setViewVisible(false)
            layoutLoading.setViewVisible(true)
            layoutSuccess.setViewVisible(false)
        }
        layoutLoading.setClickListener {
            layoutScan.setViewVisible(false)
            layoutDump.setViewVisible(false)
            layoutLoading.setViewVisible(false)
            layoutSuccess.setViewVisible(true)
        }
        layoutSuccess.setClickListener {
            layoutScan.setViewVisible(true)
            layoutDump.setViewVisible(false)
            layoutLoading.setViewVisible(false)
            layoutSuccess.setViewVisible(false)
        }

        ivScanCode.setClickListener {
            toast(tagCheck + "开门")
            openTrash()
        }

        tvScanHint.setClickListener {

            Handler().postDelayed({
                toast(tagCheck + "关门")
                closeTrash()
            }, 1500)
        }*/

    }

    var handler = Handler {
        val what = it.what
        when (what) {
            PANNEL_WHAT -> {
                handPanel(it.obj as ByteArray)
            }
            WEIGHT_WHAT -> {
                handWeight(it.obj as ByteArray)
            }
        }
        false
    }


    private fun openTrash() {
        when (tagCheck) {
            YiMainActivity.DIANCHI -> {
                mPanelManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.CLOSE_DIANCHI))
            }

            YiMainActivity.BOLI -> {
                mPanelManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.CLOSE_BOLI))
            }
            YiMainActivity.JINSHU -> {
                mPanelManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.CLOSE_JINSHU))
            }
            YiMainActivity.SULIAO -> {
                mPanelManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.CLOSE_SULIAO))
            }


        }
    }

    private fun closeTrash() {
        loading("门正在关闭,请稍等......")
        Handler().postDelayed({
            when (tagCheck) {
                YiMainActivity.DIANCHI -> {
                    mPanelManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.OPEN_DIANCHI))
                }

                YiMainActivity.BOLI -> {
                    mPanelManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.OPEN_BOLI))
                }
                YiMainActivity.JINSHU -> {
                    mPanelManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.OPEN_JINSHU))
                }
                YiMainActivity.SULIAO -> {
                    mPanelManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.OPEN_SULIAO))
                }

            }
        },200)

    }

    private fun handWeight(bytes: ByteArray) {

        if (startWeight) {
            weightBuilder.append(ConvertUtils.bytes2HexString(bytes))
            if (!weightBuilder.startsWith("55")) {
                weightBuilder.delete(0, weightBuilder.length)
            }
            if (weightBuilder.startsWith("55") && weightBuilder.length == 36) {
                startWeight = false
                var weight = YiUtils.getWeight(weightBuilder.toString(), tagCheck)
                if (weight.toDouble() > 25.0) {
                    if (count < 6) {
                        startWeight = true
                        count += 1
                        weightBuilder.delete(0, weightBuilder.length)
                        return
                    } else {
                        weight = "0.0"
                    }
                }

                val jifen: Double
                val status: String
                val name = when (tagCheck) {
                    YiMainActivity.JINSHU -> {
                        jifen = jinshu
                        status = "2,5"
                        "金属"
                    }
                    YiMainActivity.SULIAO -> {
                        jifen = suliao
                        status = "2,1"
                        "塑料"

                    }
                    YiMainActivity.BOLI -> {
                        jifen = boli
                        status = "2,3"
                        "玻璃"
                    }
                    YiMainActivity.DIANCHI -> {
                        jifen = zhizhang
                        status = "2,2"
                        "纸张"
                    }
                    else -> {
                        jifen = zhizhang
                        status = "2,2"
                        "纸张"
                    }
                }
                layoutScan.setViewVisible(false)
                layoutDump.setViewVisible(false)
                layoutLoading.setViewVisible(false)
                layoutSuccess.setViewVisible(true)
                tvSuccessResult.text = "用户:  ${userInfo?.fullname}\n种类:  ${name}\n重量:  ${weight}kg\n碳分:  ${BigDecimal(weight).multiply(BigDecimal(jifen))}分"
                postData(userInfo!!, weight, jifen, status)
                MediaPlayerHelper.getInstance(getActivity()).startPlay(R.raw.delivery_success)
                weightBuilder.delete(0, weightBuilder.length)
                startWeight = false
            }
        }
    }

    /**
     * 处理推拉杠
     */
    private fun handPanel(bytes: ByteArray) {

        if (startPanel) {
            val append = sbPanel.append(ConvertUtils.bytes2HexString(bytes))
            if (!append.startsWith("55")) {
                sbPanel.delete(0, sbPanel.length)
            }
            if (sbPanel.startsWith("55") && sbPanel.length == 30) {
                recorderTime=System.currentTimeMillis()
                val status = when (tagCheck) {
                    YiMainActivity.JINSHU -> {
                        sbPanel.substring(16, 18)
                    }
                    YiMainActivity.SULIAO -> {
                        sbPanel.substring(20, 22)

                    }
                    YiMainActivity.BOLI -> {
                        sbPanel.substring(12, 14)
                    }
                    YiMainActivity.DIANCHI -> {
                        sbPanel.substring(8, 10)
                    }
                    else -> {
                        "00"
                    }
                }
                when (status.toString().toUpperCase(Locale.ROOT)) {
                    //缩回  门打开
                    "07", "0B" -> {
                        if (layoutScan.visibility==View.VISIBLE){
                            layoutScan.setViewVisible(false)
                            layoutDump.setViewVisible(true)
                            layoutLoading.setViewVisible(false)
                            layoutSuccess.setViewVisible(false)
                            cancelLoading()
                            MediaPlayerHelper.getInstance(context).startPlay(R.raw.put_in_garbage)
                            if (timer != null) {
                                timer!!.start()
                            }
                            startPanel = false
                        }

                    }
                    //伸出  门关闭
                    "03", "04" -> {
                        if (layoutDump.visibility==View.VISIBLE){
                            layoutScan.setViewVisible(false)
                            layoutDump.setViewVisible(false)
                            layoutLoading.setViewVisible(true)
                            layoutSuccess.setViewVisible(false)
                            cancelLoading()
                            MediaPlayerHelper.getInstance(context).startPlay(R.raw.weighting)
                            Handler().postDelayed({
                                startWeight = true
                            }, 800)
                            startPanel = false
                        }

                    }

                }
                sbPanel.delete(0, sbPanel.length)
            }
        }
    }

    private fun postData(rowBean: YiUserInfo.RowBean, weight: String, d: Double, status: String) {

        recorderTime=System.currentTimeMillis()
        RxHttpUtils.createApi(ApiService::class.java)
                .postData(rowBean.fullname, rowBean.id, weight, rowBean.items, rowBean.phone,
                        AppUtils.getDeviceId(context), BigDecimal(weight).multiply(BigDecimal(d)).toString(), status, "3")
                .compose(Transformer.switchSchedulers())
                .subscribe(object : CommonObserver<Any?>() {
                    override fun onError(errorMsg: String) {
                        Handler().postDelayed({
                            restartSelf()
                        }, 3000)
                    }

                    override fun onSuccess(t: Any?) {
                        Handler().postDelayed({
                            restartSelf()
                        }, 3000)
                    }
                })
    }

    private fun setSerialListener() {

        mPanelManager.setOnOpenSerialPortListener(object : OnOpenSerialPortListener {
            override fun onSuccess(device: File?) {
//                tvScanHint.text="串口连接成功"+device?.toString()
            }

            override fun onFail(device: File?, status: OnOpenSerialPortListener.Status?) {
//                tvScanHint.text="串口连接失败"+status?.name
            }
        })

        mPanelManager.setOnSerialPortDataListener(object : OnSerialPortDataListener {
            override fun onDataReceived(bytes: ByteArray?) {
                val message = Message()
                message.what = PANNEL_WHAT
                message.obj = bytes
                handler.sendMessage(message)

            }

            override fun onDataSent(bytes: ByteArray?) {


            }

        })
        mWeightManager.setOnSerialPortDataListener(object : OnSerialPortDataListener {
            override fun onDataReceived(bytes: ByteArray?) {
                val message = Message()
                message.what = WEIGHT_WHAT
                message.obj = bytes
                handler.sendMessage(message)
            }

            override fun onDataSent(bytes: ByteArray?) {
            }

        })
    }

    private fun initView() {

        etScan.addTextChangedListener(watcher)
        etScan.inputType = 0
        MediaPlayerHelper.getInstance(context).startPlay(R.raw.scan_qr_code_or_swipe_card)
        initTimer()
        peelWeight()
        canScan = true
        startPanel = false
        startWeight = false
        scanCode()
        setdelay()
        queryJifen()

    }

    private fun queryJifen() {
        RxHttpUtils.createApi(ApiService::class.java)
                .queryJifen("2")
                .compose(Transformer.switchSchedulers())
                .subscribe(object : CommonObserver<YiJifen?>() {
                    override fun onError(errorMsg: String) {
                    }

                    override fun onSuccess(t: YiJifen?) {
                        jinshu = t?.jinshu?.toDoubleOrNull() ?: 0.0
                        boli = t?.boli?.toDoubleOrNull() ?: 0.0
                        suliao = t?.suliao?.toDoubleOrNull() ?: 0.0
                        zhizhang = t?.zhiban?.toDoubleOrNull() ?: 0.0

                    }
                })
    }

    private fun setdelay() {
        if (disposable == null) {
            disposable = Observable.interval(5, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        if (activity!=null&&activity is YiMainActivity){
                           /* val yiMainActivity = activity as YiMainActivity
                            yiMainActivity.netWorkState(null)*/
                        }
                        if (System.currentTimeMillis() - recorderTime > 60000L) {
                            restartSelf()
                        }
                    }
        }
    }

    private fun restartSelf() {
        if (context == null) return
        val intent = Intent(context, YiMainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        mActivity?.finish()
        mActivity?.overridePendingTransition(0, 0)
    }

    private fun scanCode() {
        if (delay == null) {
            delay = RxHttpUtils.createApi(ApiService::class.java)
                    .scanCode(AppUtils.getDeviceId(context))
                    .delay(5, TimeUnit.SECONDS)
                    .repeat(24)
                    .compose(Transformer.switchSchedulers())
                    .subscribe({
                        if (it.status == "1") {
                            val row = it.row
                            queryUser(row, "0")
                            if (delay != null) {
                                delay!!.dispose()
                            }
                            canGoHome = false
                            setGoHome()
                            canScan = false
                        } else {
                            canScan = true
                        }
                    }) {

                        recorderTime=System.currentTimeMillis()
                        canScan = true
                    }

        }
    }

    private fun peelWeight() {

        Handler().postDelayed({
            mWeightManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.PEEL_DIANCHI))
        }, 200)
        Handler().postDelayed({
            mWeightManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.PEEL_BOLI))
        }, 400)
        Handler().postDelayed({
            mWeightManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.PEEL_JINSHU))
        }, 600)
        Handler().postDelayed({
            mWeightManager.sendBytes(ConvertUtils.hexString2Bytes(YiConstant.PEEL_SULIAO))
        }, 800)

    }

    private fun initTimer() {
        if (timer == null) {
            timer = object : CountDownTimer(20000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    tvTime.text = "${millisUntilFinished / 1000L}s"
                }

                override fun onFinish() {

                    tvTime.text = "0s"
                    closeTrash()
                    btnClose.isEnabled = false
                    Handler().postDelayed({
                        startPanel = true
                    }, 800)
                }

            }
        }
        btnClose.setClickListener {
            recorderTime = System.currentTimeMillis()
            closeTrash()
            if (timer != null) {
                timer!!.cancel()
                timer = null
            }
            Handler().postDelayed({
                startPanel = true
            }, 800)
        }

    }

    private fun initData() {
        tagCheck = arguments?.getString("tag") ?: "error"
        val str3 = "\",\"fenlei\":\"可回收/废纸板\"}"
        val str4 = "{\"status\":\"2,2\",\"wyid\":\""
        tvDumpType.text = when (tagCheck) {
            YiMainActivity.DIANCHI -> {

                ivScanCode.setImageBitmap(QRUtils.getInstance().createQRCode(str4 + AppUtils.getDeviceId(getActivity()) + str3))
                ivDump.setImageResource(R.drawable.zz)
                "纸张"
            }

            YiMainActivity.BOLI -> {
                ivDump.setImageResource(R.drawable.bl)
                ivScanCode.setImageBitmap(QRUtils.getInstance().createQRCode("{\"status\":\"2,3\",\"wyid\":\"" + AppUtils.getDeviceId(getActivity()) + "\",\"fenlei\":\"可回收/废玻璃\"}"))
                "玻璃"
            }
            YiMainActivity.JINSHU -> {
                ivDump.setImageResource(R.drawable.js)
                ivScanCode.setImageBitmap(QRUtils.getInstance().createQRCode("{\"status\":\"2,5\",\"wyid\":\"" + AppUtils.getDeviceId(getActivity()) + "\",\"fenlei\":\"可回收/金属\"}"))
                "金属"
            }
            YiMainActivity.SULIAO -> {
                ivDump.setImageResource(R.drawable.sl)
                ivScanCode.setImageBitmap(QRUtils.getInstance().createQRCode("{\"status\":\"2,1\",\"wyid\":\"" + AppUtils.getDeviceId(getActivity()) + "\",\"fenlei\":\"可回收/塑料\"}"))
                "塑料"
            }
            else -> {
                ivDump.setImageResource(R.drawable.zz)
                ivScanCode.setImageBitmap(QRUtils.getInstance().createQRCode(str4 + AppUtils.getDeviceId(getActivity()) + str3))
                "纸张"
            }
        }

    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        etScan.inputType = 0
        if (hidden) {
            onHide()
        } else {
            onShow()
        }

    }

    private fun onHide() {

        etScan.removeTextChangedListener(watcher)
        etScan.setText("")
        etScan.isEnabled = false
        MediaPlayerHelper.getInstance(context).release()
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
        canScan = false
        startPanel = false
        startWeight = false
        count = 0
        if (delay != null) {
            delay!!.dispose()
            delay = null
        }
        if (disposable != null) {
            disposable!!.dispose()
            disposable = null
        }
        handler.removeMessages(WEIGHT_WHAT)
        handler.removeMessages(PANNEL_WHAT)
        clearCode()
    }

    private fun clearCode() {
        RxHttpUtils.createApi(ApiService::class.java)
                .clearCode(AppUtils.getDeviceId(context))
                .compose(Transformer.switchSchedulers())
                .subscribe(object : CommonObserver<Any?>() {
                    override fun onError(errorMsg: String) {
                    }

                    override fun onSuccess(t: Any?) {
                    }
                })
    }

    private fun onShow() {

        layoutScan.setViewVisible(true)
        layoutDump.setViewVisible(false)
        layoutLoading.setViewVisible(false)
        layoutSuccess.setViewVisible(false)
        etScan.setText("")
        etScan.addTextChangedListener(watcher)
        etScan.inputType = 0
        etScan.isEnabled = true
        MediaPlayerHelper.getInstance(context).startPlay(R.raw.scan_qr_code_or_swipe_card)
        initTimer()
        peelWeight()
        btnClose.isEnabled = true
        canScan = true
        startWeight = false
        startPanel = false
        count = 0
        scanCode()
        canGoHome = true
        setGoHome()
        recorderTime = System.currentTimeMillis()
        setdelay()
        setSerialListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        etScan.removeTextChangedListener(watcher)
        etScan.setText("")
        MediaPlayerHelper.getInstance(context).release()
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
        canScan = false
        startPanel = false
        startWeight = false
        count = 0
        if (delay != null) {
            delay!!.dispose()
            delay = null
        }
        if (disposable != null) {
            disposable!!.dispose()
            disposable = null
        }
        handler.removeMessages(WEIGHT_WHAT)
        handler.removeMessages(PANNEL_WHAT)
    }

    companion object {

        const val PANNEL_WHAT = 10000
        const val WEIGHT_WHAT = 10001
        fun newInstance(tag: String): YiScanFragment {
            val yiScanFragment = YiScanFragment()
            val bundle = Bundle()
            bundle.putString("tag", tag)
            yiScanFragment.arguments = bundle
            return yiScanFragment
        }
    }

}