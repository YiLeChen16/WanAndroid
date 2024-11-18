package com.yl.wanandroid.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.NestedScrollingParent
import androidx.core.view.NestedScrollingParentHelper


/**
 * @description: TODO
 * @author YL Chen
 * @date 2024/11/18 21:20
 * @version 1.0
 */
class MyNestedScrollParent(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs), NestedScrollingParent {

    private var parentHelper: NestedScrollingParentHelper = NestedScrollingParentHelper(this)

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        // 允许垂直滚动
        return nestedScrollAxes and View.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
        parentHelper.onNestedScrollAccepted(child, target, axes)
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        // 判断父视图是否已滑动到顶部（scrollY == 0）并且正在向下滑动（dy < 0）
        val canScrollUp = scrollY > 0
        val isAtTop = scrollY == 0

        if (dy < 0 && isAtTop) {
            // 如果父视图已经滑动到顶部并且用户正在向下拉，则交给子视图处理
            target.dispatchNestedPreScroll(dx, dy, consumed, null)
        } else {
            // 否则，父视图继续处理滑动
            consumed[1] = dy
            scrollBy(0, dy)
        }
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        // 如果有未消耗的滑动事件，交给子视图处理
        if (dyUnconsumed != 0) {
            target.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, null)
        }
    }
}
