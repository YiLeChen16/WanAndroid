package com.yl.testdemo

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener


/**
 * @description: 自定义BannerView
 * @author YL Chen
 * @date 2024/9/8 16:26
 * @version 1.0
 */
open class BannerView(
    context: Context,
    attrs: AttributeSet?,
) : ConstraintLayout(context, attrs) {
    init {
        initView(context, attrs)
    }

    constructor(context: Context) : this(context, null)


    private val Default_Duration = 3000
    private var DefaultIndicatorNormalColor: Int = resources.getColor(R.color.white)
    private var DefaultIndicatorSelectedColor: Int = resources.getColor(R.color.colorPrimary)
    open var mDuration: Int = Default_Duration
    open var mLoop: Boolean = true
    open var mIndicatorNormalColor: Int = DefaultIndicatorNormalColor
    open var mIndicatorSelectedColor: Int = DefaultIndicatorSelectedColor

    lateinit var mViewPager: ViewPager
    lateinit var mPointLayout: LinearLayout

    private lateinit var mAdapter: ViewPagerAdapter

    private fun initView(context: Context, attrs: AttributeSet?) {
        //获取自定义属性
        val a = context.obtainStyledAttributes(attrs, R.styleable.BannerView)
        mDuration = a.getInteger(R.styleable.BannerView_duration, Default_Duration)
        mLoop = a.getBoolean(R.styleable.BannerView_loop, true)
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
        LogUtils.d(this@BannerView, "setData-->$bannerData")
        //为ViewPager设置适配器
        mAdapter = ViewPagerAdapter()
        mViewPager.adapter = mAdapter
        mAdapter.setData(bannerData)
        LogUtils.d(this@BannerView, "mAdapter-->${mAdapter}")
        //初始化指示器
        setUpIndicator(mAdapter.count)
        //设置轮播图条目监听事件
        mAdapter.setOnItemListener(object : ViewPagerAdapter.OnItemListener {
            //条目被点击
            override fun onItemClick(toUrl: String) {
                //TODO：：跳转到url界面
            }
        })
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
        //添加指示器
        for (i in 0 until count) {
            val point = View(context)
            //设置指示器颜色
            point.setBackgroundColor(resources.getColor(R.color.white))
            val layoutParams = LinearLayout.LayoutParams(
                SizeUtils.dip2px(context, 10f), SizeUtils.dip2px(context, 10f)
            )
            layoutParams.setMargins(
                SizeUtils.dip2px(context, 10f),
                0,
                SizeUtils.dip2px(context, 10f),
                0
            )
            point.layoutParams = layoutParams
            mPointLayout.addView(point)
        }
    }

}