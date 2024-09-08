package com.yl.wanandroid.utils

import android.content.Context

/**
 * 尺寸转化工具类
 */
object SizeUtils {
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale: Float = context.getResources().getDisplayMetrics().density
        return (dpValue * scale + 0.5f).toInt()
    }
}