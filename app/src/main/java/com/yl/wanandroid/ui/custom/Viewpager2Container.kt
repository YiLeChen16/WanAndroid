package com.yl.wanandroid.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.RelativeLayout
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

/**
 * @description: Viewpager2包裹类，用于处理ViewPager2嵌套使用导致的滑动冲突
 * 不直接继承ViewPager2再重新其中的拦截方法解决，是因为ViewPager2是final的
 * @author YL Chen
 * @date 2024/11/21 20:28
 * @version 1.0
 */
//@JvmOverloads 是一个 Kotlin 注解，用来为具有默认参数的 Kotlin 函数自动生成多个 Java 方法重载版本
//使得具有默认值的参数的方法在java中可以被调用
class ViewPager2Container @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {

    private var mViewPager2: ViewPager2? = null//存储在容器中找到的 ViewPager2 实例
    private var disallowParentInterceptDownEvent = true//用来控制是否允许父视图拦截触摸事件
    //录触摸事件开始时的坐标，用于判断滑动方向和距离。
    private var startX = 0
    private var startY = 0

    //该方法会在视图的布局完成后调用，它遍历容器的子视图，并寻找其中的 ViewPager2 实例。如果没有找到 ViewPager2，则抛出异常
    override fun onFinishInflate() {
        super.onFinishInflate()
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            if (childView is ViewPager2) {
                mViewPager2 = childView
                break
            }
        }
        if (mViewPager2 == null) {
            throw IllegalStateException("The root child of ViewPager2Container must contains a ViewPager2")
        }
    }

    //该方法用于拦截触摸事件。根据 ViewPager2 的状态，判断是否需要拦截事件。
    //如果 ViewPager2 的 isUserInputEnabled 为 false 或者它的适配器项数小于或等于1，则不进行拦截，直接传递给父视图。
    //如果需要拦截事件，根据滑动方向（水平或垂直）处理不同的事件逻辑。
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val doNotNeedIntercept = (!mViewPager2!!.isUserInputEnabled
                || (mViewPager2?.adapter != null
                && mViewPager2?.adapter!!.itemCount <= 1))
        if (doNotNeedIntercept) {
            return super.onInterceptTouchEvent(ev)
        }
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = ev.x.toInt()
                startY = ev.y.toInt()
                parent.requestDisallowInterceptTouchEvent(!disallowParentInterceptDownEvent)
            }
            MotionEvent.ACTION_MOVE -> {
                val endX = ev.x.toInt()
                val endY = ev.y.toInt()
                val disX = abs(endX - startX)
                val disY = abs(endY - startY)
                if (mViewPager2!!.orientation == ViewPager2.ORIENTATION_VERTICAL) {
                    onVerticalActionMove(endY, disX, disY)
                } else if (mViewPager2!!.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                    onHorizontalActionMove(endX, disX, disY)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> parent.requestDisallowInterceptTouchEvent(false)
        }
        return super.onInterceptTouchEvent(ev)
    }

    //处理横向滑动模式的ViewPager2的滑动事件
    private fun onHorizontalActionMove(endX: Int, disX: Int, disY: Int) {
        if (mViewPager2?.adapter == null) {
            return
        }
        if (disX > disY) {
            val currentItem = mViewPager2?.currentItem
            val itemCount = mViewPager2?.adapter!!.itemCount
            if (currentItem == 0 && endX - startX > 0) {
                parent.requestDisallowInterceptTouchEvent(false)//让父View处理滑动
            } else {
                //currentItem != 0(当前ViewPager2处于中间项或最后一项) || endX - startX <= 0（纵向滑动）
                //若当前ViewPager2中的子View是最后一个，让父View处理滑动
                //若当前不是横向滑动，由父View处理滑动
                parent.requestDisallowInterceptTouchEvent(currentItem != itemCount - 1
                        || endX - startX >= 0)
            }
        } else if (disY > disX) {
            //纵向滑动，由父View处理滑动
            parent.requestDisallowInterceptTouchEvent(false)
        }
    }

    //处理纵向滑动模式的ViewPager2的滑动事件
    private fun onVerticalActionMove(endY: Int, disX: Int, disY: Int) {
        if (mViewPager2?.adapter == null) {
            return
        }
        val currentItem = mViewPager2?.currentItem
        val itemCount = mViewPager2?.adapter!!.itemCount
        if (disY > disX) {
            if (currentItem == 0 && endY - startY > 0) {
                //当前ViewPager2的子View为第一个，且向下滑动
                parent.requestDisallowInterceptTouchEvent(false)//由父View处理滑动事件
            } else {
                //currentItem > 0(当前ViewPager2的子View为中间或末尾项) || endY - startY <= 0（横向滑动）
                //当前ViewPager2的子View为末尾项 或 横向滑动 时，由父View处理滑动事件
                parent.requestDisallowInterceptTouchEvent(currentItem != itemCount - 1
                        || endY - startY >= 0)
            }
        } else if (disX > disY) {
            //横向滑动，由父View处理滑动事件
            parent.requestDisallowInterceptTouchEvent(false)
        }
    }

    /**
     * 设置是否允许在当前View的{@link MotionEvent#ACTION_DOWN}事件中禁止父View对事件的拦截，该方法
     * 用于解决CoordinatorLayout+CollapsingToolbarLayout在嵌套ViewPager2Container时引起的滑动冲突问题。
     *
     * 设置是否允许在ViewPager2Container的{@link MotionEvent#ACTION_DOWN}事件中禁止父View对事件的拦截，该方法
     * 用于解决CoordinatorLayout+CollapsingToolbarLayout在嵌套ViewPager2Container时引起的滑动冲突问题。
     *
     * @param disallowParentInterceptDownEvent 是否允许ViewPager2Container在{@link MotionEvent#ACTION_DOWN}事件中禁止父View拦截事件，默认值为false
     *   true 不允许ViewPager2Container在{@link MotionEvent#ACTION_DOWN}时间中禁止父View的时间拦截，
     *   设置disallowIntercept为true可以解决CoordinatorLayout+CollapsingToolbarLayout的滑动冲突
     *   false 允许ViewPager2Container在{@link MotionEvent#ACTION_DOWN}时间中禁止父View的时间拦截，
     */
    fun disallowParentInterceptDownEvent(disallowParentInterceptDownEvent: Boolean) {
        this.disallowParentInterceptDownEvent = disallowParentInterceptDownEvent
    }
}