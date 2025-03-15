package com.yl.wanandroid.ui.fragment.system

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout
import com.yl.wanandroid.R
import com.yl.wanandroid.base.fragment.BaseFragment
import com.yl.wanandroid.databinding.FragmentSystemBinding
import com.yl.wanandroid.ui.adapter.system.course.SystemViewPagerAdapter
import com.yl.wanandroid.utils.LogUtils

/**
 * @description: 体系
 * @author YL Chen
 * @date 2024/9/7 16:02
 * @version 1.0
 */
class SystemFragment :
    BaseFragment<FragmentSystemBinding>(R.layout.fragment_system) {

    private var systemViewPagerAdapter: SystemViewPagerAdapter? = null

    override fun initView() {
        super.initView()
        //禁用刷新布局
        mRefreshLayout.setEnableRefresh(false)
        mRefreshLayout.setEnableLoadMore(false)
        initTab()
        systemViewPagerAdapter = SystemViewPagerAdapter(this)
        mBinding.viewpager.adapter = systemViewPagerAdapter
        mBinding.viewpager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                mBinding.tabLayout.getTabAt(position)?.select()
            }
        })
        mBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab == null)
                    return
                mBinding.viewpager.currentItem = tab.position
                //选中.字体加粗
                val tabSelected = tab.customView as TextView
                tabSelected.textSize = 18f
                tabSelected.setTextColor(context!!.getColor(R.color.md_theme_primary))
                tabSelected.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                if(tab == null)
                    return
                //未选中
                val tabSelected = tab.customView as TextView
                tabSelected.textSize = 14f
                tabSelected.setTextColor(context!!.getColor(R.color.md_theme_onSurface))
                tabSelected.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    override fun initData() {
        mMultiplyStateView.showSuccess()
    }

    //初始化tab条目的样式
    @SuppressLint("InflateParams")
    private fun initTab() {
        val initData = listOf("体系", "课程")
        LogUtils.d(this, "initTab-->mBinding.tab.tabCount==${mBinding.tabLayout.tabCount}")
        for (i in initData.indices) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_tab, null) as TextView
            view.text = initData[i]
            val tab: TabLayout.Tab = mBinding.tabLayout.newTab().setCustomView(view)
            mBinding.tabLayout.addTab(tab,false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.viewpager.adapter = null
        systemViewPagerAdapter?.fragments?.clear()
    }

}