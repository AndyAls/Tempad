package padd.qlckh.cn.tempad.yipingfang

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import com.golong.commlib.util.setClickListener
import com.golong.commlib.util.setViewVisible
import kotlinx.android.synthetic.main.activity_main_yi.*
import padd.qlckh.cn.tempad.BaseActivity
import padd.qlckh.cn.tempad.R
import padd.qlckh.cn.tempad.SettingActivity


/**
 * @author Andy
 * @date   2020/11/24 17:11
 * Desc:
 */
class YiMainActivity : BaseActivity() {

    private var checkedFragmentTag = "home"
    override fun initView() {
        llBoli.setClickListener {
            viewClick(BOLI)
        }
        llDianChi.setClickListener {
            viewClick(DIANCHI)
        }
        llJishu.setClickListener {
            viewClick(JINSHU)
        }
        llSuliao.setClickListener {
            viewClick(SULIAO)
        }
        ivSetting.setClickListener {

            startActivity(Intent(this,SettingActivity::class.java))
        }

        ivHome.setClickListener {
            llBoli.setBackgroundResource(0)
            llSuliao.setBackgroundResource(0)
            llJishu.setBackgroundResource(0)
            llDianChi.setBackgroundResource(0)

            var homeFragment: YiHomeFragment? = null
            if (supportFragmentManager.findFragmentByTag("home") == null) {
                homeFragment = YiHomeFragment.newInstance()
            } else {
                homeFragment = supportFragmentManager.findFragmentByTag("home") as YiHomeFragment?
            }
            switchContent(homeFragment, "home")

        }

    }

    fun setHomeBtnVisible(isHide: Boolean) {
        ivHome.setViewVisible(isHide)
    }

    fun switchContent(to: Fragment?, tag: String) {
        if (to == null) {
        } else if (supportFragmentManager.findFragmentByTag(this.checkedFragmentTag) == null) {
        } else if (supportFragmentManager.findFragmentByTag(this.checkedFragmentTag) !== to) {
            val fm: FragmentManager = supportFragmentManager
            val ft: FragmentTransaction = fm.beginTransaction()
            if (to.isAdded) {
                ft.hide(supportFragmentManager.findFragmentByTag(this.checkedFragmentTag)!!).show(to).commitAllowingStateLoss()
                fm.executePendingTransactions()
            } else {
                ft.hide(supportFragmentManager.findFragmentByTag(this.checkedFragmentTag)!!).add(R.id.fl_frame, to, tag).commitAllowingStateLoss()
                fm.executePendingTransactions()
            }

        }
        this.checkedFragmentTag = tag
    }

    fun viewClick(tag: String) {

        when (tag) {
            BOLI -> {

                llBoli.setBackgroundResource(R.drawable.code_perform)
                llSuliao.setBackgroundResource(0)
                llJishu.setBackgroundResource(0)
                llDianChi.setBackgroundResource(0)

            }
            JINSHU -> {
                llBoli.setBackgroundResource(0)
                llSuliao.setBackgroundResource(0)
                llJishu.setBackgroundResource(R.drawable.code_perform)
                llDianChi.setBackgroundResource(0)
            }
            SULIAO -> {
                llBoli.setBackgroundResource(0)
                llSuliao.setBackgroundResource(R.drawable.code_perform)
                llJishu.setBackgroundResource(0)
                llDianChi.setBackgroundResource(0)

            }
            DIANCHI -> {
                llBoli.setBackgroundResource(0)
                llSuliao.setBackgroundResource(0)
                llJishu.setBackgroundResource(0)
                llDianChi.setBackgroundResource(R.drawable.code_perform)
            }
        }
        var yiScanFragment: YiScanFragment? = null
        if (supportFragmentManager.findFragmentByTag(tag) == null) {
            yiScanFragment = YiScanFragment.newInstance(tag)
        } else {
            yiScanFragment = supportFragmentManager.findFragmentByTag(tag) as YiScanFragment?
        }

        switchContent(yiScanFragment, tag)
    }

    override fun initDate() {
        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.add(R.id.fl_frame, YiHomeFragment.newInstance(), "home")
        beginTransaction.commit()
    }

    override fun showError(msg: String?) {
    }

    override fun release() {
    }

    override fun getContentView(): Int {

        return R.layout.activity_main_yi
    }

    companion object {

        const val DIANCHI = "dianchi"
        const val BOLI = "boli"
        const val JINSHU = "jinshu"
        const val SULIAO = "suliao"
    }
}