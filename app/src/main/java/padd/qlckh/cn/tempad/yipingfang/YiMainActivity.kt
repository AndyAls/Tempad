package padd.qlckh.cn.tempad.yipingfang

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.view.KeyEvent
import com.golong.commlib.util.setClickListener
import com.golong.commlib.util.setViewVisible
import kotlinx.android.synthetic.main.activity_main_yi_daping.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import padd.qlckh.cn.tempad.ApiService
import padd.qlckh.cn.tempad.BaseActivity
import padd.qlckh.cn.tempad.ConvertUtils
import padd.qlckh.cn.tempad.JsonUtil
import padd.qlckh.cn.tempad.R
import padd.qlckh.cn.tempad.SettingActivity
import padd.qlckh.cn.tempad.http.RxHttpUtils
import padd.qlckh.cn.tempad.http.interceptor.Transformer
import padd.qlckh.cn.tempad.http.observer.CommonObserver
import padd.qlckh.cn.tempad.http.utils.AppUtils


/**
 * @author Andy
 * @date   2021/11/5 17:03
 * @link   {http://blog.csdn.net/andy_l1}
 * Desc:    YiMainActivity.kt
 */
class YiMainActivity : BaseActivity() {

    private var checkedFragmentTag = "home"
    private var canGoHome = true

    override fun initView() {
        try {
            YiUtils.setNavigationBarVisible(this, true)
        } catch (e: Exception) {
        }
        EventBus.getDefault().register(this)
        llBoli.setClickListener {
            /*  test();
              return@setClickListener*/
            viewClick(BOLI)
        }
        llDianChi.setClickListener {
            viewClick(ZHIZHANG)
        }
        llJishu.setClickListener {
            viewClick(JINSHU)
        }
        llSuliao.setClickListener {
            viewClick(SULIAO)
        }
        ivSetting.setClickListener {

            startActivity(Intent(this, SettingActivity::class.java))
        }

        ivHome.setClickListener {
            goHome()

        }

        btnCountry.setClickListener {
            if (canGoHome) {
                h5Click("country")
                btnCountry.isSelected = true
                btnVedio.isSelected = false
                btnQuery.isSelected = false
                btnPut.isSelected = false
            }
        }
        btnVedio.setClickListener {
            if (canGoHome) {
                h5Click("vedio")
                btnCountry.isSelected = false
                btnVedio.isSelected = true
                btnQuery.isSelected = false
                btnPut.isSelected = false
            }
        }
        btnPut.setClickListener {
            if (canGoHome) {
                btnCountry.isSelected = false
                btnVedio.isSelected = false
                btnQuery.isSelected = false
                btnPut.isSelected = true
                btnClick("put")
            }
        }
        btnQuery.setClickListener {
            if (canGoHome) {
                btnCountry.isSelected = false
                btnVedio.isSelected = false
                btnQuery.isSelected = true
                btnPut.isSelected = false
                btnClick("query")
            }

        }


        ivIcon.setOnLongClickListener {

            DialogUtils.showEditDialog(this, "管理员模式", "", "确认", "取消", object : OnDialogClickListener {
                override fun onSureClick(psw: String) {

                    when (psw) {
                        "666888" -> {
                            finish()
                        }
                        "888666" -> {
                            startActivity(Intent(this@YiMainActivity, YiPannelActivity::class.java))
                        }
                        else -> {
                            showLong("请输入正确的密码退出应用")
                        }
                    }
                }

                override fun onCancleClick() {

                }


            })
            false
        }

    }

    private fun test() {
        println("------------------")
        val open = ConvertUtils.json2Bytes(YiConstant.OPEN_BOLI)
        println(open.asList())
        val json = ConvertUtils.bytes2Json(open)
        println(json)
        val jsonValid = JsonUtil.isJsonValid(YiConstant.OPEN_BOLI)
        val jsonValid1 = JsonUtil.isJsonValid(json)
        println("$jsonValid ---> $jsonValid1")

        val orderReply = JsonUtil.json2Object2("{\"r\":8001,\"d\":{\"bin_no\":1,\"code\":1}}", OrderReply::class.java)
        println(orderReply)


    }

    fun canGoHome(goHome: Boolean) {
        this.canGoHome = goHome
    }

    private fun btnClick(tag: String) {
        if (canGoHome) {
            llBoli.setBackgroundResource(0)
            llSuliao.setBackgroundResource(0)
            llJishu.setBackgroundResource(0)
            llDianChi.setBackgroundResource(0)
            var yiHandFragment: YiHandleFragment? = null
            if (supportFragmentManager.findFragmentByTag(tag) == null) {
                yiHandFragment = YiHandleFragment.newInstance(tag)
            } else {
                yiHandFragment = supportFragmentManager.findFragmentByTag(tag) as YiHandleFragment?
            }
            switchContent(yiHandFragment, tag)
        }
    }

