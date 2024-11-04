package com.yl.wanandroid.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.yl.wanandroid.Constant
import com.yl.wanandroid.R
import com.yl.wanandroid.model.BannerDataBean
import com.yl.wanandroid.ui.activity.WebViewActivity
import com.yl.wanandroid.utils.LogUtils
import javax.inject.Inject

/**
 * @description: 自定义轮播图适配器
 * @author YL Chen
 * @date 2024/9/9 20:57
 * @version 1.0
 */
open class BannerViewPagerAdapter @Inject constructor() : PagerAdapter() {

    private var mBannerData: MutableList<BannerDataBean> = mutableListOf()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        LogUtils.d(this@BannerViewPagerAdapter, "instantiateItem...")
        val itemView =
            LayoutInflater.from(container.context).inflate(R.layout.item_pager, container, false)
        val iv = itemView.findViewById<ImageView>(R.id.iv)
        if (iv.parent is ViewGroup) {
            (iv.parent as ViewGroup).removeView(iv)
        }
        if (position == 0) {
            iv.tag = "last"
        } else if (position == count - 1) {
            iv.tag = "first"
        }
        LogUtils.d(this,"mBannerData.size-->${mBannerData.size}")
        val realPosition: Int = position % mBannerData.size
        LogUtils.d(this,"realPosition-->${realPosition}")

        //设置加载网络图片的大小
        val picUrl: String = mBannerData[realPosition].imagePath
        LogUtils.d(this@BannerViewPagerAdapter, "picUrl-->$picUrl")
        Glide.with(container.context).load(picUrl).into(iv)
        iv.scaleType = ImageView.ScaleType.CENTER_CROP
        //为条目设置点击监听事件
        iv.setOnClickListener {
            LogUtils.d(this@BannerViewPagerAdapter, "toUrl-->${mBannerData[realPosition].url}")
            val intent = Intent(container.context, WebViewActivity::class.java)
            intent.putExtra(Constant.toWebUrlKey,mBannerData[realPosition].url)//携带数据跳转
            container.context.startActivity(intent)
        }
        container.addView(iv)
        return iv
    }


    //提供方法给外界设置数据
    open fun setData(bannerData: MutableList<BannerDataBean>) {
        this.mBannerData.clear()
        this.mBannerData.addAll(bannerData)
        notifyDataSetChanged()
        LogUtils.d(this@BannerViewPagerAdapter, "setData-->${mBannerData.size}")
    }

    override fun getCount(): Int {
        //无限轮播
        LogUtils.d(this,"mBannerData.size-->${mBannerData.size}")
        //LogUtils.d(this,"getCount-->${if (mBannerData.size == 0) 0 else Integer.MAX_VALUE}")
        return mBannerData.size  //由于此处想要实现轮播图的无限循环，故将最大值设置为Integer的最大值;
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        LogUtils.d("ViewPagerAdapter", "isViewFromObject-->${view == `object`}")
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}