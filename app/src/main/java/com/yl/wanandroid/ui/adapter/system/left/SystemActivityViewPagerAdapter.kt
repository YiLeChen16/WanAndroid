package com.yl.wanandroid.ui.adapter.system.left

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yl.wanandroid.ui.fragment.system.left.SystemActivityFragment

/**
 * @description: 体系下某条目所有关键词的文章ViewPager
 * @author YL Chen
 * @date 2025/2/7 16:42
 * @version 1.0
 */
class SystemActivityViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private var systemActivityFragmentList = mutableListOf<SystemActivityFragment>()

    //暴露方法给外界设置数据
    fun setData(cids: ArrayList<Int>?) {
        if (cids == null){
            return
        }
        systemActivityFragmentList.clear()
        for (cid in cids) {
            //创建相应数量的Fragment并存入集合中
            val systemActivityFragment = SystemActivityFragment(cid)
            systemActivityFragmentList.add(systemActivityFragment)
        }
    }

    override fun getItemCount(): Int {
        return systemActivityFragmentList.size
    }


    override fun createFragment(position: Int): Fragment {
        return systemActivityFragmentList[position]
    }


}