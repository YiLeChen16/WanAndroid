package com.yl.wanandroid.ui.custom

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * @description: TODO
 * @author YL Chen
 * @date 2025/2/9 19:08
 * @version 1.0
 */
class NoScrollLayoutManager(context: Context) : LinearLayoutManager(context) {
    override fun canScrollVertically(): Boolean {
        return false  // 禁止竖直方向滚动
    }

    override fun canScrollHorizontally(): Boolean {
        return false  // 禁止水平方向滚动
    }
}
