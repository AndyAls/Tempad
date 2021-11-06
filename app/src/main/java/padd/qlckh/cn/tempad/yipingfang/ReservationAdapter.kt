package padd.qlckh.cn.tempad.yipingfang

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import padd.qlckh.cn.tempad.ApiService
import padd.qlckh.cn.tempad.R

/**
 * @author Andy
 * @date 2021/11/5 16:59
 * Desc:
 */
class ReservationAdapter(datas: ArrayList<YiPutBean.RowBean>)
    : BaseQuickAdapter<YiPutBean.RowBean, BaseViewHolder>(R.layout.reservaion_list_item, datas) {
    override fun convert(helper: BaseViewHolder?, item: YiPutBean.RowBean?) {

        helper!!.setText(R.id.tvCategroy, item!!.classname)
        Glide.with(mContext)
                .load(ApiService.IMG_URL + item.img)
                .into(helper.getView(R.id.img))
    }

}