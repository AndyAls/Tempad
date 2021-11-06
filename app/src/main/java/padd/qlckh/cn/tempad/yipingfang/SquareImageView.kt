package padd.qlckh.cn.tempad.yipingfang

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

/**
 * @author Andy
 * @date   2021/11/5 17:02
 * @link   {http://blog.csdn.net/andy_l1}
 * Desc:    SquareImageView.kt
 */
class SquareImageView:ImageView{
    constructor(context: Context):super(context)
    constructor(context: Context,attributes: AttributeSet):super(context,attributes)
    constructor(context: Context,attributes: AttributeSet,defStyle:Int):super(context,attributes,defStyle)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}