package padd.qlckh.cn.tempad.yipingfang

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.InputFilter
import android.text.Spanned
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import com.golong.commlib.util.setClickListener
import com.golong.commlib.util.toast
import padd.qlckh.cn.tempad.R

/**
 * @author Andy
 * @date   2021/12/4 11:16
 * Desc:
 */
object DialogUtils {

    fun showEditDialog(
            context: Activity,
            title: String?,
            content: String?,
            sureText: String?,
            cancleText: String?,
            listener: OnDialogClickListener?,
            ifCancleView: Boolean = true,
            isCancelable: Boolean = false,
            contentColor: Int = Color.parseColor("#333333"),
            sureTextColor: Int = context.resources.getColor(R.color.colorPrimary),
            cancleTextColor: Int = Color.parseColor("#666666")
    ): Dialog? {

        val dialog = Dialog(context)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        dialog.window!!.setBackgroundDrawableResource(R.drawable.bg_pic_save)
        dialog.setCancelable(isCancelable)
        dialog.setCanceledOnTouchOutside(isCancelable)
        dialog.setContentView(R.layout.dialog_edittext)
        val attributes = dialog.window!!.attributes
        val windowManager = context.windowManager
        val display = windowManager.defaultDisplay
        attributes.width = (display.width * 0.5).toInt()
        attributes.dimAmount = 0.5f
        dialog.window!!.attributes = attributes
        val tvContent = dialog.window!!.findViewById<TextView>(R.id.etv_content) as EditText
        val tvSure = dialog.window!!.findViewById<TextView>(R.id.tv_sure)
        val tvTitle = dialog.window!!.findViewById<TextView>(R.id.tv_title)
        val tvCancle = dialog.window!!.findViewById<TextView>(R.id.tv_cancel)
        tvContent.setTextColor(contentColor)
        tvCancle.setTextColor(cancleTextColor)
        tvSure.setTextColor(sureTextColor)

        val view = dialog.window!!.findViewById<View>(R.id.view)
        if (title != null && "" != title.trim { it <= ' ' }) {
            tvTitle.text = title
        }
        if (content != null && "" != content.trim { it <= ' ' }) {
            tvContent.setText(content)
            tvContent.setSelection(content.length)
        }
        if (sureText != null && "" != sureText.trim { it <= ' ' }) {
            tvSure.text = sureText
        }
        if (cancleText != null && "" != cancleText.trim { it <= ' ' }) {
            tvCancle.text = cancleText
        }
        if (ifCancleView) {
            tvCancle.visibility = View.VISIBLE
            view.visibility = View.VISIBLE
        } else {
            tvCancle.visibility = View.GONE
            view.visibility = View.GONE
        }

        tvCancle.setClickListener { v ->
            listener?.onCancleClick()
            dialog.dismiss()
        }
        tvSure.setClickListener { v ->

            listener?.onSureClick(tvContent.text.toString().trim())
            dialog.dismiss()
        }
        if (context.isFinishing) {
            return null
        }
        if (dialog.isShowing) {
            dialog.dismiss()
        }
        dialog.show()
        return dialog
    }

}