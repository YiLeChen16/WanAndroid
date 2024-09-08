package com.yl.wanandroid.ui.activity

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseActivity
import com.yl.wanandroid.databinding.ActivityMainBinding
import com.yl.wanandroid.ui.fragment.HomeFragment
import com.yl.wanandroid.ui.fragment.MyFragment
import com.yl.wanandroid.ui.fragment.ProjectFragment
import com.yl.wanandroid.ui.fragment.SystemFragment
import com.yl.wanandroid.utils.LogUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    /**
     * 初始化界面
     */
    override fun initView() {
        //导航
        val homeFragment = HomeFragment.newInstance()
        val projectFragment = ProjectFragment.newInstance()
        val systemFragment = SystemFragment.newInstance()
        val myFragment = MyFragment.newInstance()
        val fragmentList = arrayOf(homeFragment, projectFragment, systemFragment, myFragment)
        mBinding.fragmentContainer.setAdapter(
            object : FragmentStateAdapter(this) {
                override fun getItemCount(): Int {
                    return fragmentList.size
                }

                override fun createFragment(position: Int): Fragment {
                    return fragmentList[position]
                }
            }
        )
        //给底部导航栏设置监听
        //与viewPager绑定
        mBinding.bottomNavigation.setOnItemSelectedListener {
                item->
            LogUtils.d("bottomNavigation", "itemID->${item.itemId}")
            when (item.itemId) {
                R.id.home -> {
                    mBinding.fragmentContainer.currentItem = 0
                    LogUtils.d("bottomNavigation", "home clicked")
                }

                R.id.project -> {
                    mBinding.fragmentContainer.currentItem = 1
                    LogUtils.d("bottomNavigation", "project clicked")
                }

                R.id.system -> {
                    mBinding.fragmentContainer.currentItem = 2
                    LogUtils.d("bottomNavigation", "system clicked")
                }
                R.id.my -> {
                    mBinding.fragmentContainer.currentItem = 3
                    LogUtils.d("bottomNavigation", "my clicked")
                }
            }
            true
        }

        mBinding.fragmentContainer.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                //在页面被选中时调用
                when (position) {
                    0 -> mBinding.bottomNavigation.selectedItemId = R.id.home
                    1 -> mBinding.bottomNavigation.selectedItemId = R.id.project
                    2 -> mBinding.bottomNavigation.selectedItemId = R.id.system
                    3 -> mBinding.bottomNavigation.selectedItemId = R.id.my
                }
            }
        })
        super.initView()
    }


    override fun initData() {

    }

}

