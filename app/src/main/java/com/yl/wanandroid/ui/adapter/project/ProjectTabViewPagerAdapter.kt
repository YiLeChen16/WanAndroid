package com.yl.wanandroid.ui.adapter.project

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yl.wanandroid.model.ProjectCategoryDataBeanItem
import com.yl.wanandroid.ui.fragment.project.ProjectTabFragment

/**
 * @description: ProjectFragment的ViewPager适配器
 * @author YL Chen
 * @date 2024/11/27 23:04
 * @version 1.0
 */
class ProjectTabViewPagerAdapter(fragment:Fragment): FragmentStateAdapter(fragment) {
    var tabFragmentList = mutableListOf<ProjectTabFragment>()

    override fun getItemCount(): Int {
        return tabFragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return tabFragmentList[position]
    }

    //对外暴露设置数据的方法
    fun setFragmentSize(projectCategoryDataBeanItems: MutableList<ProjectCategoryDataBeanItem>){
        //创建对应个数的ProjectTabFragment
        for (projectCategoryDataBeanItem in projectCategoryDataBeanItems) {
            val projectTabFragment = ProjectTabFragment(projectCategoryDataBeanItem.id)
            //存储到集合中
            tabFragmentList.add(projectTabFragment)
        }
    }
}