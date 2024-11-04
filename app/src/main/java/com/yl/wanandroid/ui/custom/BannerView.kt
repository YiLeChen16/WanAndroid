package com.yl.wanandroid.ui.custom

import android.content.Context
import android.graphics.Outline
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.yl.wanandroid.R
import com.yl.wanandroid.model.BannerDataBean
import com.yl.wanandroid.ui.adapter.BannerViewPagerAdapter
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.utils.SizeUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @description: 自定义BannerView
 * @author YL Chen
 * @date 2024/9/8 16:26
 * @version 1.0
 */
@AndroidEntryPoint
open class BannerView : ConstraintLayout {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defy: Int) : super(context, attrs, defy) {
        initView(context, attrs)
    }

    //创建协程作用域
    val scope = CoroutineScope(Job() + Dispatchers.Main)

    private var job: Job? = null

    private val Default_Loop = false
    private val Default_Duration = 3000
    private var DefaultIndicatorNormalColor: Int = resources.getColor(R.color.grey)
    private var DefaultIndicatorSelectedColor: Int = resources.getColor(R.color.md_theme_primary)
    open var mDuration: Int = Default_Duration
    open var mLoop: Boolean = Default_Loop
        set(value) {
            field = value
            if (value) {
                //开启自动轮播
                startLoop()
            } else {
                //停止自动轮播
                stopLoop()
            }
        }

    //停止轮播
    private fun stopLoop() {
        job?.cancel()
    }

    //开始轮播
    private fun startLoop() {
        job = scope.launch {
            while (true) {
                val count = mViewPager.adapter?.count ?: 0
                if (count > 0) {
                    var currentItem = mViewPager.currentItem
                    currentItem++
                    if (currentItem == mViewPager.adapter?.count) {
                        currentItem = 0
                        mViewPager.setCurrentItem(currentItem, false)
                    }
                    mViewPager.currentItem = currentItem
                }
                delay(mDuration.toLong())
            }
        }
    }


    open var mIndicatorNormalColor: Int = DefaultIndicatorNormalColor
    open var mIndicatorSelectedColor: Int = DefaultIndicatorSelectedColor

    lateinit var mViewPager: ViewPager
    lateinit var mPointLayout: LinearLayout

    private var isIndicatorSetup = false

    @Inject
    lateinit var mAdapter: BannerViewPagerAdapter

    private fun initView(context: Context, attrs: AttributeSet?) {
        //获取自定义属性
        val a = context.obtainStyledAttributes(attrs, R.styleable.BannerView)
        mDuration = a.getInteger(R.styleable.BannerView_duration, Default_Duration)
        mLoop = a.getBoolean(R.styleable.BannerView_loop, Default_Loop)
        mIndicatorNormalColor =
            a.getColor(R.styleable.BannerView_indicatorNormalColor, DefaultIndicatorNormalColor)
        mIndicatorSelectedColor =
            a.getColor(R.styleable.BannerView_indicatorSelectedColor, DefaultIndicatorSelectedColor)
        a.recycle()
        //载入布局,第三个参数要填true才会自动加载到布局中
        LayoutInflater.from(context).inflate(
            R.layout.custom_banner_view,
            this,
            true
        )
        //获取布局控件
        mViewPager = this.findViewById(R.id.view_pager)
        mPointLayout = this.findViewById(R.id.point_Layout)
    }


    //暴露方法给外界设置数据
    open fun setData(bannerData: MutableList<BannerDataBean>) {
        mViewPager.adapter = mAdapter
        mAdapter.setData(bannerData)
        //初始化指示器
        setUpIndicator(mAdapter.count)
        //设置轮播图切换监听
        mViewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                //更新指示器
                updateIndicator(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }


    /**
     * 更新指示器
     * @param currentPosition Int
     */
    private fun updateIndicator(currentPosition: Int) {
        LogUtils.d(this@BannerView, "updateIndicator--")
        for (i in 0 until mPointLayout.childCount) {
            if (currentPosition == i) {
                val childAt = mPointLayout.getChildAt(i)
                childAt.setBackgroundColor(mIndicatorSelectedColor)
            } else {
                val childAt = mPointLayout.getChildAt(i)
                childAt.setBackgroundColor(mIndicatorNormalColor)
            }
        }
    }

    /**
     * 初始化轮播图指示器个数
     * @param count Int
     */
    private fun setUpIndicator(count: Int) {
        //确保只初始化一次
        if (isIndicatorSetup) return
        isIndicatorSetup = true
        mPointLayout.removeAllViews()
        LogUtils.d(this@BannerView, "setUpIndicator--")
        //添加指示器
        for (i in 0 until count) {
            val point = ImageView(context)
            //设置指示器颜色
            point.setBackgroundColor(mIndicatorNormalColor)
            val layoutParams = LinearLayout.LayoutParams(
                SizeUtils.dip2px(context, 5f), SizeUtils.dip2px(context, 5f)
            )
            layoutParams.setMargins(
                SizeUtils.dip2px(context, 15f),
                0,
                SizeUtils.dip2px(context, 15f),
                0
            )
            if (i == 0) {
                point.setBackgroundColor(mIndicatorSelectedColor)
            }
            point.layoutParams = layoutParams
            //实现圆形指示器
            point.outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View?, outline: Outline?) {
                    outline?.setOval(0, 0, view!!.width, view.height)
                }
            }
            point.clipToOutline = true
            mPointLayout.addView(point)
        }
    }

}