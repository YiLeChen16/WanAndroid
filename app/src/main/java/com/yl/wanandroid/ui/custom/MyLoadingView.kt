package com.yl.wanandroid.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.*
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.yl.wanandroid.R
import com.yl.wanandroid.utils.LogUtils
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

//自定义正在加载控件
class MyLoadingView(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs),
    LifecycleOwner {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs)

    private var mDegrees: Float = 10.0f//初始旋转角度

    //创建协程作用域
    val scope = CoroutineScope(Job() + Dispatchers.Main)

    private var job: Job? = null


    //init相当于调用了super
    init {
        //设置加载图片
        setImageResource(R.mipmap.loading)
    }

    override fun onDraw(canvas: Canvas) {
        //绕中心旋转图片，达到加载的效果(必须在super之前调用才有效果)
        LogUtils.d(this@MyLoadingView, "onDraw...$mDegrees")
        canvas.rotate(mDegrees, (width / 2).toFloat(), (height / 2).toFloat())
        super.onDraw(canvas)
    }


    //停止旋转
    fun stopRotate() {
        //取消协程
        job?.cancel()
    }

    //开始旋转
    fun startRotate() {
        //开启子线程
        //让加载圈旋转
        job = scope.launch {
           //如果当前fragment的状态不是加载状态，则停止旋转
           //使用isActive在每次循环开启之前检查协程是否已经被取消，直接设置为true不检查的话会导致协程取消失败
           while (isActive) {
               mDegrees = (mDegrees + 10) % 360 // 循环旋转
               //刷新界面
               invalidate()
               delay(20)
                }
        }
    }


    //实现自定义View引入LifeCycle效果
    private val mRegistry = LifecycleRegistry(this)

    override val lifecycle: Lifecycle
        get() = mRegistry


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mRegistry.currentState = Lifecycle.State.CREATED
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mRegistry.currentState = Lifecycle.State.DESTROYED
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        if (visibility == VISIBLE) {
            mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
            mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            LogUtils.d(this, "view 显示")
            startRotate()
        } else if (visibility == GONE || visibility == INVISIBLE) {
            mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
            LogUtils.d(this, "view 隐藏")
            //停止转动
            stopRotate()
        }
    }
}