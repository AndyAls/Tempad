package padd.qlckh.cn.tempad.yipingfang

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import padd.qlckh.cn.tempad.setClickListener
import padd.qlckh.cn.tempad.setViewVisible
import padd.qlckh.cn.tempad.toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_yi_handle.*
import padd.qlckh.cn.tempad.ApiService
import padd.qlckh.cn.tempad.BaseFragment
import padd.qlckh.cn.tempad.ConvertUtils
import padd.qlckh.cn.tempad.MediaPlayerHelper
import padd.qlckh.cn.tempad.MediaPlayerHelper2
import padd.qlckh.cn.tempad.R
import padd.qlckh.cn.tempad.http.RxHttpUtils
import padd.qlckh.cn.tempad.http.interceptor.Transformer
import padd.qlckh.cn.tempad.http.observer.CommonObserver
import padd.qlckh.cn.tempad.manager.OnSerialPortDataListener
import java.util.concurrent.TimeUnit

/**
 * @author Andy
 * @date   2021/11/5 10:15
 * Desc:
 */
class YiHandleFragment : BaseFragment() {

    var canScan = true
    private var checkTag = ""
    private lateinit var detailAdapter: DetailAdapter
    private lateinit var reservationAdapter: ReservationAdapter
    var recorderTime = System.currentTimeMillis()
    private var userInfo: YiUserInfo.RowBean? = null
    private var disposable: Disposable? = null
    var isResume = false;
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_yi_handle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setSerialListener()
        super.onViewCreated(view, savedInstanceState)
        isResume = true;
        checkTag = arguments?.getString(TAG) ?: ""
        initView()
        initRv()
        initData()
    }

    private fun setSerialListener() {
        mScanManager.setOnSerialPortDataListener(object : OnSerialPortDataListener {
            override fun onDataReceived(bytes: ByteArray?) {
                val message = Message.obtain()
                message.what = YiScanFragment.SCAN_WHAT
                message.obj = bytes
                handler.sendMessage(message)
            }

            override fun onDataSent(bytes: ByteArray?) {
            }

        })
    }

    private fun initData() {

        Handler().postDelayed({
            mScanManager.sendBytes(ConvertUtils.hexString2Bytes("200061014AD503"))
        }, 100)
        Handler().postDelayed({
//            mScanManager.sendBytes(ConvertUtils.hexString2Bytes("20003000CF03"))
        }, 200)
    }

    private fun initRv() {
        reservationRv.setHasFixedSize(true)
        reservationRv.layoutManager = GridLayoutManager(context, 5)
        reservationAdapter = ReservationAdapter(arrayListOf())
        reservationRv.adapter = reservationAdapter

        detailRv.setHasFixedSize(true)
        detailRv.layoutManager = LinearLayoutManager(context)
        detailAdapter = DetailAdapter(arrayListOf())
        detailRv.adapter = detailAdapter


    }

    var watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            val scanStr = s?.toString() ?: ""
            recorderTime = System.currentTimeMillis()
            if (scanStr.isEmpty()) return
            if (scanStr.length == 10&&canScan) {
                canScan = false
                queryUser(scanStr)
                etScan.setText("")
            } else if (scanStr.length>10) {
                etScan.setText("")
            }
        }

    }
    var handler = Handler(Looper.getMainLooper()) {
        if (isResume) {
            val what = it.what
            when (what) {
                YiScanFragment.SCAN_WHAT -> {
                    handScan(it.obj as ByteArray)
                }
            }
        }
        false
    }
    private val scanBuilder = StringBuilder()
    private fun handScan(bytes: ByteArray) {

        var append = scanBuilder.append(ConvertUtils.hexStringToAscii(ConvertUtils.bytes2HexString(bytes)))
        if (append.length == 9) {
            append = append.append('0').append(append)
        }
        val scanStr = append.toString()
        recorderTime = System.currentTimeMillis()
        if (scanStr.isEmpty()) return
        if (scanStr.length == 10 && canScan) {
            MediaPlayerHelper2.getInstance(context).startPlay(R.raw.didi)
            canScan = false
            queryUser(scanStr)
            scanBuilder.delete(0, scanBuilder.length)
        } else if (scanStr.length > 10) {
            scanBuilder.delete(0, scanBuilder.length)
        }
    }
    private fun initView() {
        layoutScan.setViewVisible(true)
        layoutQueryResult.setViewVisible(false)
        layoutDetail.setViewVisible(false)
        layoutCategory.setViewVisible(false)
        llReservation.setViewVisible(false)

        if (checkTag == "put") {
            tvScanHint.text = "请在下侧感应区刷卡预约"
            MediaPlayerHelper.getInstance(context).startPlay(R.raw.swipe_card_book)
            tvTitle.text = "预约投放"
        } else {
            tvScanHint.text = "请在下侧感应区刷卡后查看"
            MediaPlayerHelper.getInstance(context).startPlay(R.raw.swipe_card_see)
            tvTitle.text = "碳分查询"
        }
        canScan = true
        etScan.setText("")
        etScan.addTextChangedListener(watcher)
        etScan.inputType = 0
        etScan.isEnabled = true
        setdelay()

    }

    private fun queryUser(scanStr: String) {
        recorderTime = System.currentTimeMillis()
        RxHttpUtils.createApi(ApiService::class.java)
                .queryYiInfo(scanStr)
                .compose(Transformer.switchSchedulers())
                .subscribe(object : CommonObserver<YiUserInfo?>() {
                    override fun onError(errorMsg: String) {
                        canScan = true
                        toast(errorMsg ?: "")
                    }

                    override fun onSuccess(t: YiUserInfo?) {
                        if (t != null && !t.row.isNullOrEmpty()) {
                            userInfo = t.row[0]
                            queryData(scanStr)

                        } else {
                            showLong("获取用户信息失败")
                            canScan = true
                        }
                    }
                })

    }

    private fun queryData(scanStr: String) {

        if (checkTag == "put") {
            queryPut(scanStr)
        } else {
            queryMark(scanStr)
        }
    }

    private fun queryMark(scanStr: String) {
        layoutScan.setViewVisible(false)
        layoutQueryResult.setViewVisible(true)
        layoutDetail.setViewVisible(false)
        layoutCategory.setViewVisible(false)
        llReservation.setViewVisible(false)
        tvQueryResult.text = "用户:    ${userInfo?.fullname ?: ""}\n碳分:    ${userInfo?.jifen ?: "0"}分"
        recorderTime = System.currentTimeMillis()
        btnQueryDetail.setClickListener {
            queryMarkDetail()
        }


    }

    private fun queryMarkDetail() {
        RxHttpUtils.createApi(ApiService::class.java)
                .queryMarkDetail(userInfo?.id ?: "", "1")
                .compose(Transformer.switchSchedulers())
                .subscribe(object : CommonObserver<YiMarkDetailBean?>() {
                    override fun onError(errorMsg: String) {
                        toast(errorMsg ?: "")
                        recorderTime = System.currentTimeMillis()
                    }

                    override fun onSuccess(t: YiMarkDetailBean?) {
                        if (t != null && !t.row.isNullOrEmpty()) {
                            recorderTime = System.currentTimeMillis()
                            layoutScan.setViewVisible(false)
                            layoutQueryResult.setViewVisible(false)
                            layoutDetail.setViewVisible(true)
                            layoutCategory.setViewVisible(false)
                            llReservation.setViewVisible(false)
                            detailAdapter.replaceData(t.row)
                            ivBack.setClickListener {
                                layoutScan.setViewVisible(true)
                                layoutQueryResult.setViewVisible(false)
                                layoutDetail.setViewVisible(false)
                                layoutCategory.setViewVisible(false)
                                llReservation.setViewVisible(false)
                            }
                        } else {
                            showLong("暂时没有记录")
                        }
                    }
                })

    }

    private fun queryPut(scanStr: String) {

        RxHttpUtils.createApi(ApiService::class.java)
                .queryPut(scanStr)
                .compose(Transformer.switchSchedulers())
                .subscribe(object : CommonObserver<YiPutBean?>() {
                    override fun onError(errorMsg: String) {
                        recorderTime = System.currentTimeMillis()
                    }

                    override fun onSuccess(t: YiPutBean?) {
                        if (t != null) {
                            recorderTime = System.currentTimeMillis()
                            layoutScan.setViewVisible(false)
                            layoutQueryResult.setViewVisible(false)
                            layoutDetail.setViewVisible(false)
                            layoutCategory.setViewVisible(true)
                            llReservation.setViewVisible(false)
                            reservationAdapter.replaceData(t.row)
                            reservationAdapter.setOnItemClickListener { adapter, view, position ->
                                val rowBean = adapter.data[position] as YiPutBean.RowBean

                                reservationRow(scanStr, rowBean)
                            }
                        }
                    }
                })

    }

    private fun reservationRow(scanStr: String, rowBean: YiPutBean.RowBean) {
        RxHttpUtils.createApi(ApiService::class.java)
                .reservationRow(userInfo?.fullname ?: "",
                        userInfo?.phone ?: "",
                        userInfo?.id ?: "",
                        rowBean.id, rowBean.classname, userInfo?.items ?: "")
                .compose(Transformer.switchSchedulers())
                .subscribe(object : CommonObserver<Any?>() {
                    override fun onError(errorMsg: String) {
                        recorderTime = System.currentTimeMillis()
                    }

                    override fun onSuccess(t: Any?) {
                        recorderTime = System.currentTimeMillis()
                        layoutScan.setViewVisible(false)
                        layoutQueryResult.setViewVisible(false)
                        layoutDetail.setViewVisible(false)
                        layoutCategory.setViewVisible(false)
                        llReservation.setViewVisible(true)
                        MediaPlayerHelper.getInstance(context).startPlay(R.raw.booking_success)
                        Handler().postDelayed({
                            canScan = true
                            layoutScan.setViewVisible(true)
                            layoutQueryResult.setViewVisible(false)
                            layoutDetail.setViewVisible(false)
                            layoutCategory.setViewVisible(false)
                            llReservation.setViewVisible(false)
                        }, 9000)

                    }
                })

    }

    private fun setdelay() {

        if (disposable == null) {
            disposable = Observable.interval(5, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        if (System.currentTimeMillis() - recorderTime > 60000L) {
                            restartSelf()
                        }
                    }
        }

    }

    private fun restartSelf() {
        if (mActivity!=null && isAdded){
            val intent = Intent(mActivity, YiMainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            mActivity?.finish()
            mActivity?.overridePendingTransition(0, 0)
        }

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            hide()
        } else {
            show()
        }
    }

    private fun show(){
        isResume = true;
        initView()
    }
    private fun hide() {
        isResume = false;
        etScan.removeTextChangedListener(watcher)
        etScan.setText("")
        etScan.isEnabled = false
        etScan.inputType = 0
        canScan = false
        MediaPlayerHelper.getInstance(context).release()
        if (disposable != null) {
            if (!disposable!!.isDisposed) {
                disposable!!.dispose()
                disposable = null
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isResume = false;
    }
    companion object {

        const val TAG = "tag"
        fun newInstance(tag: String): YiHandleFragment {
            val yiHandleFragment = YiHandleFragment()
            val bundle = Bundle()
            bundle.putString(TAG, tag)
            yiHandleFragment.arguments = bundle
            return yiHandleFragment
        }
    }
}