    private var createDialog: AlertDialog? = null
    var receiver = NetWorkStateReceiver()
    val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
    /* override fun onResume() {
         super.onResume()
         try {
             val hasNetwork = NetUtils.hasNetwork(this)
             ivNet.setImageResource(if (hasNetwork) R.drawable.ic_good_net else R.drawable.ic_bad_net)
             if (createDialog != null && createDialog!!.isShowing) {
                 createDialog!!.dismiss()
                 createDialog = null
             }
             if (!hasNetwork) {
                 if (createDialog == null) {
                     createDialog = createDialog("当前网络状态不可用,影响应用正常使用")
                     createDialog?.show()
                 }
             }
             canGoHome = hasNetwork
         } catch (e: Exception) {
         }
     }
*/

    private fun goHome() {
        if (canGoHome) {
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
                ft.hide(supportFragmentManager.findFragmentByTag(this.checkedFragmentTag)!!).add(R.id.fl_frame, to, tag)
                    .commitAllowingStateLoss()
                fm.executePendingTransactions()
            }

        }
        val b = to is YiH5Fragment
        if (!b || to == supportFragmentManager.findFragmentByTag("country")) {
            val vedioFragment = supportFragmentManager.findFragmentByTag("vedio")
            if (vedioFragment != null) {
                supportFragmentManager.beginTransaction().remove(vedioFragment).commitNowAllowingStateLoss()
            }
        }
        if (!b || to == supportFragmentManager.findFragmentByTag("vedio")) {
            val countryFragment = supportFragmentManager.findFragmentByTag("country")
            if (countryFragment != null) {
                supportFragmentManager.beginTransaction().remove(countryFragment).commitNowAllowingStateLoss()
            }
        }
        this.checkedFragmentTag = tag
    }

    fun viewClick(tag: String) {
        if (canGoHome) {
            resetBtn()
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

                ZHIZHANG -> {
                    llBoli.setBackgroundResource(0)
                    llSuliao.setBackgroundResource(0)
                    llJishu.setBackgroundResource(0)
                    llDianChi.setBackgroundResource(R.drawable.code_perform)
                }
            }
            var yiScanFragment: YiScanFragment? = null
            yiScanFragment = if (supportFragmentManager.findFragmentByTag(tag) == null) {
                YiScanFragment.newInstance(tag)
            } else {
                supportFragmentManager.findFragmentByTag(tag) as YiScanFragment?
            }

            switchContent(yiScanFragment, tag)
        }

    }


    private fun h5Click(tag: String) {
        if (canGoHome) {
            llBoli.setBackgroundResource(0)
            llSuliao.setBackgroundResource(0)
            llJishu.setBackgroundResource(0)
            llDianChi.setBackgroundResource(0)
            var yiH5Fragment: YiH5Fragment? = null
            yiH5Fragment = if (supportFragmentManager.findFragmentByTag(tag) == null) {
                YiH5Fragment.newInstance(if (tag == "vedio") "http://a.365igc.cn/ypf/view/dist/index.html#/view" else "http://a.365igc.cn/ypf/view/dist/index.html#/picture")
            } else {
                supportFragmentManager.findFragmentByTag(tag) as YiH5Fragment?
            }
            switchContent(yiH5Fragment, tag)
        }


    }

    private fun resetBtn() {
        btnCountry.isSelected = false
        btnVedio.isSelected = false
        btnQuery.isSelected = false
        btnPut.isSelected = false
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return true
    }

    override fun initDate() {
        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.add(R.id.fl_frame, YiHomeFragment.newInstance(), "home")
        val commit = beginTransaction.commit()
        registerReceiver(receiver, intentFilter)
        addCode()
    }

    private fun addCode() {
        RxHttpUtils.createApi(ApiService::class.java)
            .addCode(AppUtils.getDeviceId(this))
            .compose(Transformer.switchSchedulers())
            .subscribe(object : CommonObserver<AddCodeResq?>() {
                override fun onError(errorMsg: String) {
                }

                override fun onSuccess(t: AddCodeResq?) {
                    if (t != null && t.row != null) {
                        val row = t.row
                        if (row.status == "1") {
                            llDianChi.isEnabled = false
                            llJishu.isEnabled = false
                            llSuliao.isEnabled = false
                            llBoli.isEnabled = false
                            ivHome.isEnabled = false
                            btnCountry.isEnabled = false
                            btnVedio.isEnabled = false
                            btnQuery.isEnabled = false
                            btnPut.isEnabled = false
                            showDialog("改设备不可用,请联系客服")
                            return
                        }
                    }
                    llDianChi.isEnabled = true
                    llJishu.isEnabled = true
                    llSuliao.isEnabled = true
                    llBoli.isEnabled = true
                    ivHome.isEnabled = true
                    btnCountry.isEnabled = true
                    btnVedio.isEnabled = true
                    btnQuery.isEnabled = true
                    btnPut.isEnabled = true

                }
            })
    }

    override fun showError(msg: String?) {
    }

    override fun release() {
//        RxHttpUtils.cancelAllRequest()
        unregisterReceiver(receiver)
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun netWorkState(stateEvent: NetWorkStateEvent?) {
//        onResume()
    }

    override fun getContentView(): Int {

        return R.layout.activity_main_yi
    }

    companion object {

        const val ZHIZHANG = "dianchi"
        const val BOLI = "boli"
        const val JINSHU = "jinshu"
        const val SULIAO = "suliao"
    }
}