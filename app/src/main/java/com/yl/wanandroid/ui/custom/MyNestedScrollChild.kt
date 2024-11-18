package com.yl.wanandroid.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout
import androidx.core.view.NestedScrollingChild
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat


/**
 * @description: TODO
 * @author YL Chen
 * @date 2024/11/18 21:33
 * @version 1.0
 */
class MyNestedScrollChild(context: Context, attrs: AttributeSet):LinearLayout(context,attrs),NestedScrollingChild {
    private var mScrollingChildHelper: NestedScrollingChildHelper = NestedScrollingChildHelper(this)
    private val mScrollOffset = IntArray(2)
    private val mScrollConsumed = IntArray(2)
    private var mLastTouchY = 0
    private var mVisiableHeight = 0
    private var mFullHeight = 0
    private var mCanScrollY = 0

    init {
        mScrollingChildHelper.isNestedScrollingEnabled = true
    }

    override fun startNestedScroll(axes: Int): Boolean {
        return mScrollingChildHelper.startNestedScroll(axes)
    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?
    ): Boolean {
        return mScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastTouchY = (e.rawY + 0.5f).toInt()
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
            }

            MotionEvent.ACTION_MOVE -> {
                val y = (e.rawY + 0.5f).toInt()
                var dy = mLastTouchY - y
                mLastTouchY = y
                if (dispatchNestedPreScroll(0, dy, mScrollConsumed, mScrollOffset)) {
                    dy -= mScrollConsumed[1]
                }
                scrollBy(0, dy)
            }
        }
        return true
    }

    override fun scrollTo(x: Int, y: Int) {
        var y = y
        if (y > mCanScrollY) {
            y = mCanScrollY
        }
        if (y < 0) {
            y = 0
        }
        super.scrollTo(x, y)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec1 = heightMeasureSpec
        if (mVisiableHeight <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec1)
            mVisiableHeight = measuredHeight
        } else {
            heightMeasureSpec1 = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            super.onMeasure(widthMeasureSpec, heightMeasureSpec1)
            if (mFullHeight <= 0) {
                mFullHeight = measuredHeight
                mCanScrollY = mFullHeight - mVisiableHeight
            }
        }
    }
}