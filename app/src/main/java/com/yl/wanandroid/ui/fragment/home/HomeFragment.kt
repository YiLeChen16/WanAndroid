package com.yl.wanandroid.ui.fragment.home

import android.graphics.Typeface
import android.view.LayoutInflater
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseVMFragment
import com.yl.wanandroid.model.ViewStateEnum
import com.yl.wanandroid.databinding.FragmentHomeBinding
import com.yl.wanandroid.ui.adapter.HomeTabViewPagerAdapter
import com.yl.wanandroid.ui.custom.BannerView
import com.yl.wanandroid.utils.LogUtils
import com.yl.wanandroid.viewmodel.home.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.yl.wanandroid.BR
import com.yl.wanandroid.ui.custom.ViewPager2Container


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

    private lateinit var homeTabViewPagerAdapter: HomeTabViewPagerAdapter


    override fun initView() {
        super.initView()
        //禁止刷新
        mRefreshLayout.setEnableRefresh(false)
        mRefreshLayout.setEnableLoadMore(false)
        initTab()//初始化tabitem的样式
        //绑定tabLayout和ViewPager2
        homeTabViewPagerAdapter = HomeTabViewPagerAdapter(this)
        mBinding.tabViewPager.adapter = homeTabViewPagerAdapter
        // 联动 Tab 和 ViewPager2
        mBinding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                mBinding.tabViewPager.currentItem = tab.position // 设置 ViewPager2 当前项
                //选中.字体加粗
                val tabSelected = tab.customView as TextView
                tabSelected.textSize = 18f
                tabSelected.setTextColor(context!!.getColor(R.color.md_theme_primary))
                tabSelected.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
                tabSelected.text = tab.text
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                //未选中
                val tabSelected = tab.customView as TextView
                tabSelected.textSize = 14f
                tabSelected.setTextColor(context!!.getColor(R.color.md_theme_onSurface))
                tabSelected.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
                tabSelected.text = tab.text
            }

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

    //初始化tab条目的样式
    private fun initTab() {
        val initData = listOf("鸿蒙", "推荐", "问答")
        LogUtils.d(this, "initTab-->mBinding.tab.tabCount==${mBinding.tab.tabCount}")
        for (i in 0 until mBinding.tab.tabCount) {
            val tabAt = mBinding.tab.getTabAt(i)
            val view = LayoutInflater.from(context).inflate(R.layout.item_tab, null) as TextView
            view.text = initData[i]
            tabAt?.setCustomView(view)
        }
        mBinding.tab.isTabIndicatorFullWidth = false
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

    override fun getVariableId(): Int {
        return BR.homeViewModel
    }
}