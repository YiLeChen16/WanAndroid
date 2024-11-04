package com.yl.wanandroid.ui.fragment.home

import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMFragment
import com.yl.wanandroid.base.ViewStateEnum
import com.yl.wanandroid.databinding.FragmentHomeBinding
import com.yl.wanandroid.ui.adapter.HomeTabViewPagerAdapter
import com.yl.wanandroid.ui.custom.BannerView
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.viewmodel.home.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * @description: 首页
 * @author YL Chen
 * @date 2024/9/7 15:42
 * @version 1.0
 */
@AndroidEntryPoint
class HomeFragment :
    BaseVMFragment<FragmentHomeBinding, HomeFragmentViewModel>(R.layout.fragment_home) {
    companion object {
        private var homeFragment: HomeFragment? = null
        fun newInstance(): HomeFragment {
            if (homeFragment == null) {
                homeFragment = HomeFragment()
            }
            return homeFragment!!
        }
    }

    lateinit var homeTabViewPagerAdapter: HomeTabViewPagerAdapter


    override fun initView() {
        super.initView()
        //禁止刷新
        mRefreshLayout.setEnableRefresh(false)
        mRefreshLayout.setEnableLoadMore(false)
        //绑定tabLayout和ViewPager2
        homeTabViewPagerAdapter = HomeTabViewPagerAdapter(this)
        mBinding.tabViewPager.adapter = homeTabViewPagerAdapter
        //setDefaultItem(1)
        // 联动 Tab 和 ViewPager2
        mBinding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                mBinding.tabViewPager.currentItem = tab.position // 设置 ViewPager2 当前项
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        // 添加 ViewPager2 的页面变化监听
        mBinding.tabViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                mBinding.tab.getTabAt(position)?.select() // 更新 Tab 选中状态
            }
        })
        //设置默认选中第二页
        mBinding.tabViewPager.setCurrentItem(1, false)

    }

    override fun initVMData() {
        mViewModel.getBannerData()
    }

    override fun observeLiveData() {
        mViewModel.bannerDatas.observe(viewLifecycleOwner) { bannerData ->
            //将数据装载到Banner控件中
            LogUtils.d(this@HomeFragment, "bannerData-->${bannerData.toString()}")
            if (bannerData != null) {
                mViewModel.changeStateView(ViewStateEnum.VIEW_LOAD_SUCCESS)
                //自定义控件无法直接使用DataBing，会报空指针异常
                //val bannerView = rootView.findViewById<BannerView>(R.id.banner_view)
                val bannerView = mBinding.root.findViewById<BannerView>(R.id.banner_view)
                LogUtils.d(this@HomeFragment, "initVMData-->$bannerView")
                bannerView.setData(bannerData)
            }
        }
    }

}