package com.golong.commlib.util

import android.content.Context
import android.media.MediaScannerConnection
import android.view.View
import padd.qlckh.cn.tempad.view.IToast
import java.io.File
import java.nio.charset.Charset
import java.util.*


/**
 * Created by Snow on 2017/10/10.
 * Description:
 */
@JvmField
val TAG: String = "==="

/**
 * try catch  的exception 的日志输出tag
 */
@JvmField
val E_TAG = "E_TAG"


fun toast(message: CharSequence) {

    if (message.isEmpty()) {
        return
    }
    val config = IToast.Builder()
        .setTextSize(16)
        .setCornerRadius(8)
        .build()

    if (message.toString().length < 18) {
        IToast.showShort(message, config)
    } else {
        IToast.showLong(message, config)
    }
}



fun CharSequence.toUtf8(): String {
    return String(this.toString().toByteArray(), Charset.forName("UTF-8"))
}

/**
 * 判断字符串位是纯数字
 */
fun String.IsNum(): Boolean {
    return this.all {
        it.isDigit()
    }
}

/**
 * @param index 插入字符的索引
 * @param element 需要插入的
 * a b c  d  e  f  g
 */
fun String.insert(index: Int, element: String = "\n"): String {
    val buffer = StringBuilder()
    this.toCharArray().forEachIndexed { i, c ->
        if (index == i) {
            buffer.append(element)
        }
        buffer.append(c)
    }
    return buffer.toString()
}

fun View.setViewVisible(isVisibel: Boolean) {

    if (isVisibel) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun View.setViewInVisible(isInVisibel: Boolean) {
    if (isInVisibel) {
        this.visibility = View.INVISIBLE
    } else {
        this.visibility = View.VISIBLE
    }
}

/**
 * 限制view 点击事件每隔多长时间才能有效执行一次
 * @param interval 间隔的毫秒值
 */
fun View?.setClickListener(interval: Long = 2000, block: (view: View) -> Unit) {
    var lastTime: Long = 0
    if (this == null) return
    this.setOnClickListener {
        val currentTime = Calendar.getInstance().timeInMillis
        if (currentTime - lastTime >= interval) {
            lastTime = currentTime
            block(it)
        } else {
        }
    }
}

fun matchUrl(content: String): List<String> {
    val rg = Regex("src=\"([^\"]+)", RegexOption.IGNORE_CASE)
    val findAll = rg.findAll(content)
    val list = mutableListOf<String>()
    findAll.forEach {
        list.add(it.groupValues[1])
    }
    return list
}
