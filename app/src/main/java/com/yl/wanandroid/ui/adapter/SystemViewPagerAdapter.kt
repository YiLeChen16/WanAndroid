package com.yl.wanandroid.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yl.wanandroid.ui.fragment.system.SystemChildFragment
import com.yl.wanandroid.ui.fragment.system.SystemCourseFragment

/**
 * @description:
 * @author YL Chen
 * @date 2024/12/18 22:10
 * @version 1.0
 */
class SystemViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val fragments = mutableListOf<Fragment>()

    init {
        val systemChildFragment = SystemChildFragment()
        val systemCourseFragment = SystemCourseFragment()
        fragments.add(systemChildFragment)
        fragments.add(systemCourseFragment)
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}