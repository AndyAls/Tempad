package padd.qlckh.cn.tempad.yipingfang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.scan_fragment_yi.*
import padd.qlckh.cn.tempad.BaseFragment
import padd.qlckh.cn.tempad.R

/**
 * @author Andy
 * @date   2020/11/25 15:28
 * Desc:
 */
class YiScanFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.scan_fragment_yi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
    }

    private fun initData() {
        val s = arguments?.getString("tag") ?: "error"
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser ) {
            val s = arguments?.getString("tag") ?: "error"
        }
    }

    companion object {

        fun newInstance(tag: String): YiScanFragment {
            val yiScanFragment = YiScanFragment()
            val bundle = Bundle()
            bundle.putString("tag", tag)
            yiScanFragment.arguments = bundle
            return yiScanFragment
        }
    }

}