package com.yl.wanandroid.ui.adapter.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yl.wanandroid.Constant
import com.yl.wanandroid.ui.fragment.home.wenda.HotOrNormalWendaFragment

/**
 * @description: 首页问答Fragment的ViewPager适配器
 * @author YL Chen
 * @date 2024/11/19 17:58
 * @version 1.0
 */
class WendaTabViewPagerAdapter(fragment:Fragment): FragmentStateAdapter(fragment) {
    private val fragments = mutableListOf<HotOrNormalWendaFragment>()
    init {
        //创建所需Fragment
        val hotWendaFragment = HotOrNormalWendaFragment.newInstance(Constant.HOT_WENDA_FRAGMENT_TYPE)
        val moreWendaFragment = HotOrNormalWendaFragment.newInstance(Constant.NORMAL_WENDA_FRAGMENT_TYPE)
        fragments.add(hotWendaFragment)
        fragments.add(moreWendaFragment)
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}