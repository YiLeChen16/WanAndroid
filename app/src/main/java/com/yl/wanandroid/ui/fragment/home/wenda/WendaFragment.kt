package com.yl.wanandroid.ui.fragment.home.wenda

import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.yl.wanandroid.R
import com.yl.wanandroid.base.BaseFragment
import com.yl.wanandroid.databinding.FragmentWendaBinding
import com.yl.wanandroid.ui.adapter.WendaTabViewPagerAdapter
import com.yl.wanandroid.ui.custom.ViewPager2Container

/**
 * @description: 首页问答Fragment
 * @author YL Chen
 * @date 2024/10/20 16:22
 * @version 1.0
 */
class WendaFragment : BaseFragment<FragmentWendaBinding>(R.layout.fragment_wenda) {

    override fun initView() {
        super.initView()
        //禁止刷新
        mRefreshLayout.setEnableRefresh(false)
        mRefreshLayout.setEnableLoadMore(false)
        //绑定tabLayout 和Viewpager2
        val tabViewPagerAdapter = WendaTabViewPagerAdapter(this)
        mBinding.contentViewpager.adapter = tabViewPagerAdapter
        //联动tab 和viewpager2
        mBinding.wendaTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                mBinding.contentViewpager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
        //添加viewpager2的页面变化监听
        mBinding.contentViewpager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                mBinding.wendaTab.getTabAt(position)?.select()
            }
        })
        val viewPager2Container =
            mBinding.root.findViewById<ViewPager2Container>(R.id.view_pager2_container)
        viewPager2Container.disallowParentInterceptDownEvent(false)
        mMultiplyStateView.showSuccess()//展示成功视图
    }

    override fun initData() {
    }
}