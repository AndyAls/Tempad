package padd.qlckh.cn.tempad.yipingfang

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import padd.qlckh.cn.tempad.R
import padd.qlckh.cn.tempad.http.utils.TimeUtil


/**
 * @author Andy
 * @date   2021/11/5 16:42
 * Desc:
 */
class DetailAdapter(datas: ArrayList<YiMarkDetailBean.RowBean>) : BaseQuickAdapter<YiMarkDetailBean.RowBean, BaseViewHolder>(
        R.layout.detail_list_item, datas
) {
    override fun convert(helper: BaseViewHolder?, rowBean: YiMarkDetailBean.RowBean?) {

        helper?.apply {
            setText(R.id.tvTime, TimeUtil.formatTime(rowBean!!.getAddtime(), TimeUtil.Y_M_D))
            setText(R.id.tvStatus,rowBean.title)
            setText(R.id.tvJf,"+${rowBean.num}")
        }
    }
}