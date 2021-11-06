package padd.qlckh.cn.tempad.yipingfang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_h5_yi.*
import padd.qlckh.cn.tempad.BaseFragment
import padd.qlckh.cn.tempad.R

/**
 * @author Andy
 * @date   2021/11/4 14:53
 * Desc:
 */
class YiH5Fragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_h5_yi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initWeb()
    }

    private fun initWeb() {

        val url = arguments?.getString(URL) ?: ""
        webview2.loadUrl(url)
    }

    override fun onPause() {
        super.onPause()
        webview2.onPause()
        webview2.pauseTimers()
    }

    override fun onResume() {
        super.onResume()
        webview2.onResume()
        webview2.resumeTimers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        webview2.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
        webview2.clearCache(true)
        webview2.clearHistory()
        val parent = webview2.parent as ViewGroup?
        if (parent != null) {
            parent.removeView(webview2)
            webview2.destroy()
        }

    }

    companion object {
        const val URL = "url"
        fun newInstance(url: String): YiH5Fragment {
            val yiH5Fragment = YiH5Fragment()
            val bundle = Bundle()
            bundle.putString(URL, url)
            yiH5Fragment.arguments = bundle
            return yiH5Fragment
        }
    }
}