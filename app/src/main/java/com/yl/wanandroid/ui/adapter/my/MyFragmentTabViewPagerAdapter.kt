package com.yl.wanandroid.ui.adapter.my

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yl.wanandroid.model.Children
import com.yl.wanandroid.ui.fragment.my.MyTabFragment

/**
 * @description: 我的页面的ViewPager适配器
 * @author YL Chen
 * @date 2025/3/2 15:07
 * @version 1.0
 */
class MyFragmentTabViewPagerAdapter  (fragment: Fragment) : FragmentStateAdapter(fragment) {
    private var tabFragmentList = mutableListOf<MyTabFragment>()

    override fun getItemCount(): Int {
        return tabFragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return tabFragmentList[position]
    }

    //对外暴露设置数据的方法
    fun setFragmentSize(data: MutableList<Children>) {
        //创建对应个数的MyTabFragment
        if (tabFragmentList.isEmpty()){
            for (item in data) {
                val myTabFragment = MyTabFragment(item.id)
                //存储到集合中
                tabFragmentList.add(myTabFragment)
            }
        }

    }
}