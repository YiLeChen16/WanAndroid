package com.yl.wanandroid.utils

import android.graphics.drawable.ColorDrawable
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.yl.wanandroid.base.BaseApplication

/**
 * @author mingyan.su
 * @date   2023/3/20 7:39
 * @desc   资源获取工具类
 */
fun getStringArrayFromResource(@ArrayRes arrayId: Int): Array<String> {
    return BaseApplication.context.resources.getStringArray(arrayId)
}

fun getStringFromResource(@StringRes stringId: Int): String {
    return BaseApplication.context.getString(stringId)
}

fun getStringFromResource(@StringRes stringId: Int, vararg formatArgs: Any?): String? {
    return BaseApplication.context.getString(stringId, *formatArgs)
}

fun getColorFromResource(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(BaseApplication.context, colorRes)
}

fun getDimensionFromResource(@DimenRes dimenRes: Int): Int {
    return BaseApplication.context.resources
            .getDimensionPixelSize(dimenRes)
}

fun getColorDrawable(@ColorRes colorRes: Int): ColorDrawable? {
    return ColorDrawable(
        ContextCompat.getColor(
            BaseApplication.context,
            colorRes
        )
    )
}