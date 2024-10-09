package com.yl.testdemo

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide


import kotlin.math.max

/**
 * @description: 自定义轮播图适配器
 * @author YL Chen
 * @date 2024/9/9 20:57
 * @version 1.0
 */
open class ViewPagerAdapter : PagerAdapter() {

    private lateinit var mBannerData: List<BannerDataBean>
    private var listener: OnItemListener? = null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        LogUtils.d(this@ViewPagerAdapter, "instantiateItem...")
        val iv = ImageView(container.context)
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        iv.layoutParams = layoutParams
        //获取到父控件的宽高
        val width = container.measuredWidth
        val height = container.measuredHeight
        val size = (max(width.toDouble(), height.toDouble()) / 3).toInt()
        LogUtils.d(this, "size-->$size")

        //实现无限轮播
        val realPosition: Int = position % mBannerData.size

        //设置加载网络图片的大小
        val picUrl: String = mBannerData[position].imagePath
        LogUtils.d(this@ViewPagerAdapter, "picUrl-->$picUrl")
        Glide.with(container.context).load(picUrl).into(iv)
        iv.scaleType = ImageView.ScaleType.CENTER_CROP
        container.addView(iv)

        //为轮播图片设置监听事件
        iv.setOnClickListener {
            listener?.onItemClick(mBannerData[realPosition].url)
        }
        return iv

    }


    //提供方法给外界设置数据
    open fun setData(bannerData: List<BannerDataBean>) {
        this.mBannerData = bannerData
        LogUtils.d(this@ViewPagerAdapter, "setData-->${mBannerData.size}")
    }

    override fun getCount(): Int {
        //无限轮播
        return Integer.MAX_VALUE
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        LogUtils.d(this@ViewPagerAdapter, "isViewFromObject-->${view == `object`}")
        return view == `object`
    }

    //提供设置监听回调接口的方法
    open fun setOnItemListener(listener: OnItemListener) {
        this.listener = listener
    }

    //定义回调接口
    interface OnItemListener {
        /**
         * 条目被点击
         * @param toUrl String 条目的跳转url
         */
        fun onItemClick(toUrl: String)
    }

}