package padd.qlckh.cn.tempad.yipingfang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.golong.commlib.util.setClickListener
import kotlinx.android.synthetic.main.home_fragment_yi.*
import padd.qlckh.cn.tempad.BaseFragment
import padd.qlckh.cn.tempad.R

/**
 * @author Andy
 * @date   2021/11/5 17:03
 * @link   {http://blog.csdn.net/andy_l1}
 * Desc:    YiHomeFragment.kt
 */
class YiHomeFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment_yi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() {

        rlBoli.setClickListener {
            viewClick(YiMainActivity.BOLI)
        }
        rlDianchi.setClickListener {
            viewClick(YiMainActivity.ZHIZHANG)

        }
        rlJinshu.setClickListener {
            viewClick(YiMainActivity.JINSHU)
        }
        rlSuliao.setClickListener {
            viewClick(YiMainActivity.SULIAO)
        }
    }

    fun viewClick(tag: String) {
        val yiMainActivity = activity as YiMainActivity
        yiMainActivity.viewClick(tag)

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        val yiMainActivity = activity as YiMainActivity
        yiMainActivity.setHomeBtnVisible(hidden)

    }

    companion object {

        fun newInstance(): YiHomeFragment {

            return YiHomeFragment()
        }
    }


}