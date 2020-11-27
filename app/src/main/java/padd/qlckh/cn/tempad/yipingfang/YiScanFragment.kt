package padd.qlckh.cn.tempad.yipingfang

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.golong.commlib.util.setClickListener
import com.golong.commlib.util.toast
import kotlinx.android.synthetic.main.scan_fragment_yi.*
import padd.qlckh.cn.tempad.*
import padd.qlckh.cn.tempad.manager.OnOpenSerialPortListener
import padd.qlckh.cn.tempad.manager.OnSerialPortDataListener
import java.io.File

/**
 * @author Andy
 * @date   2020/11/25 15:28
 * Desc:
 */
class YiScanFragment : BaseFragment() {

    private val TAG = "YiScanFragment"
    private var tagCheck = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.scan_fragment_yi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        initData()
        setSerialListener()
    }

    private fun initListener() {
        ivScanCode.setClickListener {
            toast(tagCheck + "开门")
            openTrash()
        }

        tvScanHint.setClickListener {

            Handler().postDelayed({
                toast(tagCheck + "关门")
                closeTrash()
            }, 1500)
        }

    }

    var handler = Handler {
        val what = it.what

        when (what) {
            PANNEL_WHAT -> {
                handPanel(it.obj as ByteArray)
            }
        }
        false
    }


    private fun openTrash() {

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
    }

    private fun closeTrash() {

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

    /**
     * 处理推拉杠
     */
    private fun handPanel(bytes: ByteArray) {

        tvScanHint.text = ConvertUtils.bytes2HexString(bytes)
    }

    private fun setSerialListener() {

        mPanelManager.setOnOpenSerialPortListener(object : OnOpenSerialPortListener {
            override fun onSuccess(device: File?) {
                tvScanHint.text="串口连接成功"+device?.toString()
            }

            override fun onFail(device: File?, status: OnOpenSerialPortListener.Status?) {
                tvScanHint.text="串口连接失败"+status?.name
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
    }

    private fun initView() {

        etScan.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun initData() {
        tagCheck = arguments?.getString("tag") ?: "error"
        tvDumpType.text = when (tagCheck) {
            YiMainActivity.DIANCHI -> {

                ivDump.setImageResource(R.drawable.ds)
                "电池"
            }

            YiMainActivity.BOLI -> {
                ivDump.setImageResource(R.drawable.bl)
                "玻璃"
            }
            YiMainActivity.JINSHU -> {
                ivDump.setImageResource(R.drawable.js)
                "金属"
            }
            YiMainActivity.SULIAO -> {
                ivDump.setImageResource(R.drawable.sl)
                "塑料"
            }
            else -> {
                ivDump.setImageResource(R.drawable.ds)
                "电池"
            }
        }

    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
    }

    override fun onDestroyView() {
        super.onDestroyView()
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