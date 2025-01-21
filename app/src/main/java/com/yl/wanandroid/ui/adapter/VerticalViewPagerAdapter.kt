package com.yl.wanandroid.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.yl.wanandroid.R
import com.yl.wanandroid.model.Children

/**
 * @description: TODO
 * @author YL Chen
 * @date 2024/12/19 21:22
 * @version 1.0
 */
class VerticalViewPagerAdapter : PagerAdapter() {
    private var data: MutableList<Children> = mutableListOf()

    override fun getCount(): Int {
        TODO("Not yet implemented")
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflate = LayoutInflater.from(container.context)
            .inflate(R.layout.item_vertical_viewpager, container, false)
        //inflate.
        return data.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    fun setData(data: MutableList<Children>) {
        this.data = data
    }
}