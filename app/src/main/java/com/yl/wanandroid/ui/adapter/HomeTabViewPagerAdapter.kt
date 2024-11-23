package com.yl.wanandroid.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yl.wanandroid.ui.fragment.home.HarmonyFragment
import com.yl.wanandroid.ui.fragment.home.RecommendFragment
import com.yl.wanandroid.ui.fragment.home.wenda.WendaFragment

/**
 * @description: 首页一级Tab的ViewPager适配器
 * @author YL Chen
 * @date 2024/10/20 14:52
 * @version 1.0
 */
class HomeTabViewPagerAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {
    //管理所有Fragment
    private var fragmentList = mutableListOf<Fragment>()

    init {
        //创建所需Fragment
//        val recommendFragment = RecommendFragment.newInstance()
//        val knowledgeColumnFragment = HarmonyFragment.newInstance()
//        val wendaFragment = WendaFragment.newInstance()
        val recommendFragment = RecommendFragment()
        val knowledgeColumnFragment = HarmonyFragment()
        val wendaFragment = WendaFragment()

        fragmentList.add(knowledgeColumnFragment)
        fragmentList.add(recommendFragment)
        fragmentList.add(wendaFragment)
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}