package com.yl.wanandroid.ui.activity

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseActivity
import com.yl.wanandroid.databinding.ActivityMainBinding
import com.yl.wanandroid.utils.LogUtils

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    /**
     * 初始化界面
     */
    override fun initView() {

        //导航
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.findNavController()
        //关联底部导航栏和上方的fragmentContainerView
        NavigationUI.setupWithNavController(
            mBinding.bottomNavigation,
            navController
        )
        // 打印调试信息
        LogUtils.d(this, "NavController: $navController")
        LogUtils.d(this, "BottomNavigationView: ${mBinding.bottomNavigation}")
    }

    override fun initData() {

    }

}

