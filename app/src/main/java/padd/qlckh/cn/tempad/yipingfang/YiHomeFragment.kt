package padd.qlckh.cn.tempad.yipingfang

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.golong.commlib.util.setClickListener
import kotlinx.android.synthetic.main.home_fragment_yi.*
import padd.qlckh.cn.tempad.BaseFragment
import padd.qlckh.cn.tempad.R

/**
 * @author Andy
 * @date   2020/11/25 13:28
 * Desc:
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
            viewClick(YiMainActivity.DIANCHI)

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